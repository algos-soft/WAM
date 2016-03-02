package it.algos.wam.tabellone;

import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.milite.Milite;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapTurno;

/**
 * Componente grafico che rappresenta un Turno
 * Ospita diverse Iscrizioni
 * Created by alex on 20/02/16.
 */
public class CTurno extends VerticalLayout {


    /**
     * Costruttore vuoto
     */
    public CTurno() {
        super();

        setSpacing(false);

        setWidth("6em");

        addComponent(new CIscrizione("---"));

        //--@todo provvisorio per spaziare le righe tutte alte uguali anche se vuote - Da risolvere col css
        addComponent(new CIscrizione("---"));
        addComponent(new CIscrizione("---"));
        addComponent(new CIscrizione("---"));
    }// end of constructor



    /**
     * Costruttore completo
     *
     * @param turno singolo
     */
    public CTurno(Turno turno) {
        super();

        if (turno == null) {
            return;
        }// end of if cycle

        setSpacing(false);

        setWidth("6em");

        WrapTurno wrap = turno.getWrapTurno();
        if (wrap != null) {
            if (wrap.getIscrizioni() != null) {
                for (Iscrizione iscrizione : wrap.getIscrizioni()) {
                    Milite milite = iscrizione.getMilite();
                    if (milite != null) {
                        addComponent(new CIscrizione(milite.toString()));
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of if cycle

    }// end of constructor


    /**
     * @deprecated
     */
    public CTurno(CIscrizione... iscrizioni) {

        setSpacing(false);

        setWidth("6em");

        for (CIscrizione i : iscrizioni) {
            addComponent(i);
        }
    }

}// end of class
