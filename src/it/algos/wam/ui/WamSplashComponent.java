package it.algos.wam.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;
import it.algos.webbase.web.login.LoginButton;

/**
 * Splash screen del programma con bottone login.
 */
@SuppressWarnings("serial")
public class WamSplashComponent extends HorizontalLayout {

    public WamSplashComponent() {
        super();
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
        VerticalLayout layout = new VerticalLayout();
        layout.setHeight("100%");
        layout.setWidthUndefined();
        layout.setSpacing(true);

        Label label;

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        Resource res=LibResource.getImgResource("wam.png");
        if (res != null) {
            Image img = LibImage.getImage(res);
            layout.addComponent(img);
        }

        LoginButton lb = new LoginButton();
        layout.addComponent(lb);
        layout.setComponentAlignment(lb, Alignment.MIDDLE_CENTER);

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        return layout;
    }


}
