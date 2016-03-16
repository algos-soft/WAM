package it.algos.wam.entity.serviziofunzione;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ServizioFunzione.class)
public class ServizioFunzione_ extends BaseEntity_ {
	public static volatile SingularAttribute<ServizioFunzione, String> sigla;
}// end of entity class
