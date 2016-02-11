package it.algos.wam.entity.milite;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Milite.class)
public class Milite_ extends BaseEntity_ {
	public static volatile SingularAttribute<Milite, String> nome;
}// end of entity class
