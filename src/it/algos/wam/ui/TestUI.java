package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.UIEvents;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;

/**
 * Created by alex on 21/05/16.
 * .
 */
@Theme("wam")
public class TestUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout l = new VerticalLayout();

        Button bv = new Button("Vertical");
        bv.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showWindow(createVertical());
            }
        });

        Button bf = new Button("Form");
        bf.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                showWindow(createForm());
            }
        });


        l.addComponent(bv);
        l.addComponent(bf);

        setContent(l);

    }


    private void showWindow(Component comp) {
        final Window window = new Window();
        window.setCaption("Titolo");
        window.setHeightUndefined();

        comp.addStyleName("yellowBg");
        window.addStyleName("greenBg");


        MyForm form = new MyForm(comp);
        window.setContent(form);

        window.setResizable(false);
        window.center();
        this.addWindow(window);

        // FocusListener and bringToFront() mark the window as dirty upon focus received.
        // (needed if a FormLayout is contained in the window)
        // Due to a FormLayout bug, the size of the FormLayout is not calculated correctly
        // and the scroll bar in the window might not appear when close to the edge.
        // Reattaching the window fixes the issue.
        // Alex may-2016
        window.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent focusEvent) {
                window.markAsDirty();
            }
        });
        window.bringToFront();

    }


    private Component createVertical() {
        TextField field;
        VerticalLayout l = new VerticalLayout();
        l.setMargin(true);

        field = new TextField();
        field.setCaption("campo1");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo2");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo3");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo4");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo5");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo6");
        l.addComponent(field);

        return l;
    }

    private Component createForm() {

        TextField field;
        FormLayout l = new FormLayout();
        l.setMargin(true);
        l.setWidthUndefined();

        Button b = new Button("test");
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                l.attach();
            }
        });
        l.addComponent(b);


        field = new TextField();
        field.setCaption("campo1");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo2");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo3");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo4");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo5");
        l.addComponent(field);

        field = new TextField();
        field.setCaption("campo6");
        l.addComponent(field);

        return l;

    }


    public class MyForm extends VerticalLayout {
        public MyForm(Component comp) {
            addComponent(comp);
            //setExpandRatio(comp, 1);
        }
    }


}
