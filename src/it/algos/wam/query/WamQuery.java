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
     * Tutti i turni relativi a un dato servizio, che iniziano in un certo periodo.
     *
     * @param em       l'EntityManager da utilizzare (se nullo lo crea qui)
     * @param servizio il servizio di riferimento
     * @param d1       la data iniziale
     * @param d1       la data finale
     * @return la lista dei turni
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
     *
     * @param em l'EntityManager da utilizzare (se nullo lo crea qui)
     * @return la lista dei servizi
     */
    public static List<Servizio> queryServiziOrari(EntityManager em) {

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
        predicates.add(cb.equal(root.get(Servizio_.orario), true));
        cq.where(predicates.toArray(new Predicate[]{}));

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



}
