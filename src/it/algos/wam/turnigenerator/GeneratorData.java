package it.algos.wam.turnigenerator;

import it.algos.wam.entity.servizio.Servizio;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashMap;

/**
 * Wrapper dei dati di configurazione per il motore di generazione turni.
 */
public class GeneratorData {
    private Date d1;
    private Date d2;
    private int action;

    private HashMap<Integer, Servizio[]> mapServiziGiorno = new HashMap<>();

    public static final int ACTION_CREATE = 1;
    public static final int ACTION_DELETE = 2;

    public GeneratorData(Date d1, Date d2, int action) {
        this.d1 = d1;
        this.d2 = d2;
        this.action = action;
    }


    /**
     * Registra l'elenco dei servizi abilitati per un dato giorno
     * della settimana
     *
     * @param giorno l'indice del giorno (0=lun)
     * @param servizi l'array dei servizi del giorno
     */
    public void putGiorno(int giorno, Servizio[] servizi) {
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

        DateTime dt1=new DateTime(d1);
        DateTime dt2=new DateTime(d2);
        if(dt2.isBefore(dt1)){
            return "La data iniziale non può essere successiva alla data finale";
        }

        if((action != ACTION_CREATE) & (action != ACTION_DELETE)){
            return "Azione "+action+" non riconosciuta";
        }

        if(mapServiziGiorno.isEmpty()){
            return "Non ci sono operazioni da eseguire";
        }

        return "";
    }

    public Date getD1() {
        return d1;
    }

    public Date getD2() {
        return d2;
    }

    public int getAction() {
        return action;
    }
}
