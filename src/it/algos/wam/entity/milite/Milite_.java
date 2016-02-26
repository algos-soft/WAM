package it.algos.wam.entity.milite;

import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Milite.class)
public class Milite_ extends CompanyEntity_ {
	public static volatile SingularAttribute<Milite, String> nome;
	public static volatile SingularAttribute<Milite, String> cognome;
	public static volatile SingularAttribute<Milite, String> telefonoCellulare;
	public static volatile SingularAttribute<Milite, String> telefonoFisso;
	public static volatile SingularAttribute<Milite, String> email;
	public static volatile SingularAttribute<Milite, String> note;
	public static volatile SingularAttribute<Milite, Date> dataNascita;
	public static volatile SingularAttribute<Funzione, Company> company;
}// end of entity class
