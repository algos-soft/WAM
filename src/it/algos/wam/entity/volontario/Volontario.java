package it.algos.wam.entity.volontario;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity che descrive un Volontario
 * Estende la Entity astratta WamCompany che contiene la property wamcompany
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
public class Volontario extends WamCompanyEntity {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "volontario", cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    private List<VolontarioFunzione> volontarioFunzioni = new ArrayList();

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
    public Volontario() {
        this(null, "", "");
    }// end of constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param company croce di appartenenza
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     */
    public Volontario(WamCompany company, String nome, String cognome) {
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
    public Volontario(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare) {
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
    public Volontario(WamCompany company, String nome, String cognome, Date dataNascita, String cellulare, boolean dipendente) {
        super();
        if (this.getCompany() == null) {
            super.setCompany(company);
        }// fine del blocco if
        setNome(nome);
        setCognome(cognome);
        setDataNascita(dataNascita);
        setCellulare(cellulare);
        setDipendente(dipendente);
    }// end of constructor

    /**
     * Recupera una istanza di Volontario usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Volontario, null se non trovata
     */
    public static Volontario find(long id) {
        Volontario instance = null;
        BaseEntity entity = AQuery.queryById(Volontario.class, id);

        if (entity != null) {
            if (entity instanceof Volontario) {
                instance = (Volontario) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Volontario usando la query di tutte e sole le property obbligatorie
     *
     * @param company valore della property Company
     * @param nome    valore della property Nome
     * @param cognome valore della property Cognome
     * @return istanza di Volontario, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static Volontario find(WamCompany company, String nome, String cognome) {
        Volontario instance = null;

        //@todo questo non funziona - si blocca
//        Container.Filter f1 = new Compare.Equal(Company_.companyCode.getName(), wamcompany.getCompanyCode());
//        Container.Filter f2 = new Compare.Equal(Milite_.nome.getName(), nome);
//        Container.Filter f3 = new Compare.Equal(Milite_.cognome.getName(), cognome);
//        Container.Filter filter = new And(f1, f2, f3);
//        ArrayList<BaseEntity> militi = AQuery.getList(Milite.class, filter);
//
//        if (militi != null && militi.size() > 0) {
//            instance = (Milite) militi.get(0);
//        }// end of if cycle

//        ArrayList<Volontario> militiPerCognome = (ArrayList<Milite>) AQuery.queryList(Volontario.class, Milite_.cognome, cognome);
        //@todo da migliorare
        List<Volontario> militiPerCognome = (List<Volontario>) AQuery.queryList(Volontario.class, Volontario_.cognome, cognome);
        if (militiPerCognome != null && militiPerCognome.size() > 0) {
            for (Volontario milite : militiPerCognome) {
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
        long totTmp = AQuery.getCount(Volontario.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Domain Class
     *
     * @return lista di tutte le istanze di Volontario
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Volontario> findAll() {
        return (ArrayList<Volontario>) AQuery.getLista(Volontario.class);

    }// end of method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company croce di appartenenza
     * @param nome    del volontario/milite (obbligatorio)
     * @param cognome del volontario/milite (obbligatorio)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, String nome, String cognome) {
        return crea(company, nome, cognome, new ArrayList<Funzione>());
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company   croce di appartenenza
     * @param nome      del volontario/milite (obbligatorio)
     * @param cognome   del volontario/milite (obbligatorio)
     * @param listaFunz lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, ArrayList<Funzione> listaFunz) {
        return crea(company, nome, cognome, listaFunz.toArray(new Funzione[listaFunz.size()]));
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param nome     del volontario/milite (obbligatorio)
     * @param cognome  del volontario/milite (obbligatorio)
     * @param funzioni lista delle funzioni (facoltativa)
     * @return istanza di Volontario
     */
    public static Volontario crea(WamCompany company, String nome, String cognome, Funzione... funzioni) {
        Volontario vol = Volontario.find(company, nome, cognome);

        if (vol == null) {
            vol = new Volontario(company, nome, cognome);
            vol.setDipendente(false);
            vol.setAttivo(true);

            if (funzioni != null) {
                for (Funzione funz : funzioni) {
                    vol.volontarioFunzioni.add(new VolontarioFunzione(company, vol, funz));
                } // fine del ciclo for-each
            }// fine del blocco if

            vol = (Volontario) vol.save();
        }// end of if cycle

        return vol;
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

    public List<VolontarioFunzione> getVolontarioFunzioni() {
        return volontarioFunzioni;
    }// end of getter method

    public void setVolontarioFunzioni(List<VolontarioFunzione> volontarioFunzioni) {
        this.volontarioFunzioni = volontarioFunzioni;
    }//end of setter method

    public void addFunzione(Funzione funzione) {
        VolontarioFunzione volFun = null;

        if (getCompany() == null) {
            Exception e = new Exception("Impossibile aggiungere funzioni al volontario se manca la croce");
            e.printStackTrace();
            return;
        }// end of if cycle

        if (volontarioFunzioni != null) {
            volFun = new VolontarioFunzione(this, funzione);
            volFun.setCompany(getCompany());
            volontarioFunzioni.add(volFun);
        }// end of if cycle
    }// end of method


    /**
     * Rimuove una funzione dal volontario
     */
    public void removeFunzione(Funzione funzione) {
        for (VolontarioFunzione vf : volontarioFunzioni) {
            if (vf.getFunzione().equals(funzione)) {
                volontarioFunzioni.remove(vf);
                break;
            }
        }
    }


    /**
     * Verifica se il volontario ha una data funzione
     *
     * @param funz la funzione da verificare
     * @return true se ha la funzione
     */
    public boolean haFunzione(Funzione funz) {
        boolean found = false;
        List<VolontarioFunzione> vfunzioni = getVolontarioFunzioni();
        for (VolontarioFunzione vf : vfunzioni) {
            if (vf.getFunzione().equals(funz)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Verifica se il volontario è un admin
     *
     * @return true se è un admin
     */
    public boolean isAdmin() {
        return false;
    }


    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Milite con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Volontario clone() throws CloneNotSupportedException {
        try {
            return (Volontario) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volontario that = (Volontario) o;

        if (nome != null ? !nome.equals(that.nome) : that.nome != null) return false;
        if (cognome != null ? !cognome.equals(that.cognome) : that.cognome != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return !(dataNascita != null ? !dataNascita.equals(that.dataNascita) : that.dataNascita != null);

    }

    @Override
    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (cognome != null ? cognome.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (dataNascita != null ? dataNascita.hashCode() : 0);
        return result;
    }
}// end of domain class
