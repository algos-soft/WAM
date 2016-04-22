package it.algos.wam.tabellone;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
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

    // Indirizzi delle pagine interne per la navigazione del Navigator
    private static final String ADDR_TABELLONE = "tabellone";
    private static final String ADDR_EDIT_TURNO = "turno";
    private static final String ADDR_SEARCH = "ricerca";
    private static final String ADDR_GENERATE = "genera";

    private static final String ADDR_LOGIN = "login";
    /**
     * numero massimo di giorni visualizzabili nel tabellone
     */
    private static int MAX_GG_TAB = 60;
    private EntityManager entityManager;
    private TabComponent tabComponent;
    private EditorPage editorPage;
    private SearchPage searchPage;
    private GeneratorPage generatorPage;
    private Navigator navigator;
    private String homeURI;

    /**
     * Costruttore.
     *
     * @param homeURI la pagina da caricare quanto si preme il pulsante Home
     */
    public Tabellone(String homeURI) {

        this.homeURI = homeURI;


        setSizeUndefined();

        setMargin(true);

        entityManager = EM.createEntityManager();

        // todo questi componenti non vanno istanziati ora!
        // todo passare le classi al navigatore
        // todo li istanzierà il navigatore la prima volta che ci naviga
        // todo usare il flag del navigatore che evita di ricrearli tutte le volte
        tabComponent = new TabComponent();
        editorPage = new EditorPage();
        searchPage = new SearchPage();
        generatorPage = new GeneratorPage();

        creaGrid(LocalDate.now());

        // creo un Navigator e vi aggiungo i vari componenti che possono
        // essere presentati dal tabellone
        navigator = new Navigator(UI.getCurrent(), this);
        navigator.addView(ADDR_TABELLONE, tabComponent);
        navigator.addView(ADDR_EDIT_TURNO, editorPage);
        navigator.addView(ADDR_SEARCH, searchPage);
        navigator.addView(ADDR_GENERATE, generatorPage);
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
                        setWidth("100%");
                        setHeightUndefined();
                        break;
                    case ADDR_GENERATE:
                        setWidth("100%");
                        setHeightUndefined();
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
                Servizio servizio = (Servizio) cellObject;
//                editCellServizio(servizio, col, row);
                addRigaServizio(servizio, col, row);
                break;

        }


    }


    /**
     * Edita una cella di tipo Turno
     */
    private void editCellTurno(final Turno turno, int col, int row) {

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


//    /**
//     * Edita una cella di tipo Servizio
//     */
//    private void editCellServizio(final Servizio servizio, int col, int row) {
//        // crea un editor per il servizio
//        // quando si dismette l'editor, tornerà al tabellone
//        CServizioEditor editor = new CServizioEditor(servizio, entityManager);
//        editor.addDismissListener(new CTabelloneEditor.DismissListener() {
//            @Override
//            public void editorDismissed(CTabelloneEditor.DismissEvent e) {
//
//                // se ha salvato o eliminato, aggiorna la cella della griglia
//                if (e.isSaved() | e.isDeleted()) {
//                    GridTabellone grid = tabComponent.getGridTabellone();
//                    TabelloneCell cell = null;
//                    if (e.isSaved()) {
//                        grid.removeComponent(col, row);
//                        cell = new CServizioDisplay(grid, servizio);
//                        grid.addComponent(cell, col, row);
//                    }
//                    if (e.isDeleted()) {
//                        grid.removeRow(row);
//                    }
//                }
//                navigator.navigateTo(ADDR_TABELLONE);
//            }
//        });
//
//
//        // assegna l'editor e naviga alla editor view
//        editorPage.setEditor(editor);
//        navigator.navigateTo(ADDR_EDIT_SERVIZIO);
//
//    }


    /**
     * Aggiunge una ulteriore riga per un dato servizio.
     * La riga viene aggiunta dopo quella specificata in row
     */
    private void addRigaServizio(final Servizio servizio, int col, int row){
        GridTabellone grid = tabComponent.getGridTabellone();
        WRigaTab riga = new WRigaTab(servizio, new Turno[0]);
        EngineTab.insertRiga(grid, riga, row+1);
    }


    /**
     * Manda il browser all'indirizzo definito nella homeURI (se esiste)
     */
    private void goHome() {
        this.getUI().getPage().open(homeURI.toString(), null);
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

//            // bottone nuovo servizio
//            Button bNuovoServ = new Button("Crea nuovo servizio", FontAwesome.PLUS_CIRCLE);
//            addComponent(bNuovoServ);
//            bNuovoServ.addClickListener(new Button.ClickListener() {
//                @Override
//                public void buttonClick(Button.ClickEvent clickEvent) {
//                    nuovoServizio();
//                }
//            });


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
            int numGiorni = 0;
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

            MenuItem menuVai = addItem("altro", FontAwesome.BARS, null);


            menuVai.addItem("vai al giorno precedente", FontAwesome.ARROW_CIRCLE_LEFT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().minusDays(1), gg);
                }
            });

            menuVai.addItem("vai al giorno successivo", FontAwesome.ARROW_CIRCLE_RIGHT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    int gg = tabComponent.getNumGiorni();
                    creaGrid(tabComponent.getDataStart().plusDays(1), gg);
                }
            });

            menuVai.addItem("cerca periodo", FontAwesome.SEARCH, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    navigator.navigateTo(ADDR_SEARCH);
                }
            });

            menuVai.addItem("crea/cancella turni vuoti", FontAwesome.CALENDAR, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    navigator.navigateTo(ADDR_GENERATE);
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
     * Componente di alto livello con logica di navigazione in un turno/servizio da modificare.
     * Invocare il metodo setTurno() per inserire un turno da modificare.
     */
    private class GeneratorPage extends VerticalLayout implements View {

        public GeneratorPage() {
            setWidth("100%");
//            addStyleName("yellowBg");


            CTurniGenerator generator = new CTurniGenerator(entityManager);
//            generator.addStyleName("pinkBg");

            generator.addDismissListener(new CTabelloneEditor.DismissListener() {
                @Override
                public void editorDismissed(CTabelloneEditor.DismissEvent e) {
                    navigator.navigateTo(ADDR_TABELLONE);
                }
            });

            addComponent(generator);
            setComponentAlignment(generator, Alignment.MIDDLE_CENTER);

        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
        }

    }


    /**
     * Componente View con MenuBar comandi ricerca periodo.
     */
    private class SearchPage extends VerticalLayout implements View {

        private SearchForm form;

        /**
         * Constructor
         */
        public SearchPage() {
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
