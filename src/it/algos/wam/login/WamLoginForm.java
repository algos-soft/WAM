package it.algos.wam.login;

import com.vaadin.ui.Button;
import it.algos.webbase.web.login.BaseLoginForm;

/**
 * Created by alex on 25-05-2016.
 */
public class WamLoginForm extends BaseLoginForm {

    public WamLoginForm() {
        addComponent(new Button("WAM"));
    }
}
