package it.algos.wam.entity.funzione;

import it.algos.wam.entity.servizio.ServizioTableToolbar;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

/**
 * Created by alex on 08/04/16.
 */
public class FunzioneTablePortal extends TablePortal {
    public FunzioneTablePortal(ModulePop modulo) {
        super(modulo);
    }

    public TableToolbar createToolbar() {
        return new FunzioneTableToolbar(getTable());
    }

}
