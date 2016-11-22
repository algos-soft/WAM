package it.algos.wam.lib;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.DateConvertUtils;
import it.algos.webbase.web.lib.LibDate;
import it.algos.webbase.web.lib.LibText;
import org.apache.commons.lang.LocaleUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;


/**
 * Created by gac on 29 feb 2016.
 * Classe statica di libreria
 */
public abstract class LibWam {

    private static Locale LOCALE = new Locale("it");

    /**
     * Costruisce una chiave della data
     * Usato per la indicizzazione dei Turni
     */
    public static int creaChiave(Date data) {
        int chiave = 0;
        int anno;
        int giorno;

        if (data != null) {
            anno = LibDate.getYear(data);
            giorno = LibDate.getDayYear(data);
            chiave = anno * 1000 + giorno;
        }// end of if cycle

        return chiave;
    }// end of static method


    /**
     * Costruisce una chiave da una LocalDate
     */
    public static int creaChiave(LocalDate data) {
        Date d = DateConvertUtils.asUtilDate(data);
        return creaChiave(d);
    }// end of static method


    /**
     * Elabora l'URL della Request per controllare se esiste il parametro ''skip''
     * Se c'è questo parametro non va al tabellone (ma non funziona, vedi goHome() in Tabellone)
     * Il parametro serve (patch) quando si ritorna qui per la seconda (o successiva) volta, proveniendo dal tabellone
     * Elimina il parametro, per evitare che nei passaggi successivi al secondo si accumulino diversi ''skip''
     *
     * @param request the Vaadin request that caused this UI to be created
     */
    private static boolean leggeSkip(VaadinRequest request) {
        boolean esisteSkip = false;
        String skip = request.getParameter("skip");

        if (skip == null || skip.isEmpty()) {
            esisteSkip = false;
        } else {
            esisteSkip = true;

        }// end of if/else cycle

        return esisteSkip;
    }// end of method


    /**
     * Legge il valore di una funzione selezionata in un popup di funzioni
     * Usato sia da FunzioneForm che da ServozioForm
     */
    public static Funzione getFunzione(Component.Event event) {
        Funzione funz = null;
        Object obj;
        Object value;
        RelatedComboField combo;

        obj = event.getSource();
        if (obj instanceof RelatedComboField) {
            combo = (RelatedComboField) obj;
            value = combo.getValue();
            if (value instanceof Long) {
                funz = Funzione.find((Long) value);
            }// end of if cycle
        }// end of if cycle

        return funz;
    }// end of static method

    /**
     * Legge il valore di un servizio-funzione selezionato in un popup di funzioni
     * Usato sia da FunzioneForm che da ServozioForm
     */
    public static ServizioFunzione getServizioFunzione(Component.Event event) {
        ServizioFunzione serFunz = null;
        Object obj;
        Object value;
        RelatedComboField combo;

        obj = event.getSource();
        if (obj instanceof RelatedComboField) {
            combo = (RelatedComboField) obj;
            value = combo.getValue();
            if (value instanceof Long) {
                serFunz = ServizioFunzione.find((Long) value);
            }// end of if cycle
        }// end of if cycle

        return serFunz;
    }// end of static method


    /**
     * Ritorna il locale per l'applicazione
     */
    public static Locale getCurrentLocale() {
        return LOCALE;
    }



}// end of abstract static class
