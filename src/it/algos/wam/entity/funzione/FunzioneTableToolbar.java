package it.algos.wam.entity.funzione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.EntityManager;

/**
 * Created by alex on 08/04/16.
 */
public class FunzioneTableToolbar extends TableToolbar {

    public static final String CMD_MOVE_UP = "Sposta su";
    public static final Resource ICON_MOVE_UP = FontAwesome.ARROW_UP;

    public static final String CMD_MOVE_DN = "Sposta giu";
    public static final Resource ICON_MOVE_DN = FontAwesome.ARROW_DOWN;

    private MenuBar.MenuItem bMoveUp;
    private MenuBar.MenuItem bMoveDn;

    private ATable table;

    public FunzioneTableToolbar(ATable table) {

        super();

        this.table=table;

        bMoveUp = addButton(CMD_MOVE_UP, ICON_MOVE_UP, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Funzione funz = (Funzione)table.getSelectedEntity();
                Funzione adiacente = WamQuery.queryFunzioneAdiacente(table.getEntityManager(), funz, true);
                if(funz!=null && adiacente!=null){
                    swap(funz,adiacente);
                }
            }
        });

        bMoveDn=addButton(CMD_MOVE_DN, ICON_MOVE_DN, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Funzione funz = (Funzione)table.getSelectedEntity();
                Funzione adiacente = WamQuery.queryFunzioneAdiacente(table.getEntityManager(), funz, false);
                if(funz!=null && adiacente!=null){
                    swap(funz,adiacente);
                }
            }
        });

        // initial sync call (no rows selected)
        syncButtons(false, false);


    }

    /**
     * Scambia il numero d'ordine di due record
     */
    private void swap(Funzione f1, Funzione f2){
        int o1=f1.getOrdine();
        int o2=f2.getOrdine();

        f1.setOrdine(o2);
        f2.setOrdine(o1);

        EntityManager manager = table.getEntityManager();
        manager.getTransaction().begin();
        try {
            manager.merge(f1);
            manager.merge(f2);
            manager.getTransaction().commit();
            table.refresh();
        }catch (Exception e){
            manager.getTransaction().rollback();
        }

    }

    @Override
    // qui no ricerca
    protected void addSearch() {
    }

    @Override
    protected void syncButtons(boolean singleSelected, boolean multiSelected) {
        super.syncButtons(singleSelected, multiSelected);

        if(bMoveUp!=null){
            bMoveUp.setEnabled(singleSelected);
        }
        if(bMoveDn!=null){
            bMoveDn.setEnabled(singleSelected);
        }

    }
}
