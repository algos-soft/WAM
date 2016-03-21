package it.algos.wam.entity.volontariofunzione;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(VolontarioFunzione.class)
public class VolontarioFunzione_ extends BaseEntity_ {
	public static volatile SingularAttribute<VolontarioFunzione, Volontario> volontario;
	public static volatile SingularAttribute<VolontarioFunzione, Funzione> funzione;
}// end of entity class
