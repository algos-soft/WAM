package it.algos.wam.entity.milite;

import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Milite.class)
public class Milite_ extends CompanyEntity_ {
    public static volatile SingularAttribute<Milite, String> nome;
    public static volatile SingularAttribute<Milite, String> cognome;
    public static volatile SingularAttribute<Milite, String> cellulare;
    public static volatile SingularAttribute<Milite, String> telefono;
    public static volatile SingularAttribute<Milite, String> email;
    public static volatile SingularAttribute<Milite, String> note;
    public static volatile SingularAttribute<Milite, Date> dataNascita;
    public static volatile SingularAttribute<Milite, Boolean> dipendente;
    public static volatile SingularAttribute<Milite, Boolean> attivo;
}// end of entity class
