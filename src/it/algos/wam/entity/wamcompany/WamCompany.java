package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DefaultSort({"companyCode"})
public class WamCompany extends BaseCompany {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    public static String DEMO_COMPANY_CODE = "demo";

    // elenco delle relazioni OneToMany
    // servono per creare le foreign key sul db
    // che consentono la cancellazione a cascata

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Servizio> servizi;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete()
    private List<Funzione> funzioni;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Volontario> volontari;

    // parametri aggiuntivi, specifici delle Company del settore
    private String presidente;

    private Organizzazione organizzazione;

    /**
     * Costruttore senza argomenti
     * Obbligatorio per le specifiche JavaBean
     * Da non usare MAI per la creazione diretta di una nuova istanza (si perdono i controlli)
     */
    public WamCompany() {
        this("", "");
    }// end of JavaBean constructor

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param companyCode sigla di riferimento interna (obbligatoria)
     * @param name        descrizione della company (obbligatoria)
     */
    public WamCompany(String companyCode, String name) {
        this(companyCode, name, "");
    }// end of constructor

    /**
     * Costruttore completo con tutte le properties obbligatorie
     *
     * @param companyCode sigla di riferimento interna (obbligatoria)
     * @param name        descrizione della company (obbligatoria)
     * @param email       indirizzo elettronico (obbligatorio)
     */
    public WamCompany(String companyCode, String name, String email) {
        super(companyCode, name);
    }// end of constructor

    /**
     * Recupera il totale dei records della Entity
     *
     * @return numero totale di records nella Entity
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.count(WamCompany.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla azienda, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     *
     * @return istanza della Entity, null se non trovata
     */
    public static WamCompany find(long id) {
        WamCompany instance = null;
        BaseEntity entity = AQuery.find(WamCompany.class, id);

        if (entity != null) {
            if (entity instanceof WamCompany) {
                instance = (WamCompany) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una lista (array) di TUTTI i records della Entity
     *
     * @return lista di tutte le istanze della Entity
     */
    @SuppressWarnings("unchecked")
    public static List<WamCompany> findAll() {
        return (List<WamCompany>) AQuery.getList(WamCompany.class);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param code valore della property code
     *
     * @return istanza della Entity, null se non trovata
     */
    public static WamCompany findByCode(String code) {
        WamCompany company = null;

        EntityManager manager = EM.createEntityManager();
        company = findByCode(code, manager);
        manager.close();

        return company;
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param code    valore della property code
     * @param manager the EntityManager to use
     *
     * @return istanza della Entity, null se non trovata
     */
    public static WamCompany findByCode(String code, EntityManager manager) {
        WamCompany instance = null;
        BaseEntity entity = AQuery.getEntity(WamCompany.class, BaseCompany_.companyCode, code, manager);

        if (entity != null) {
            if (entity instanceof WamCompany) {
                instance = (WamCompany) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Ritorna la Demo Company
     *
     * @return la Demo Company, null se non esiste
     */
    public static WamCompany getDemo() {
        return findByCode(WAMApp.DEMO_COMPANY_CODE);
    }// end of method

    /**
     * Ritorna la Company corrente
     * (correntemente in sessione)
     *
     * @return la Company corrente, null se non c'è
     */
    public static WamCompany getCurrent() {
        WamCompany wamCompany = null;
        BaseCompany company = CompanySessionLib.getCompany();

        if (company != null) {
            wamCompany = (WamCompany) company;
        }// end of if cycle

        return wamCompany;
    }// end of method

    /**
     * Ritorna la Demo Company
     *
     * @return la Demo Company, null se non esiste
     * @deprecated
     */
    public static WamCompany getDemoCompanyOld() {
        WamCompany company = null;
        Container.Filter filter = new Compare.Equal(WamCompany_.companyCode.getName(), WAMApp.DEMO_COMPANY_CODE);
        List demoCompanies = AQuery.getListOld(WamCompany.class, filter);
        if (demoCompanies.size() > 0) {
            company = (WamCompany) demoCompanies.get(0);
        }
        return company;
    }// end of method

    @Override
    public String toString() {
        return getCompanyCode();
    }// end of method

    public String getPresidente() {
        return presidente;
    }// end of getter method

    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }//end of setter method

    public Organizzazione getOrganizzazione() {
        return organizzazione;
    }// end of getter method

    public void setOrganizzazione(Organizzazione organizzazione) {
        this.organizzazione = organizzazione;
    }//end of setter method


    /**
     * Elimina l'azienda.
     */
    @Override
    public boolean delete() {
        boolean cancellato = false;

        EntityManager manager = EM.createEntityManager();
        try {
            manager.getTransaction().begin();
            cancellato = delete(manager);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }// fine del blocco try-catch
        manager.close();

        return cancellato;
    }// end of method

    /**
     * Elimina l'azienda.
     *
     * @param manager the EntityManager to use
     */
    public boolean delete(EntityManager manager) {
        deleteAllWamData(manager);
        return super.delete(manager);
    }// end of method


    /**
     * Elimina tutti i dati di questa azienda.
     * <p>
     * L'ordine di cancellazione è critico per l'integrità referenziale
     *
     * @param manager the EntityManager to use
     */
    private void deleteAllWamData(EntityManager manager) {
        int recCancellati;

        // elimina le tabelle
        recCancellati = CompanyQuery.delete(Iscrizione.class, this, manager);
//        ServizioFunzione.resetServizi(this,manager);
//        AQuery.delete(ServizioFunzione.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Volontario.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Funzione.class, CompanyEntity_.company, this, manager);
//        AQuery.delete(Servizio.class, CompanyEntity_.company, this, manager);
//
//        // elimina i turni
//        AQuery.delete(Turno.class, CompanyEntity_.company, this, manager);

    }// end of method

}// end of entity class



