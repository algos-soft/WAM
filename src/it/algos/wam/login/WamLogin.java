package it.algos.wam.login;

import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.login.AbsLoginForm;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by alex on 15-03-2016.
 */
public class WamLogin extends Login {

    private WamLoginForm loginForm;

    public WamLogin() {
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

}
