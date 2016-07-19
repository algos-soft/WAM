package it.algos.wam.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import it.algos.webbase.web.lib.LibImage;
import it.algos.webbase.web.lib.LibResource;

/**
 * Componente con logo centrato
 * Created by alex on 19-07-2016.
 */
public class WamLogoComponent extends HorizontalLayout {

    private HorizontalLayout placeholder;

    public WamLogoComponent() {
        super();
        setSizeFull();

        Label label;

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);

        addComponent(createVerticalComponent());

        label = new Label();
        addComponent(label);
        setExpandRatio(label, 1.0f);

    }


    private Component createVerticalComponent(){

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

        placeholder = new HorizontalLayout();
        layout.addComponent(placeholder);
        layout.setComponentAlignment(placeholder, Alignment.MIDDLE_CENTER);

        label = new Label();
        layout.addComponent(label);
        layout.setExpandRatio(label, 1.0f);

        return layout;
    }

    public HorizontalLayout getPlaceholder() {
        return placeholder;
    }
}
