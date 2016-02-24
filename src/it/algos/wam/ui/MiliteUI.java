package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.wam.entity.company.CompanyModulo;
import it.algos.webbase.domain.vers.VersMod;
import it.algos.webbase.web.ui.AlgosUI;

/**
 * La UI del Milite.
 * Accede al tabellone e gestisce le proprie iscrizioni
 */
@Theme("algos")
public class MiliteUI extends  AlgosUI {

//    @Override
//    protected void init(VaadinRequest request) {
////        setContent(new Label("UI del Milite"));
//    }

    /**
     * Aggiunge i moduli specifici dell'applicazione (oltre a LogMod, VersMod, PrefMod)
     * <p>
     * Deve (DEVE) essere sovrascritto dalla sottoclasse per aggiungere i moduli alla menubar dell'applicazione <br>
     * Chiama il metodo  addModulo(...) della superclasse per ogni modulo previsto nella barra menu
     */
    @Override
    protected void addModuli() {
        this.addModulo(new VersMod());
    }// end of method


}
