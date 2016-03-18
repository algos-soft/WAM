package it.algos.wam.tabellone;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyQuery;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Motore che genera un tabellone dai wrapper di dati
 * Created by alex on 01/03/16.
 */
public class EngineTab {

    private static int GIORNI_WARNING = 4; // turno vicino (giallo)
    private static int GIORNI_ALERT = 1;  // turno molto vicino (rosso)


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
                tcomp = new CTurnoDisplay(tab, t);
            } else {
                tcomp = new CTurnoDisplay(tab, serv, currDate);
            }

            // aggiungo il componente in posizione sul tabellone
            int col = 2 + i;
            tab.addComponent(tcomp, col, row);

        }

    }


    /**
     * Crea un componente grafico con le funzioni in base al Servizio
     *
     * @param serv il servizio
     * @return il componente grafico con le funzioni
     */
    private static Component creaCompFunzioni(Servizio serv) {
        CFunzioni cfunzioni = new CFunzioni();
        Funzione funz;
        boolean obbligatoria;


        // se orario variabile, prima riga vuota per allinearsi con i turni che in questo caso avranno un titolo
        if (serv.isOrarioVariabile()) {
            Label label = new Label("&nbsp;", ContentMode.HTML);
            cfunzioni.addComponent(label);
        }

        List<ServizioFunzione> lista = serv.getServizioFunzioni();
        Collections.sort(lista);
        for (ServizioFunzione serFun : lista) {
            funz = serFun.getFunzione();
            obbligatoria = serFun.isObbligatoria();

            Component comp = cfunzioni.addFunzione(funz.getSigla());
            if (obbligatoria) {
                comp.addStyleName("cfunzioneobblig");
            }
        }


        return cfunzioni;
    }


    /**
     * Crea i wrapper per le righe di tabellone
     * Un wrapper per ogni servizio
     *
     * @param d1            la data iniziale
     * @param quantiGiorni  il numero di giorni da visualizzare
     * @param entityManager l'entityManager da utilizzare per le operazioni di persistenza
     */
    public static WTabellone creaRighe(LocalDate d1, int quantiGiorni, EntityManager entityManager) {

        LocalDate d2 = d1.plusDays(quantiGiorni - 1);
        WTabellone wtab = new WTabellone(d1, d2);

//        List<Servizio> listaServizi = (List<Servizio>) CompanyQuery.getList(Servizio.class);

        List<Servizio> listaServizi = WamQuery.queryServiziOrari(entityManager);

        if (listaServizi != null && listaServizi.size() > 0) {
            for (Servizio servizio : listaServizi) {
                List<Turno> turni = WamQuery.queryTurni(entityManager, servizio, d1, d2);
                wtab.add(new WRigaTab(servizio, turni.toArray(new Turno[0])));
            }
        }

        return wtab;

    }


}
