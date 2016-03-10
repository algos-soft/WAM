package it.algos.wam.tabellone;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.screen.ErrorScreen;

import java.time.LocalDate;

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

    private Navigator navigator;

    public Tabellone() {

        fillMenu();

        tabComponent = new TabComponent(menuBar);
        editComponent = new EditComponent();
//        tabComponent.addComponent(menuBar);
//        tabComponent.addComponent(placeholder);

//        addComponent(menuBar);
//        addComponent(placeholder);

        navigator = new Navigator(UI.getCurrent(), this);
        navigator.addView("tabellone", tabComponent);
        navigator.addView("edit", editComponent);
        navigator.setErrorView(new TabErrView());
        navigator.navigateTo("tabellone");

    }

    private void fillMenu() {
        menuBar.addItem("precedente", FontAwesome.ARROW_LEFT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate d1 = LocalDate.of(2016, 3, 2);
                creaGrid(d1);
//                WTabellone wrapper = EngineTab.creaRighe(d1,7);
//                GridTabellone grid = EngineTab.creaTabellone(wrapper);
//                setContent(grid);
            }
        });

        menuBar.addItem("da lunedì", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate d1 = LocalDate.of(2016, 3, 2);
                creaGrid(d1);
//                WTabellone wrapper = EngineTab.creaRighe(d1,7);
//                GridTabellone grid = EngineTab.creaTabellone(wrapper);
//                setContent(grid);
            }
        });

        menuBar.addItem("da oggi", FontAwesome.CALENDAR_O, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate d1 = LocalDate.of(2016, 3, 2);
                creaGrid(d1);
//                WTabellone wrapper = EngineTab.creaRighe(d1,7);
//                GridTabellone grid = EngineTab.creaTabellone(wrapper);
//                setContent(grid);
            }
        });



        menuBar.addItem("successiva", FontAwesome.ARROW_RIGHT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                LocalDate d1 = LocalDate.of(2016, 3, 9);
                creaGrid(d1);

//                WTabellone wrapper = EngineTab.creaRighe(d1,7);
//                GridTabellone grid = EngineTab.creaTabellone(wrapper);
//                setContent(grid);
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
     */
    private void creaGrid(LocalDate d1) {
        WTabellone wrapper = EngineTab.creaRighe(d1, 7);
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

        public TabComponent(MenuBar menuBar) {
            addComponent(menuBar);
            addComponent(gridPlaceholder);
        }


        public void setGrid(GridTabellone grid) {
            gridPlaceholder.removeAllComponents();
            gridPlaceholder.addComponent(grid);
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
