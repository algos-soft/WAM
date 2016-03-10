package it.algos.wam.tabellone;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Componente che rappresenta un tabellone con servizi e giorni.
 * Sulle righe sono rappresentati i servizi, sulle colonne i giorni.
 * Created by alex on 20/02/16.
 */
public class GridTabellone extends GridLayout implements View {

    public static final String W_COLONNE_TURNI="7em";   // larghezza fissa delle colonne turni
    private LocalDate dStart;
    private LocalDate dEnd;

    private ArrayList<ClickCellListener> clickCellListeners = new ArrayList();

    public GridTabellone(LocalDate dStart, LocalDate dEnd) {

        this.dStart = dStart;
        this.dEnd = dEnd;

        //addStyleName("yellowBg");

        addStyleName("ctabellone");

        setSizeUndefined();

        //setWidth("100%");
        //setMargin(true);
        setSpacing(true);

        // determina il numero di colonne turni (giorni)

        int giorni = (int) ChronoUnit.DAYS.between(this.dStart, this.dEnd) + 1;

        // dimensiona il layout
        setColumns(2 + giorni);     // prima colonna nome servizio, seconda colonna i ruoli, le successive i turni

        /* Regola l'espandibilità delle colonne:
         * Le prime 2 colonne (servizi e ruoli) sono fisse,
         * le altre (turni) riempiono lo spazio disponibile
         * in modo equamente distribuito */
        for (int i = 0; i < getColumns(); i++) {
            if (i == 0 | i == 1) {
                setColumnExpandRatio(i, 0);
            } else {
                setColumnExpandRatio(i, 100);
            }
        }

        setRows(1);    // la prima riga per i giorni, le successive per i servizi

        // aggiunge le date (prima riga)
        LocalDate d1 = dStart;
        for (int i = 0; i < giorni; i++) {
            LocalDate d = d1.plusDays(i);
            addComponent(new CGiorno(d), i + 2, 0);
        }


//        // aggiunge le righe successive
//        int col=0;
//        int row=0;
//        for(RTabellone riga : righe){
//            row++;
//
//            addComponent(riga.getServizio(), 0, row);
//
//            addComponent(riga.getRuoli(), 1, row);
//
//            col=2;
//            for (CTurno t : riga.getTurni()){
//                addComponent(t, col, row);
//                col++;
//            }
//
//        }


//        // inizialmente tutte le righe espandibili nello stesso modo
//        int nRows=getRows();
//        for (int i = 0; i < nRows; i++) {
//            setRowExpandRatio(i,100);
//        }

//        // poi definisce quali sono le righe non espandibili
//        setRowExpandRatio(0,0);
//        setRowExpandRatio(1,0);


    }

    public LocalDate getDataStart() {
        return dStart;
    }

    public LocalDate getDataEnd() {
        return dEnd;
    }

    /**
     * @return il numero di giorni rappresentati dal tabellone
     */
    public int getNumGiorni() {
        return (int) ChronoUnit.DAYS.between(dStart, dEnd) + 1;
    }

    public void addClickCellListener(ClickCellListener l) {
        clickCellListeners.add(l);
    }

    /**
     * E' stata cliccata una cella
     *
     * @param tipo       il tipo di cella
     * @param col        la colonna
     * @param row        la riga
     * @param cellObject l'oggetto di riferimento per la cella
     */
    public void cellClicked(CellType tipo, int col, int row, Object cellObject) {
        ClickCellEvent e = new ClickCellEvent(tipo, col, row, cellObject);
        for (ClickCellListener l : clickCellListeners) {
            l.cellClicked(e);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    public interface ClickCellListener {
        void cellClicked(ClickCellEvent e);
    }


    public class ClickCellEvent {
        private CellType tipo;
        private int col;
        private int row;
        private Object cellObject;

        public ClickCellEvent(CellType tipo, int col, int row, Object cellObject) {
            this.tipo=tipo;
            this.col = col;
            this.row = row;
            this.cellObject = cellObject;
        }

        public CellType getTipo() {
            return tipo;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public Object getCellObject() {
            return cellObject;
        }
    }

}
