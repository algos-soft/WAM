package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import it.algos.wam.tabellone.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 20/02/16.
 */
@Theme("wam")
@Title("WAM:test")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        RTabellone[] righe = creaRighe();
        Component comp = new CTabellone(righe);
        setContent(comp);
    }

    private RTabellone[] creaRighe() {
        List<RTabellone> lRighe = new ArrayList();

        CServizio cServ;
        CRuoli cRuoli;
        CTurno[] cTurni;

        cServ = new CServizio("Ambulanza mattino");
        cRuoli = new CRuoli("Autista", "Primo", "Secondo");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        cServ = new CServizio("Ambulanza mattino");
        cRuoli = new CRuoli("Autista", "Primo", "Secondo");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        return lRighe.toArray(new RTabellone[0]);
    }

    private CTurno[] creaTurniDemo() {
        List<CTurno> lTurni = new ArrayList();

        lTurni.add(new CTurno());
        lTurni.add(new CTurno());
        lTurni.add(new CTurno());
        lTurni.add(new CTurno());
        lTurni.add(new CTurno());
        lTurni.add(new CTurno());
        lTurni.add(new CTurno());

        return lTurni.toArray(new CTurno[0]);
    }

}
