package it.algos.wam.entity.funzione;

import it.algos.webbase.web.entity.BaseEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Funzione.class)
public class Funzione_ extends BaseEntity_ {
    public static volatile SingularAttribute<Funzione, String> siglaInterna;
    public static volatile SingularAttribute<Funzione, String> siglaVisibile;
    public static volatile SingularAttribute<Funzione, Integer> ordine;
    public static volatile SingularAttribute<Funzione, String> note;
//    public static volatile SingularAttribute<Funzione, Byte[]> icon;
    public static volatile SingularAttribute<Funzione, Integer> iconCodepoint;
}// end of entity class
