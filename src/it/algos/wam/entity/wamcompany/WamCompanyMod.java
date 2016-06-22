package it.algos.wam.entity.wamcompany;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Company module
 */
public class WamCompanyMod extends ModulePop {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Croci";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.AMBULANCE;


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public WamCompanyMod() {
        super(WamCompany.class, MENU_ADDRESS, ICON);
        getTable().setRowHeaderMode(Table.RowHeaderMode.INDEX);
    }// end of constructor


    @Override
    public ModuleForm createForm(Item item) {
        return new WamCompanyForm(item, this);
    }// end of method

    @Override
    public TablePortal createTablePortal() {
        TablePortal portaleCroce = super.createTablePortal();
        portaleCroce.delCmd(TableToolbar.CMD_SEARCH);

        return portaleCroce;
    }// end of method

    /**
     * Crea i campi visibili nella scheda (form)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{
                BaseCompany_.companyCode,
                BaseCompany_.name,
                BaseCompany_.contact,
                BaseCompany_.email,
                WamCompany_.vaiSubitoTabellone
        };
    }// end of method


    public void delete(Object id) {
        WamCompany company = WamCompany.find((Long) id);
        super.delete(id);
        fireCompanyRemoved(company);
    }// end of method


    protected void fireCompanyRemoved(WamCompany company) {
        UI ui = getUI();
        WamUI wamUI;

        if (ui instanceof WamUI) {
            wamUI = (WamUI) ui;
            wamUI.fireCompanyRemoved(company);
        }// fine del blocco if

    }// end of method

}// end of class

