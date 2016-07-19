package it.algos.wam.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Componente che visualizza lo splash e un messaggio di errore
 */
public class WamErrComponent extends WamLogoComponent {

    private String msg;

    public WamErrComponent(String msg) {
        super();
        this.msg=msg;
        getPlaceholder().addComponent(createMsgComponent());
    }

    private Component createMsgComponent() {
        Label label = new Label(msg);
        label.addStyleName("red");
        return label;
    }

}
