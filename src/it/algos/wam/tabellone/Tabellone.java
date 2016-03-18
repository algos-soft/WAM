package it.algos.wam.tabellone;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.login.Login;
import it.algos.wam.login.LoginComponent;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.screen.ErrorScreen;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Date;

/**
 * Componente Tabellone.
 * <p>
 * Contiene la business logic e gestisce la navigazione e l'interazione tra i componenti grafici di alto livello.
 * Il componente tabComponent ospita una menubar con i comandi di spostamento e la griglia del tabellone.
 * Il componente editTurnoComponent presenta il form con i dati di un turno e permette di modificarli.
 * Il componente searchComponent presenta il dialogo per impostare le conizioni di
 * ricerca per visualizzare un tabellone custom.
 */
public class Tabellone extends VerticalLayout implements View {

    /**
     * numero massimo di giorni visualizzabili nel tabellone
     */
    private static int MAX_GG_TAB = 60;

    private EntityManager entityManager;
    private TabComponent tabComponent;
    private EditorPage editorPage;
    private SearchComponent searchComponent;
    private Navigator navigator;
    private String homeURI;


    // Indirizzi delle pagine interne per la navigazione del Navigator
    private static final String ADDR_TABELLONE="tabellone";
    private static final String ADDR_EDIT_TURNO="turno";
    private static final String ADDR_EDIT_SERVIZIO="servizio";
    private static final String ADDR_SEARCH="ricerca";
    private static final String ADDR_LOGIN="login";

