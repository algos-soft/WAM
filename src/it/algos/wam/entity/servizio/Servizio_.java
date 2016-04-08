package it.algos.wam.entity.servizio;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Servizio.class)
public class Servizio_ extends CompanyEntity_ {
    public static volatile ListAttribute<Servizio, Turno> turni;
    public static volatile ListAttribute<Servizio, ServizioFunzione> servizioFunzioni;
    public static volatile SingularAttribute<Servizio, String> sigla;
    public static volatile SingularAttribute<Servizio, String> descrizione;
    public static volatile SingularAttribute<Servizio, Integer> ordine;
    public static volatile SingularAttribute<Servizio, Integer> colore;
    public static volatile SingularAttribute<Servizio, Integer> oraInizio;
    public static volatile SingularAttribute<Servizio, Integer> minutiInizio;
    public static volatile SingularAttribute<Servizio, Integer> oraFine;
    public static volatile SingularAttribute<Servizio, Integer> minutiFine;
    public static volatile SingularAttribute<Servizio, Boolean> orario;
}// end of entity class
