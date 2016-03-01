package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
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
@Title("WAM:test")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        String themeName;
        if(Page.getCurrent().getWebBrowser().isTouchDevice()) {
            themeName="wam-mobile";
        }else{
            themeName="wam-mobile";
        }
        setTheme(themeName);

        RTabellone[] righe = creaRighe();
        Component comp = new CTabellone(righe);
//        Component comp = new CRuoli("Autista", "Primo", "Secondo", "Terzo");
//        Component comp = new CServizio("Ambulanza notte");
        setContent(comp);
    }

    private RTabellone[] creaRighe() {
        List<RTabellone> lRighe = new ArrayList();

        CServizio cServ;
        CRuoli cRuoli;
        CTurno[] cTurni;

        cServ = new CServizio("Ambulanza mattino");
        cRuoli = new CRuoli("Aut", "Sec", "Ter", "Bar");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        cServ = new CServizio("Ambulanza pomeriggio");
        cRuoli = new CRuoli("Aut", "Sec", "Aiu","Ap");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));

        cServ = new CServizio("Ambulanza notte");
        cRuoli = new CRuoli("Aut", "Sec", "Ter", "Bar");
        cTurni = creaTurniDemo();
        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));


        return lRighe.toArray(new RTabellone[0]);
    }

    private CTurno[] creaTurniDemo() {
        List<CTurno> lTurni = new ArrayList();

        lTurni.add(new CTurno(creaIscrizioni(1)));
        lTurni.add(new CTurno(creaIscrizioni(2)));
        lTurni.add(new CTurno(creaIscrizioni(1)));
        lTurni.add(new CTurno(creaIscrizioni(3)));
        lTurni.add(new CTurno(creaIscrizioni(2)));
        lTurni.add(new CTurno(creaIscrizioni(3)));
        lTurni.add(new CTurno(creaIscrizioni(1)));

        return lTurni.toArray(new CTurno[0]);
    }

    private CIscrizione[] creaIscrizioni(int quale){

        List<CIscrizione> lIscrizioni = new ArrayList();

        switch (quale){
            case 1:
                lIscrizioni.add(new CIscrizione("Bianchini F."));
                lIscrizioni.add(new CIscrizione("Madella C."));
                lIscrizioni.add(new CIscrizione("---"));
                lIscrizioni.add(new CIscrizione("Rossi G."));
                break;
            case 2:
                lIscrizioni.add(new CIscrizione("Tardelli G."));
                lIscrizioni.add(new CIscrizione("Minetti F."));
                lIscrizioni.add(new CIscrizione("Della Piana S."));
                lIscrizioni.add(new CIscrizione("---"));
                break;

            case 3:
                lIscrizioni.add(new CIscrizione("Artusi S."));
                lIscrizioni.add(new CIscrizione("Parini C."));
                lIscrizioni.add(new CIscrizione("Franchi M."));
                lIscrizioni.add(new CIscrizione("Riccardi N."));
                break;


        }


        return lIscrizioni.toArray(new CIscrizione[0]);

    }

}
