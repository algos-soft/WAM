package it.algos.wam.entity.funzione;

import it.algos.wam.entity.wamcompany.WamCompany;

/**
 * Created by gac on 20 ott 2016.
 * .
 */
public interface FunzioneListener {

    void doDelete(EditorFunz editor);
    WamCompany getCompany();

}// end of interface
