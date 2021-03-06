package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.lib.LibWam;
import it.algos.wam.migration.Migration;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;

/**
 * Company module
 */
public class WamCompanyMod extends ModulePop {

    public final static String ITEM_ALL_CROCI = "Tutte";

    // versione della classe per la serializzazione
    private final static long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Associazione";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.AMBULANCE;

    private MenuBar.MenuItem menuFiltro;

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
        if (LibSession.isDeveloper()) {
            this.addMenuFiltro(menu);
            this.addMenuImport(menu);
        }// end of if cycle
    }// end of method

    /**
     * Filtro per selezionare una croce oppure tutte
     *
     * @param menu principale del modulo
     */
    private void addMenuFiltro(MenuBar.MenuItem menu) {
        menuFiltro = menu.addItem("Filtro", null, null);
        creaFiltri();
    }// end of method

    /**
     * Creazione dei filtri singoli
     */
    public void creaFiltri() {
        menuFiltro.removeChildren();
        addCommandAllCroci(menuFiltro);

        for (WamCompany company : WamCompany.findAll()) {
            addCommandSingolaCroce(menuFiltro, company);
        }// end of for cycle

        WamCompany companyCurrent = (WamCompany) CompanySessionLib.getCompany();
        spuntaMenu(menuFiltro, companyCurrent);
    }// end of method

    /**
     * Filtro per importare i dati da webambulanze
     *
     * @param menu principale del modulo
     */
    private void addMenuImport(MenuBar.MenuItem menu) {

        menu.addItem("Importa 2017", null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if (LibWam.isCompany()) {
                    new Migration(LibWam.getCompanySigla(), 2017);
                } else {
                    new Migration(2017);
                }// end of if/else cycle
                creaFiltri();
                getTable().refresh();
            }// end of inner method
        });// end of anonymous inner class

        menu.addItem("Importa all", null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                if (LibWam.isCompany()) {
                    new Migration(LibWam.getCompanySigla());
                } else {
                    new Migration();
                }// end of if/else cycle
                creaFiltri();
                getTable().refresh();
            }// end of inner method
        });// end of anonymous inner class

    }// end of method

    /**
     * Costruisce un menu per selezionare tutte le croci
     *
     * @param menuItem del modulo a cui aggiungere i sottomenu
     */
    private void addCommandAllCroci(MenuBar.MenuItem menuItem) {
        MenuBar.MenuItem sottoMenu = menuItem.addItem(ITEM_ALL_CROCI, null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                fireCompanyChanged(null);
            }// end of inner method
        });// end of anonymous inner class

        sottoMenu.setCheckable(true);
    }// end of method

    /**
     * Costruisce un menu per selezionare la singola croce da filtrare
     *
     * @param menuItem a cui agganciare il bottone/item
     * @param company  croce da filtrare
     */
    private void addCommandSingolaCroce(MenuBar.MenuItem menuItem, WamCompany company) {
        MenuBar.MenuItem sottoMenu = menuItem.addItem(LibText.primaMaiuscola(company.getCompanyCode()), null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                fireCompanyChanged(company);
            }// end of inner method
        });// end of anonymous inner class

        sottoMenu.setCheckable(true);
    }// end of method

    @Override
    public ModuleForm createForm(Item item) {
        return new WamCompanyForm(item, this);
    }// end of method

    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new WamCompanyTable(this);
    }// end of method

    @Override
    public TablePortal createTablePortal() {
        TablePortal portaleCroce = super.createTablePortal();
        portaleCroce.delCmd(TableToolbar.CMD_SEARCH);

        if (!LibSession.isDeveloper()) {
            portaleCroce.getToolbar().setCreateButtonVisible(false);
            portaleCroce.getToolbar().setDeleteButtonVisible(false);
            portaleCroce.getToolbar().setSearchButtonVisible(false);
            portaleCroce.getToolbar().setSelectButtonVisible(false);
        }// end of if cycle

        return portaleCroce;
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
            item.setChecked(false);
        }// end of for cycle

        if (company == null) {
            sigla = ITEM_ALL_CROCI;
        } else {
            sigla = LibText.primaMaiuscola(company.getCompanyCode());
        }// end of if/else cycle
        for (MenuBar.MenuItem item : menuItem.getChildren()) {
            if (item.getText().equals(sigla)) {
                item.setChecked(true);
            }// end of if cycle
        }// end of for cycle

    }// end of method


    /**
     *
     */
    protected void fireCompanyChanged(WamCompany company) {
        CompanySessionLib.setCompany(company);
        Page.getCurrent().reload();
    }// end of method


    protected void fireCompanyRemoved(WamCompany company) {
        UI ui = getUI();
        WamUI wamUI;

        if (ui instanceof WamUI) {
            wamUI = (WamUI) ui;
            wamUI.fireCompanyRemoved(company);
        }// fine del blocco if

    }// end of method

    /**
     * Invoked when table data changes
     */
    protected void tableDataChanged() {
        super.tableDataChanged();

        if (!LibSession.isDeveloper()) {
            TablePortal tablePortal = getTablePortal();
            if (tablePortal != null) {
                TableToolbar tableToolbar = tablePortal.getToolbar();
                if (tableToolbar != null) {
                    tableToolbar.setInfoText("");
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

    }// end of method

}// end of class

