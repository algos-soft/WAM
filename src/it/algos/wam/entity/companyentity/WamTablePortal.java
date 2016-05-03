package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.util.HashMap;

/**
 * Created by gac on 27 feb 2016.
 * Sovrascrive la classe standard per aggiungere un bottone/menu di filtro sulla croce da selezionare
 */
public class WamTablePortal extends TablePortal {

    private final static String MENU_CROCI_CAPTION = "Croce";
    private final static String ITEM_ALL_CROCI = "tutte";
    private TableToolbar toolbar;
    private HashMap<WamCompany, MenuBar.MenuItem> croci;


    public WamTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        toolbar = super.createToolbar();
        toolbar.setCreate(true);

        addMenuCroci();
//        setFiltro(Livello.info);
        return toolbar;
    }// end of method

    /**
     * Croci selection.
     * <p>
     * Costruisce un menu per selezionare la croce da filtrare
     * Costruisce i menuItem in funzione delle croci esistenti
     */
    private void addMenuCroci() {
        MenuBar.MenuItem item = null;
        MenuBar.MenuItem subItem;
        croci = new HashMap<WamCompany, MenuBar.MenuItem>();

        item = toolbar.addButton(MENU_CROCI_CAPTION, FontAwesome.NAVICON, null);

        subItem = item.addItem(ITEM_ALL_CROCI, null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                setFiltro(null);
            }// end of inner method
        });// end of anonymous inner class
        croci.put(null, subItem);
        for (WamCompany company : WamCompany.findAll()) {
            subItem = item.addItem(company.toString(), null, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    setFiltro(company);
                }// end of inner method
            });// end of anonymous inner class
            croci.put(company, subItem);
        }// end of for cycle

    }// end of method


    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    private void setFiltro(WamCompany company) {
        Container.Filter filter = null;
        ATable table = this.getTable();
        Container.Filterable cont = null;

        if (table != null) {
            cont = table.getFilterableContainer();
            if (company != null) {
                ((ELazyContainer)cont).setFilter(company);
            } else {
                ((ELazyContainer)cont).setFilter(null);
            }// fine del blocco if-else

            table.refresh();
        }// end of if cycle

        this.spuntaMenu(company);
    }// end of method

    /**
     * Spunta il menu selezionato
     * Elimina la spunta in tutti gli altri
     */
    private void spuntaMenu(WamCompany croceSelezionata) {
        MenuBar.MenuItem subItem;

        if (croci.containsKey(croceSelezionata)) {
            subItem = croci.get(croceSelezionata);
            if (subItem != null) {
                for (WamCompany croce : croci.keySet()) {
                    croci.get(croce).setIcon(FontAwesome.MINUS);
                }// end of for cycle

                subItem.setIcon(FontAwesome.CHECK);
            }// fine del blocco if
        }// fine del blocco if

    }// end of method

}// end of class
