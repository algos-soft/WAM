package it.algos.wam.login;

import com.vaadin.server.Page;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.login.AbsLoginForm;
import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.UserIF;

import java.net.URI;

/**
 * Created by alex on 15-03-2016.
 */
public class WamLogin extends Login {

    @Override
    protected AbsLoginForm getLoginForm() {
        return new WamLoginForm();
    }

    @Override
    protected UserIF userFromNick(String username) {
        return Volontario.queryByNick(username);
    }

}
