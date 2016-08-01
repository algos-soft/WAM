package it.algos.wam.daemons;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import it.algos.wam.email.WamEmailService;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.DateConvertUtils;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
    EntityManager manager;

    public CompanyTasks() {
        manager = EM.createEntityManager();
    }

    /**
     * Esegue un task per tutte le company.<p>
     * ATTENZIONE! Questa procedura è invocata da un thread sul server.
     * Quindi non abbiamo sessioni e di conseguenza non abbiamo una company corrente!
     * Pertanto tutte le chiamate che partono da qui devono:
     * - 1) NON usare mai CompanyQuery perché la company nella sessione è nulla;
     * - 2) Passare sempre esplicitamente la company ai metodi chiamati;
     * - 3) Assegnare esplicitamente la company al record se creano dei record.
     */
    @Override
    public void run() {

        // spazzola tutte le company ed esegue il controllo
        manager = EM.createEntityManager();
        JPAContainer<WamCompany> companies = JPAContainerFactory.makeNonCachedReadOnly(WamCompany.class, manager);
        for (Iterator<Object> i = companies.getItemIds().iterator(); i.hasNext(); ) {

            Object itemId = i.next();
            EntityItem<WamCompany> item = companies.getItem(itemId);
            WamCompany company = item.getEntity();
            run(company);

        }
        manager.close();


    }


    /**
     * Esegue tutti i tasks per una singola company.
     * - 1) NON usare mai CompanyQuery perché la company nella sessione è nulla;
     * - 2) Passare sempre esplicitamente la company ai metodi chiamati;
     * - 3) Assegnare esplicitamente la company al record se creano dei record.
     *
     * @param company la company
     */
    public void run(WamCompany company) {

        logger.log(Level.INFO, "start tasks " + company);

        // invia le notifiche per i turni che iniziano prossimamente

        if(CompanyPrefs.inviaNotificaInizioTurno.getBool(company)){
            try {
                notificaInizioTurno(company);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // qui eventuale altro codice da eseguire per ogni Company
        // ...

    }

    /**
     * Manda una notifica a tutti i volontari che iniziano il turno prossimamente
     */
    private void notificaInizioTurno(WamCompany company) throws Exception {

        int ore = CompanyPrefs.quanteOrePrimaNotificaInizioTurno.getInt(company);

        // controlla che la funzione sia correttamente configurata
        if(ore<=0){
            throw new Exception("ore prima notifica inizio turno non configurate");
        }

        String addr=CompanyPrefs.senderAddress.getString(company);
        if(addr.equals("")){
            throw new Exception("indirizzo mittente non configurato");
        }

        // recupera l'elenco delle iscrizioni che iniziano prossimamente e non sono ancora state notificate
        Iscrizione[] iscrizioniDaNotificare = getIscrizioniDaNotificare(company);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM 'ore' HH:mm");

        // per ogni iscrizione, invia la notifica
        for (Iscrizione iscrizione : iscrizioniDaNotificare) {
            Volontario volontario = iscrizione.getVolontario();
            String email = volontario.getEmail();
            if (email != null && !email.isEmpty()) {

                Turno turno = iscrizione.getTurno();
                Servizio serv = turno.getServizio();

                // stringa con data e ora di inizio
                Date dataInizio = turno.getInizio();
                int oraInizio = serv.getOraInizio();
                int minutoInizio = serv.getMinutiInizio();
                LocalDate date = DateConvertUtils.asLocalDate(dataInizio);
                LocalDateTime datetime = date.atStartOfDay();
                datetime=datetime.plusHours(oraInizio);
                datetime=datetime.plusMinutes(minutoInizio);
                Date newDate = DateConvertUtils.asUtilDate(datetime);
                String dataora = dateFormat.format(newDate);

                String subject = "Inizio turno " + serv.getDescrizione() + " il " + dataora;

                String text = subject;

                List<Iscrizione> iscrizioni = turno.getIscrizioni();
                if (iscrizioni.size() > 0) {
                    text = "Attualmente sono iscritti:";
                    for (Iscrizione i : iscrizioni) {
                        Funzione funz = i.getServizioFunzione().getFunzione();
                        text += "\n- ";
                        text += i.getVolontario().getNickname();
                        text += " (";
                        text += funz.getSiglaVisibile();
                        text += ")";
                    }
                }

                try {
                    WamEmailService.sendMail(company, email, subject, text);
                } catch (EmailException e) {
                    e.printStackTrace();
                }

            }// end if

        }// end for

    }


    /**
     * Recupera tutte le iscrizioni non notificate relative a turni
     * che iniziano entro un certo numero di ore da adesso,
     * per una data company
     *
     * @param company     la company
     */
    private Iscrizione[] getIscrizioniDaNotificare(WamCompany company) {

        int oreInAvanti=CompanyPrefs.quanteOrePrimaNotificaInizioTurno.getInt(company);

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Iscrizione> cq = cb.createQuery(Iscrizione.class);
        Root<Iscrizione> root = cq.from(Iscrizione.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(CompanyEntity_.company), company));
        predicates.add(cb.equal(root.get(Iscrizione_.notificaInviata), false));

        // le iscrizioni che iniziano entro oreInAvanti da adesso
        LocalDateTime currDateTime = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime maxDateTime = currDateTime.plusHours(oreInAvanti);
        Join<Iscrizione, Turno> jTurno = root.join(Iscrizione_.turno);
        predicates.add(cb.greaterThan(jTurno.get(Turno_.inizio), DateConvertUtils.asUtilDate(currDateTime)));
        predicates.add(cb.lessThanOrEqualTo(jTurno.get(Turno_.inizio), DateConvertUtils.asUtilDate(maxDateTime)));

        cq.where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Iscrizione> q = manager.createQuery(cq);
        List<Iscrizione> lista = q.getResultList();

        return lista.toArray(new Iscrizione[0]);
    }




}