    /**
     * Costruttore.
     *
     * @param homeURI la pagina da caricare quanto si preme il pulsante Home
     */
    public Tabellone(String homeURI) {

        this.homeURI = homeURI;

        //addStyleName("greenBg");

        setSizeUndefined();

        setMargin(true);

        entityManager = EM.createEntityManager();

        tabComponent = new TabComponent();
        creaGrid(LocalDate.now());

        editorPage = new EditorPage();
        searchComponent = new SearchComponent();

        // creo un Navigator e vi aggiungo i vari componenti che possono
        // essere presentati dal tabellone
        navigator = new Navigator(UI.getCurrent(), this);
        navigator.addView(ADDR_TABELLONE, tabComponent);
        navigator.addView(ADDR_EDIT_TURNO, editorPage);
        navigator.addView(ADDR_EDIT_SERVIZIO, editorPage);
        navigator.addView(ADDR_SEARCH, searchComponent);
        navigator.addView(ADDR_LOGIN, new LoginComponent());
        navigator.setErrorView(new TabErrView());
        navigator.navigateTo(ADDR_TABELLONE);

        // Listener di cambio View nel Navigator.
        // In funzione della pagina in cui stiamo entrando, cambio
        // la dimensione del Tabellone
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                String name = event.getViewName();
                boolean cont = true;
                switch (name) {
                    case ADDR_TABELLONE:
                        setSizeUndefined();
                        break;
                    case ADDR_SEARCH:
                        setSizeFull();
                        break;
                    case ADDR_EDIT_TURNO:
                        setSizeFull();
                        break;
                    case ADDR_EDIT_SERVIZIO:
                        setSizeFull();
                        break;
                    default:
                        cont = false;
                        goHome();
                }

                return cont;

            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }

        });

    }

    /**
     * Costruttore.
     */
    public Tabellone() {
        this(null);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    /**
     * Crea una GridTabellone a partire dalla data richiesta e per il numero di giorni richiesto
     * e la mette nel componente visibile
     *
     * @param d1           la data iniziale
     * @param quantiGiorni il numero di giorni da visualizzare
     */
    private void creaGrid(LocalDate d1, int quantiGiorni) {
        WTabellone wrapper = EngineTab.creaRighe(d1, quantiGiorni, entityManager);
        GridTabellone grid = EngineTab.creaTabellone(wrapper);
        tabComponent.setGrid(grid);

        // aggiunge al tabellone un listener per la cella cliccata
        grid.addClickCellListener(new GridTabellone.ClickCellListener() {
            @Override
            public void cellClicked(GridTabellone.ClickCellEvent e) {
                Tabellone.this.cellClicked(e.getTipo(), e.getCol(), e.getRow(), e.getCellObject());
            }
        });

    }


    /**
     * Crea una GridTabellone a partire dalla data richiesta della durata di 7 giorni
     * e la mette nel componente visibile
     *
     * @param d1 la data iniziale
     */
    private void creaGrid(LocalDate d1) {
        creaGrid(d1, 7);
    }


    /**
     * E' stata cliccata una cella del tabellone
     *
     * @param tipo       tipo di cella
     * @param col        la colonna
     * @param row        la riga
     * @param cellObject l'oggetto specifico trasportato nella cella
     */
    private void cellClicked(CellType tipo, int col, int row, Object cellObject) {
        Turno turno;

        switch (tipo) {
            case TURNO:
                turno = (Turno) cellObject;
                editCellTurno(turno, col, row);
                break;
            case NO_TURNO:
                InfoNewTurnoWrap wrapper = (InfoNewTurnoWrap) cellObject;
                LocalDate dInizio = wrapper.getData();
                Servizio serv = wrapper.getServizio();
                turno = new Turno();
                turno.setInizio(DateConvertUtils.asUtilDate(dInizio));
                turno.setServizio(serv);
                editCellTurno(turno, col, row);
                break;
            case SERVIZIO:
                Servizio servizio = (Servizio)cellObject;
                editCellServizio(servizio, col, row);
                break;

        }


    }


    /**
     * Edita una cella di tipo Turno
     */
    private void editCellTurno(final Turno turno, int col, int row){

        // crea un editor per il turno
        // quando si dismette l'editor, tornerà al tabellone
        CTurnoEditor editor = new CTurnoEditor(turno, entityManager);
        editor.addDismissListener(new CTabelloneEditor.DismissListener() {
            @Override
            public void editorDismissed(CTabelloneEditor.DismissEvent e) {

                // se ha salvato o eliminato, aggiorna la cella della griglia
                if (e.isSaved() | e.isDeleted()) {
                    GridTabellone grid = tabComponent.getGridTabellone();
                    grid.removeComponent(col, row);
                    TabelloneCell cell = null;
                    if (e.isSaved()) {
                        cell = new CTurnoDisplay(grid, turno);
                    }
                    if (e.isDeleted()) {
                        cell = new CTurnoDisplay(grid, turno.getServizio(), turno.getData1());
                    }
                    grid.addComponent(cell, col, row);
                }
                navigator.navigateTo(ADDR_TABELLONE);
            }
        });


        // assegna l'editor e naviga alla editor view
        editorPage.setEditor(editor);
        navigator.navigateTo(ADDR_EDIT_TURNO);
    }


    /**
     * Edita una cella di tipo Servizio
     */
    private void editCellServizio(final Servizio servizio, int col, int row){
        // crea un editor per il servizio
        // quando si dismette l'editor, tornerà al tabellone
        CServizioEditor editor = new CServizioEditor(servizio, entityManager);
        editor.addDismissListener(new CTabelloneEditor.DismissListener() {
            @Override
            public void editorDismissed(CTabelloneEditor.DismissEvent e) {

                // se ha salvato o eliminato, aggiorna la cella della griglia
                if (e.isSaved() | e.isDeleted()) {
                    GridTabellone grid = tabComponent.getGridTabellone();
                    TabelloneCell cell = null;
                    if (e.isSaved()) {
                        grid.removeComponent(col, row);
                        cell = new CServizioDisplay(grid, servizio);
                        grid.addComponent(cell, col, row);
                    }
                    if (e.isDeleted()) {
                        grid.removeRow(row);
                    }
                }
                navigator.navigateTo(ADDR_TABELLONE);
            }
        });


        // assegna l'editor e naviga alla editor view
        editorPage.setEditor(editor);
        navigator.navigateTo(ADDR_EDIT_SERVIZIO);

    }

    /**
     * Crea un nuovo servizio e lo presenta nell'editor.
     * Se registrato, aggiunge una riga con il servizio al tabellone.
     * Invocato dal bottone Crea Nuovo Servizio nel Tabellone
     */
    public void nuovoServizio(){

        // crea un nuovo servizio
        Servizio servizio = new Servizio("extra","servizio extra");
        servizio.setOrario(false);

        // crea un editor per il servizio
        // quando si dismette l'editor, tornerà al tabellone
        CServizioEditor editor = new CServizioEditor(servizio, entityManager);
        editor.addDismissListener(new CTabelloneEditor.DismissListener() {
            @Override
            public void editorDismissed(CTabelloneEditor.DismissEvent e) {

                // se ha salvato aggiunge una nuova riga col servizio
                if (e.isSaved()) {
                    if (e.isSaved()) {
                        WRigaTab wRiga = new WRigaTab(servizio, null);
                        EngineTab.addRiga(tabComponent.getGridTabellone(), wRiga);
                    }
                }
                navigator.navigateTo(ADDR_TABELLONE);
            }
        });


        // assegna l'editor e naviga alla editor view
        editorPage.setEditor(editor);
        navigator.navigateTo(ADDR_EDIT_SERVIZIO);

    }



        /**
         * Manda il browser all'indirizzo definito nella homeURI (se esiste)
         */
    private void goHome() {
        if (homeURI != null) {
            String addr = homeURI.toString()+"?skip=1";
            this.getUI().getPage().open(addr, null);
//            this.getUI().getPage().open("http://"+homeURI.toString()+"?skip=1","");
        }
    }


    public Navigator getNavigator() {
        return navigator;
    }


    /**
     * Componente View con MenuBar comandi tabellone e griglia tabellone.
     * Invocare setGrid() per sostituire la griglia.
     */
    private class TabComponent extends VerticalLayout implements View {

        private VerticalLayout gridPlaceholder = new VerticalLayout();
        private GridTabellone gridTabellone;

        public TabComponent() {

            HorizontalLayout menuPlaceholder = new HorizontalLayout();
            menuPlaceholder.setSpacing(true);
            menuPlaceholder.addComponent(new TabMenuBarHome());
            menuPlaceholder.addComponent(new TabMenuBar());
            menuPlaceholder.addComponent(new TabMenuBarLogin());

            setSpacing(true);
            addComponent(menuPlaceholder);
            addComponent(gridPlaceholder);

            // bottone nuovo servizio
            Button bNuovoServ = new Button("Crea nuovo servizio", FontAwesome.PLUS_CIRCLE);
            addComponent(bNuovoServ);
            bNuovoServ.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    nuovoServizio();
                }
            });


        }


        public void setGrid(GridTabellone grid) {
            this.gridTabellone = grid;
            gridPlaceholder.removeAllComponents();
            gridPlaceholder.addComponent(grid);
        }

        public GridTabellone getGridTabellone() {
            return gridTabellone;
        }

        public LocalDate getDataStart() {
            LocalDate data = null;
            if (gridTabellone != null) {
                data = gridTabellone.getDataStart();
            }
            return data;
        }

        public int getNumGiorni() {
            int numGiorni=0;
            if (gridTabellone != null) {
                numGiorni = gridTabellone.getNumGiorni();
            }
            return numGiorni;
        }



        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }


    }


    /**
     * Menu bar con i comandi di movimento del tabellone
     */
    private class TabMenuBar extends MenuBar {
        public TabMenuBar() {
            MenuItem item = addItem("indietro", FontAwesome.ARROW_CIRCLE_LEFT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().minusDays(gg), gg);
                }
            });


            addItem("oggi", FontAwesome.CALENDAR_O, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    creaGrid(LocalDate.now());
                }
            });

            addItem("lunedì", FontAwesome.CALENDAR_O, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    LocalDate d1 = LocalDate.now();
                    int numDow = d1.getDayOfWeek().getValue();
                    LocalDate d2 = d1.minusDays(numDow - 1);
                    creaGrid(d2);
                }
            });


            addItem("avanti", FontAwesome.ARROW_CIRCLE_RIGHT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().plusDays(gg), gg);
                }
            });

            MenuItem menuVai = addItem("vai", FontAwesome.ARROW_CIRCLE_DOWN, null);


            menuVai.addItem("giorno precedente", FontAwesome.ARROW_CIRCLE_LEFT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().minusDays(1), gg);
                }
            });

            menuVai.addItem("giorno successivo", FontAwesome.ARROW_CIRCLE_RIGHT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().plusDays(1), gg);
                }
            });

            menuVai.addItem("cerca", FontAwesome.SEARCH, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    navigator.navigateTo(ADDR_SEARCH);
                }
            });


        }
    }

    /**
     * Menu bar di destra
     */
    private class TabMenuBarHome extends MenuBar {

        public TabMenuBarHome() {

            addItem("Home", FontAwesome.HOME, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    goHome();
                }
            });


        }
    }

    /**
     * Menu bar di login
     */
    private class TabMenuBarLogin extends MenuBar {

        public TabMenuBarLogin() {

            addItem("Login", FontAwesome.USER, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    Login.login();
                }
            });


        }
    }


    /**
     * Componente di alto livello con logica di navigazione in un turno/servizio da modificare.
     * Invocare il metodo setTurno() per inserire un turno da modificare.
     */
    private class EditorPage extends VerticalLayout implements View {

        public EditorPage() {
            setSizeFull();
        }

//        /**
//         * Assegna un Turno a questo componente.
//         * Crea l'editor di turno e lo aggiunge graficamente
//         *
//         * @param turno il turno da editare
//         * @param col   la colonna della griglia dove è visualizzato il turno
//         * @param row   la riga della griglia dove è visualizzato il turno
//         */
//        public void setTurno(Turno turno, int col, int row) {
//            removeAllComponents();
//
//            // Rimuove tutti i listeners dall'editor precedente
//            // prima di perdere il riferimento e crearne uno nuovo.
//            // Altrimenti il vecchio editor avrebbe un listener registrato
//            // che impedirebbe la garbace collection.
//            // Non posso farlo quando la chiamata parte dal listener
//            // perché avrei una ConcurrentModificationException.
//            if(editor!=null){
//                editor.removeAllDismissListeners();
//            }
//
//            editor = new CTurnoEditor(turno, entityManager);
//            addComponent(editor);
//            setComponentAlignment(editor, Alignment.MIDDLE_CENTER);
//
//            // quando si dismette l'editor, torna al tabellone
//            editor.addDismissListener(new CTurnoEditor.DismissListener() {
//                @Override
//                public void editorDismissed(CTurnoEditor.DismissEvent e) {
//
//                    // se ha salvato o eliminato il turno aggiorna la cella della griglia
//                    if (e.isSaved() | e.isDeleted()) {
//                        GridTabellone grid = tabComponent.getGridTabellone();
//                        grid.removeComponent(col, row);
//                        TabelloneCell cell=null;
//                        if(e.isSaved()){
//                            cell = new CTurnoDisplay(grid, turno);
//                        }
//                        if(e.isDeleted()){
//                            cell=new CTurnoDisplay(grid, turno.getServizio(), turno.getData1());
//                        }
//                        grid.addComponent(cell, col, row);
//                    }
//                    navigator.navigateTo(ADDR_TABELLONE);
//                }
//            });
//
//
//
//        }


        /**
         * Assegna un Editor a questo componente.
         * e lo aggiunge graficamente
         *
         * @param editor l'editor da mostrare nella pagina
         */
        public void setEditor(CTabelloneEditor editor) {
            removeAllComponents();
            addComponent(editor);
            setComponentAlignment(editor, Alignment.MIDDLE_CENTER);
        }


        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
        }


    }




    /**
     * Componente View con MenuBar comandi ricerca periodo.
     */
    private class SearchComponent extends VerticalLayout implements View {

        private SearchForm form;

        /**
         * Constructor
         */
        public SearchComponent() {
            setSizeFull();
            form = new SearchForm();
            addComponent(form);
            setComponentAlignment(form, Alignment.MIDDLE_CENTER);
            //addStyleName("yellowBg");
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
        }


    }

    /**
     * Componente View con MenuBar comandi ricerca perdiodo.
     */
    private class SearchForm extends AForm {

        /**
         * Constructor
         */
        public SearchForm() {
            super(new PropertysetItem());
            setSizeUndefined();


            getItem().addItemProperty("data", new ObjectProperty(new Date()));
            getItem().addItemProperty("giorni", new ObjectProperty(7));
            init();


            //addStyleName("blueBg");

            setConfirmText("Conferma");
            getToolbar().removeStyleName("toolbar");


            addFormListener(new FormListener() {
                @Override
                public void cancel_() {
                    navigator.navigateTo(ADDR_TABELLONE);
                }

                @Override
                public void commit_() {
                    int numGiorni = getNumGiorni();
                    if (numGiorni > 0 && numGiorni <= MAX_GG_TAB) {
                        LocalDate data = getDataInizio();
                        if (data != null) {
                            creaGrid(data, numGiorni);
                            navigator.navigateTo(ADDR_TABELLONE);
                        } else {
                            Notification.show("La data non può essere nulla.", Notification.Type.WARNING_MESSAGE);
                        }
                    } else {
                        Notification.show("Minimo 1 giorno, massimo " + MAX_GG_TAB + " giorni.", Notification.Type.WARNING_MESSAGE);
                    }
                }

            });

        }

        @Override
        public void createFields() {
            addField("data", new DateField("Data iniziale"));
            addField("giorni", new IntegerField("Numero di giorni"));
        }

        private int getNumGiorni() {
            Object obj = getFieldValue("giorni");
            return Lib.getInt(obj);
        }

        private LocalDate getDataInizio() {
            LocalDate data = null;
            Object obj = getFieldValue("data");
            if (obj != null && obj instanceof Date) {
                Date d = (Date) obj;
                data = DateConvertUtils.asLocalDate(d);
            }
            return data;
        }


    }


    /**
     * Error screen se il Navigator non trova una view
     */
    private class TabErrView extends ErrorScreen implements View {

        public TabErrView() {
            super("View non trovata");
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }
    }


}
