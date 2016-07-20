package it.algos.wam.login;

import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.login.AbsLoginForm;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by alex on 15-03-2016.
 */
public class WamLogin extends Login {

    private WamLoginForm loginForm;

    public WamLogin(BaseCompany company) {
        setCookiePrefix(company.getCompanyCode());
        loginForm = new WamLoginForm();
    }

    @Override
    public AbsLoginForm getLoginForm() {
        return loginForm;
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
        if(login.isLogged()){
            if (login != null) {
                UserIF user = login.getUser();
                if (user instanceof Volontario) {
                    volontario = (Volontario) user;
                }
            }
        }

        return volontario;
    }

}
