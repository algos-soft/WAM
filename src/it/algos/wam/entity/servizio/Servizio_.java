package it.algos.wam.entity.servizio;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.entity.BaseEntity_;

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
    public static volatile SingularAttribute<Servizio, Integer> funzioniObbligatorie;
}// end of entity class
