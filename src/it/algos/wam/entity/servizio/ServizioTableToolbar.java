package it.algos.wam.entity.servizio;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.EntityManager;

/**
 * Created by alex on 7-04-2016.
 */
public class ServizioTableToolbar extends TableToolbar {

//    public static final String CMD_MOVE_UP = "Sposta su";
//    public static final Resource ICON_MOVE_UP = FontAwesome.ARROW_UP;
//
//    public static final String CMD_MOVE_DN = "Sposta giu";
//    public static final Resource ICON_MOVE_DN = FontAwesome.ARROW_DOWN;
//
//    private MenuBar.MenuItem bMoveUp;
//    private MenuBar.MenuItem bMoveDn;
//
//    private ATable table;

    public ServizioTableToolbar(ATable table) {

//        super();
//
//        this.table=table;
//
//        bMoveUp = addButton(CMD_MOVE_UP, ICON_MOVE_UP, new MenuBar.Command() {
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Servizio serv = (Servizio)table.getSelectedEntity();
//                Servizio adiacente = WamQuery.queryServizioAdiacente(table.getEntityManager(), serv, true);
//                if(serv!=null && adiacente!=null){
//                    swap(serv,adiacente);
//                }
//            }
//        });
//
//        bMoveDn=addButton(CMD_MOVE_DN, ICON_MOVE_DN, new MenuBar.Command() {
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                Servizio serv = (Servizio)table.getSelectedEntity();
//                Servizio adiacente = WamQuery.queryServizioAdiacente(table.getEntityManager(), serv, false);
//                if(serv!=null && adiacente!=null){
//                    swap(serv,adiacente);
//                }
//            }
//        });
//
//        // initial sync call (no rows selected)
//        syncButtons(false, false);
//

    }

//    /**
//     * Scambia il numero d'ordine di due servizi
//     */
//    private void swap(Servizio s1, Servizio s2){
//        int o1=s1.getOrdine();
//        int o2=s2.getOrdine();
//
//        s1.setOrdine(o2);
//        s2.setOrdine(o1);
//
//        EntityManager manager = table.getEntityManager();
//        manager.getTransaction().begin();
//        try {
//            manager.merge(s1);
//            manager.merge(s2);
//            manager.getTransaction().commit();
//            table.refresh();
//        }catch (Exception e){
//            manager.getTransaction().rollback();
//        }
//
//    }
//
//    @Override
//    // qui no ricerca
//    protected void addSearch() {
//    }
//
//    @Override
//    public void syncButtons(boolean singleSelected, boolean multiSelected) {
//        super.syncButtons(singleSelected, multiSelected);
//
//        if(bMoveUp!=null){
//            bMoveUp.setEnabled(singleSelected);
//        }
//        if(bMoveDn!=null){
//            bMoveDn.setEnabled(singleSelected);
//        }
//
//    }
}
