package it.algos.wam.login;

import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.WamLoginComponent;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.*;

/**
 * Created by alex on 15-03-2016.
 * .
 */
public class WamLogin extends Login {

    private WamLoginForm loginForm;
    private WamUserProfileForm profileForm;

    public WamLogin(BaseCompany company) {
        setCookiePrefix(company.getCompanyCode());
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
        if(login!=null){
            if(login.isLogged()){
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


}
