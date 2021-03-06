package it.algos.wam.entity.wamcompany;

/**
 * Created by gac on 27 ago 2016.
 * Le tre maggiori realtà di aumbulanze
 */
public enum Organizzazione {

    cri("CRI", "Croce Rossa Italiana"),
    anpas("ANPAS", "Associazione Nazionale Pubbliche Assistenze"),
    misericordia("MISERICORDIA", "Confederazione delle Misericordie d’Italia"),
    csv("CSV", "Centri di Servizio per il Volontariato");

    private String sigla;
    private String descrizione;

    /**
     * Costruttore interno dell'Enumeration
     */
    Organizzazione(String sigla, String descrizione) {
        this.sigla = sigla;
        this.descrizione = descrizione;
    }// fine del costruttore interno


    public static Organizzazione get(String sigla) {
        Organizzazione org = null;

        for (Organizzazione orgTmp : values()) {
            if (orgTmp.getSigla().toLowerCase().equals(sigla.toLowerCase())) {
                org = orgTmp;
                break;
            }// fine del blocco if
        } // fine del ciclo for-each

        return org;
    }// fine del metodo statico

    @Override
    public String toString() {
        return sigla + " - " + descrizione;
    }// end of method

    public String getSigla() {
        return sigla;
    }// end of getter method

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }//end of setter method

    public String getDescrizione() {
        return descrizione;
    }// end of getter method

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }//end of setter method
}// end of Enumeration class
