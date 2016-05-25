package it.algos.wam.entity.serviziofunzione;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ServizioFunzione.class)
public class ServizioFunzione_ extends BaseEntity_ {
	public static volatile SingularAttribute<ServizioFunzione, Servizio> servizio;
	public static volatile SingularAttribute<ServizioFunzione, Funzione> funzione;
}// end of entity class
