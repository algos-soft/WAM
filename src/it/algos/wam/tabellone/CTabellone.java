package it.algos.wam.tabellone;

import com.vaadin.ui.GridLayout;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Componente che rappresenta un tabellone con servizi e giorni.
 * Sulle righe sono rappresentati i servizi, sulle colonne i giorni.
 * Created by alex on 20/02/16.
 */
public class CTabellone extends GridLayout {

    private LocalDate dStart;
    private LocalDate dEnd;

    private ArrayList<ClickCellListener> clickCellListeners = new ArrayList();

    public CTabellone(LocalDate dStart, LocalDate dEnd) {

        this.dStart = dStart;
        this.dEnd = dEnd;

        addStyleName("yellowBg");
        addStyleName("ctabellone");

        //setWidth("100%");
        setMargin(true);
        setSpacing(true);

        // determina il numero di colonne turni (giorni)

        int giorni = (int) ChronoUnit.DAYS.between(this.dStart, this.dEnd)+1;

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
        LocalDate d1 =dStart;
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
    public int getNumGiorni(){
        return (int)ChronoUnit.DAYS.between(dStart, dEnd)+1;
    }

    public void addClickCellListener(ClickCellListener l){
        clickCellListeners.add(l);
    }

    /**
     * E' stata cliccata una cella alla posizione col,row
     */
    public void cellClicked(int col, int row) {
        LocalDate data=dStart.plusDays(col-2);
        ClickCellEvent e = new ClickCellEvent(data, row);
        for(ClickCellListener l : clickCellListeners){
            l.cellClicked(e);
        }


    }

    public interface ClickCellListener {
        void cellClicked(ClickCellEvent e);
    }

    public void fireTurnoClicked(Turno turno, Servizio servizio, LocalDate data){
    }

    public class ClickCellEvent {
        private int row;
        private LocalDate data;

        public ClickCellEvent(LocalDate data, int row) {
            this.data = data;
            this.row=row;
        }

        public int getRow() {
            return row;
        }

        public LocalDate getData() {
            return data;
        }
    }

}
