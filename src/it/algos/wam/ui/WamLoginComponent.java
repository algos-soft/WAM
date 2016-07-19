package it.algos.wam.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.wam.login.WamLogin;

/**
 * Componente di Login
 * Created by alex on 19-07-2016.
 */
public class WamLoginComponent extends WamLogoComponent {
    private WamLogin login;

    public WamLoginComponent(WamLogin login) {
        super();
        this.login=login;

        getPlaceholder().addComponent(createLoginLayout());
    }


    private Component createLoginLayout() {
        Component comp;

        FormLayout fl = new FormLayout();
        comp = login.getLoginForm().getUsernameField();
        fl.addComponent(comp);
        comp = login.getLoginForm().getPassField();
        fl.addComponent(comp);
        comp = login.getLoginForm().getRememberField();
        fl.addComponent(comp);

        Button lb = new Button("Login");
        lb.setIcon(FontAwesome.SIGN_IN);
        fl.addComponent(lb);
        lb.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                login.attemptLogin();
            }
        });

        return fl;

    }
}
