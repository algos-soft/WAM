package it.algos.wam.settings;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.wamcompany.WamCompany;
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
public enum CompanyPrefs {

    tabellonePubblico("tabellonePubblico", PrefType.bool, true, "Tabellone liberamente visibile, anche senza essere loggato"),
    creazioneTurniNormali("creazioneTurniNormali", PrefType.bool, true, "I volontari possono creare turni normali"),
    creazioneTurniExtra("creazioneTurniExtra", PrefType.bool, false, "I volontari possono creare turni extra"),

    senderAddress("senderAddress", PrefType.email, "", "Indirizzo email del mittente"),

    sendMailToBackup("sendMailToBackup", PrefType.bool, false, "Se invia ogni mail anche a una casella di backup"),

    backupMailbox("backupMailbox", PrefType.email, "pinco.pallino@mail.com", "La casella di backup delle email"),

    inviaNotificaInizioTurno("inviaNotificaInizioTurno", PrefType.bool, true, "Se invia le notifiche di inizio turno"),

    quanteOrePrimaNotificaInizioTurno("quanteOrePrimaNotificaInizioTurno", PrefType.integer, 24, "Quante ore prima invia le notifiche di inizio turno"),

    modoCancellazione("modoCancellazione", PrefType.integer, Iscrizione.MODE_CANC_PRE, "Modalità di controllo della cancIscrizione iscrizione (0=nessuno, 1=post, 2=pre)"),
    cancMinutiDopoIscrizione("cancMinutiDopoIscrizione", PrefType.integer, 15, "Per quanti minuti dopo l'iscrizione il volontario si può cancellare"),
    cancOrePrimaInizioTurno("cancOrePrimaInizioTurno", PrefType.integer, 24, "Fino a quante ore prima dell'inizio turno il volontario si può cancellare"),
    turnoAlertOrePrima("turnoAlertOrePrima", PrefType.integer, 24, "Quante ore prima dell'inizio un turno incompleto si colora di rosso"),
    turnoWarningOrePrima("turnoWarningOrePrima", PrefType.integer, 96, "Quante ore prima dell'inizio un turno incompleto si colora di giallo");



    private String code;
    private PrefType type;
    private Object defaultValue;
    private String descrizione;


    CompanyPrefs(String key, PrefType type, Object defaultValue) {
        this(key, type, defaultValue, "");
    }// end of internal constructor


    CompanyPrefs(String key, PrefType type, Object defaultValue, String descrizione) {
        this.code = key;
        this.type = type;
        this.defaultValue = defaultValue;
        this.descrizione = descrizione;
    }// end of internal constructor


    /**
     * Retrieves this preference's value as String
     */
    public String getString() {
        return Pref.getString(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as String
     */
    public String getString(WamCompany company) {
        return Pref.getString(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as boolean
     */
    public boolean getBool() {
        return Pref.getBool(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as boolean
     */
    public boolean getBool(WamCompany company) {
        return Pref.getBool(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as int
     */
    public int getInt() {
        return Pref.getInt(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as int
     */
    public int getInt(WamCompany company) {
        return Pref.getInt(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as BigDecimal
     */
    public BigDecimal getDecimal() {
        return Pref.getDecimal(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as BigDecimal
     */
    public BigDecimal getDecimal(WamCompany company) {
        return Pref.getDecimal(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as Date
     */
    public Date getDate() {
        return Pref.getDate(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as Date
     */
    public Date getDate(WamCompany company) {
        return Pref.getDate(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as Image
     */
    public Image getImage() {
        return Pref.getImage(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as Image
     */
    public Image getImage(WamCompany company) {
        return Pref.getImage(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as Resource
     */
    public Resource getResource() {
        return Pref.getResource(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as Resource
     */
    public Resource getResource(WamCompany company) {
        return Pref.getResource(code, company, defaultValue);
    }// end of method


    /**
     * Retrieves this preference's value as byte[]
     */
    public byte[] getBytes() {
        return Pref.getBytes(code, defaultValue);
    }// end of method

    /**
     * Retrieves this preference's value as byte[]
     */
    public byte[] getBytes(WamCompany company) {
        return Pref.getBytes(code, company, defaultValue);
    }// end of method


    /**
     * Writes the default value in the storage for this preference
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * If the preference does not exist it is created now.
     */
    public void put() {
        put(defaultValue);
    }// end of method

    /**
     * Writes a value in the storage for this preference
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * If the preference does not exist it is created now.
     *
     * @param value the value
     */
    public void put(Object value) {
        Pref.put(code, type, value);
    }// end of method

    /**
     * Writes the default value in the storage for this preference
     * Filtrato sulla company passata come parametro.
     * If the preference does not exist it is created now.
     *
     * @param company the company
     */
    public void put(WamCompany company) {
        put(company, defaultValue);
    }// end of method

    /**
     * Writes a value in the storage for this preference
     * Filtrato sulla company passata come parametro.
     * If the preference does not exist it is created now.
     *
     * @param company the company
     * @param value   the value
     */
    public void put(WamCompany company, Object value) {
        Pref.put(code, company, type, value, descrizione);
    }// end of method


    /**
     * Writes the default value in the storage for this preference
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * La crea SOLO se non esiste già
     */
    public void crea() {
        crea(defaultValue);
    }// end of method

    /**
     * Writes a value in the storage for this preference
     * Filtrato sulla company corrente (che viene regolata nella superclasse CompanyEntity)
     * La crea SOLO se non esiste già
     *
     * @param value the value
     */
    public void crea(Object value) {
        Pref.crea(code, type, value);
    }// end of method

    /**
     * Writes the default value in the storage for this preference
     * Filtrato sulla company passata come parametro.
     * La crea SOLO se non esiste già
     *
     * @param company the company
     */
    public void crea(WamCompany company) {
        crea(company, defaultValue);
    }// end of method

    /**
     * Writes a value in the storage for this preference
     * Filtrato sulla company passata come parametro.
     * La crea SOLO se non esiste già
     *
     * @param company the company
     * @param value   the value
     */
    public void crea(WamCompany company, Object value) {
        Pref.crea(code, company, type, descrizione, value);
    }// end of method


    /**
     * Removes this preference from the storage.
     * <p>
     */
    public void remove() {
        Pref.remove(code, null);
    }// end of method

    /**
     * Removes this preference from the storage.
     * <p>
     */
    public void remove(WamCompany company) {
        Pref.remove(code, company);
    }// end of method

    public String getDescrizione() {
        return descrizione;
    }
}// end of enumeration class
