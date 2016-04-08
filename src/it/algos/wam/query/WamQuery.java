package it.algos.wam.query;

import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.Lib;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Query specifiche del progranna WAM.
 * Created by alex on 11-03-2016.
 */
public class WamQuery {


    /**
     * Tutti i turni relativi a un dato servizio, che hanno data di inizio compresa in un certo periodo.
     * La lista è ordinata per data inizio turno.
     *
     * @param em       l'EntityManager da utilizzare (se nullo lo crea qui)
     * @param servizio il servizio di riferimento
     * @param d1       la data di inizio periodo
     * @param d1       la data di fine periodo
     * @return la lista dei turni in ordine di data inizio
     */
    public static List<Turno> queryTurni(EntityManager em, Servizio servizio, LocalDate d1, LocalDate d2) {

        Date data1 = DateConvertUtils.asUtilDate(d1);
        Date data2 = DateConvertUtils.asUtilDate(d2);

        // se non specificato EM, ne crea uno locale
        boolean localEM = false;
        if (em == null) {
            em = EM.createEntityManager();
            localEM = true;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Turno> cq = cb.createQuery(Turno.class);
        Root<Turno> root = cq.from(Turno.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(CompanyQuery.creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Turno_.servizio), servizio));
        predicates.add(cb.greaterThanOrEqualTo(root.get(Turno_.inizio), data1));
        predicates.add(cb.lessThanOrEqualTo(root.get(Turno_.inizio), data2));
        cq.where(predicates.toArray(new Predicate[]{}));
        cq.orderBy(cb.asc(root.get(Turno_.inizio)));

        TypedQuery<Turno> q = em.createQuery(cq);
        List<Turno> turni = q.getResultList();

        // eventualmente chiude l'EM locale
        if (localEM) {
            em.close();
        }

        return turni;
    }


    /**
     * Tutti i servizi orari da visualizzare nel tabellone (orario=true).
     * Elencati nell'ordine di apparizione
     *
     * @param em     l'EntityManager da utilizzare (se nullo lo crea qui)
     * @param orario true per i servizi orari, false per i servizi variabili
     * @return la lista dei servizi
     */
    public static List<Servizio> queryServizi(EntityManager em, boolean orario) {

        // se non specificato EM, ne crea uno locale
        boolean localEM = false;
        if (em == null) {
            em = EM.createEntityManager();
            localEM = true;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Servizio> cq = cb.createQuery(Servizio.class);
        Root<Servizio> root = cq.from(Servizio.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(CompanyQuery.creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Servizio_.orario), orario));
        cq.where(predicates.toArray(new Predicate[]{}));
        cq.orderBy(cb.asc(root.get(Servizio_.ordine)));


        TypedQuery<Servizio> q = em.createQuery(cq);
        List<Servizio> servizi = q.getResultList();


        // eventualmente chiude l'EM locale
        if (localEM) {
            em.close();
        }

        return servizi;

    }


    /**
     * Tutte le iscrizioni relative a un dato ServizioFunzione.
     *
     * @param em l'EntityManager da utilizzare (se nullo lo crea qui)
     * @return la lista delle iscrizioni
     */
    public static List<Iscrizione> queryIscrizioniServizioFunzione(EntityManager em, ServizioFunzione sf) {

        // se non specificato EM, ne crea uno locale
        boolean localEM = false;
        if (em == null) {
            em = EM.createEntityManager();
            localEM = true;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Iscrizione> cq = cb.createQuery(Iscrizione.class);
        Root<Iscrizione> root = cq.from(Iscrizione.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(CompanyQuery.creaFiltroCompany(root, cb));
        predicates.add(cb.equal(root.get(Iscrizione_.servizioFunzione), sf));
        cq.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Iscrizione> q = em.createQuery(cq);
        List<Iscrizione> lista = q.getResultList();


        // eventualmente chiude l'EM locale
        if (localEM) {
            em.close();
        }

        return lista;

    }


    /**
     * Il servizio adiacente ad un dato servizio (per numero d'ordine).
     *
     * @param em       l'EntityManager da utilizzare
     * @param servizio il servizio di riferimento
     * @param sopra    true per cercare sopra, false per cercare sotto
     * @return il servizio adiacente, null se non trovato
     */
    public static Servizio queryServizioAdiacente(EntityManager em, Servizio servizio, boolean sopra) {

        Servizio adiacente = null;

        // se non specificato EM, ne crea uno locale
        boolean localEM = false;
        if (em == null) {
            em = EM.createEntityManager();
            localEM = true;
        }

        int numOrdine = servizio.getOrdine();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Servizio> cq = cb.createQuery(Servizio.class);
        Root<Servizio> root = cq.from(Servizio.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(CompanyQuery.creaFiltroCompany(root, cb));
        if (sopra) {
            predicates.add(cb.lessThan(root.get(Servizio_.ordine), numOrdine));
            cq.orderBy(cb.desc(root.get(Servizio_.ordine)));
        } else {
            predicates.add(cb.greaterThan(root.get(Servizio_.ordine), numOrdine));
            cq.orderBy(cb.asc(root.get(Servizio_.ordine)));
        }
        cq.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Servizio> q = em.createQuery(cq);
        q.setMaxResults(1);
        List<Servizio> lista = q.getResultList();
        if (lista.size() > 0) {
            adiacente = lista.get(0);
        }

        // eventualmente chiude l'EM locale
        if (localEM) {
            em.close();
        }

        return adiacente;

    }


    /**
     * Recupera il massimo numero d'ordine di servizio fino ad ora attribuito.
     *
     * @param em l'EntityManager da utilizzare
     * @return il massimo numero d'ordine, 0 se non ce ne sono
     */
    public static int queryMaxOrdineServizio(EntityManager em) {
        int maxOrdine = 0;

        // se non specificato EM, ne crea uno locale
        boolean localEM = false;
        if (em == null) {
            em = EM.createEntityManager();
            localEM = true;
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery(Integer.class);
        Root root = cq.from(Servizio.class);
        cq.select(cb.max(root.get(Servizio_.ordine)));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(CompanyQuery.creaFiltroCompany(root, cb));
        cq.where(predicates.toArray(new Predicate[]{}));

        Object result = em.createQuery(cq).getSingleResult();
        if (result != null && result instanceof Number) {
            maxOrdine = Lib.getInt(result);
        }

        // eventualmente chiude l'EM locale
        if (localEM) {
            em.close();
        }

        return maxOrdine;
    }


}
