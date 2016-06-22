package it.algos.wam.entity.volontario;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.servizio.ServizioTablePortal;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

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


    /**
     * Crea i campi visibili nella scheda (form)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{
                WamCompanyEntity_.company,
                Volontario_.funzioni,
                Volontario_.nome,
                Volontario_.cognome,
                Volontario_.cellulare,
                Volontario_.telefono,
                Volontario_.dataNascita,
                Volontario_.email,
                Volontario_.note,
                Volontario_.dipendente,
                Volontario_.attivo};
    }// end of method

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
                Volontario_.attivo};
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

//        tablePortal = createTablePortal();
//        tablePortal.table = this.createTable();
//        tablePortal.table.refresh();

//        return new VolontarioTable(this);
    }// end of method

}// end of class

