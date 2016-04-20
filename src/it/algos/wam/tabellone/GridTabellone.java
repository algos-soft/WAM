package it.algos.wam.tabellone;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
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

    public static final String W_COLONNE_TURNI = "8em";   // larghezza fissa delle colonne turni
    public static final String H_ISCRIZIONI = "1.6em";   // altezza fissa delle celle iscrizione

    private LocalDate dStart;
    private LocalDate dEnd;

    private ArrayList<ClickCellListener> clickCellListeners = new ArrayList();

    public GridTabellone(LocalDate dStart, LocalDate dEnd) {

        this.dStart = dStart;
        this.dEnd = dEnd;

        //addStyleName("yellowBg");

        addStyleName("ctabellone");

        setSizeUndefined();

        setSpacing(true);

        // determina il numero di colonne turni (giorni)
        int giorni = (int) ChronoUnit.DAYS.between(this.dStart, this.dEnd) + 1;

        // dimensiona il layout
        setColumns(1 + giorni);     // prima colonna nome servizio, seconda colonna i ruoli, le successive i turni

        setRows(1);    // la prima riga per i giorni, le successive per i servizi

        // aggiunge le date (prima riga)
        LocalDate d1 = dStart;
        for (int i = 0; i < giorni; i++) {
            LocalDate d = d1.plusDays(i);
            addComponent(new CGiorno(d), i + 1, 0);
        }

    }


    /**
     * Metodo addComponent sovrascritto per registrare le coordinate
     * nelle celle che vengono aggiunte al tabellone
     *
     * @param comp il componente
     * @param col  la colonna
     * @param row  la riga
     */
    @Override
    public void addComponent(Component comp, int col, int row) throws OverlapsException, OutOfBoundsException {
        if (comp != null && comp instanceof TabelloneCell) {
            TabelloneCell tcell = (TabelloneCell) comp;
            tcell.setX(col);
            tcell.setY(row);
        }
        super.addComponent(comp, col, row);
    }

    /**
     * Metodo removeRow sovrascritto per scalare di 1 verso l'alto
     * l'indice di riga di tutte le celle che restano nel tabellone
     * nelle righe successive a quella cancellata
     *
     * @param row  la riga
     */
    @Override
    public void removeRow(int row) {
        int maxRow = getRows();
        int maxCol = getColumns();
        for(int r=row+1; r<maxRow; r++){
            for(int c=0; c<maxCol; c++){
                Component comp = getComponent(c, r);
                if (comp != null && comp instanceof TabelloneCell) {
                    TabelloneCell tcell = (TabelloneCell) comp;
                    tcell.setY(tcell.getY()-1);
                }
            }
        }
        super.removeRow(row);
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
            this.tipo = tipo;
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
