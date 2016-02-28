package it.algos.wam.tabellone;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import java.time.LocalDate;
import java.util.Date;

/**
 * Componente che rappresenta un tabellone con servizi e giorni.
 * Sulle righe sono rappresentati i servizi, sulle colonne i giorni.
 * Created by alex on 20/02/16.
 */
public class CTabellone extends GridLayout {

    private Date dStart;
    private Date dEnd;

    public CTabellone(RTabellone... righe) {

        addStyleName("yellowBg");
        addStyleName("ctabellone");

        //setWidth("100%");
        setMargin(true);
        setSpacing(true);

        // determina il numero di colonne turni (giorni)
        int giorni=0;
        for(RTabellone riga : righe){
            int quanti=riga.getTurni().length;
            if (quanti>giorni){
                giorni=quanti;
            }
        }

        // dimensiona il layout
        setColumns(2+giorni);     // prima colonna nome servizio, seconda colonna i ruoli, le successive i turni
        setRows(1+righe.length);    // la prima riga per i giorni, le successive per i turni

        // aggiunge le date (prima riga)
        LocalDate d1 = LocalDate.now();
        for(int i=0; i<giorni;i++){
            LocalDate d = d1.plusDays(i);
            addComponent(new CGiorno(d),i+2,0);
        }


        // aggiunge le righe successive
        int col=0;
        int row=0;
        for(RTabellone riga : righe){
            row++;

            addComponent(riga.getServizio(), 0, row);

            addComponent(riga.getRuoli(), 1, row);

            col=2;
            for (CTurno t : riga.getTurni()){
                addComponent(t, col, row);
                col++;
            }

        }


        /* Espandibilità delle colonne:
        * Le prime 2 colonne (servizi e ruoli) sono fisse,
        * le altre (turni) riempiono lo spazio disponibile
        * in modo equamente distribuito */
        for (int i = 0; i < getColumns(); i++) {
            if(i==0 | i==1) {
                setColumnExpandRatio(i,0);
            }else{
                setColumnExpandRatio(i,100);
            }
        }


//        // inizialmente tutte le righe espandibili nello stesso modo
//        int nRows=getRows();
//        for (int i = 0; i < nRows; i++) {
//            setRowExpandRatio(i,100);
//        }

//        // poi definisce quali sono le righe non espandibili
//        setRowExpandRatio(0,0);
//        setRowExpandRatio(1,0);



    }

}
