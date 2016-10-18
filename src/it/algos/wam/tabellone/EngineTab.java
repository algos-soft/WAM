package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.query.WamQuery;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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
        insertRiga(tab, riga, tab.getRows());
    }


    /**
     * Inserisce una riga in un tabellone esistente
     * Tutte le celle successive vengono spostate in basso di 1
     *
     * @param tab  il tabellone
     * @param riga la riga da aggiungere
     * @param pos  l'indice della riga prima della quale aggiungere la nuova riga
     */
    public static void insertRiga(GridTabellone tab, WRigaTab riga, int pos) {

        // aggiunge una riga al tabellone
        tab.insertRow(pos);
        int row = pos;

        // crea e aggiunge componente grafico del servizio
        Servizio serv = riga.getServizio();
        CServizioDisplay s = new CServizioDisplay(tab, serv);

        // per righe a orario variabile: solo l'ultima riga ha la possibilità di crearne altre
        if (!serv.isOrario()) {
            s.setCreaNuova(riga.isUltimaDelGruppo());
        }

        tab.addComponent(s, 0, row);

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
            int col = 1 + i;
            tab.addComponent(tcomp, col, row);

        }

        // spazzola tutte le celle successive e aumenta di 1
        // il numero di riga memorizzato nella cella
        int numRows = tab.getRows();
        int numCols = tab.getColumns();
        for (int r = pos + 1; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                Component comp = tab.getComponent(c, r);
                if (comp instanceof TabelloneCell) {
                    TabelloneCell cell = (TabelloneCell) comp;
                    cell.setY(cell.getY() + 1);
                }
            }
        }


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

        List<Servizio> listaServizi;

        // todo la lista dei servizi deve essere composta:
        // todo sempre, da tutti i servizi abilitati
        // todo se il tabellone contiene giorni passati, aggiunge anche i servizi che hanno dei turni associati

        // aggiunge una riga per ogni servizio con orario prestabilito
        listaServizi = Servizio.getListVisibiliConOrario(entityManager);
        if (listaServizi != null && listaServizi.size() > 0) {
            for (Servizio servizio : listaServizi) {
                    List<Turno> turni = WamQuery.queryTurni(entityManager, servizio, d1, d2);
                    wtab.add(new WRigaTab(servizio, turni.toArray(new Turno[0])));
                }// end of if cycle
        }

        // aggiunge una o più righe per ogni servizio con orario variabile
        listaServizi = Servizio.getListVisibiliSenzaOrario(entityManager);
        if (listaServizi != null && listaServizi.size() > 0) {
            for (Servizio servizio : listaServizi) {
                    WRigaTab[] righe = creaRighe(entityManager, servizio, d1, d2);
                    for (WRigaTab riga : righe) {
                        wtab.add(riga);
                    }
                }// end of if cycle
            }

        return wtab;

    }

    /**
     * Dati un servizio e un periodo, ritorna un wrapper per ogni riga di tabellone da creare.
     * Potrebbero essere più di uno se ci sono più turni nello stesso giorno.
     * In questo caso crea righe aggiuntive per mostrare tutti i turni.
     */
    private static WRigaTab[] creaRighe(EntityManager em, Servizio serv, LocalDate d1, LocalDate d2) {
        ArrayList<WRigaTab> righe = new ArrayList<>();

        // recupera tutti i turni del servizio, in ordine di data inizio
        List<Turno> turni = WamQuery.queryTurni(em, serv, d1, d2);

        ArrayList<ArrayList<Turno>> gruppiTurni = new ArrayList();
        LocalDate currDate = LocalDate.of(1900, 1, 1);
        int numGruppo = 0;
        for (Turno turno : turni) {
            LocalDate dataTurno = turno.getData1();

            // trova il numero della lista da usare
            ArrayList<Turno> lista;
            if (!dataTurno.equals(currDate)) {
                numGruppo = 0;
            } else {
                numGruppo++;
            }

            // crea la lista se manca
            if (gruppiTurni.size() < (numGruppo + 1)) {
                gruppiTurni.add(new ArrayList<Turno>()); // la prima lista di turni
            }

            // aggiunge il turno alla lista
            lista = gruppiTurni.get(numGruppo);
            lista.add(turno);

            // aggiorna la data corrente
            currDate = dataTurno;

        }

        // se non ci sono turni, crea sempre almeno una riga per il servizio
        if (gruppiTurni.size() == 0) {
            gruppiTurni.add(new ArrayList<Turno>());
        }

        // crea un wrapper di riga per ogni gruppo di turni creato
        // solo l'ultimo wrapper avrà il flag UltimaDelGruppo=true
        for (int i = 0; i < gruppiTurni.size(); i++) {
            ArrayList<Turno> gruppo = gruppiTurni.get(i);
            WRigaTab wrapper = new WRigaTab(serv, gruppo.toArray(new Turno[0]));
            if (i < gruppiTurni.size() - 1) {
                wrapper.setUltimaDelGruppo(false);
            }
            righe.add(wrapper);
        }

        return righe.toArray(new WRigaTab[0]);

    }


}
