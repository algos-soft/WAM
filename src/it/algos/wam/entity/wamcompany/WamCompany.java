package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;


@Entity
@DefaultSort({"companyCode"})
public class WamCompany extends BaseCompany {

    private static final long serialVersionUID = 1L;

    public static String DEMO_COMPANY_CODE="demo";


    //--mostra il tabellone alla partenza; in caso contrario va alla home
    private boolean vaiSubitoTabellone = true;

    // elenco delle relazioni OneToMany
    // servono per creare le foreign key sul db
    // che consentono la cancellazione a cascata

//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Milite> militi;

//    @OneToMany(mappedBy = "wamcompany", targetEntity=Evento.class)
//    @CascadeOnDelete
//    private List<Evento> eventi;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Insegnante> insegnanti;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Lettera> lettere;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Allegato> allegati;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<ModoPagamento> modiPagamento;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Progetto> progetti;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Sala> sale;
//
//    @OneToMany(mappedBy = "wamcompany")
//    @CascadeOnDelete
//    private List<Scuola> scuola;
//
//    @OneToMany(mappedBy = "wamcompany", targetEntity=PrefEventoEntity.class)
//    @CascadeOnDelete
//    private List<PrefEventoEntity> prefs;

    public WamCompany() {
        super();
    }// end of constructor


//	public void createDemoData(){
//		DemoDataGenerator.createDemoData(this);
//	};

    /**
     * Recupera una istanza di Company usando la query di una property specifica
     *
     * @param code valore della property code
     * @return istanza di Company, null se non trovata
     */
    public static WamCompany findByCode(String code) {
        WamCompany instance = null;
        BaseEntity entity = AQuery.queryOne(WamCompany.class, BaseCompany_.companyCode, code);

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
     * Ritorna la Demo Company
     *
     * @return la Demo Company, null se non esiste
     * @deprecated
     */
    public static WamCompany getDemoCompanyOld() {
        WamCompany company = null;
        Container.Filter filter = new Compare.Equal(WamCompany_.companyCode.getName(), WAMApp.DEMO_COMPANY_CODE);
        List demoCompanies = AQuery.getList(WamCompany.class, filter);
        if (demoCompanies.size() > 0) {
            company = (WamCompany) demoCompanies.get(0);
        }
        return company;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Domain Class
     *
     * @return lista di tutte le istanze di Company
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<WamCompany> findAll() {
        return (ArrayList<WamCompany>) AQuery.getLista(WamCompany.class);
    }// end of method


    @Override
    public String toString() {
        return getCompanyCode();
    }// end of method

    public boolean isVaiSubitoTabellone() {
        return vaiSubitoTabellone;
    }// end of getter method

    public void setVaiSubitoTabellone(boolean vaiSubitoTabellone) {
        this.vaiSubitoTabellone = vaiSubitoTabellone;
    }//end of setter method

    /**
     * Elimina tutti i dati di questa azienda.
     * <p>
     * L'ordine di cancellazione è critico per l'integrità referenziale
     */
    public void deleteAllData() {

        // elimina le tabelle
        AQuery.delete(Volontario.class, Volontario_.company, this);

//		AQuery.delete(Lettera.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(EventoPren.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(Prenotazione.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(TipoRicevuta.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(Rappresentazione.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Sala.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Evento.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Stagione.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Progetto.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(ModoPagamento.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Insegnante.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Scuola.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(OrdineScuola.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Comune.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Destinatario.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(Mailing.class,  CompanyEntity_.wamcompany, this);
//		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.wamcompany, this);
//
//		// elimina gli utenti
//		AQuery.delete(UtenteRuolo.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(Ruolo.class, CompanyEntity_.wamcompany, this);
//		AQuery.delete(Utente.class, CompanyEntity_.wamcompany, this);
//
//		// elimina le preferenze
//		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.wamcompany, this);

    }


}// end of entity class



