package it.algos.wam.entity.companyentity;

import com.vaadin.data.Container;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ELazyContainer;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.EntityManager;

/**
 * Created by gac on 07 mag 2016.
 * Sovrascrive la classe standard per aggiungere due bottoni/menu di spostamento dei records
 */
public class WamTablePortal extends TablePortal {

    public static final String CMD_MOVE_UP = "Sposta su";
    public static final Resource ICON_MOVE_UP = FontAwesome.ARROW_UP;
    public static final String CMD_MOVE_DN = "Sposta giu";
    public static final Resource ICON_MOVE_DN = FontAwesome.ARROW_DOWN;
    protected boolean useAllCompany;
    private boolean usaBottoniSpostamento;
    private MenuBar.MenuItem bMoveUp;
    private MenuBar.MenuItem bMoveDn;


    /**
     * Costruttore base
     */
    public WamTablePortal(ModulePop modulo) {
        super(modulo);
    }// end of constructor


    /**
     * Creates the toolbar
     * Barra standard con 5 bottoni (nuovo, modifica, elimina, cerca, selezione)
     * Sovrascrivibile, per aggiungere/modificare bottoni
     */
    @Override
    public TableToolbar createToolbar() {
        boolean developer = LibSession.isDeveloper();
        boolean admin = LibSession.isAdmin();
        toolbar = super.createToolbar();
        toolbar.setCreate(true);
        toolbar.setSelectButtonVisible(false);

        if (developer) {
            if (CompanySessionLib.isCompanySet()) {
                useAllCompany = false;
            } else {
                useAllCompany = true;
            }// end of if/else cycle
            if (isUsaBottoniSpostamento()) {
                if (useAllCompany) {
                    syncButtonsSpostamento(false);
                } else {
                    syncButtonsSpostamento(true);
                }// end of if/else cycle
            } else {
                syncButtonsSpostamento(false);
            }// end of if/else cycle
        } else {
            if (admin) {
                useAllCompany = false;
                if (isUsaBottoniSpostamento()) {
                    syncButtonsSpostamento(true);
                } else {
                    syncButtonsSpostamento(false);
                }// end of if/else cycle
            } else {
                toolbar = null;
            }// fine del blocco if-else
        }// end of if/else cycle

        return toolbar;
    }// end of method


    /*
     * Spostamento in su ed in giu dei singoli records.
     * <p>
     * Da usare solo per il funzionamento di una singola company
     * L'utente normale lo vede sempre
     * Il developer lo può usare solo se ha filtrato la singola company
     *
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
    @SuppressWarnings("all")
    protected void setFiltro(WamCompany company) {
        WamTable table = (WamTable) this.getTable();
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

        table.setColumnCollapsed(WamCompanyEntity_.company.getName(), !useAllCompany);
    }// end of method


    /**
     * Spostamento effettivo, in su o in giu del singolo record.
     * Sovrascritto
     *
     * @param sopra true per spostare in alto, false per spostare in basso
     */
    public void spostaRecord(boolean sopra) {
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
     * Sincronizza la company selezionata
     * <p>
     * Modificata la selezione della company.
     * Regola il filtro sulla company
     * Sincronizza lo stato dei bottoni.
     * Regola la company della sessione
     */
    private void syncBaseCompany(WamCompany companyNew) {

        if (companyNew == null) {
            useAllCompany = true;
            setFiltro(null);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(false);
            }// end of if cycle
        } else {
            useAllCompany = false;
            setFiltro(companyNew);
            if (isUsaBottoniSpostamento()) {
                syncButtonsSpostamento(true);
            }// end of if cycle
        }// end of if/else cycle
        CompanySessionLib.setCompany(companyNew);
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
        syncBaseCompany(companyNew);
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
        if (toolbar != null) {
            toolbar.syncButtons(singleSelected, multiSelected);
        }// end of if cycle

        if (bMoveUp != null) {
            bMoveUp.setEnabled(singleSelected);
        }// end of if cycle

        if (bMoveDn != null) {
            bMoveDn.setEnabled(singleSelected);
        }// end of if cycle

    }// end of method


    protected boolean isUsaBottoniSpostamento() {
        return usaBottoniSpostamento;
    }// end of getter method

    protected void setUsaBottoniSpostamento(boolean usaBottoniSpostamento) {
        this.usaBottoniSpostamento = usaBottoniSpostamento;
    }//end of setter method

}// end of class
