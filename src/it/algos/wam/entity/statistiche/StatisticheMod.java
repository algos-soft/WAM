package it.algos.wam.entity.statistiche;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.renderers.DateRenderer;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.query.SortProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by gac on 19/02/17.
 * Gestione (minimale) del modulo specifico
 */
public class StatisticheMod extends WamMod {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    private static Integer[] anni = {2012, 2013, 2014, 2015, 2016, 2017};
    private static String COL_VOL = "volontario";
    private static String COL_ULT = "ultimo turno";
    private static String COL_GIO = "giorni";
    private static String COL_TUR = "turni";
    private static String COL_ORE = "ore";

    private Grid grid;
    private int giorniDaInizioAnno = 0;
    private static Date PRIMA_DATA = LibDate.creaData(1, 1, 2000);
    private List<Funzione> listaFunzioni;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Statistiche";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.TASKS;

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public StatisticheMod() {
        super(Iscrizione.class, MENU_ADDRESS, ICON);
        giorniDaInizioAnno = LibDate.diff(LibDate.getPrimoGennaio(2017), new Date());
        listaFunzioni = Funzione.getListByCurrentCompany();
        setCompositionRoot(createGrid());
        coloraRighe();
    }// end of constructor

    /**
     * Crea i sottomenu specifici del modulo
     * <p>
     * Invocato dal metodo AlgosUI.addModulo()
     * Sovrascritto dalla sottoclasse
     *
     * @param menu principale del modulo
     */
    @Override
    public void addSottoMenu(MenuBar.MenuItem menu) {
        this.creaAnni(menu);
    }// end of method

    /**
     * Creazione dei filtri singoli (uno per ogni anno)
     */
    public void creaAnni(MenuBar.MenuItem menu) {
        for (int anno : anni) {
            addCommandSingoloAnno(menu, anno);
        }// end of for cycle
    }// end of method

    /**
     * Costruisce un menu per selezionare il singolo anno da filtrare
     *
     * @param menuItem a cui agganciare il bottone/item
     * @param anno     di riferimento
     */
    private void addCommandSingoloAnno(MenuBar.MenuItem menuItem, int anno) {
        menuItem.addItem("" + anno, null, new MenuBar.Command() {
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                spuntaMenu(menuItem, company);
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    private void coloraRighe() {
        grid.setRowStyleGenerator(rowRef -> {// Java 8
            Frequenza frequenza = controllaFrequenza(rowRef);
            switch (frequenza) {
                case sufficiente:
                    return "verde";
                case scarsa:
                    return "blue";
                case insufficiente:
                    return "rosso";
                default: // caso non definito
                    return "blue";
            } // fine del blocco switch
        });
    }// end of method


    private Frequenza controllaFrequenza(Grid.RowReference rowRef) {
        Frequenza frequenza = null;
        int turni = 0;
        int turniMensili = 2;
        int giorniTurno = 30 / turniMensili;
        Item item = rowRef.getItem();
        Property prop = rowRef.getItem().getItemProperty("turni");
        if (prop != null) {
            Object obj = prop.getValue();
            if (obj != null && obj instanceof Integer) {
                turni = (Integer) obj;
            }// end of if cycle

        }// end of if cycle
        int turniMinimi = giorniDaInizioAnno / giorniTurno;

        if (turni > turniMinimi) {
            frequenza = Frequenza.sufficiente;
        } else {
            if (turni == turniMinimi) {
                frequenza = Frequenza.scarsa;
            } else {
                frequenza = Frequenza.insufficiente;
            }// end of if/else cycle
        }// end of if/else cycle

        return frequenza;
    }// end of method

    /**
     * Crea una Grid filtrata sulla company corrente
     *
     * @return the Table
     */
    public Grid createGrid() {
        grid = new Grid();
        grid.setWidth("1400px");
        grid.setHeight("800px");
        DateRenderer dataRenderer = new DateRenderer("%1$te %1$tb %1$tY", Locale.ITALIAN);

        List<Volontario> listaVolontari = (List<Volontario>) CompanyQuery.getList(Volontario.class, new SortProperty(Volontario_.cognome));

        grid.addColumn(COL_VOL, String.class);
        grid.addColumn(COL_ULT, Date.class);
        grid.addColumn(COL_GIO, Integer.class);
        grid.addColumn(COL_TUR, Integer.class);
        grid.addColumn(COL_ORE, Integer.class);

        for (Funzione funz : listaFunzioni) {
            grid.addColumn(funz.getCode(), Integer.class);
        }// end of for cycle

        grid.getColumn(COL_ULT).setRenderer(dataRenderer);

        for (Volontario vol : listaVolontari) {
            grid.addRow(elaboraRiga(vol));
        }// end of for cycle

        return grid;
    }// end of method

    private Object[] elaboraRiga(Volontario vol) {
        ArrayList riga = new ArrayList();
        int turni = 0;
        int ore = 0;
        int oreIsc = 0;
        int oreFunz = 0;
        Date oggi = new Date();
        Date dataTurno = null;
        Date last = PRIMA_DATA;
        List<Iscrizione> listaIscrizioni = (List<Iscrizione>) CompanyQuery.getList(Iscrizione.class, Iscrizione_.volontario, vol);
        int delta = 0;
        ArrayList<Integer> oreFunzione = new ArrayList<>();
        turni = listaIscrizioni.size();
        Funzione funz;
        int pos;
        int value;

        for (int k = 0; k < listaFunzioni.size(); k++) {
            oreFunzione.add(0);
        }// end of for cycle

        for (Iscrizione isc : listaIscrizioni) {
            dataTurno = isc.getTurno().getInizio();

            if (LibDate.isPrecedente(dataTurno, oggi)) {
                oreIsc = isc.getMinutiEffettivi() / 60;
                ore += oreIsc;
                isc.getVolontario().getNome();
                last = LibDate.maggiore(last, dataTurno);
                funz = isc.getFunzione();
                if (listaFunzioni.contains(funz)) {
                    pos = listaFunzioni.indexOf(funz);
                    oreFunz = oreFunzione.get(pos);
                    oreFunz += oreIsc;
                    oreFunzione.set(pos, oreFunz);
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        delta = LibDate.diff(last, oggi);
        if (last.equals(PRIMA_DATA)) {
            last = null;
            delta = 0;
            turni = 0;
            ore = 0;
        }// end of if cycle

        riga.add(vol.getCognome() + " " + vol.getNome());
        riga.add(last);
        if (delta > 0) {
            riga.add(delta);
        } else {
            riga.add((Integer) null);
        }// end of if/else cycle
        if (turni > 0) {
            riga.add(turni);
        } else {
            riga.add((Integer) null);
        }// end of if/else cycle
        if (ore > 0) {
            riga.add(ore);
        } else {
            riga.add((Integer) null);
        }// end of if/else cycle

        for (int k = 0; k < oreFunzione.size(); k++) {
            value = oreFunzione.get(k);
            if (value > 0) {
                riga.add(value);
            } else {
                riga.add((Integer) null);
            }// end of if/else cycle
        }// end of for cycle


        return riga.toArray();
    }// end of method


    public enum Frequenza {
        insufficiente, scarsa, sufficiente;
    }// end of enumeration

}// end of class
