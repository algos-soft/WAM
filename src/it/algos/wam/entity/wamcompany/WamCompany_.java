package it.algos.wam.entity.wamcompany;

import it.algos.wam.entity.company.Company;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(WamCompany.class)
public class WamCompany_ extends BaseEntity_ {
	public static volatile SingularAttribute<WamCompany, Company> company;
}// end of entity class
