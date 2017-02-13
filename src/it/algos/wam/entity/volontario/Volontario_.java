package it.algos.wam.entity.volontario;

import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Volontario.class)
public class Volontario_ extends CompanyEntity_ {
    public static volatile SingularAttribute<Volontario, String> cognome;
    public static volatile SingularAttribute<Volontario, String> nome;
    public static volatile SingularAttribute<Volontario, String> codeCompanyUnico;
    public static volatile SingularAttribute<Volontario, String> cellulare;
    public static volatile SingularAttribute<Volontario, String> telefono;
    public static volatile SingularAttribute<Volontario, String> email;
    public static volatile SingularAttribute<Volontario, Date> dataNascita;
    public static volatile SingularAttribute<Volontario, String> password;
    public static volatile SingularAttribute<Volontario, Boolean> invioMail;
    public static volatile SingularAttribute<Volontario, String> note;
    public static volatile SingularAttribute<Volontario, Boolean> admin;
    public static volatile SingularAttribute<Volontario, Boolean> dipendente;
    public static volatile SingularAttribute<Volontario, Boolean> attivo;
    public static volatile SingularAttribute<Volontario, Date> scadenzaPatente;
    public static volatile SingularAttribute<Volontario, Date> scadenzaBLSD;
    public static volatile SingularAttribute<Volontario, Date> scadenzaTrauma;
    public static volatile SingularAttribute<Volontario, Date> scadenzaNonTrauma;
    public static volatile ListAttribute<Volontario, VolontarioFunzione> funzioni;
    public static volatile SingularAttribute<Volontario, Integer> oreAnno;
    public static volatile SingularAttribute<Volontario, Integer> turniAnno;
    public static volatile SingularAttribute<Volontario, Integer> oreExtra;

}// end of entity class
