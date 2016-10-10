package it.algos.wam;

import it.algos.webbase.web.AlgosApp;

/**
 * Contenitore di costanti della applicazione
 */
public abstract class WAMApp extends AlgosApp {


    /**
     * Flags condizionali di utilizzi diversi (validi in tutta l'applicazione)
     */
    public final static boolean PASSWORD_CRIPTATE = false;
    public final static boolean ADMIN_VEDE_PASSWORD = true;

    public static final String DEMO_COMPANY_CODE = "demo";
    public static final String TEST_COMPANY_CODE = "test";

    /**
     * Name of the local folder for images.<br>
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";


}// end of abstract static class
