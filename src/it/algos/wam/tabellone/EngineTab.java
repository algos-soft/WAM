package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapServizio;

import java.time.LocalDate;
import java.util.List;

/**
 * Motore che genera un tabellone dai wrapper di dati
 * Created by alex on 01/03/16.
 */
public class EngineTab {

    /**
     * Genera un tabellone da un array di wrapper di riga
     */
    public static CTabellone creaTabellone(WTabellone wrapper) {
        LocalDate d1 = wrapper.getD1();
        LocalDate d2 = wrapper.getD2();
        CTabellone tabellone = new CTabellone(d1, d2);
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
    public static void addRiga(CTabellone tab, WRigaTab riga) {

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
    private static TabelloneCell creaCompTurno(CTabellone tab, Turno turno) {
        Servizio serv = turno.getServizio();
        int numFunzioni = serv.getNumFunzioni();
        CTurnoDisplay comp = new CTurnoDisplay(tab, numFunzioni, turno);
        Iscrizione[] iscrizioni = turno.getIscrizioni();
        for (Iscrizione i : iscrizioni) {
            String nome = i.getMilite().toString();
            CIscrizione ci = new CIscrizione(nome);
            Funzione f = i.getFunzione();
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


}