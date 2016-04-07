package it.algos.wam.entity.servizio;

import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by alex on 7-04-2016.
 */
public class ServizioTablePortal extends TablePortal {

    public ServizioTablePortal(ModulePop modulo) {
        super(modulo);
    }

    public TableToolbar createToolbar() {
        return new ServizioTableToolbar(getTable());
    }

}
