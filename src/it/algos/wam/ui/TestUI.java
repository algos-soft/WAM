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
        cRuoli = new CRuoli("Autista", "Primo", "Secondo", "Terzo");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        cServ = new CServizio("Ambulanza pom.");
        cRuoli = new CRuoli("Autista", "Primo", "Aiutante","Apprendista");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        return lRighe.toArray(new RTabellone[0]);
    }

    private CTurno[] creaTurniDemo() {
        List<CTurno> lTurni = new ArrayList();

        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));
        lTurni.add(new CTurno(creaIscrizioni()));

        return lTurni.toArray(new CTurno[0]);
    }

    private CIscrizione[] creaIscrizioni(){
        List<CIscrizione> lIscrizioni = new ArrayList();

        lIscrizioni.add(new CIscrizione("Bianchini F."));
        lIscrizioni.add(new CIscrizione("Madella C."));
        lIscrizioni.add(new CIscrizione("---"));
        lIscrizioni.add(new CIscrizione("Rossi G."));

        return lIscrizioni.toArray(new CIscrizione[0]);

    }

}
