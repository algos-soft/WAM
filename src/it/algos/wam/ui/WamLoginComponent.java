package it.algos.wam.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.login.WamLogin;
import it.algos.wam.login.WamLoginForm;
import it.algos.webbase.web.login.AbsLoginForm;
import it.algos.webbase.web.login.Login;

/**
 * Componente di Login
 * Created by alex on 19-07-2016.
 */
public class WamLoginComponent extends WamLogoComponent {
    private Login login;

    public WamLoginComponent(Login login) {
        super();
        this.login=login;

        getPlaceholder().addComponent(createLoginLayout());
    }


    private Component createLoginLayout() {
        Component comp;

        AbsLoginForm form = login.getLoginForm();
//        AbsLoginForm form = new WamLoginForm();

        FormLayout fl = new FormLayout();
        comp = form.getUsernameField();
        comp.setWidth("15em");
        fl.addComponent(comp);
        if(comp instanceof Focusable){
            ((Focusable)comp).focus();
        }

        comp = form.getPassField();
        comp.setWidth("15em");
        fl.addComponent(comp);

        comp = form.getRememberField();
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

        lb.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        lb.addStyleName(ValoTheme.BUTTON_PRIMARY);

        return fl;

    }

    private Login getLogin(){
        return Login.getLogin();
    }

}
