package it.algos.wam.entity.turnoiscrizione;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TurnoIscrizione.class)
public class TurnoIscrizione_ extends BaseEntity_ {
	public static volatile SingularAttribute<TurnoIscrizione, String> sigla;
}// end of entity class
