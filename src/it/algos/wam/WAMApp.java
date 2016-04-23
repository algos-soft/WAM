package it.algos.wam;

import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.AlgosApp;

/**
 * Contenitore di costanti della applicazione
 */
public abstract class WAMApp extends AlgosApp {

    public static final String DEMO_COMPANY_CODE = "demo";
    public static final String TEST_COMPANY_CODE = "test";

    /**
     * Name of the local folder for images.<br>
     */
    public static final String IMG_FOLDER_NAME = "WEB-INF/data/img/";

    private static Volontario loggedUser;

    /**
     * PROVVISORIO
     * Ritorna l'utente correntemente loggato
     *
     * @return l'utente loggato
     */
    public static Volontario getLoggedUser() {
        if(loggedUser==null){
            loggedUser= Volontario.find(1);
        }
        return loggedUser;
    }

    /**
     * PROVVISORIO
     * Verifica se il volontario è un admin
     *
     * @return true se è un admin
     */
    public static boolean isAdmin() {
        boolean admin = false;
        Volontario vol = getLoggedUser();
        if (vol != null) {
            admin = vol.isAdmin();
        }
        return admin;
    }


}// end of abstract static class
