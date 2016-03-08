package it.algos.wam.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Gac on 08 mar 2016.
 * .
 */
public class BaseComponent extends VerticalLayout {

    private Navigator nav;
    private VerticalLayout header = new VerticalLayout();
    private VerticalLayout body = new VerticalLayout();
    private VerticalLayout footer = new VerticalLayout();

    /**
     */
    public BaseComponent(UI ui) {
        this(ui, null);
    }// end of constructor

    /**
     */
    public BaseComponent(UI ui, Navigator nav) {
        if (nav == null) {
            nav = new Navigator(ui, body);
        }// fine del blocco if
        this.nav = nav;
        this.addComponent(header);
        this.addComponent(body);
        this.addComponent(footer);
    }// end of constructor

    public void putHeader(Component header) {
        this.header.removeAllComponents();
        this.header.addComponent(header);
    }

//    public void putBody(Component body) {
//        this.body.removeAllComponents();
//        this.body.addComponent(body);
//    }

    public void putFooter(Component footer) {
        this.footer.removeAllComponents();
        this.footer.addComponent(footer);
    }

    public Navigator getNav() {
        return nav;
    }
}
