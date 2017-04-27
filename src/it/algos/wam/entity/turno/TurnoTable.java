package it.algos.wam.entity.turno;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.ServizioColorPicker;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibTable;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

/**
 * Created by gac on 27/03/17.
 * Sovrascrive la classe standard per aggiungere una colonna generata 'al volo'
 */
public class TurnoTable extends WamTable {

    // id della colonna generata "inizio"
    private static final String COL_INIZIO = Turno_.inizio.getName();

    // id della colonna generata "colore"
    private static final String COL_GRUPPO = "gruppo";

    // id della colonna generata "ordine"
    private static final String COL_ORDINE = "ordine";

    // id della colonna generata "sigla"
    private static final String COL_SIGLA = "sigla";

    // id della colonna generata "orologio"
    private static final String COL_OROLOGIO = "orologio";

    // id della colonna generata "orario"
    private static final String COL_ORARIO = "orario";

    // id della colonna generata "segnato"
    private static final String COL_SEGNATO = "segnato";

    // id della colonna generata "valido"
    private static final String COL_VALIDO = "valido";

    // id della colonna generata "completo"
    private static final String COL_COMPLETO = "completo";

    // id della colonna generata "ordine"
    private static final String COL_STATO = "stato";

    // id della colonna generata "iscrizioni"
    private static final String COL_ISCRIZIONI = "iscrizioni";

    //--titolo della table
    private static String CAPTION = "Turni di servizio anno 2017";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public TurnoTable(ModulePop module) {
        super(module, CAPTION);
    }// end of constructor


