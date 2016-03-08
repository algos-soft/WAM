package it.algos.wam.entity.wamcompany;

import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(BaseCompany.class)
public class WamCompany_ extends BaseCompany_ {
	public static volatile SingularAttribute<WamCompany, String> alfa;
}// end of entity class
