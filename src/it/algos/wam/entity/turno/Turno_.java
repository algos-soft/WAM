package it.algos.wam.entity.turno;

import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.wrap.WrapTurno;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Turno.class)
public class Turno_ extends BaseEntity_ {
    public static volatile SingularAttribute<Turno, Long> chiave;
    public static volatile SingularAttribute<Turno, Servizio> servizio;
    public static volatile SingularAttribute<Turno, Date> inizio;
    public static volatile SingularAttribute<Turno, Date> fine;
    public static volatile SingularAttribute<Turno, WrapTurno> wrapTurno;
    public static volatile SingularAttribute<Turno, String> titoloExtra;
    public static volatile SingularAttribute<Turno, String> localitàExtra;
    public static volatile SingularAttribute<Turno, String> note;
    public static volatile SingularAttribute<Turno, Boolean> assegnato;
}// end of entity class
