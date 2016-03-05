package it.algos.wam.entity.milite;

import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity che descrive un Milite
 * Estende la Entity astratta WamCompany che contiene la property company
 * <p>
 * Classe di tipo JavaBean
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * <p>
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 * <p>
 */
@Entity
public class Milite extends WamCompany {

    //--nome del volontario (obbligatorio)
    @NotEmpty
    @Column(length = 20)
    @Index
    private String nome = "";

    //--cognome del volontario (obbligatorio)
    @NotEmpty
    @Column(length = 30)
    @Index
    private String cognome = "";

    //--dati personali facoltativi
    private String cellulare = "";
    private String telefono = "";
    private String email = "";
    private String note = "";
    private Date dataNascita = null;

    //--dati dell'associazione
    private boolean dipendente = false;
    private boolean attivo = true;

    //--scadenza certificati
    //--data di scadenza del certificato BSD
    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaBLSD = null;
//    //--data di scadenza del certificato Trauma
//    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaTrauma = null;
//    //--data di scadenza del certificato Non Trauma
//    //--se non valorizzata, il milite non ha acquisito il certificato
//    private Date scadenzaNonTrauma = null;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Milite() {
        this(null, "", "");
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param company croce di appartenenza
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     */
    public Milite(Company company, String nome, String cognome) {
        this(company, nome, cognome, null, "");
    }// end of constructor

    /**
     * Costruttore
     *
     * @param company     croce di appartenenza
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     */
    @SuppressWarnings("all")
    public Milite(Company company, String nome, String cognome, Date dataNascita, String cellulare) {
        this(company, nome, cognome, dataNascita, cellulare, false);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param company     croce di appartenenza
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     */
    public Milite(Company company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente) {
        super();
        super.setCompany(company);
        setNome(nome);
        setCognome(cognome);
        setDataNascita(dataNascita);
        setCellulare(cellulare);
        setDipendente(dipendente);
    }// end of constructor

    /**
     * Recupera una istanza di Milite usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Milite, null se non trovata
     */
    public static Milite find(long id) {
        Milite instance = null;
        BaseEntity entity = AQuery.queryById(Milite.class, id);

        if (entity != null) {
            if (entity instanceof Milite) {
                instance = (Milite) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Milite usando la query di tutte e sole le property obbligatorie
     *
     * @param company valore della property Company
     * @param nome    valore della property Nome
     * @param cognome valore della property Cognome
     * @return istanza di Milite, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Milite find(Company company, String nome, String cognome) {
        Milite instance = null;

        //@todo questo non funziona - si blocca
//        Container.Filter f1 = new Compare.Equal(Company_.companyCode.getName(), company.getCompanyCode());
//        Container.Filter f2 = new Compare.Equal(Milite_.nome.getName(), nome);
//        Container.Filter f3 = new Compare.Equal(Milite_.cognome.getName(), cognome);
//        Container.Filter filter = new And(f1, f2, f3);
//        ArrayList<BaseEntity> militi = AQuery.getList(Milite.class, filter);
//
//        if (militi != null && militi.size() > 0) {
//            instance = (Milite) militi.get(0);
//        }// end of if cycle


//        ArrayList<Milite> militiPerCognome = (ArrayList<Milite>) AQuery.queryList(Milite.class, Milite_.cognome, cognome);
        List<Milite> militiPerCognome = (List<Milite>) AQuery.queryList(Milite.class, Milite_.cognome, cognome);
        if (militiPerCognome != null && militiPerCognome.size() > 0) {
            for (Milite milite : militiPerCognome) {
                if (milite.getNome().equals(nome) && milite.getCompany().getId().equals(company.getId())) {
                    instance = milite;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera il valore del numero totale di records della Domain Class
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Milite.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Domain Class
     *
     * @return lista di tutte le istanze di Milite
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Milite> findAll() {
        return (ArrayList<Milite>) AQuery.getLista(Milite.class);
    }// end of method

    /**
     * Creazione iniziale di un milite
     * Lo crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza di Milite
     */
    public static Milite crea(Company company, String nome, String cognome) {
        return crea(company, nome, cognome, null, "");
    }// end of static method


    /**
     * Creazione iniziale di un milite
     * Lo crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @return istanza di Milite
     */
    public static Milite crea(Company company, String nome, String cognome, Date dataNascita, String cellulare) {
        return crea(company, nome, cognome, dataNascita, cellulare, false);
    }// end of static method

    /**
     * Creazione iniziale di un milite
     * Lo crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     * @return istanza di Milite
     */
    public static Milite crea(Company company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente) {
        return crea(company, nome, cognome, dataNascita, cellulare, dipendente, true);
    }// end of static method

    /**
     * Creazione iniziale di un milite
     * Lo crea SOLO se non esiste già
     *
     * @param company     croce di appartenenza
     * @param nome        del volontario/milite (obbligatorio)
     * @param cognome     del volontario/milite (obbligatorio)
     * @param dataNascita del volontario/milite (facoltativo)
     * @param cellulare   del volontario/milite (facoltativo)
     * @param dipendente  dell'associazione NON volontario
     * @param attivo      all'interno dell'associazione
     * @return istanza di Milite
     */
    public static Milite crea(Company company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente, boolean attivo) {
        Milite milite = Milite.find(company, nome, cognome);

        if (milite == null) {
            milite = new Milite(company, nome, cognome, dataNascita, cellulare, dipendente);
            milite.setAttivo(attivo);
            milite.save();
        }// end of if cycle

        return milite;
    }// end of static method

    /**
     * Abbreviazione visibile nel tabellone e nei popup
     */
    @Override
    public String toString() {
        return getCognome() + " " + getNome().substring(0, 1) + ".";
    }// end of method

    public String getNomeCognome() {
        return getNome() + " " + getCognome();
    }// end of getter method


//    public String pippo(){
//        return "";
//    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }// end of getter method

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }// end of setter method

    public String getCognome() {
        return cognome;
    }// end of getter method

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }//end of setter method

    public String getCellulare() {
        return cellulare;
    }// end of getter method

    public void setCellulare(String telefonoCellulare) {
        this.cellulare = telefonoCellulare;
    }//end of setter method

    public String getTelefono() {
        return telefono;
    }// end of getter method

    public void setTelefono(String telefonoFisso) {
        this.telefono = telefonoFisso;
    }//end of setter method

    public String getEmail() {
        return email;
    }// end of getter method

    public void setEmail(String email) {
        this.email = email;
    }//end of setter method

    public String getNote() {
        return note;
    }// end of getter method

    public void setNote(String note) {
        this.note = note;
    }//end of setter method

    public Date getDataNascita() {
        return dataNascita;
    }// end of getter method

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }//end of setter method

    public boolean isDipendente() {
        return dipendente;
    }// end of getter method

    public void setDipendente(boolean dipendente) {
        this.dipendente = dipendente;
    }//end of setter method

    public boolean isAttivo() {
        return attivo;
    }// end of getter method

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Milite con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Milite clone() throws CloneNotSupportedException {
        try {
            return (Milite) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
