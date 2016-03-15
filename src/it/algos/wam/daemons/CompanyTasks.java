package it.algos.wam.daemons;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.EM;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Task eseguito periodicamente (vedi WamScheduler).
 * Esegue del codice per ogni Company.
 * Uso:
 * E' un Runnable, implementare il codice da eseguire nel metodo
 * run(Company company) che viene eseguito una volta per ogni Company.
 */
public class CompanyTasks implements Runnable {
    private final static Logger logger = Logger.getLogger(CompanyTasks.class.getName());


    /**
     * Esegue un task per tutte le company.<p>
     * ATTENZIONE! Questa procedura è invocata da un thread sul server.
     * Quindi non abbiamo sessioni e di conseguenza non abbiamo una company corrente!
     * Pertanto tutte le chiamate che partono da qui devono:
     * - 1) NON usare mai CompanyQuery perché la company è nulla;
     * - 2) Passare sempre esplicitamente la company ai metodi chiamati;
     * - 3) Assegnare esplicitamente la Company al record se creano dei record.
     */
    @Override
    public void run() {

        // spazzola tutte le company
        // se la preferenza è ON e l'ora corrisponde, esegue il controllo
        logger.log(Level.INFO, "start esecuzione tasks per tutte le company");

        DateTime dt = new DateTime();
        //int currentHour = dt.getHourOfDay();

        EntityManager manager = EM.createEntityManager();
        JPAContainer<WamCompany> companies = JPAContainerFactory.makeNonCachedReadOnly(WamCompany.class, manager);
        for (Iterator<Object> i = companies.getItemIds().iterator(); i.hasNext(); ) {

            Object itemId = i.next();
            EntityItem<WamCompany> item = companies.getItem(itemId);
            WamCompany company = item.getEntity();
            run(company);

//            boolean doChecks = Lib.getBool(CompanyPrefs.doRunSolleciti.get(company));
//            if (doChecks) {
//                int checkHour = Lib.getInt(CompanyPrefs.oraRunSolleciti.get(company));
//                if (checkHour == currentHour) {
//                    run(company);
//                }
//            }

        }
        manager.close();

        logger.log(Level.INFO, "end esecuzione tasks per tutte le company");

    }


    /**
     * Esegue tutti i tasks per una data company.
     *
     * @param company la company
     */
    public void run(WamCompany company) {

        logger.log(Level.INFO, "start tasks "+company);

        // qui il codice da eseguire per ogni Company
        // ...

        logger.log(Level.INFO, "end tasks "+company);
    }



}
