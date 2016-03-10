package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapServizio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Motore che genera un tabellone dai wrapper di dati
 * Created by alex on 01/03/16.
 */
public class EngineTab {

    /**
     * Genera un tabellone da un array di wrapper di riga
     */
    public static GridTabellone creaTabellone(WTabellone wrapper) {
        LocalDate d1 = wrapper.getD1();
        LocalDate d2 = wrapper.getD2();
        GridTabellone tabellone = new GridTabellone(d1, d2);
        for (WRigaTab riga : wrapper) {
            addRiga(tabellone, riga);
        }
        return tabellone;
    }

    /**
     * Aggiunge una riga in fondo a un tabellone esistente
     *
     * @param tab  il tabellone
     * @param riga la riga da aggiungere
     */
    public static void addRiga(GridTabellone tab, WRigaTab riga) {

        // aggiunge una riga al tabellone
        int row = tab.getRows();
        tab.setRows(row + 1);

        // crea e aggiunge componente grafico del servizio
        Servizio serv = riga.getServizio();
        CServizio s = new CServizio(serv.getOrario(), serv.getDescrizione());
        tab.addComponent(s, 0, row);

        // crea e aggiunge il componente grafico dele funzioni
        Component cfunzioni = creaCompFunzioni(serv);
        tab.addComponent(cfunzioni, 1, row);

        // crea e aggiunge i componenti grafici per i turni
        int totGiorni = tab.getNumGiorni();
        for (int i = 0; i < totGiorni; i++) {

            // recupero il turno del giorno
            LocalDate currDate = tab.getDataStart().plusDays(i);
            Turno t = riga.getTurno(currDate);

            // creo il componente grafico
            TabelloneCell tcomp;
            if (t != null) {
                tcomp = creaCompTurno(tab, t);
            } else {
                InfoNewTurnoWrap wrapper=new InfoNewTurnoWrap(serv, currDate);
                tcomp = new CNoTurno(tab, wrapper);
            }

            // aggiungo il componente in posizione sul tabellone
            int col = 2 + i;
            tcomp.setX(col);
            tcomp.setY(row);
            tab.addComponent(tcomp, col, row);

        }

    }


    /**
     * Crea un componente grafico con le iscrizioni per un turno
     *
     * @param turno il turno
     * @return il componente grafico
     */
    private static TabelloneCell creaCompTurno(GridTabellone tab, Turno turno) {
        Servizio serv = turno.getServizio();
        int numFunzioni = serv.getNumFunzioni();
        CTurnoDisplay comp = new CTurnoDisplay(tab, numFunzioni, turno);

        int row=0;
        comp.addComponent(new Label("titolo"), 0, row);

        Iscrizione[] iscrizioni = turno.getIscrizioni();
        for (Iscrizione iscr : iscrizioni) {
            String nome = iscr.getMilite().toString();
            CIscrizione ci = new CIscrizione(nome);
            Funzione f = iscr.getFunzione();
            int pos = serv.getPosFunzione(f);
            if (pos >= 0) {
                try {
                    comp.addComponent(ci, 0, pos);
                } catch (Exception e) {
                    e.printStackTrace();
                    //@todo non aggiunge la seconda riga (gac)
                    //todo perché ci sono 2 autisti sullo stesso servizio - alex
                }
            }
        }
        return comp;
    }


    /**
     * Crea un componente grafico con le funzioni in base al Servizio
     *
     * @param serv il servizio
     * @return il componente grafico con le funzioni
     */
    private static Component creaCompFunzioni(Servizio serv) {
        CFunzioni cfunzioni = new CFunzioni();

        // se orario variabile, prima riga vuota per allinearsi con i turni che in questo caso avranno il titolo
        if(serv.isOrarioVariabile()){
            cfunzioni.addFunzione("rigavuota");
        }

        List<WrapServizio.Wrap> wrappers = serv.getWrapServizio().getWrap();
        for (WrapServizio.Wrap w : wrappers) {
            Funzione f = w.funzione;
            boolean obblig = w.obbligatoria;
            Component comp = cfunzioni.addFunzione(f.getSigla());
            if (obblig) {
                comp.addStyleName("cfunzioneobblig");
            }
        }
        return cfunzioni;
    }


    /**
     * Crea i wrapper per le righe di tabellone
     * Un wrapper per ogni servizio
     */
    public static WTabellone creaRighe(LocalDate d1, int quantiGiorni) {

        LocalDate d2=d1.plusDays(quantiGiorni-1);
        WTabellone wtab =new WTabellone(d1, d2);

        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
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


}
