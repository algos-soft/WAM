package it.algos.wam.login;

import it.algos.webbase.web.login.Login;
import it.algos.webbase.web.login.LoginButton;

/**
 * Created by alex on 20-07-2016.
 */
public class WamLoginButton extends LoginButton {


    /**
     * Constructor
     */
    public WamLoginButton() {
        super(new WamLogin());
    }

}
