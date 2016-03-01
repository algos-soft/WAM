package it.algos.wam.tabellone;

import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Contenitore di un insieme di righe di tabellone di tabellone.
 * Questo non è un componente grafico ma solo un wrapper.
 * Questo wrapper è dato in pasto all'engine che crea graficamente il tabellone.
 * Created by alex on 01/03/16.
 */
public class WRigheTab extends ArrayList<WRigaTab>{

    /**
     * Determina la data minima tra tutti i turni delle varie righe
     * @return la data minima
     */
    public LocalDate getMinDate() {
        LocalDate date=null;
        for(WRigaTab riga : this){
            LocalDate inizio= riga.getMinDate();
            if(inizio!=null){
                if (date==null || inizio.isBefore(date)){
                    date=inizio;
                }
            }
        }
        return date;
    }


    /**
     * Determina la data massima tra tutti i turni delle varie righe
     * @return la data massima
     */
    public LocalDate getMaxDate() {
        LocalDate date=null;
        for(WRigaTab riga : this){
            LocalDate fine= riga.getMaxDate();
            if(fine!=null){
                if (date==null || fine.isAfter(date)){
                    date=fine;
                }
            }
        }
        return date;
    }


}
