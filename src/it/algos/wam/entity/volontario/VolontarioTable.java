package it.algos.wam.entity.volontario;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamTable;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.volontariofunzione.VolontarioFunzione;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by gac on 25 mag 2016.
 * .
 */
public class VolontarioTable extends WamTable {

    // larghezza delle colonne funzioni
    private static int LAR_SCADENZE = 85;
    private static int LAR_FUNZIONI = 85;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yy");

    //--titolo della table
    private static String CAPTION = "Elenco di tutti i volontari, con spuntate le funzioni per cui è abilitato";

    /**
     * Costruttore
     *
     * @param module di riferimento (obbligatorio)
     */
    public VolontarioTable(ModulePop module) {
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
        List<Funzione> listaFunzioni = Funzione.getListByCurrentCompany();
        for (Funzione funz : listaFunzioni) {
            addGeneratedColumn(LibText.primaMaiuscola(funz.getCode()), new FunzioniColumnGenerator(funz));
        }// end of for cycle
    }// end of method


    /**
     * Returns an array of the visible columns ids. Ids might be of type String or Attribute.<br>
     * This implementations returns all the columns (no order).
     *
     * @return the list
     */
    @Override
    protected Object[] getDisplayColumns() {
        ArrayList lista = new ArrayList<>();
        List<Funzione> listaFunzioni = Funzione.getListByCurrentCompany();
        boolean usaGestioneCertificati = CompanyPrefs.usaGestioneCertificati.getBool();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
            lista.add(Volontario_.codeCompanyUnico);
        }// end of if cycle

        lista.add(Volontario_.attivo);
        lista.add(Volontario_.admin);
        lista.add(Volontario_.cognome);
        lista.add(Volontario_.nome);
        lista.add(Volontario_.cellulare);
        lista.add(Volontario_.email);
        lista.add(Volontario_.invioMail);

        if (usaGestioneCertificati) {
            lista.add(Volontario_.scadenzaBLSD);
            lista.add(Volontario_.scadenzaNonTrauma);
            lista.add(Volontario_.scadenzaTrauma);
        }// end of if cycle


        for (Funzione funz : listaFunzioni) {
            lista.add(LibText.primaMaiuscola(funz.getCode()));
        }// end of for cycle

        return lista.toArray();
    }// end of method



    protected void fixSort() {
        Container cont = getContainerDataSource();
        if (cont instanceof Sortable) {
            Sortable sortable = (Sortable) cont;
            sortable.sort(new Object[]{Volontario_.cognome.getName()}, new boolean[]{true});
        }// end of if cycle
    }// end of method


    protected void fixColumn() {
        List<Funzione> listaFunzioni = Funzione.getListByCurrentCompany();
        boolean usaGestioneCertificati = CompanyPrefs.usaGestioneCertificati.getBool();

        setColumnHeader(Volontario_.attivo, "OK");
        setColumnHeader(Volontario_.admin, "Admin");
        setColumnHeader(Volontario_.cognome, "Cognome");
        setColumnHeader(Volontario_.nome, "Nome");
        setColumnHeader(Volontario_.cellulare, "Cellulare");
        setColumnHeader(Volontario_.email, "Mail");
        setColumnHeader(Volontario_.invioMail, "Invio");
        if (usaGestioneCertificati) {
            setColumnHeader(Volontario_.scadenzaBLSD, "BLSD");
            setColumnHeader(Volontario_.scadenzaTrauma, "BPHT");
            setColumnHeader(Volontario_.scadenzaNonTrauma, "PNT");
        }// end of if cycle

        setColumnWidth(Volontario_.company, 70);
        setColumnWidth(Volontario_.codeCompanyUnico, 70);
        setColumnWidth(Volontario_.attivo, 50);
        setColumnWidth(Volontario_.admin, 70);
        setColumnWidth(Volontario_.cognome, 180);
        setColumnWidth(Volontario_.nome, 130);
        setColumnWidth(Volontario_.cellulare, 140);
        setColumnWidth(Volontario_.email, 270);
        setColumnWidth(Volontario_.invioMail, 60);
        if (usaGestioneCertificati) {
            setColumnWidth(Volontario_.scadenzaBLSD, LAR_SCADENZE);
            setColumnWidth(Volontario_.scadenzaTrauma, LAR_SCADENZE);
            setColumnWidth(Volontario_.scadenzaNonTrauma, LAR_SCADENZE);
        }// end of if cycle

        setColumnAlignment(Volontario_.attivo, Align.CENTER);
        setColumnAlignment(Volontario_.admin, Align.CENTER);
        setColumnAlignment(Volontario_.invioMail, Align.CENTER);
        if (usaGestioneCertificati) {
            setColumnAlignment(Volontario_.scadenzaBLSD, Align.CENTER);
            setColumnAlignment(Volontario_.scadenzaTrauma, Align.CENTER);
            setColumnAlignment(Volontario_.scadenzaNonTrauma, Align.CENTER);
        }// end of if cycle

        for (Funzione funz : listaFunzioni) {
            setColumnWidth(LibText.primaMaiuscola(funz.getCode()), LAR_FUNZIONI);
            setColumnAlignment(LibText.primaMaiuscola(funz.getCode()), Align.CENTER);
        }// end of for cycle
    }// end of method

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
        String string = null;
        Object value;

        // Format for Dates
        if (property.getType() == Date.class) {
            value = property.getValue();
            if (value != null && value instanceof Date) {
                Date date = (Date) value;
                try {
                    string = this.dateFormat.format(date);
                } catch (Exception e) {
                }
            }

        }

        // Format for Booleans
        if (property.getType() == Boolean.class) {
            string = "";
            value = property.getValue();

            if (value != null && value instanceof Boolean) {
                if ((boolean) value) {
                    string = "\u2714";
                }
            }

        }

        // none of the above
        if (string == null) {
            string = super.formatPropertyValue(rowId, colId, property);
        }

        return string;
    }

    /**
     * Colonna generata.
     */
    private class FunzioniColumnGenerator implements ColumnGenerator {

        private Funzione funz;

        /**
         * Costruttore minimo con tutte le properties obbligatorie
         */
        public FunzioniColumnGenerator(Funzione funz) {
            this.funz = funz;
        }// end of constructor

        /**
         * Genera la cella.
         */
        public Component generateCell(Table source, Object itemId, Object columnId) {
            Volontario vol = null;
            VolontarioFunzione volFunz = null;
            Label label = null;

            if (itemId instanceof Long) {
                vol = Volontario.find((Long) itemId);
            }// fine del blocco if

            if (vol != null && funz != null) {
                volFunz = VolontarioFunzione.findByVolFun(vol, funz);
            }// fine del blocco if

            if (volFunz != null) {
                label = new Label(FontAwesome.CHECK.getHtml(), ContentMode.HTML);
                label.addStyleName("verde");
            } else {
               label = new Label(FontAwesome.CLOSE.getHtml(), ContentMode.HTML);
                label.addStyleName("rosso");
            }// fine del blocco if-else

            return label;
        }// end of method
    }// end of inner class

}// end of class
