package it.algos.wam.entity.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.milite.Milite_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;

import javax.persistence.Entity;
import java.util.List;


@Entity
@DefaultSort({"companyCode"})
public class Company extends BaseCompany {

    private static final long serialVersionUID = 8238775575826490450L;

    // elenco delle relazioni OneToMany
    // servono per creare le foreign key sul db
    // che consentono la cancellazione a cascata

//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Milite> militi;

//    @OneToMany(mappedBy = "company", targetEntity=Evento.class)
//    @CascadeOnDelete
//    private List<Evento> eventi;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Insegnante> insegnanti;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Lettera> lettere;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Allegato> allegati;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<ModoPagamento> modiPagamento;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Progetto> progetti;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Sala> sale;
//
//    @OneToMany(mappedBy = "company")
//    @CascadeOnDelete
//    private List<Scuola> scuola;
//
//    @OneToMany(mappedBy = "company", targetEntity=PrefEventoEntity.class)
//    @CascadeOnDelete
//    private List<PrefEventoEntity> prefs;

    public Company() {
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
    public static Company findByCode(String code) {
        Company instance = null;
        BaseEntity entity = AQuery.queryOne(Company.class, BaseCompany_.companyCode, code);

        if (entity != null) {
            if (entity instanceof Company) {
                instance = (Company) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Ritorna la Demo Company
     *
     * @return la Demo Company, null se non esiste
     */
    public static Company getDemo() {
        return findByCode(WAMApp.DEMO_COMPANY_CODE);
    }// end of method

    /**
     * Ritorna la Demo Company
     *
     * @return la Demo Company, null se non esiste
     * @deprecated
     */
    public static Company getDemoCompanyOld() {
        Company company = null;
        Container.Filter filter = new Compare.Equal(Company_.companyCode.getName(), WAMApp.DEMO_COMPANY_CODE);
        List demoCompanies = AQuery.getList(Company.class, filter);
        if (demoCompanies.size() > 0) {
            company = (Company) demoCompanies.get(0);
        }
        return company;
    }// end of method

    /**
     * Elimina tutti i dati di questa azienda.
     * <p>
     * L'ordine di cancellazione è critico per l'integrità referenziale
     */
    public void deleteAllData() {

        // elimina le tabelle
        AQuery.delete(Milite.class, Milite_.company, this);

//		AQuery.delete(Lettera.class, CompanyEntity_.company, this);
//		AQuery.delete(EventoPren.class, CompanyEntity_.company, this);
//		AQuery.delete(Prenotazione.class, CompanyEntity_.company, this);
//		AQuery.delete(TipoRicevuta.class, CompanyEntity_.company, this);
//		AQuery.delete(Rappresentazione.class,  CompanyEntity_.company, this);
//		AQuery.delete(Sala.class,  CompanyEntity_.company, this);
//		AQuery.delete(Evento.class,  CompanyEntity_.company, this);
//		AQuery.delete(Stagione.class,  CompanyEntity_.company, this);
//		AQuery.delete(Progetto.class,  CompanyEntity_.company, this);
//		AQuery.delete(ModoPagamento.class,  CompanyEntity_.company, this);
//		AQuery.delete(Insegnante.class,  CompanyEntity_.company, this);
//		AQuery.delete(Scuola.class,  CompanyEntity_.company, this);
//		AQuery.delete(OrdineScuola.class,  CompanyEntity_.company, this);
//		AQuery.delete(Comune.class,  CompanyEntity_.company, this);
//		AQuery.delete(Destinatario.class,  CompanyEntity_.company, this);
//		AQuery.delete(Mailing.class,  CompanyEntity_.company, this);
//		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.company, this);
//
//		// elimina gli utenti
//		AQuery.delete(UtenteRuolo.class, CompanyEntity_.company, this);
//		AQuery.delete(Ruolo.class, CompanyEntity_.company, this);
//		AQuery.delete(Utente.class, CompanyEntity_.company, this);
//
//		// elimina le preferenze
//		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.company, this);

    }


}// end of entity class



