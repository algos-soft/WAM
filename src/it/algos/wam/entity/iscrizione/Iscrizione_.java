package it.algos.wam.entity.iscrizione;

import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.sql.Timestamp;

@StaticMetamodel(Iscrizione.class)
public class Iscrizione_ extends BaseEntity_ {

	public static volatile SingularAttribute<Iscrizione, Turno> turno;
	public static volatile SingularAttribute<Iscrizione, Volontario> volontario;
	public static volatile SingularAttribute<Iscrizione, ServizioFunzione> servizioFunzione;
	public static volatile SingularAttribute<Iscrizione, Timestamp> lastModifica;
	public static volatile SingularAttribute<Iscrizione, Integer> minutiEffettivi;
	public static volatile SingularAttribute<Iscrizione, Boolean> esisteProblema;
	public static volatile SingularAttribute<Iscrizione, String> nota;

}// end of entity class
