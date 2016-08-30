package it.algos.wam.entity.wamcompany;

/**
 * Created by gac on 27 ago 2016.
 * .
 */
public enum Organizzazione {
    cri, anpas, misericordia;

    public static Organizzazione get(String sigla) {
        Organizzazione org = null;

        for (Organizzazione orgTmp : values()) {
            if (orgTmp.toString().equals(sigla)) {
                org = orgTmp;
                break;
            }// fine del blocco if
        } // fine del ciclo for-each

        return org;
    }// fine del metodo statico

}// end of Enumeration class
