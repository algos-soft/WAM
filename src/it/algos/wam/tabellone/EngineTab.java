package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.query.WamQuery;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapServizio;
import it.algos.webbase.multiazienda.CompanyQuery;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Motore che genera un tabellone dai wrapper di dati
 * Created by alex on 01/03/16.
 */
public class EngineTab {

    private static int GIORNI_WARNING=4; // turno vicino (giallo)
    private static int GIORNI_ALERT=1;  // turno molto vicino (rosso)

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
                tcomp = creaCompNoTurno(tab, serv, currDate);
            }

            // aggiungo il componente in posizione sul tabellone
            int col = 2 + i;
            tab.addComponent(tcomp, col, row);

        }

    }


    /**
     * Crea un componente grafico con le iscrizioni per un turno
     *
     * @param turno il turno
     * @return il componente grafico
     */
    public static TabelloneCell creaCompTurno(GridTabellone tab, Turno turno) {
        Servizio serv = turno.getServizio();
        int numFunzioni = serv.getNumFunzioni();
        CTurnoDisplay comp = new CTurnoDisplay(tab, numFunzioni, turno);


        // Colora lo sfondo del turno
        // se è nel passato, non colora
        // se è oggi, colora di azzurro
        // se è un po' nel futuro, colora in base all'urgenza di completamento
        // se è molto nel futuro, non colora
        String bgStyle=null;
        String fgStyle=null;
        LocalDate dataTurno=turno.getData1();
        if(dataTurno.equals(LocalDate.now())){  // è oggi
            bgStyle="cturno-today";
            fgStyle="ciscrizione-light";
        }else{
            if(LocalDate.now().isBefore(dataTurno)){   // è nel futuro
                long ggMancanti = ChronoUnit.DAYS.between(LocalDate.now(), turno.getData1());
                if(ggMancanti<=GIORNI_WARNING){
                    String[] styles = coloraTurnoUrgenza(comp, turno);
                    bgStyle=styles[0];
                    fgStyle=styles[1];
                }
            }
        }
        // background dell'intero turno
        if(bgStyle!=null){
            comp.addStyleName(bgStyle);
        }



        // aggiunge le iscrizioni
        int row=0;
        for(Funzione f : serv.getFunzioni()){
            Iscrizione iscr = turno.getIscrizione(f);
            Component ci;
            if(iscr!=null){
                String nome = iscr.getVolontario().toString();
                ci = new CIscrizione(nome);
            }else{
                ci = new CIscrizione("");
            }
            // foreground dell'iscrizione
            if(bgStyle!=null){
                ci.addStyleName(fgStyle);
            }

            comp.addComponent(ci, 0, row);
            row++;
        }

        return comp;
    }


    /**
     * Colorazione di un turno futuro in funzione dell'urgenza di completamento
     * - se è valido è comunque verde
     * - se non è valido
     *     - se è vicino è giallo
     *     - se è molto vicino è rosso
     */
    private static String[] coloraTurnoUrgenza(TabelloneCell cella, Turno turno){

        String bgStyle;
        String fgStyle;

        // controlla se il turno è valido
        boolean valido = turno.isValido();

        // se il turno è valido è in ogni caso verde
        if(valido){
            bgStyle="cturno-ok";
            fgStyle="ciscrizione-light";
        }else{  // turno non valido

            bgStyle="cturno-warning";
            fgStyle="ciscrizione-dark";

            // se il turno è vicino e giallo, se è vicinissimo è rosso, se è lontano resta com'è
            long ggMancanti = ChronoUnit.DAYS.between(LocalDate.now(), turno.getData1());
            if(ggMancanti<=GIORNI_ALERT){
                bgStyle="cturno-alert";
                fgStyle="ciscrizione-light";
            }
        }

        return new String[]{bgStyle, fgStyle};

    }

    /**
     * Crea un componente grafico di assenza turno
     * <p>
     * Contiene le informazioni di base per creare un nuovo turno quando cliccato
     *
     * @return il componente grafico
     */
    public static TabelloneCell creaCompNoTurno(GridTabellone tab, Servizio serv, LocalDate dataInizio){
        InfoNewTurnoWrap wrapper=new InfoNewTurnoWrap(serv, dataInizio);
        TabelloneCell tcomp = new CNoTurno(tab, wrapper);
        return tcomp;
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

        List<WrapServizio.Wrap> wrappers = serv.getWrapServizio().getWrappers();
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
    public static WTabellone creaRighe(LocalDate d1, int quantiGiorni, EntityManager entityManager) {

        LocalDate d2=d1.plusDays(quantiGiorni-1);
        WTabellone wtab =new WTabellone(d1, d2);

        List<Servizio> listaServizi = (List<Servizio>)CompanyQuery.getList(Servizio.class);

        if (listaServizi != null && listaServizi.size() > 0) {
            for (Servizio servizio : listaServizi) {
                List<Turno> turni = WamQuery.queryTurni(entityManager, servizio, d1, d2);
                wtab.add(new WRigaTab(servizio, turni.toArray(new Turno[0])));
            }
        }

        return wtab;

    }


}
