package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.EntityManager;
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
    private final static String ITEM_ALL_CROCI = "...";
    protected boolean useAllCompany;
    private boolean usaBottoniSpostamento;
    private HashMap<WamCompany, MenuBar.MenuItem> croci;
    private MenuBar.MenuItem bCroci;
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
            useAllCompany = true;
            addMenuCroci();
            fixInizialeCompany();
        } else {
            useAllCompany = false;
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(true);
            }// end of if cycle
        }// end of if/else cycle

        return toolbar;
    }// end of method

    /**
     * Regolazione iniziale se è selezionata una company.
     */
    private void fixInizialeCompany() {
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
        MenuBar.MenuItem subItem;
        croci = new HashMap<WamCompany, MenuBar.MenuItem>();

        bCroci = toolbar.addButton(MENU_CROCI_CAPTION, FontAwesome.NAVICON, null);

        subItem = bCroci.addItem(ITEM_ALL_CROCI, null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                fireCompanyChanged(null);
            }// end of inner method
        });// end of anonymous inner class
        for (WamCompany company : WamCompany.findAll()) {
            subItem = bCroci.addItem(LibText.primaMaiuscola(company.getCompanyCode()), null, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    fireCompanyChanged(company);
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
                    spostaRecord(true);
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

        if (bMoveDn == null) {
            bMoveDn = toolbar.addButton(CMD_MOVE_DN, ICON_MOVE_DN, new MenuBar.Command() {
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    spostaRecord(false);
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

        // initial sync call (no rows selected)
        syncButtons(false, false);

    }// end of method

    /*
     * Elimina i menu sposta records.
     * <p>
     * Elimina il componente grafico
     * Elimina l'oggetto comando
     */
    private void delMenuSpostaRecords() {

        super.delCmd(CMD_MOVE_UP);
        bMoveUp = null;

        super.delCmd(CMD_MOVE_DN);
        bMoveDn = null;

    }// end of method

    /**
     * Shows in the table only the needed wamcompany
     * Creates a filter corresponding to the needed wamcompany in the table
     * I filtri sono comprensivi del livello sottostante (GreaterOrEqual)
     */
    protected void setFiltro(WamCompany company) {
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

        getTable().setColumnCollapsed(WamCompanyEntity_.company.getName(), !useAllCompany);

        this.spuntaMenu(company);
    }// end of method

    /**
     * Spunta il menu selezionato
     * Elimina la spunta in tutti gli altri
     */
    private void spuntaMenu(WamCompany croceSelezionata) {
        MenuBar.MenuItem subItem;

        if (croceSelezionata != null && croci.containsKey(croceSelezionata)) {
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
     * Spostamento effettivo, in su o in giu del singolo record.
     * Sovrascritto
     *
     * @param sopra true per spostare in alto, false per spostare in basso
     */
    protected void spostaRecord(boolean sopra) {
    }// end of method

    /**
     * Effettua la transazione di swap
     */
    protected void swapCommit(BaseEntity entity1, BaseEntity entity2) {
        EntityManager manager = table.getEntityManager();
        manager.getTransaction().begin();
        try {
            manager.merge(entity1);
            manager.merge(entity2);
            manager.getTransaction().commit();
            table.refresh();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        }

    }// end of method

    /**
     * Aggiunge una company al menu
     * <p>
     * Creata una nuova company.
     */
    public void addCompany(WamCompany companyNew) {

    }// end of method

    /**
     * Elimina una company dal menu
     * <p>
     * Cancellata una company.
     */
    public void deleteCompany(WamCompany companyNew) {

    }// end of method

    /**
     * Sincronizza la company selezionata
     * <p>
     * Modificata la selezione della company.
     * Regola il filtro sulla company
     * Sincronizza lo stato dei bottoni.
     * Regola la company della sessione
     */
    public void syncCompany(WamCompany companyNew) {

        if (companyNew == null) {
            useAllCompany = true;
            setFiltro(null);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(false);
            }// end of if cycle
            bCroci.setText(ITEM_ALL_CROCI);
        } else {
            useAllCompany = false;
            setFiltro(companyNew);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(true);
            }// end of if cycle
            bCroci.setText(LibText.primaMaiuscola(companyNew.getCompanyCode()));
        }// end of if/else cycle
        CompanySessionLib.setCompany(companyNew);

        table.deselectAll();
        table.refresh();
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

    protected void fireCompanyChanged(WamCompany company) {
        UI ui = getUI();
        WamUI wamUI;

        if (ui instanceof WamUI) {
            wamUI = (WamUI) ui;
            wamUI.fireCompanyChanged(company);
        }// fine del blocco if

    }// end of method

    protected boolean isUsaBottoniSpostamento() {
        return usaBottoniSpostamento;
    }// end of getter method

    protected void setUsaBottoniSpostamento(boolean usaBottoniSpostamento) {
        this.usaBottoniSpostamento = usaBottoniSpostamento;
    }//end of setter method

}// end of class
