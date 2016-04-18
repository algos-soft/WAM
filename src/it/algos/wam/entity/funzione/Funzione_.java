package it.algos.wam.entity.funzione;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Funzione.class)
public class Funzione_ extends BaseEntity_ {
    public static volatile SingularAttribute<Funzione, String> sigla;
    public static volatile SingularAttribute<Funzione, String> descrizione;
    public static volatile SingularAttribute<Funzione, Integer> ordine;
    public static volatile SingularAttribute<Funzione, String> note;
    public static volatile SingularAttribute<Funzione, Byte[]> icon;
}// end of entity class
