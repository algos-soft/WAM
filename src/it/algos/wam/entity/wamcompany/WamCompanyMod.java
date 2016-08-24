package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;

/**
 * Company module
 */
public class WamCompanyMod extends ModulePop {

    public final static String ITEM_ALL_CROCI = "Tutte";

    // versione della classe per la serializzazione
    private final static long serialVersionUID = 1L;

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

    /**
     * Crea i sottomenu specifici del modulo
     * <p>
     * Invocato dal metodo AlgosUI.addModulo()
     * Sovrascritto dalla sottoclasse
     *
     * @param menu principale del modulo
     */
    @Override
    public void addSottoMenu(MenuBar.MenuItem menu) {
        MenuBar.MenuItem menuItem = menu.addItem("Filtro", null, null);

        addCommandAllCroci(menuItem);

        for (WamCompany company : WamCompany.findAll()) {
            addCommandSingolaCroce(menuItem, company);
        }// end of for cycle

    }// end of method

    /**
     * Costruisce un menu per selezionare tutte le croci
     *
     * @param menuItem del modulo a cui aggiungere i sottomenu
     */
    private void addCommandAllCroci(MenuBar.MenuItem menuItem) {
        MenuBar.MenuItem menu = null;

        menuItem.addItem(ITEM_ALL_CROCI, null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                fireCompanyChanged(null);
                spuntaMenu(menuItem, null);
            }// end of inner method
        });// end of anonymous inner class

    }// end of method

    /**
     * Costruisce un menu per selezionare la singola croce da filtrare
     *
     * @param menuItem a cui agganciare il bottone/item
     * @param company  croce da filtrare
     */
    private void addCommandSingolaCroce(MenuBar.MenuItem menuItem, WamCompany company) {

        menuItem.addItem(LibText.primaMaiuscola(company.getCompanyCode()), null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                fireCompanyChanged(company);
                spuntaMenu(menuItem, company);
            }// end of inner method
        });// end of anonymous inner class

    }// end of method

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
                WamCompany_.tabellonePubblico
        };
    }// end of method

    public void delete(Object id) {
        WamCompany company = WamCompany.find((Long) id);
        company.delete();
        fireCompanyRemoved(company);
    }// end of method

    /**
     * Spunta il menu selezionato
     * Elimina la spunta in tutti gli altri
     *
     * @param menuItem a cui agganciare il bottone/item
     * @param company  croce da filtrare
     */
    private void spuntaMenu(MenuBar.MenuItem menuItem, WamCompany company) {
        String sigla = "";

        for (MenuBar.MenuItem item : menuItem.getChildren()) {
            item.setIcon(FontAwesome.MINUS);
        }// end of for cycle

        if (company == null) {
            sigla = ITEM_ALL_CROCI;
        } else {
            sigla = LibText.primaMaiuscola(company.getCompanyCode());
        }// end of if/else cycle
        for (MenuBar.MenuItem item : menuItem.getChildren()) {
            if (item.getText().equals(sigla)) {
                item.setIcon(FontAwesome.CHECK);
            }// end of if cycle
        }// end of for cycle

    }// end of method

    /**
     *
     */
    protected void fireCompanyChanged(WamCompany company) {
        UI ui = getUI();
        WamUI wamUI;

        if (ui instanceof WamUI) {
            wamUI = (WamUI) ui;
            wamUI.fireCompanyChanged(company);
        }// fine del blocco if
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

