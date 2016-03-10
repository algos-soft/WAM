package it.algos.wam.tabellone;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.screen.ErrorScreen;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * Tabellone con menubar dei comandi.
 * <p>
 * Created by alex on 09/03/16.
 */
public class Tabellone extends VerticalLayout implements View {

    private MenuBar menuBar = new MenuBar();
    //    private VerticalLayout placeholder =new VerticalLayout();
    private TabComponent tabComponent;
    private EditComponent editComponent;
    private SearchComponent searchComponent;

    private Navigator navigator;

    public Tabellone() {

        setSizeFull();

        fillMenu();

        tabComponent = new TabComponent(menuBar);
        creaGrid(LocalDate.now());

        editComponent = new EditComponent();
        searchComponent = new SearchComponent();

        navigator = new Navigator(UI.getCurrent(), this);
        navigator.addView("tabellone", tabComponent);
        navigator.addView("edit", editComponent);
        navigator.addView("search", searchComponent);
        navigator.setErrorView(new TabErrView());
        navigator.navigateTo("tabellone");

    }

    private void fillMenu() {
        menuBar.addItem("precedente", FontAwesome.ARROW_LEFT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate current = tabComponent.getDataStart();
                if (current != null) {
                    creaGrid(current.minusWeeks(1));
                } else {
                    Notification.show("Data corrente nulla!", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        menuBar.addItem("da lunedì", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate d1 = LocalDate.now();
                int numDow = d1.getDayOfWeek().getValue();
                LocalDate d2 = d1.minusDays(numDow - 1);
                creaGrid(d2);
            }
        });

        menuBar.addItem("da oggi", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                creaGrid(LocalDate.now());
            }
        });

        menuBar.addItem("successiva", FontAwesome.ARROW_RIGHT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate current = tabComponent.getDataStart();
                if (current != null) {
                    creaGrid(current.plusWeeks(1));
                } else {
                    Notification.show("Data corrente nulla!", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        menuBar.addItem("cerca", FontAwesome.SEARCH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                navigator.navigateTo("search");
            }
        });


    }

//    public void setContent(GridTabellone grid){
//        tabComponent.setGrid(grid);
//    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    /**
     * Crea una GridTabellone a partire dalla data richiesta e la mette
     * nel componente visibile
     *
     * @param d1           la data iniziale
     * @param quantiGiorni il numero di giorni da visualizzare
     */
    private void creaGrid(LocalDate d1, int quantiGiorni) {
        WTabellone wrapper = EngineTab.creaRighe(d1, quantiGiorni);
        GridTabellone grid = EngineTab.creaTabellone(wrapper);
        tabComponent.setGrid(grid);

        // aggiunge un listener per la cella cliccata al tabellone
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
     * @param d1           la data iniziale
     */
    private void creaGrid(LocalDate d1) {
        creaGrid(d1, 7);
    }


    /**
     * E' stata cliccata una cella del tabellone
     */
    private void cellClicked(CellType tipo, int col, int row, Object cellObject) {
        Turno turno = null;
        CTurnoEditor editor;
        switch (tipo) {
            case TURNO:
                turno = (Turno) cellObject;
                break;
            case NO_TURNO:
                InfoNewTurnoWrap wrapper = (InfoNewTurnoWrap) cellObject;
                LocalDate dInizio = wrapper.getData();
                Servizio serv = wrapper.getServizio();
                turno = new Turno();
                turno.setInizio(DateConvertUtils.asUtilDate(dInizio));
                turno.setServizio(serv);
                break;
        }

        editor = new CTurnoEditor(turno);
        editor.addDismissListener(new CTurnoEditor.DismissListener() {
            @Override
            public void editorDismissed(CTurnoEditor.DismissEvent e) {
                navigator.navigateTo("tabellone");
            }
        });

        editComponent.setEditor(editor);
        //navigator.navigateTo("turno");

    }


    /**
     * Componente View con MenuBar comandi tabellone e griglia tabellone.
     * Invocare setGrid per sostituire la griglia.
     */
    class TabComponent extends VerticalLayout implements View {

        private VerticalLayout gridPlaceholder = new VerticalLayout();
        private GridTabellone gridTabellone;

        public TabComponent(MenuBar menuBar) {
            addComponent(menuBar);
            addComponent(gridPlaceholder);
        }


        public void setGrid(GridTabellone grid) {
            this.gridTabellone = grid;
            gridPlaceholder.removeAllComponents();
            gridPlaceholder.addComponent(grid);
        }

        public LocalDate getDataStart() {
            LocalDate data = null;
            if (gridTabellone != null) {
                data = gridTabellone.getDataStart();
            }
            return data;
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }

    }


    /**
     * Componente View con MenuBar comandi editor turno e editor turno.
     * Invocare setEditor per sostituire l'editor.
     */
    class EditComponent extends VerticalLayout implements View {

        private VerticalLayout placeholder = new VerticalLayout();

        public EditComponent() {
            MenuBar mb = new MenuBar();
            mb.addItem("Uno", new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {

                }
            });

            mb.addItem("Due", new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {

                }
            });

            addComponent(mb);
            addComponent(placeholder);
        }


        public void setEditor(CTurnoEditor cEditor) {
            placeholder.removeAllComponents();
            placeholder.addComponent(cEditor);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }

    }


    /**
     * Componente View con MenuBar comandi ricerca perdiodo.
     */
    class SearchComponent extends VerticalLayout implements View {

        private SearchForm form;

        /**
         * Constructor
         */
        public SearchComponent() {
            setSizeFull();
            form = new SearchForm();
            addComponent(form);
            setComponentAlignment(form, Alignment.MIDDLE_CENTER);
            addStyleName("yellowBg");
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }


    }

    /**
     * Componente View con MenuBar comandi ricerca perdiodo.
     */
    class SearchForm extends AForm {

        /**
         * Constructor
         */
        public SearchForm() {
            super(new PropertysetItem());
            setSizeUndefined();
            getItem().addItemProperty("data", new ObjectProperty(new Date()));
            getItem().addItemProperty("giorni", new ObjectProperty(7));
            init();

            addStyleName("blueBg");

            setConfirmText("Conferma");



            addFormListener(new FormListener() {
                @Override
                public void cancel_() {
                    navigator.navigateTo("tabellone");
                }

                @Override
                public void commit_() {
                    int numGiorni = getNumGiorni();
                    if(numGiorni>0 && numGiorni<60){
                        LocalDate data = getDataInizio();
                        if(data!=null){
                            creaGrid(data, numGiorni);
                            navigator.navigateTo("tabellone");
                        }else{
                            Notification.show("La data non può essere nulla.", Notification.Type.WARNING_MESSAGE);
                        }
                    }else{
                        Notification.show("Minimo 1 giorno, massimo 60 giorni.", Notification.Type.WARNING_MESSAGE);
                    }
                }

            });

        }

        @Override
        public void createFields() {
            addField("data", new DateField("Data iniziale"));
            addField("giorni", new IntegerField("Numero di giorni"));
        }

        private int getNumGiorni(){
            Object obj = getFieldValue("giorni");
            return Lib.getInt(obj);
        }

        private LocalDate getDataInizio(){
            LocalDate data=null;
            Object obj = getFieldValue("data");
            if(obj!=null && obj instanceof Date){
                Date d = (Date)obj;
                data = DateConvertUtils.asLocalDate(d);
            }
            return data;
        }



    }


    /**
     * Error screen se il Navigator non trova una view
     */
    class TabErrView extends ErrorScreen implements View {

        public TabErrView() {
            super("View non trovata");
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }
    }
}
