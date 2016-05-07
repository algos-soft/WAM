package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.lib.LibSession;
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

    public static final String CMD_MOVE_UP = "Sposta su";
    public static final Resource ICON_MOVE_UP = FontAwesome.ARROW_UP;
    public static final String CMD_MOVE_DN = "Sposta giu";
    public static final Resource ICON_MOVE_DN = FontAwesome.ARROW_DOWN;
    private final static String MENU_CROCI_CAPTION = "Croce";
    private final static String ITEM_ALL_CROCI = "tutte";
    protected TableToolbar toolbar;
    private boolean usaBottoniSpostamento;
    private HashMap<WamCompany, MenuBar.MenuItem> croci;
    private MenuBar.MenuItem bMoveUp;
    private MenuBar.MenuItem bMoveDn;

    public WamTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor

    public TableToolbar createToolbar() {
        boolean utenteSviluppatore = LibSession.isDeveloper();
        toolbar = super.createToolbar();
        toolbar.setCreate(true);

        if (utenteSviluppatore) {
            addMenuCroci();
            fixCompany();
        } else {
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(true);
            }// end of if cycle
        }// end of if/else cycle

        return toolbar;
    }// end of method


    /**
     * Regolazione iniziale se è selezionata una company.
     */
    private void fixCompany() {
        BaseCompany company = CompanySessionLib.getCompany();

        if (company != null) {
            syncCompany((WamCompany) company);
        } else {
            syncCompany(null);
        }// end of if/else cycle

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
                syncCompany(null);
            }// end of inner method
        });// end of anonymous inner class
        croci.put(null, subItem);
        for (WamCompany company : WamCompany.findAll()) {
            subItem = item.addItem(company.toString(), null, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    syncCompany(company);
                }// end of inner method
            });// end of anonymous inner class
            croci.put(company, subItem);
        }// end of for cycle

    }// end of method


    /*
     * Spostamento in su ed in giu dei singoli records.
     * <p>
     * Da usare solo per il funzionamento di una singola company
     * L'utente normale lo vede sempre
     * Il developer lo può usare solo se ha filtrato la singola company
     */
    private void addMenuSpostaRecords() {

        if (bMoveUp == null) {
            bMoveUp = toolbar.addButton(CMD_MOVE_UP, ICON_MOVE_UP, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    spostaSu();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

        if (bMoveDn == null) {
            bMoveDn = toolbar.addButton(CMD_MOVE_DN, ICON_MOVE_DN, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    spostaGiu();
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

        // initial sync call (no rows selected)
        syncButtons(false, false);

    }// end of method


    /*
     * Elimina i menu sposta records.
     */
    private void delMenuSpostaRecords() {
        Component compMoveUp;
        Component compMoveDn;

        compMoveUp = getComp(bMoveUp);
        if (compMoveUp != null) {
            toolbar.commandLayout.removeComponent(compMoveUp);
            bMoveUp = null;
        }// end of if cycle

        compMoveDn = getComp(bMoveDn);
        if (compMoveDn != null) {
            toolbar.commandLayout.removeComponent(compMoveDn);
            bMoveDn = null;
        }// end of if cycle

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
                ((ELazyContainer) cont).setFilter(company);
            } else {
                ((ELazyContainer) cont).setFilter(null);
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

    /**
     * Spostamento in su del singolo record.
     * Sovrascritto
     */
    protected void spostaSu() {
    }// end of method

    /**
     * Spostamento in giu del singolo record.
     * Sovrascritto
     */
    protected void spostaGiu() {
    }// end of method


    /**
     * Modificata la selezione della company.
     * Regola il filtro sulla company
     * Sincronizza lo stato dei bottoni.
     */
    protected void syncCompany(WamCompany company) {

        if (company == null) {
            setFiltro(null);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(false);
            }// end of if cycle

        } else {
            setFiltro(company);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(true);
            }// end of if cycle
        }// end of if/else cycle

    }// end of method

    /**
     * Regola l'esistenza dei bottoni di spostamento
     * Spostamento in su ed in giu dei singoli records.
     * <p>
     * Da usare solo per il funzionamento di una singola company
     * L'utente normale li vede sempre
     * Il developer li può usare solo se ha filtrato la singola company
     */
    protected void syncButtonsSpostamento(boolean esistenti) {

        if (esistenti) {
            addMenuSpostaRecords();
        } else {
            delMenuSpostaRecords();
        }// end of if/else cycle

    }// end of method

    protected void syncButtons(boolean singleSelected, boolean multiSelected) {
        toolbar.syncButtons(singleSelected, multiSelected);

        if (bMoveUp != null) {
            bMoveUp.setEnabled(singleSelected);
        }// end of if cycle

        if (bMoveDn != null) {
            bMoveDn.setEnabled(singleSelected);
        }// end of if cycle
    }// end of method

    /**
     * Recupera il componente grafico corrispondente al menu indicato.
     */
    protected Component getComp(MenuBar.MenuItem item) {
        Component comp = null;
        int max = toolbar.commandLayout.getComponentCount();
        MenuBar bottoneVisibile;
        MenuBar.MenuItem itemTmp;

        for (int k = 0; k < max; k++) {
            comp = toolbar.commandLayout.getComponent(k);
            if (comp instanceof MenuBar) {
                bottoneVisibile = (MenuBar) comp;
                itemTmp = bottoneVisibile.getItems().get(0);
                if (itemTmp == item) {
                    return comp;
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return comp;
    }// end of method

    protected boolean isUsaBottoniSpostamento() {
        return usaBottoniSpostamento;
    }// end of getter method

    protected void setUsaBottoniSpostamento(boolean usaBottoniSpostamento) {
        this.usaBottoniSpostamento = usaBottoniSpostamento;
    }//end of setter method
}// end of class
