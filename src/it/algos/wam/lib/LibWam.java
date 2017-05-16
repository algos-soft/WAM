package it.algos.wam.lib;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.login.WamLogin;
import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.domain.pref.PrefType;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.field.RelatedComboField;
import it.algos.webbase.web.lib.*;
import org.apache.commons.lang.LocaleUtils;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by gac on 29 feb 2016.
 * Classe statica di libreria
 */
public abstract class LibWam {

    private static Locale LOCALE = new Locale("it");

    /**
     * Costruisce una chiave per la data di oggi
     * Usato per la indicizzazione dei Turni
     */
    public static int creaChiaveOdierna() {
        Date oggi = LibDate.today();
        return LibWam.creaChiave(oggi);
    }// end of static method

    /**
     * Costruisce una chiave per il primo gennaio dell'anno corrente
     * Usato per la indicizzazione dei Turni
     */
    public static int creaChiavePrimaGennaio() {
        Date primoGennaio = LibDate.getPrimoGennaio();
        return LibWam.creaChiave(primoGennaio);
    }// end of static method

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

    /**
     * Controlla se la company è correttamente selezionata
     */
    public static Boolean isCompany() {
        return CompanySessionLib.getCompany() != null;
    }// end of method

    /**
     * Ritorna la company col casting
     */
    public static WamCompany getCompany() {
        return (WamCompany) CompanySessionLib.getCompany();
    }// end of method

    /**
     * Ritorna la sigla della company
     */
    public static String getCompanySigla() {
        if (isCompany()) {
            return getCompany().getCompanyCode();
        } else {
            return "";
        }// end of if/else cycle
    }// end of method

    /**
     * Ritorna l'utente loggato
     */
    public static String getLoggato() {
        String utente = "";
        WamLogin login = (WamLogin) LibSession.getLogin();
        Volontario vol = null;

        if (login != null && login.getUser() != null && login.getUser() instanceof Volontario) {
            vol = (Volontario) login.getUser();
        }// end of if cycle

        if (vol != null) {
            utente = vol.getNomeCognome();
        }// end of if cycle

        return utente;
    }// end of method

    /**
     * Nuova label
     * Testo normale, senza html
     */
    public static Label label(String testo) {
        return label(testo, null);
    }// end of method

    /**
     * Nuova label
     * Con html
     */
    public static Label labelHtml(String testo) {
        return label(testo, Html.plain);
    }// end of method

    /**
     * Nuova label
     * Testo html strong
     */
    public static Label labelStrong(String testo) {
        return label(testo, Html.strong);
    }// end of method

    /**
     * Nuova label
     * Testo html small
     */
    public static Label labelSmall(String testo) {
        return label(testo, Html.small);
    }// end of method


    /**
     * Nuova label
     */
    public static Label label(String testo, Html tag) {
        Label label = null;

        if (tag != null) {
            if (tag == Html.plain) {
                label = new Label(testo, ContentMode.HTML);
            } else {
                label = new Label(tag.get(testo), ContentMode.HTML);
            }// end of if/else cycle
        } else {
            label = new Label(testo);
        }// end of if/else cycle

        return label;
    }// end of method

    /**
     * Regola una preferenza della company
     * La crea, se non esiste
     */
    public static void setPrefBool(WamCompany company, EntityManager manager, String code, String descrizione, boolean value) {
        Pref pref = null;
        String codeCompanyUnico = getCodiceUnico(company.getCompanyCode(), code);
        boolean esiste = Pref.isEntityByCodeCompanyUnico(codeCompanyUnico, manager);

        if (!esiste) {
            pref = Pref.crea(code, company, PrefType.bool, descrizione);
        }// end of if cycle

        if (pref != null) {
            pref.setBool(value);
            pref.save(manager);
        }// end of if cycle

    }// end of method

    /**
     * Codice unico
     */
    public static String getCodiceUnico(String companyCode, String code) {
        return LibText.primaMaiuscola(companyCode) + LibText.primaMaiuscola(code);
    }// end of method


    /**
     * Aggiunge il campo company
     * <p>
     * in caso di developer, aggiunge (a sinistra) la colonna della company
     * aggiunge tutte le altre property, definite nella sottoclasse
     * Chiamato dalla sottoclasse
     */
    public static Attribute<?, ?>[] addCompanyField(Attribute... elenco) {
        ArrayList<Attribute> lista = new ArrayList<>();

        if (LibSession.isDeveloper()) {
            lista.add(WamCompanyEntity_.company);
        }// end of if cycle

        for (Attribute attr : elenco) {
            lista.add(attr);
        }// end of for cycle

        return lista.toArray(new Attribute[lista.size()]);
    }// end of method

}// end of abstract static class
