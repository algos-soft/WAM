package it.algos.wam.tabellone;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import java.time.LocalDate;

/**
 * Componente che rappresenta una singola Iscrizione
 * Created by alex on 21/02/16.
 */
public class CIscrizione extends HorizontalLayout {

    private FontAwesome icon;
    private Label label;

    public CIscrizione(String nome, FontAwesome icon) {
        super();

        label=new Label();
        label.setContentMode(ContentMode.HTML);

        this.icon=icon;

        label.setValue(nome);

        setHeight(GridTabellone.H_ISCRIZIONI);

        addStyleName("ciscrizione");

        Label iconLabel = new Label();
        iconLabel.setContentMode(ContentMode.HTML);
        iconLabel.setWidth("1.3em");
        iconLabel.addStyleName("ciscrizione");
//      //  iconLabel.addStyleName("bfunzione");

        if(icon!=null){
            iconLabel.setValue(icon.getHtml());
        }

        addComponent(iconLabel);
        addComponent(label);

    }

//    @Override
//    public void setValue(String stringValue) {
//        if(icon!=null){
//            String html=icon.getHtml();
//            stringValue = html+" "+stringValue;
//        }
//        super.setValue(stringValue);
//    }
}
