package it.algos.wam.turnigenerator;

import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Motore di generazione/eliminazione di turni
 */
public class TurniGenEngine {

    private GeneratorData data;
    private ArrayList<GiornoProgressListener> giornoProgressListeners = new ArrayList<>();
    private ArrayList<EngineDoneListener> engineDoneListeners = new ArrayList<>();
    private boolean abort;
    private long lastUpdateMillis;
    private EntityManager em;

    /**
     * @param data i dati di impostazione del motore
     */
    public TurniGenEngine(GeneratorData data, EntityManager em) {
        this.data = data;
        this.em = em;
    }


    /**
     * Avvia il motore
     */
    public void start() {
        boolean localEM = false;

        // se manca l'EntityManager lo crea ora
        if (this.em == null) {
            this.em = EM.createEntityManager();
            localEM = true;
        }

        abort = false;
        em.getTransaction().begin();

        lastUpdateMillis = System.currentTimeMillis();

        int quantiGiorni = getQuantiGiorni();

        try {
            for (int i = 0; i < quantiGiorni; i++) {

                // do work per il giorno
                LocalDate giorno = data.getD1().plusDays(i);
                elaboraGiorno(giorno);

                // progress update - max 1 al secondo
                if ((System.currentTimeMillis() - lastUpdateMillis) > 1000) {
                    lastUpdateMillis = System.currentTimeMillis();
                    fireGiornoProgressListeners(i + 1);
                }

                // se abort è acceso forza uscita
                if (abort) {
                    break;
                }

            }


        } catch (Exception e) {
            abort = true;
            e.printStackTrace();
        }


        if (!abort) {
            em.getTransaction().commit();
        } else {
            em.getTransaction().rollback();
        }

        // se l'EntityManager è stato creato localmente lo chiude
        if (localEM) {
            em.close();
        }

        fireEngineDoneListeners(!abort);

    }


    /**
     * Elabora tutti i servizi per un dato giorno
     *
     * @param giorno il giorno
     */
    private void elaboraGiorno(LocalDate giorno) {

        DayOfWeek dow = giorno.getDayOfWeek();
        Servizio[] servizi = data.getServizi(dow);

        if (servizi != null) {
            for (Servizio serv : servizi) {
                elaboraServizioGiorno(serv, giorno);
            }
        }

    }

    /**
     * Elabora un servizi per un dato giorno
     *
     * @param serv   il servizio
     * @param giorno il giorno
     */
    private void elaboraServizioGiorno(Servizio serv, LocalDate giorno) {
        Turno turno;

        switch (data.getAction()) {

            case GeneratorData.ACTION_CREATE:

                // crea il turno solo se non esiste già
                turno = WamQuery.queryTurnoServizioGiorno(em, serv, giorno);
                if (turno == null) {
                    turno = new Turno(serv, DateConvertUtils.asUtilDate(giorno));
//                    turno.setServizio(serv);
//                    turno.setInizio(DateConvertUtils.asUtilDate(giorno));
                    em.persist(turno);
                }
                break;

            case GeneratorData.ACTION_DELETE:
                // cancella il turno se esiste e non ci sono iscrizioni
                turno = WamQuery.queryTurnoServizioGiorno(em, serv, giorno);
                if (turno != null) {
                    List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniTurno(em, turno);
                    if (iscrizioni.size() == 0) {
                        em.remove(turno);
                    }
                }
                break;
        }
    }


    /**
     * Ritorna il numero totale di turni da creare / cancellare
     */
    public int getQuantiGiorni() {
        return data.getNumGiorni();
    }


    /**
     * Abortisce l'esecuzione
     */
    public void abort() {
        abort = true;
    }


    /**
     * Notifica tutti i listeners di giorno eseguito
     *
     * @param progress il numero di giorni fatti in totale
     */
    private void fireGiornoProgressListeners(int progress) {
        for (GiornoProgressListener l : giornoProgressListeners) {
            l.progressUpdate(progress);
        }
    }

    public void addGiornoProgressListener(GiornoProgressListener l) {
        giornoProgressListeners.add(l);
    }

    /**
     * @param success = true se il lavoro dell'engine è terminato
     *                correttamente e la transazione è stata conclusa
     */
    private void fireEngineDoneListeners(boolean success) {
        for (EngineDoneListener l : engineDoneListeners) {
            l.engineDone(success);
        }
    }

    public void addEngineDoneListener(EngineDoneListener l) {
        engineDoneListeners.add(l);
    }


    public interface GiornoProgressListener {
        /**
         * Invocato ogni volta che un giorno è fatto
         *
         * @param progress il numero di giorni fatti in totale
         */
        void progressUpdate(int progress);
    }


    public interface EngineDoneListener {
        /**
         * @param success = true se il lavoro dell'engine è terminato
         *                correttamente e la transazione è stata conclusa
         */
        void engineDone(boolean success);
    }

}
