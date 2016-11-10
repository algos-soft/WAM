package it.algos.wam.entity.companyentity;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.wamcompany.WamCompany;

/**
 * Created by gac on 20 ott 2016.
 * .
 */
public interface FunzioneListener {

    void doDeleteFunz(EditorWam editor);

    void doDeleteServ(EditorServ editor);

    WamCompany getCompany();
    Funzione getFunzione();
}// end of interface
