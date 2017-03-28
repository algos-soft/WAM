package it.algos.wam.entity.turno;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
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
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzioneTable;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

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

    // id della colonna generata "giorno"
    private static final String COL_GIORNO = "giorno";

    // id della colonna generata "colore"
    private static final String COL_COLORE_SERVIZIO = "gruppo";

    // id della colonna generata "ordine"
    private static final String COL_ORDINE_SERVIZIO = "ordine";

    // id della colonna generata "sigla"
    private static final String COL_SIGLA_SERVIZIO = "servizio";

    // id della colonna generata "orario"
    private static final String COL_ORARIO = "orario";

    // id della colonna generata "iscrizioni"
    private static final String COL_ISCRIZIONI = "iscrizioni";

    // id della colonna generata "ordine"
    private static final String COL_STATO = "stato";

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
        addGeneratedColumn(COL_GIORNO, new TurnoTable.GiornoColumnGenerator());
        addGeneratedColumn(COL_COLORE_SERVIZIO, new ColoreColumnGenerator());
        addGeneratedColumn(COL_ORDINE_SERVIZIO, new TurnoTable.OrdineColumnGenerator());
        addGeneratedColumn(COL_SIGLA_SERVIZIO, new TurnoTable.SiglaColumnGenerator());
        addGeneratedColumn(COL_ORARIO, new TurnoTable.OrarioColumnGenerator());
        addGeneratedColumn(COL_ISCRIZIONI, new TurnoTable.IscrizioniColumnGenerator());
        addGeneratedColumn(COL_STATO, new TurnoTable.StatoColumnGenerator());
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
                COL_GIORNO,
                COL_COLORE_SERVIZIO,
                COL_ORDINE_SERVIZIO,
                COL_SIGLA_SERVIZIO,
                COL_ISCRIZIONI,
                COL_STATO,
                //              Turno_.inizio,
                //               Turno_.fine,
                //               Turno_.titoloExtra,
                //               Turno_.localitaExtra,
        };// end of array
    }// end of method

    @Override
    protected void fixColumn() {
        setColumnHeader(COL_ORDINE_SERVIZIO, "##");

        setColumnAlignment(COL_GIORNO, Align.CENTER);
        setColumnAlignment(COL_COLORE_SERVIZIO, Align.CENTER);
        setColumnAlignment(COL_ORDINE_SERVIZIO, Align.CENTER);
        setColumnAlignment(COL_STATO, Align.CENTER);

        setColumnExpandRatio(COL_ISCRIZIONI, 2);

        setColumnWidth(COL_GIORNO, 140);
        setColumnWidth(COL_COLORE_SERVIZIO, 80);
        setColumnWidth(COL_ORDINE_SERVIZIO, 45);
        setColumnWidth(COL_SIGLA_SERVIZIO, 120);
        setColumnWidth(COL_STATO, 45);
    }// end of method


    /**
     * Colonna generata: giorno.
     */
    private class GiornoColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Label labelGiorno = null;
            Date inizio = null;
            String giornoTxt = "";
            final Item item = source.getItem(itemId);

            Property prop = item.getItemProperty(Turno_.inizio.getName());
            if (prop != null) {
                inizio = (Date) prop.getValue();
            }// end of if cycle

            if (inizio != null) {
                giornoTxt = LibDate.toStringDMYY(inizio);
                labelGiorno = new Label(giornoTxt);
            }// end of if cycle

            DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, d MMM");
            giornoTxt = formatter.print(new DateTime(inizio));
            labelGiorno = new Label(giornoTxt);

            return labelGiorno;
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: colore.
     */
    private class ColoreColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Property prop;
            Item item = source.getItem(itemId);
            prop = item.getItemProperty(Turno_.servizio.getName());
            Servizio serv = (Servizio) prop.getValue();
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
     */
    private class OrdineColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Label ordine = null;
            Servizio serv = null;
            final Item item = source.getItem(itemId);

            Property prop = item.getItemProperty(Turno_.servizio.getName());
            if (prop != null) {
                serv = (Servizio) prop.getValue();
            }// end of if cycle

            if (serv != null) {
                ordine = new Label("" + serv.getOrdine());
            }// end of if cycle

            return ordine;
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: sigla.
     */
    private class SiglaColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table table, Object itemId, Object columnId) {
            Item item = table.getItem(itemId);
            BeanItem bi = LibBean.fromItem(item);
            Turno turno = (Turno) bi.getBean();

            Servizio serv = turno.getServizio();
            String str = serv.getSigla();

            return new Label(str, ContentMode.HTML);
        }// end of inner method
    }// end of inner class


    /**
     * Colonna generata: orario.
     */
    private class OrarioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            String s;
            Item item = source.getItem(itemId);
            BeanItem bi = LibBean.fromItem(item);
            Turno turno = (Turno) bi.getBean();
            Servizio serv = turno.getServizio();

            if (serv.isOrario()) {
                s = serv.getStrOrario();
            } else {
                s = "variabile";
            }

            return new Label(s);
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: iscrizioni.
     */
    private class IscrizioniColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella delle funzioni.
         * Usando una Label come componente, la selezione della
         * riga funziona anche cliccando sulla colonna custom.
         */
        public Component generateCell(Table table, Object itemId, Object columnId) {
            String str = "";
            String tag = ";&nbsp;&nbsp;";
            Item item = table.getItem(itemId);
            BeanItem bean = LibBean.fromItem(item);
            Turno turno = (Turno) bean.getBean();
            Servizio servizio = turno.getServizio();
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
//                serv = servFunz.getServizio();
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

    /**
     * Colonna generata: stato.
     */
    private class StatoColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella del colore.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Property prop;
            Item item = source.getItem(itemId);
            prop = item.getItemProperty(Turno_.servizio.getName());
            Servizio serv = (Servizio) prop.getValue();
            int codColore = serv.getColore();
            ColorPicker picker = new ServizioColorPicker();
            picker.setWidth("45");
            picker.setColor(new Color(200, 100, 100));
            picker.setCaption("&#8203;"); //zero-width space
            picker.setReadOnly(true);

            return picker;
        }// end of method
    }// end of class

}// end of class
