package it.algos.wam.entity.iscrizione;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.ServizioColorPicker;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.TurnoTable;
import it.algos.wam.entity.turno.Turno_;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibTable;
import it.algos.webbase.web.module.ModulePop;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by gac on 08/04/17.
 * Sovrascrive la classe standard
 */
public class IscrizioneTable extends WamTable {

    // id della colonna generata "giorno"
    private static final String COL_GIORNO = Turno_.inizio.getName();

    // id della colonna generata "colore"
    private static final String COL_COLORE_SERVIZIO = "Gruppo";

    // id della colonna generata "inizio"
    private static final String COL_INIZIO = "Inizio";

    // id della colonna generata "servizio"
    private static final String COL_SIGLA_SERVIZIO = "Servizio";

    // id della colonna generata "funzione"
    private static final String COL_SIGLA_FUNZIONE = "Funzione";

    //--titolo della table
    private static String CAPTION = "Iscrizioni di ogni volontario per ogni servizio effettuato in ogni turno";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public IscrizioneTable(ModulePop module) {
        super(module, CAPTION);
    }// end of constructor


    /**
     * Create additional columns
     * (add generated columns, nested properties...)
     * <p>
     * Override in the subclass
     */
    @Override
    protected void createAdditionalColumns() {
        addGeneratedColumn(COL_GIORNO, new IscrizioneTable.GiornoColumnGenerator());
        addGeneratedColumn(COL_COLORE_SERVIZIO, new IscrizioneTable.ColoreColumnGenerator());
        addGeneratedColumn(COL_INIZIO, new IscrizioneTable.InizioColumnGenerator());
        addGeneratedColumn(COL_SIGLA_SERVIZIO, new IscrizioneTable.ServizioColumnGenerator());
        addGeneratedColumn(COL_SIGLA_FUNZIONE, new IscrizioneTable.FunzioneColumnGenerator());
    }// end of method

    /**
     * Returns an array of the visible columns ids. Ids might be of type String
     * or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        if (LibSession.isDeveloper()) {
            return new Object[]{
                    WamCompanyEntity_.company,
                    COL_GIORNO,
                    COL_COLORE_SERVIZIO,
                    COL_SIGLA_SERVIZIO,
                    COL_INIZIO,
                    COL_SIGLA_FUNZIONE,
                    Iscrizione_.volontario.getName(),
                    Iscrizione_.tsCreazione.getName(),
                    Iscrizione_.minutiEffettivi.getName(),
                    Iscrizione_.esisteProblema.getName(),
                    Iscrizione_.notificaInviata.getName()
            };// end of array
        } else {
            return new Object[]{
                    COL_GIORNO,
                    COL_COLORE_SERVIZIO,
                    COL_SIGLA_SERVIZIO,
                    COL_INIZIO,
                    COL_SIGLA_FUNZIONE,
                    Iscrizione_.volontario.getName(),
                    Iscrizione_.tsCreazione.getName(),
                    Iscrizione_.minutiEffettivi.getName(),
                    Iscrizione_.esisteProblema.getName(),
                    Iscrizione_.notificaInviata.getName()
            };// end of array
        }// end of if/else cycle

    }// end of method



    @Override
    protected void fixColumn() {
        setColumnHeader(COL_GIORNO, FontAwesome.CALENDAR.getHtml());
        setColumnHeader(COL_INIZIO, FontAwesome.CLOCK_O.getHtml());
        setColumnHeader(Iscrizione_.volontario.getName(), FontAwesome.USER.getHtml());
        setColumnHeader(Iscrizione_.tsCreazione.getName(), FontAwesome.CALENDAR_O.getHtml());
        setColumnHeader(Iscrizione_.minutiEffettivi.getName(), "Durata");
        setColumnHeader(Iscrizione_.esisteProblema.getName(), FontAwesome.WARNING.getHtml());
        setColumnHeader(Iscrizione_.notificaInviata.getName(), FontAwesome.MAIL_FORWARD.getHtml());

        setColumnAlignment(COL_GIORNO, Align.CENTER);
        setColumnAlignment(COL_COLORE_SERVIZIO, Align.CENTER);

        setColumnWidth(COL_GIORNO, 140);
        setColumnWidth(COL_COLORE_SERVIZIO, 80);
        setColumnWidth(COL_INIZIO, 80);
        setColumnWidth(COL_SIGLA_SERVIZIO, 120);
        setColumnWidth(COL_SIGLA_FUNZIONE, 120);
        setColumnWidth(Iscrizione_.tsCreazione, 200);
        setColumnWidth(Iscrizione_.minutiEffettivi, 100);
        setColumnWidth(Iscrizione_.esisteProblema, 80);
        setColumnWidth(Iscrizione_.notificaInviata, 80);
    }// end of method

    /**
     * Colonna generata: giorno.
     */
    private class GiornoColumnGenerator implements ColumnGenerator {

        public Component generateCell(Table source, Object itemId, Object columnId) {
            Turno turno = getTurno(source, itemId);
            Date inizio = turno.getInizio();
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
     */
    private class ColoreColumnGenerator implements ColumnGenerator {

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
     * Colonna generata: inizio.
     */
    private class InizioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            String labelText;

            if (serv.isOrario()) {
                labelText = serv.getOraInizio()+":00";
            } else {
                labelText = "var";
            }// end of if/else cycle

            return new Label(labelText);
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: servizio.
     */
    private class ServizioColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Servizio serv = getServizio(source, itemId);
            return new Label(serv.getSigla());
        }// end of inner method
    }// end of inner class

    /**
     * Colonna generata: funzione.
     */
    private class FunzioneColumnGenerator implements ColumnGenerator {

        /**
         * Genera la cella della durata.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Funzione funz = getFunzione(source, itemId);
            return new Label(funz.getSigla());
        }// end of inner method
    }// end of inner class

    private Turno getTurno(Table source, Object itemId) {
        return (Turno) LibTable.getPropValue(source, itemId, Iscrizione_.turno.getName());
    }// end of method

    private Servizio getServizio(Table source, Object itemId) {
        return (Servizio) LibTable.getPropValue(source, itemId, ServizioFunzione_.servizio.getName());
    }// end of method

    private Funzione getFunzione(Table source, Object itemId) {
        return (Funzione) LibTable.getPropValue(source, itemId, ServizioFunzione_.funzione.getName());
    }// end of method

}// end of class
