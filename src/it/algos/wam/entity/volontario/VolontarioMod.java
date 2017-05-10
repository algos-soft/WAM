package it.algos.wam.entity.volontario;


import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.funzione.FunzioneSearch;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.ServizioTablePortal;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.search.SearchManager;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * Modulo del Volontario  (in alcuni casi detto Milite)
 */
public class VolontarioMod extends WamMod {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Volontari";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.USER;


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public VolontarioMod() {
        super(Volontario.class, MENU_ADDRESS, ICON);

        addRecordSavedListener(new RecordSavedListener() {
            @Override
            public void recordCreated(RecordEvent e) {
            }

            @Override
            public void recordSaved(RecordEvent e) {
            }
        });

        addRecordDeletedListener(new RecordDeletedListener() {
            @Override
            public void recordDeleted(RecordEvent e) {
            }
        });
    }// end of constructor

    @Override
    public ModuleForm createForm(Item item) {
        return new VolontarioForm(item, this);
    }// end of method

    /**
     * Create the Search Manager
     *
     * @return the SearchManager
     */
    @Override
    public SearchManager createSearchManager() {
        return new VolontarioSearch(this);
    }// end of method


    @Override
    public ATable createTable() {
        return new VolontarioTable(this);
    }// end of method

    @Override
    public TablePortal createTablePortal() {
        return new VolontarioTablePortal(this);
    }// end of method

    /**
     * Crea i campi visibili nella lista (table)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    @SuppressWarnings("all")
    protected Attribute<?, ?>[] creaFieldsList() {
        return super.addCompanyField(new Attribute[]{
                Volontario_.nome,
                Volontario_.cognome,
                Volontario_.dataNascita,
                Volontario_.cellulare,
                Volontario_.dipendente,
                Volontario_.attivo});
    }// end of method


//    /**
//     * Crea i campi visibili nella scheda (form)
//     * <p>
//     * Come default spazzola tutti i campi della Entity <br>
//     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
//     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
//     */
//    protected Attribute<?, ?>[] creaFieldsForm() {
//        return new Attribute[]{
//                WamCompanyEntity_.company,
//                Volontario_.funzioni,
//                Volontario_.nome,
//                Volontario_.cognome,
//                Volontario_.cellulare,
//                Volontario_.telefono,
//                Volontario_.dataNascita,
//                Volontario_.email,
//                Volontario_.note,
//                Volontario_.dipendente,
//                Volontario_.attivo};
//    }// end of method

    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return new Attribute[]{
                WamCompanyEntity_.company,
                Volontario_.nome,
                Volontario_.cognome,
                Volontario_.cellulare,
                Volontario_.email,
                Volontario_.dipendente,
                Volontario_.attivo,
                Volontario_.admin,
                Volontario_.invioMail,
        };
    }// end of method

    /**
     * Override di delete per controllare che non ci siano
     * volontari iscritti a qualche turno
     */
    @Override
    public void delete() {
        boolean cancella = true;
        Volontario vol = null;
        int num = 0;

        for (Object id : getTable().getSelectedIds()) {
            vol = getVol(id);
            num = AQuery.count(Iscrizione.class, Iscrizione_.volontario, vol);
            if (num > 0) {
                cancella = false;
                break;
            }// end of if cycle
        }// end of for cycle

        if (cancella) {
            super.delete();
        } else {
            if (getTable().getSelectedIds().length == 1) {
                Notification.show("Impossibile cancellare il volontario selezionato perché è già iscritto ad alcuni turni.", Notification.Type.WARNING_MESSAGE);
            } else {
                Notification.show("Impossibile cancellare i volontari selezionati perché sono già iscritti ad alcuni turni.", Notification.Type.WARNING_MESSAGE);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

    @Override
    public void companyAdded(WamCompany company) {
        super.companyAdded(company);
    }// end of method

    @Override
    public void companyRemoved(WamCompany company) {
        super.companyRemoved(company);
    }// end of method

    @Override
    public void companyChanged(WamCompany company) {
        super.companyChanged(company);

        tablePortal = createTablePortal();
        tablePortal.table = this.createTable();
        tablePortal.table.refresh();
    }// end of method

    private Volontario getVol(Object id) {
        Volontario volontario = null;
        BaseEntity entity = getEntityManager().find(Volontario.class, id);

        if (entity != null) {
            volontario = (Volontario) entity;
        }// end of if cycle

        return volontario;
    }// end of method

}// end of class

