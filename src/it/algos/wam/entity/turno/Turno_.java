package it.algos.wam.entity.turno;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;
import java.util.Date;

@StaticMetamodel(Turno.class)
public class Turno_ extends BaseEntity_ {
	public static volatile SingularAttribute<Turno, Servizio> servizio;
	public static volatile SingularAttribute<Turno, Date> inizio;
	public static volatile SingularAttribute<Turno, Date> fine;
	public static volatile SingularAttribute<Turno, Funzione> funzione1;
	public static volatile SingularAttribute<Turno, Funzione> funzione2;
	public static volatile SingularAttribute<Turno, Funzione> funzione3;
	public static volatile SingularAttribute<Turno, Funzione> funzione4;
	public static volatile SingularAttribute<Turno, Milite> milite1;
	public static volatile SingularAttribute<Turno, Milite> milite2;
	public static volatile SingularAttribute<Turno, Milite> milite3;
	public static volatile SingularAttribute<Turno, Milite> milite4;
	public static volatile SingularAttribute<Turno, Timestamp> lastModificaFunzione1;
	public static volatile SingularAttribute<Turno, Timestamp> lastModificaFunzione2;
	public static volatile SingularAttribute<Turno, Timestamp> lastModificaFunzione3;
	public static volatile SingularAttribute<Turno, Timestamp> lastModificaFunzione4;
	public static volatile SingularAttribute<Turno, Integer> oreMilite1;
	public static volatile SingularAttribute<Turno, Integer> oreMilite2;
	public static volatile SingularAttribute<Turno, Integer> oreMilite3;
	public static volatile SingularAttribute<Turno, Integer> oreMilite4;
	public static volatile SingularAttribute<Turno, Boolean> problemiFunzione1;
	public static volatile SingularAttribute<Turno, Boolean> problemiFunzione2;
	public static volatile SingularAttribute<Turno, Boolean> problemiFunzione3;
	public static volatile SingularAttribute<Turno, Boolean> problemiFunzione4;
	public static volatile SingularAttribute<Turno, String> notaFunzione1;
	public static volatile SingularAttribute<Turno, String> notaFunzione2;
	public static volatile SingularAttribute<Turno, String> notaFunzione3;
	public static volatile SingularAttribute<Turno, String> notaFunzione4;
	public static volatile SingularAttribute<Turno, String> titoloExtra;
	public static volatile SingularAttribute<Turno, String> localitàExtra;
	public static volatile SingularAttribute<Turno, String> note;
	public static volatile SingularAttribute<Turno, Boolean> assegnato;
}// end of entity class
