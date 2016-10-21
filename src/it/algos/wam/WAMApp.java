package it.algos.wam;

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

    /**
     * Name of the local folder for images.<br>
     * static initialisation block
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";

    /**
     * Static initialisation block
     *
     * Sovrascrive una variabile statica della classe generale,
     * per modificarne il comportamento solo in questa applicazione
     */
    static {
        AlgosApp.DISPLAY_NEW_RECORD_ONLY = false;
    }// end of static method

}// end of abstract static class
