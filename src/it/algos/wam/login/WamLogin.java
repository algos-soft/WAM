package it.algos.wam.login;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.WamLoginComponent;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibCookie;
import it.algos.webbase.web.lib.LibCrypto;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.*;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * Created by alex on 15-03-2016.
 * .
 */
public class WamLogin extends Login {

    private WamLoginForm loginForm;
    private WamUserProfileForm profileForm;

    public WamLogin(BaseCompany company) {
        setCookiePrefix(WAMApp.class.getPackage().getName() + "." + company.getCompanyCode());
        loginForm = new WamLoginForm();
        profileForm = new WamUserProfileForm();
    }

    @Override
    public AbsLoginForm getLoginForm() {
        return loginForm;
    }

    @Override
    public AbsUserProfileForm getUserProfileForm() {
        return profileForm;
    }

    @Override
    protected UserIF userFromNick(String username) {
        return Volontario.queryByNick(username);
    }

    /**
     * Ritorna il volontario correntemente loggato
     *
     * @return il volontario loggato
     */
    public static Volontario getLoggedVolontario() {
        Volontario volontario = null;
        Login login = Login.getLogin();
        if (login != null) {
            if (login.isLogged()) {
                UserIF user = login.getUser();
                if (user instanceof Volontario) {
                    volontario = (Volontario) user;
                }
            }
        }

        return volontario;
    }


    /**
     * Displays the Login form
     * Cra un nuovo form prima di ogni visualizzazione
     */
    public void showLoginForm() {
        loginForm = new WamLoginForm();
        super.showLoginForm();
    }


    @Override
    public void logout() {
        super.logout();
        CompanySessionLib.setCompany(null);
    }

    @Override
    protected String getLoginPath() {
        return "/";
    }

    /**
     * Controlla se ci sono cookies con un login valido
     * Registra l'oggetto UserIF nella Login (se valido)
     */
    public void checkLoggato() {
        String username = LibCookie.getCookieValue(getLoginKey());
        String encPass = LibCookie.getCookieValue(getPasswordKey());
        String clearPass = LibCrypto.decrypt(encPass);
        checkLoggato(username, clearPass);
    }// end of method

    /**
     * Controlla le credenziali eventualmente presenti nella request
     * Registra l'oggetto UserIF nella Login (se valido)
     */
    public void checkLoggato(VaadinRequest request) {
        checkLoggato(request.getParameter("utente"), request.getParameter("password"));
    }// end of method

    /**
     * Controlla le credenziali ricevute come parametri
     * Registra l'oggetto UserIF nella Login (se valido)
     */
    public void checkLoggato(String utente, String password) {
        if (utente != null && !utente.equals("")) {
            Volontario volontario = Volontario.queryByNick(utente);
            if (volontario != null) {
                if (volontario.getPassword().equals(password)) {
                    this.setUser(volontario);
                }// end of if cycle
            }// end of if cycle
        }// end of if cycle
    }// end of method


}// end of class
