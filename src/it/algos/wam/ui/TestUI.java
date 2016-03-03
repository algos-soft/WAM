package it.algos.wam.ui;

import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.lib.LibWam;
import it.algos.wam.tabellone.*;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.LibDate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 20/02/16.
 */
@Title("WAM:test")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        String themeName;
        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
            themeName = "wam-mob";
        } else {
            themeName = "wam";
        }
        setTheme(themeName);

        WTabellone wrapper = creaRighe();
        CTabellone tab = EngineTab.creaTabellone(wrapper);


        //Component comp = new CTabellone(righe);


//        Component comp = new CRuoli("Autista", "Primo", "Secondo", "Terzo");
//        Component comp = new CServizio("Ambulanza notte");
        setContent(tab);
    }

//    /**
//     * @deprecated
//     */
//    private RTabellone[] creaRigheOldOld() {
//        List<RTabellone> lRighe = new ArrayList();
//        CServizio cServ;
//        CRuoli cRuoli;
//        CTurno[] cTurni;
//
//        cServ = new CServizio("Ambulanza mattino");
//        cRuoli = new CRuoli("Aut", "Sec", "Ter", "Bar");
//        cTurni = creaTurniDemo();
//        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));
//
//        cServ = new CServizio("Ambulanza pomeriggio");
//        cRuoli = new CRuoli("Aut", "Sec", "Aiu", "Ap");
//        cTurni = creaTurniDemo();
//        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));
//
//        cServ = new CServizio("Ambulanza notte");
//        cRuoli = new CRuoli("Aut", "Sec", "Ter", "Bar");
//        cTurni = creaTurniDemo();
//        lRighe.add(new RTabellone(cServ, cRuoli, cTurni));
//
//
//        return lRighe.toArray(new RTabellone[0]);
//
//    }

//    /**
//     * Una volta stabilita la company (di norma non qui), crea tante righe quanti sono i servizi
//     */
//    private RTabellone[] creaRigheOld() {
//        List<RTabellone> lRighe = new ArrayList();
//        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
//        ArrayList<Servizio> listaServizi = null;
//        WrapServizio wrapServizio = null;
//        Date dataIniziale = LibDate.creaData(2, 3, 2016);
//        int primoGiorno = LibWam.creaChiave(dataIniziale);
//
//        CServizio cServ;
//        CRuoli cRuoli;
//        CTurno[] cTurni=null;
//
//        if (company != null) {
//            listaServizi = Servizio.findAll(company);
//        }// end of if cycle
//
//        if (listaServizi != null && listaServizi.size() > 0) {
//            for (Servizio servizio : listaServizi) {
//                wrapServizio = servizio.getWrapServizio();
//                cServ = new CServizio(servizio);
//                cRuoli = new CRuoli(wrapServizio);
//              cTurni = creaTurni(company, servizio, primoGiorno);
////                cTurni = creaTurniDemo();
//                lRighe.add(new RTabellone(cServ, cRuoli, cTurni));
//            }// end of for cycle
//        }// end of if cycle
//
//        return lRighe.toArray(new RTabellone[0]);
//
//    }// end of method


    /**
     * Crea i wrapper per le righe di tabellone
     * Un wrapper per ogni servizio
     */
    private WTabellone creaRighe() {

        LocalDate d1=LocalDate.of(2016, 2, 28);
        LocalDate d2=d1.plusDays(6);
        WTabellone wtab =new WTabellone(d1, d2);

        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        ArrayList<Servizio> listaServizi = null;

        int primoGiorno = LibWam.creaChiave(d1);

        if (company != null) {
            listaServizi = Servizio.findAll(company);
        }

        if (listaServizi != null && listaServizi.size() > 0) {

            long giorni = d1.until( d2, ChronoUnit.DAYS)+1;

            for (Servizio servizio : listaServizi) {


                List<Turno> turni = new ArrayList<>();

                // todo qui fare una sola query dal... al... non un ciclo!
                for (int chiave = primoGiorno; chiave < primoGiorno+giorni; chiave++) {
                    Turno turno = Turno.find(company, servizio, chiave);
                    if (turno!=null){
                        turni.add(turno);
                    }
                }
                wtab.add(new WRigaTab(servizio, turni.toArray(new Turno[0])));
            }
        }

        return wtab;

    }


    private CTurno[] creaTurni(Company company, Servizio servizio, int primoGiorno) {
        return creaTurni(company, servizio, primoGiorno, 7);
    }// end of method

    private CTurno[] creaTurni(Company company, Servizio servizio, int primoGiorno, int giorni) {
        List<CTurno> listaTurni = new ArrayList();
        Turno turno = null;
        CTurno cTurno = null;

        for (int chiave = primoGiorno; chiave < primoGiorno+giorni; chiave++) {
            turno = Turno.find(company, servizio,chiave);

            if (turno != null) {
                cTurno= new CTurno(turno);
            } else {
                cTurno= new CTurno();
            }// end of if/else cycle
            listaTurni.add(cTurno);
        }// end of for cycle

        return listaTurni.toArray(new CTurno[0]);
    }// end of method

    /**
     * @deprecated
     */
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

    private CIscrizione[] creaIscrizioni(int quale) {

        List<CIscrizione> lIscrizioni = new ArrayList();

        switch (quale) {
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
