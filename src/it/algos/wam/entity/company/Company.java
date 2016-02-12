package it.algos.wam.entity.company;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.milite.Milite_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.EntityQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
@DefaultSort({"companyCode"})
public class Company extends BaseCompany {

	private static final long serialVersionUID = 8238775575826490450L;
	public static final String DEMO_COMPANY_CODE="demo";

	// elenco delle relazioni OneToMany
	// servono per creare le foreign key sul db
	// che consentono la cancellazione a cascata

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Milite> militi;

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
	 * Elimina tutti i dati di questa azienda.
	 * <p>
	 * L'ordine di cancellazione è critico per l'integrità referenziale
	 */
	public void deleteAllData(){

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

	/**
	 * Ritorna la Demo Company
	 * @return la Demo Company, null se non esiste
	 */
	public static Company getDemoCompany(){
		Company company=null;
		Container.Filter filter = new Compare.Equal(Company_.companyCode.getName(), DEMO_COMPANY_CODE);
		List demoCompanies = AQuery.getList(Company.class, filter);
		if(demoCompanies.size()>0) {
			company=(Company)demoCompanies.get(0);
		}
		return company;
	}


}// end of entity class



