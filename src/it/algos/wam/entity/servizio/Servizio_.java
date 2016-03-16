package it.algos.wam.entity.servizio;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Servizio.class)
public class Servizio_ extends CompanyEntity_ {
    public static volatile SingularAttribute<Servizio, String> sigla;
    public static volatile SingularAttribute<Servizio, String> descrizione;
    public static volatile SingularAttribute<Servizio, Integer> ordine;
    public static volatile SingularAttribute<Servizio, Integer> durata;
    public static volatile SingularAttribute<Servizio, Integer> oraInizio;
    public static volatile SingularAttribute<Servizio, Integer> minutiInizio;
    public static volatile SingularAttribute<Servizio, Integer> oraFine;
    public static volatile SingularAttribute<Servizio, Integer> minutiFine;
    public static volatile SingularAttribute<Servizio, Boolean> primo;
    public static volatile SingularAttribute<Servizio, Boolean> fineGiornoSuccessivo;
    public static volatile SingularAttribute<Servizio, Boolean> visibile;
    public static volatile SingularAttribute<Servizio, Boolean> orario;
    public static volatile SingularAttribute<Servizio, Boolean> multiplo;
    public static volatile SingularAttribute<Servizio, Integer> persone;
    public static volatile ListAttribute<Servizio, Funzione> funzioni;

}// end of entity class
