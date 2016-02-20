package it.algos.wam.tabellone;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import java.util.Date;

/**
 * Componente che rappresenta un tabellone con servizi e giorni.
 * Sulle righe sono rappresentati i serizi, sulle colonne i giorni.
 * Created by alex on 20/02/16.
 */
public class CTabellone extends GridLayout {

    private Date dStart;
    private Date dEnd;

    public CTabellone(RTabellone... righe) {

        // determina il numero di colonne turni
        int colTurni=0;
        for(RTabellone riga : righe){
            int quanti=riga.getTurni().length;
            if (quanti>colTurni){
                colTurni=quanti;
            }
        }

        // dimensiona il layout
        setColumns(2+colTurni);     // prima colonna nome servizio, seconda colonna i ruoli, le successive i turni
        setRows(1+righe.length);    // la prima riga per i giorni, le successive per i turni

        // aggiunge i componenti
        int col=0;
        int row=0;
        for(RTabellone riga : righe){

        }

    }

}
