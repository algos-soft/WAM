package it.algos.wam.entity.turno;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Turno.class)
public class Turno_ extends BaseEntity_ {
	public static volatile SingularAttribute<Turno, String> sigla;
}// end of entity class
