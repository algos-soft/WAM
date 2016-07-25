package it.algos.wam.turnigenerator;

import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.lib.DateConvertUtils;
import org.joda.time.DateTime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;


/**
 * Wrapper dei dati di configurazione per il motore di generazione turni.
 */
public class GeneratorData {
    private LocalDate d1;
    private LocalDate d2;
    private int action;

    private HashMap<DayOfWeek, Servizio[]> mapServiziGiorno = new HashMap<>();

    public static final int ACTION_CREATE = 1;
    public static final int ACTION_DELETE = 2;

    public GeneratorData(LocalDate d1, LocalDate d2, int action) {
        this.d1 = d1;
        this.d2 = d2;
        this.action = action;
    }


    /**
     * Registra l'elenco dei servizi abilitati per un dato giorno
     * della settimana
     *
     * @param giorno il giorno della settimana
     * @param servizi l'array dei servizi del giorno
     */
    public void putGiorno(DayOfWeek giorno, Servizio[] servizi) {
        mapServiziGiorno.put(giorno, servizi);
    }


    /**
     * Controlla se i dati sono validi e congrui.
     *
     * @return stringa vuota se ok, motivo se non validi
     */
    public String checkValid() {
        if(d1==null){
            return "La data iniziale non può essere nulla";
        }

        if(d2==null){
            return "La data finale non può essere nulla";
        }

        if(d2.isBefore(d1)){
            return "La data iniziale non può essere successiva alla data finale";
        }

        if((action != ACTION_CREATE) & (action != ACTION_DELETE)){
            return "Azione "+action+" non riconosciuta";
        }

        if(mapServiziGiorno== null || mapServiziGiorno.isEmpty()){
            return "Non ci sono operazioni da eseguire (mappa nulla o vuota)";
        }

        if(!isWorkToDo()){
            return "Non ci sono operazioni da eseguire";
        }


        return "";
    }

    /**
     * Controlla se ci sono operazioni da fare
     */
    private boolean isWorkToDo(){
        boolean work=false;
        for(Servizio[] listaServ : mapServiziGiorno.values()){
            if(listaServ.length>0){
                work=true;
                break;
            }

        }
        return work;
    }

    /**
     * @return il numero di giorni da elaborare
     */
    public int getNumGiorni(){
        return (int)ChronoUnit.DAYS.between(getD1(), getD2())+1;
    }

    /**
     * @return l'elenco dei servizi per un dato giorno della settimana - null se non ce ne sono
     */
    public Servizio[] getServizi(DayOfWeek dow){
        return mapServiziGiorno.get(dow);
    }

    public LocalDate getD1() {
        return d1;
    }

    public LocalDate getD2() {
        return d2;
    }

    public int getAction() {
        return action;
    }
}
