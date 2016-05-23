package it.algos.wam.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;

/**
 * Created by alex on 21/05/16.
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
        window.setHeight("90%");

        comp.addStyleName("yellowBg");
        window.addStyleName("greenBg");

        //comp.setHeight("100%");


//        comp.setHeightUndefined();
//
//        VerticalLayout l = new VerticalLayout();
//        l.addComponent(comp);
//        window.setContent(l);
//        l.setExpandRatio(comp, 1);
        //window.setExpandRatio(comp, 1);

        MyForm form = new MyForm(comp);
        window.setContent(form);

        window.setResizable(false);
        window.center();
        this.getUI().addWindow(window);
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
