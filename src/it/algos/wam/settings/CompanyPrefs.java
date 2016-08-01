package it.algos.wam.settings;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.domain.pref.PrefType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Company preferences.
 * <p>
 * Defines the preferences and the methods to access them.<br>
 * Each preference has a key, a type and a default value.
 */

public enum CompanyPrefs  {

    // se il tabellone è liberamente accessibile in visione senza login
    tabellonePubblico("tabellonePubblico", PrefType.bool, true),

    // indirizzo email del mittente
    senderAddress("senderAddress", PrefType.string, ""),

    // se invia ogni mail anche a una casella di backup
    sendMailToBackup("sendMailToBackup", PrefType.bool, false),

    // la casella di backup delle email
    backupMailbox("backupMailbox", PrefType.string, ""),

    // se invia le notifiche di inizio turno
    inviaNotificaInizioTurno("inviaNotificaInizioTurno", PrefType.bool, true),

    // quante ore prima invia le notifiche di inizio turno
    quanteOrePrimaNotificaInizioTurno("quanteOrePrimaNotificaInizioTurno", PrefType.integer, 24),;


    ;

    private String code;
    private PrefType type;
    private Object defaultValue;

    private CompanyPrefs(String key, PrefType type, Object defaultValue) {
        this.code = key;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves this preference's value as boolean
     */
    public boolean getBool() {
        return Pref.getBool(code, defaultValue);
    }

    /**
     * Retrieves this preference's value as byte[]
     */
    public byte[] getBytes() {
        return Pref.getBytes(code,  defaultValue);
    }

    /**
     * Retrieves this preference's value as Date
     */
    public Date getDate() {
        return Pref.getDate(code, defaultValue);
    }

    /**
     * Retrieves this preference's value as BigDecimal
     */
    public BigDecimal getDecimal() {
        return Pref.getDecimal(code, defaultValue);
    }

    /**
     * Retrieves this preference's value as int
     */
    public int getInt() {
        return Pref.getInt(code,  defaultValue);
    }

    /**
     * Retrieves this preference's value as String
     */
    public String getString() {
        return Pref.getString(code,  defaultValue);
    }

    /**
     * Retrieves this preference's value as Image
     */
    public Image getImage() {
        return Pref.getImage(code, defaultValue);
    }

    /**
     * Retrieves this preference's value as Resource
     */
    public Resource getResource() {
        return Pref.getResource(code,  defaultValue);
    }

    /**
     * Writes a value in the storage for this preference
     * <p>
     * If the preference does not exist it is created now.
     *
     * @param value the value
     */
    public void put(Object value) {
        Pref.put(code, value, type);
    }

    /**
     * Removes this preference from the storage.
     * <p>
     */
    public void remove() {
        Pref.remove(code, null);
    }


}
