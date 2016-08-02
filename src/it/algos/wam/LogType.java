package it.algos.wam;

/**
 * Enum delle categorie di log di questa applicazione
 * Created by alex on 2-08-2016.
 */
public enum LogType {
    cancellazione("CANC"),
    iscrizione("ISCR");

    String tag;

    LogType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}
