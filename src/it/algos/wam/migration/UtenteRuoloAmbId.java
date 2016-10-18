package it.algos.wam.migration;

import java.io.Serializable;

/**
 * Created by alex on 18-10-2016.
 */
public class UtenteRuoloAmbId implements Serializable {
    long ruolo_id = 0;
    long utente_id = 0;

    public UtenteRuoloAmbId() {
        this(0,0);
    }

    public UtenteRuoloAmbId(long ruolo_id, long utente_id) {
        this.ruolo_id = ruolo_id;
        this.utente_id = utente_id;
    }

}
