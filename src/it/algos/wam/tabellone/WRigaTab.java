package it.algos.wam.tabellone;

import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;

/**
 * Contenitore degli elementi necessari per creare una riga di tabellone.
 * Questo non è un componente grafico ma solo un wrapper.
 * Questo wrapper è dato in pasto all'engine che crea i componenti grafici visualizzati nel tabellone.
 * Created by alex on 01/03/16.
 */
public class WRigaTab {

    private Servizio servizio;
    private Turno[] turni;

    public WRigaTab(Servizio servizio, Turno[] turni) {
        this.servizio = servizio;
        this.turni = turni;
    }

    public Servizio getServizio() {
        return servizio;
    }

    public void setServizio(Servizio servizio) {
        this.servizio = servizio;
    }

    public Turno[] getTurni() {
        return turni;
    }

    public void setTurni(Turno[] turni) {
        this.turni = turni;
    }

    /**
     * Ritorna la data minima tra le date dei turni
     *
     * @return la data minima
     */
    public LocalDate getMinDate() {
        LocalDate date = null;
        for (Turno t : turni) {
            LocalDate inizioTurno = DateConvertUtils.asLocalDate(t.getInizio());
            if (inizioTurno != null) {
                if (date == null || inizioTurno.isBefore(date)) {
                    date = inizioTurno;
                }
            }
        }
        return date;
    }

    /**
     * Ritorna la data massima tra le date dei turni
     *
     * @return la data massima
     */
    public LocalDate getMaxDate() {
        LocalDate date = null;
        for (Turno t : turni) {
            LocalDate fineTurno = DateConvertUtils.asLocalDate(t.getFine());
            if(fineTurno!=null){
                if (date == null || fineTurno.isAfter(date)) {
                    date = fineTurno;
                }
            }
        }
        return date;
    }

    /**
     * Ritorna il turno corrispondente a una certa data
     * @return il turno, null se non previsto turno nella data specificata
     */
    public Turno getTurno(LocalDate currDate) {
        Turno turno=null;
        for(Turno t : turni){
            LocalDate dStart=DateConvertUtils.asLocalDate(t.getInizio());
            if(dStart.equals(currDate)){
                turno=t;
                break;
            }
        }
        return turno;
    }

}
