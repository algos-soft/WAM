package it.algos.wam.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;

/**
 * Splash screen del programma.
 */
@SuppressWarnings("serial")
public class WamSplashComponent extends VerticalLayout {

    public WamSplashComponent() {
        super();
        setSizeFull();

        Label label;

        // horizontal: label left + image + label right
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("100%");

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        Resource res=LibResource.getImgResource("splash_image.png");
        if (res != null) {
            Image img = LibImage.getImage(res);
            layout.addComponent(img);
        }

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        // vertical: label top + image layout + label bottom
        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);

        addComponent(layout);

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);

    }


}
