package it.algos.wam.tabellone;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.lib.LibWam;
import it.algos.wam.query.WamQuery;
import it.algos.wam.wrap.Iscrizione;
import it.algos.wam.wrap.WrapServizio;
import it.algos.webbase.multiazienda.CompanyQuery;

import javax.persistence.EntityManager;
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


        // controlla se il turno è valido
        boolean valido = turno.isValido();

        // se il turno è valido è comunque verde
        if(valido){
            comp.addStyleName("turno-green");
        }else{  // turno non valido
            // se il turno è vicino e giallo, se è vicinissimo è rosso, se è lontano resta com'è
            long ggMancanti = ChronoUnit.DAYS.between(turno.getData1(), LocalDate.now());
            if(ggMancanti<=3){
                comp.addStyleName("turno-yellow");
            }
            if(ggMancanti<=1){
                comp.addStyleName("turno-red");
            }
        }


        int row=0;

        Iscrizione[] iscrizioni = turno.getIscrizioni();
        for (Iscrizione iscr : iscrizioni) {
            String nome = iscr.getVolontario().toString();
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
