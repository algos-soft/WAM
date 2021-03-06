package it.algos.wam.entity.turno;

import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Turno.class)
public class Turno_ extends BaseEntity_ {
    public static volatile SingularAttribute<Turno, Long> chiave;
    public static volatile SingularAttribute<Turno, Servizio> servizio;
    public static volatile SingularAttribute<Turno, Date> inizio;
    public static volatile SingularAttribute<Turno, Date> fine;
    public static volatile ListAttribute<Turno, Iscrizione> iscrizioni;
    public static volatile SingularAttribute<Turno, String> titoloExtra;
    public static volatile SingularAttribute<Turno, String> localitaExtra;
    public static volatile SingularAttribute<Turno, String> note;
}// end of entity class
