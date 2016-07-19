package it.algos.wam.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import it.algos.wam.login.WamLogin;
import it.algos.wam.login.WamLoginForm;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.login.LoginButton;
import it.algos.webbase.web.login.LoginEvent;
import it.algos.webbase.web.login.LoginListener;
import it.algos.webbase.web.login.UserIF;

/**
 * Componente di Login
 * Created by alex on 19-07-2016.
 */
public class WamLoginComponent extends HorizontalLayout {
    private WamLogin login;

    public WamLoginComponent(WamLogin login) {
        super();
        this.login=login;
        setSizeFull();

        Label label;

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);


        addComponent(createCenterComponent());

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);


    }


    private Component createCenterComponent(){
        Component comp;
        VerticalLayout layout = new VerticalLayout();
        layout.setHeight("100%");
        layout.setWidthUndefined();
        layout.setSpacing(true);

        Label label;

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        Resource res= LibResource.getImgResource("wam.png");
        if (res != null) {
            Image img = LibImage.getImage(res);
            layout.addComponent(img);
            layout.setComponentAlignment(img, Alignment.MIDDLE_CENTER);
        }

        FormLayout fl = new FormLayout();
        comp = login.getLoginForm().getUsernameField();
        fl.addComponent(comp);
        comp = login.getLoginForm().getPassField();
        fl.addComponent(comp);
        comp = login.getLoginForm().getRememberField();
        fl.addComponent(comp);
        layout.addComponent(fl);
        layout.setComponentAlignment(fl, Alignment.MIDDLE_CENTER);

        Button lb = new Button("Login");
        lb.setIcon(FontAwesome.SIGN_IN);
        layout.addComponent(lb);
        layout.setComponentAlignment(lb, Alignment.MIDDLE_CENTER);
        lb.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                login.attemptLogin();
            }
        });

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        return layout;
    }

}