    /**
     * Initializes the table.
     * Must be called from the costructor in each subclass
     * Chiamato dal costruttore di ModuleTable
     */
    @Override
    protected void init() {
        super.init();

        this.setColumnCollapsed(Turno_.chiave.getName(), CompanySessionLib.isCompanySet());
    }// end of method

    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_INIZIO, new TurnoTable.InizioColumnGenerator());
        addGeneratedColumn(COL_GRUPPO, new GruppoColumnGenerator());
        addGeneratedColumn(COL_ORDINE, new TurnoTable.OrdineColumnGenerator());
        addGeneratedColumn(COL_SIGLA, new TurnoTable.SiglaColumnGenerator());
        addGeneratedColumn(COL_OROLOGIO, new TurnoTable.OrologioColumnGenerator());
        addGeneratedColumn(COL_ORARIO, new TurnoTable.OrarioColumnGenerator());
        addGeneratedColumn(COL_SEGNATO, new TurnoTable.SegnatoColumnGenerator());
        addGeneratedColumn(COL_VALIDO, new TurnoTable.ValidoColumnGenerator());
        addGeneratedColumn(COL_COMPLETO, new TurnoTable.CompletoColumnGenerator());
        addGeneratedColumn(COL_STATO, new TurnoTable.StatoColumnGenerator());
        addGeneratedColumn(COL_ISCRIZIONI, new TurnoTable.IscrizioniColumnGenerator());
    }// end of method


    /**
     * Returns an array of the visible columns ids. Ids might be of type String or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        return new Object[]{
                WamCompanyEntity_.company,
                Turno_.chiave,
                COL_INIZIO,
                COL_GRUPPO,
                COL_ORDINE,
                COL_SIGLA,
                COL_ORARIO,
                COL_OROLOGIO,
                COL_SEGNATO,
                COL_VALIDO,
                COL_COMPLETO,
                COL_STATO,
                COL_ISCRIZIONI,
        };// end of array
    }// end of method

    @Override
    protected void fixColumn() {
        setColumnHeader(COL_INIZIO, "data");
        setColumnHeader(COL_ORDINE, "##");
        setColumnHeader(COL_OROLOGIO, FontAwesome.CLOCK_O.getHtml());
        setColumnHeader(COL_SEGNATO, "isc");
        setColumnHeader(COL_COMPLETO, "full");

        setColumnAlignment(COL_INIZIO, Align.CENTER);
        setColumnAlignment(COL_GRUPPO, Align.CENTER);
        setColumnAlignment(COL_ORDINE, Align.CENTER);
        setColumnAlignment(COL_OROLOGIO, Align.LEFT);
        setColumnAlignment(COL_COMPLETO, Align.CENTER);
        setColumnAlignment(COL_STATO, Align.CENTER);

        setColumnExpandRatio(COL_ISCRIZIONI, 2);

        setColumnWidth(COL_INIZIO, 140);
        setColumnWidth(COL_GRUPPO, 80);
        setColumnWidth(COL_ORDINE, 45);
        setColumnWidth(COL_SIGLA, 120);
        setColumnWidth(COL_SEGNATO, 65);
        setColumnWidth(COL_OROLOGIO, 80);
        setColumnWidth(COL_COMPLETO, 65);
        setColumnWidth(COL_STATO, 80);
    }// end of method

    private Servizio getServizio(Table source, Object itemId) {
        return (Servizio) LibTable.getPropValue(source, itemId, Turno_.servizio.getName());
    }// end of method


    /**
     * Colonna generata: inizio.
     * Presentazione sintetica della data di inizio turno
     * Giorno della settimana (tre lettere), giorno del mese e mese
     */
    private class InizioColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Date inizio = (Date) LibTable.getPropValue(source, itemId, columnId);
            DateTimeFormatter formatter;
            String giornoTxt = "";

            if (inizio != null) {
                formatter = DateTimeFormat.forPattern("EEE, d MMM");
                giornoTxt = formatter.print(new DateTime(inizio));
            }// end of if cycle

            return new Label(giornoTxt);
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: colore.
     * Raggruppamento dei servizi per gruppo, identificato da un colore
     */
    private class GruppoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            int codColore = serv.getColore();
            ColorPicker picker = new ServizioColorPicker();
            picker.setWidth("45");
            picker.setColor(new Color(codColore));
            picker.setCaption("&#8203;"); //zero-width space
            picker.setReadOnly(true);

            return picker;
        }// end of method
    }// end of class


    /**
     * Colonna generata: ordine.
     * Numero d'ordine progressivo del servizio
     * Ordine di presentazione dei servizi nel tabellone
     */
    private class OrdineColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            Label ordine = null;

            if (serv != null) {
                ordine = new Label("" + serv.getOrdine());
            }// end of if cycle

            return ordine;
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: sigla.
     * Sigla breve del servizio
     */
    private class SiglaColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            Label sigla = null;

            if (serv != null) {
                sigla = new Label(serv.getSigla(), ContentMode.HTML);
            }// end of if cycle

            return sigla;
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: orario.
     * Spunta verde se il servizio ha orario fisso
     * Spunta brossa se il servizio ha orario variabile e non prefissato
     */
    private class OrarioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            Label label;

            if (serv.isOrario()) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
                label = new Label(FontAwesome.CLOSE.getHtml(), ContentMode.HTML);
                label.addStyleName("rosso");
            }// fine del blocco if-else

            return label;
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: orologio.
     * Orario di inizio del servizio (senza frazioni di ora)
     * "var" se il servizio non ha orario prefissato
     */
    private class OrologioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            String labelText;

            if (serv.isOrario()) {
                labelText = serv.getOraInizio() + ":00";
            } else {
                labelText = "var";
            }// end of if/else cycle

            return new Label(labelText);
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: segnato.
     * Se esiste almeno una iscrizione
     */
    private class SegnatoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = (Turno) LibBean.getEntity(source, itemId);
            Label label;

            if (turno.isAssegnato()) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
                label = new Label(FontAwesome.CLOSE.getHtml(), ContentMode.HTML);
                label.addStyleName("rosso");
            }// fine del blocco if-else

            return label;
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: valido.
     * Se ci sono tutte le iscrizioni delle funzioni obbligatorie
     */
    private class ValidoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = (Turno) LibBean.getEntity(source, itemId);
            Label label;

            if (turno.isValido()) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
                label = new Label(FontAwesome.CLOSE.getHtml(), ContentMode.HTML);
                label.addStyleName("rosso");
            }// fine del blocco if-else

            return label;
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: completo.
     * Se ci sono tutte le iscrizioni delle funzioni, sia obbligatorie che facoltative
     */
    private class CompletoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = (Turno) LibBean.getEntity(source, itemId);
            Label label;

            if (turno.isCompleto()) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
                label = new Label(FontAwesome.CLOSE.getHtml(), ContentMode.HTML);
                label.addStyleName("rosso");
            }// fine del blocco if-else

            return label;
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: stato.
     */
    private class StatoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = (Turno) LibBean.getEntity(source, itemId);
            boolean segnato = turno.isAssegnato();
            boolean valido = turno.isValido();
            boolean completo = turno.isCompleto();
            Color colore = null;
            ColorPicker picker = new ServizioColorPicker();
            picker.setWidth("45");

            // verde forte
            if (completo) {
                colore = new Color(40, 220, 40);
            } else {
                // verde
                if (segnato && valido) {
                    colore = new Color(20, 240, 20);
                }// end of if cycle

                // azzurro
                if (!segnato && valido) {
                    colore = new Color(20, 240, 240);
                }// end of if cycle

                // giallo
                if (segnato && !valido) {
                    colore = new Color(240, 240, 20);
                }// end of if cycle

                // rosso
                if (!segnato && !valido) {
                    colore = new Color(240, 20, 20);
                }// end of if cycle
            }// end of if/else cycle

            picker.setColor(colore);
            picker.setCaption("&#8203;"); //zero-width space
            picker.setReadOnly(true);

            return picker;
        }// end of method
    }// end of class

    /**
     * Colonna generata: iscrizioni.
     */
    private class IscrizioniColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = (Turno) LibBean.getEntity(source, itemId);
            Servizio servizio = getServizio(source, itemId);
            String str = "";
            String tag = ";&nbsp;&nbsp;";
            Servizio serv;
            Funzione funz;
            Volontario vol = null;
            Volontario volTmp;
            String sigla;
            int codePoint;
            ServizioFunzione servFunzIsc;
            List<Iscrizione> iscrizioni = turno.getIscrizioni();
            List<ServizioFunzione> lista = servizio.getServizioFunzioniOrdine();

            for (ServizioFunzione servFunz : lista) {
                vol = null;
                funz = servFunz.getFunzione();
                codePoint = funz.getIconCodepoint();

                for (Iscrizione isc : iscrizioni) {
                    volTmp = isc.getVolontario();
                    servFunzIsc = isc.getServizioFunzione();
                    if (servFunzIsc.equals(servFunz)) {
                        vol = volTmp;
                    }// end of if cycle
                }// end of for cycle

                if (vol != null) {
                    //--due diverse rappresentazioni grafiche. meglio la seconda
                    if (false) {
                        str += vol.toString() + " come ";
                        if (codePoint > 0) {
                            FontAwesome glyph = FontAwesome.fromCodepoint(codePoint);
                            str += glyph.getHtml() + " ";
                        }// fine del blocco if
                        sigla = funz.getCode();
                        if (servFunz.isObbligatoria()) {
                            str += "<font color=\"red\">" + sigla + "</font>";
                        } else {
                            str += "<font color=\"green\">" + sigla + "</font>";
                        }
                        str += tag;
                    } else {
                        if (codePoint > 0) {
                            FontAwesome glyph = FontAwesome.fromCodepoint(codePoint);
                            str += glyph.getHtml() + " ";
                        }// fine del blocco if
                        sigla = funz.getCode();
                        if (servFunz.isObbligatoria()) {
                            str += "<font color=\"red\">" + sigla + "</font>";
                        } else {
                            str += "<font color=\"green\">" + sigla + "</font>";
                        }
                        str += " -> " + vol.toString();

                        str += tag;
                    }// end of if/else cycle
                }// end of if cycle

            }// end of for cycle
            str = LibText.levaCoda(str, tag);

            return new Label(str, ContentMode.HTML);
        }// end of inner method
    }// end of inner class

    public void sort() {
    }

}// end of class
