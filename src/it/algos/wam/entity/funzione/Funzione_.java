package it.algos.wam.entity.funzione;

import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Funzione.class)
public class Funzione_ extends BaseEntity_ {
    public static volatile SingularAttribute<Funzione, String> sigla;
    public static volatile SingularAttribute<Funzione, String> codeCompanyUnico;
    public static volatile SingularAttribute<Funzione, String> descrizione;
    public static volatile SingularAttribute<Funzione, Integer> ordine;
    public static volatile SingularAttribute<Funzione, Integer> iconCodepoint;
    public static volatile ListAttribute<Funzione, ServizioFunzione> servizioFunzioni;
    public static volatile ListAttribute<Funzione, VolontarioFunzione> volontarioFunzioni;
}// end of entity class

