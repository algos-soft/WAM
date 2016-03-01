package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.wrap.WrapServizio;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Motore che genera un tabellone dai wrapper di dati
 * Created by alex on 01/03/16.
 */
public class EngineTab {

    /**
     * Genera un tabellone da un array di wrapper di riga
     */
    public static CTabellone creaTabellone(WRigheTab wrapper) {
        LocalDate d1 = wrapper.getMinDate();
        LocalDate d2 = wrapper.getMaxDate();
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

        // aggiunge una riga al GridLayout
        int row = tab.getRows();
        tab.setRows(row+1);

        // componente grafico servizio
        Servizio serv = riga.getServizio();
        CServizio s = new CServizio(serv.getOrario(), serv.getDescrizione());
        tab.addComponent(s, 0, row);

        // componente grafico funzioni
        Component cfunzioni = creaCompFunzioni(serv);
        tab.addComponent(cfunzioni, 1, row);

        // componenti grafici turni
        riga.getTurni();
        int i=2;
        for (Turno t : riga.getTurni()) {
            Date inizio = t.getInizio();
            CTurno comp = new CTurno();
            tab.addComponent(comp, i, row);
            i++;
        }

    }


    /**
     * Crea un componente frafico con le funzioni in base al Servizio
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
            Component comp = cfunzioni.addFunzione(f.getDescrizione());
            if (obblig) {
                comp.addStyleName("cfunzioneobblig");
            }
        }
        return cfunzioni;
    }


}
