package it.algos.wam;

import it.algos.webbase.domain.pref.Pref;
import it.algos.webbase.web.AlgosApp;

/**
 * Contenitore di costanti della applicazione
 * Regola alcuni flag di comportamento del framwork di base
 */
public class WAMApp extends AlgosApp {


    /**
     * Flags condizionali di utilizzi diversi (validi in tutta l'applicazione)
     */
    public final static boolean PASSWORD_CRIPTATE = false;
    public final static boolean ADMIN_VEDE_PASSWORD = true;

    public static final String DEMO_COMPANY_CODE = "demo";
    public static final String TEST_COMPANY_CODE = "test";

    //--chiavi per preferenze
    public final static String DISPLAY_FOOTER_INFO = "displayFooterInfo";
    public final static String DISPLAY_TOOLTIPS = "displayTooltips";
    public final static String DISPLAY_FIELD_ORDINE = "displayFieldOrdine";
    public final static String USA_FORM_LAYOUT = "usaFormLayout";
    public final static String USA_REFRESH_DEMO = "usaRefreshDemo";
    public final static String USA_MIGRATION = "usaMigration";
    public final static String INFO_APP = "Algos s.r.l. - wam.0.8 del 15.11.16";

    /**
     * Name of the local folder for images.<br>
     * static initialisation block
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";


    /**
     * Static initialisation block
     *
     * Sovrascrive alcune variabili statiche della classe generale,
     * per modificarne il comportamento solo in questa applicazione
     */
    static {
        AlgosApp.DISPLAY_NEW_RECORD_ONLY = false;
        AlgosApp.DISPLAY_TOOLTIPS = Pref.getBool(DISPLAY_TOOLTIPS, null, true);
    }// end of static method

}// end of abstract static class
