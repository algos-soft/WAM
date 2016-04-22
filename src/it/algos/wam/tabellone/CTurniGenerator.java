package it.algos.wam.tabellone;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Componente per impostare il generatore/eliminatore di turni vuoti
 */
public class CTurniGenerator extends CTabelloneEditor {

    private DateField dateField1;
    private DateField dateField2;

    public CTurniGenerator(EntityManager entityManager) {
        super(entityManager);

        addComponent(creaCompTitolo());
        addComponent(creaCompDettaglio());

        Component comp = creaPanComandi();
        addComponent(comp);
        setComponentAlignment(comp, Alignment.BOTTOM_CENTER);

    }


    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<strong>" + "Generatore turni vuoti"+ "</strong>", ContentMode.HTML));

        return layout;
    }



    /**
     * Crea il componente che visualizza i dettagli del turno
     * (nome ecc..)
     *
     * @return il componente di dettaglio
     */
    private Component creaCompDettaglio() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        HorizontalLayout layoutDate = new HorizontalLayout();
        layoutDate.setSpacing(true);
        dateField1 = new DateField("Dal giorno");
        LocalDate tomorrow=LocalDate.now().plusDays(1);
        dateField1.setValue(DateConvertUtils.asUtilDate(tomorrow));
        dateField1.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Object value=valueChangeEvent.getProperty().getValue();
                if(value != null && value instanceof Date){
                    Date date = (Date)value;
                    LocalDate d2=DateConvertUtils.asLocalDate(date).plusWeeks(1);
                    dateField2.setValue(DateConvertUtils.asUtilDate(d2));
                }
            }
        });


        dateField2 = new DateField("Al giorno");
        LocalDate date = DateConvertUtils.asLocalDate(dateField1.getValue());
        Date duWeek=DateConvertUtils.asUtilDate(date.plusWeeks(1));
        dateField2.setValue(duWeek);

        layoutDate.addComponent(dateField1);
        layoutDate.addComponent(dateField2);

        ArrayList<Servizio> servizi=new ArrayList<>();
        servizi.addAll(WamQuery.queryServizi(entityManager, true));
        servizi.addAll(WamQuery.queryServizi(entityManager, false));

        GridLayout grid = new GridLayout(8,servizi.size()+1);
        grid.setSpacing(true);

        grid.addComponent(new Label("Lun"), 1, 0);
        grid.addComponent(new Label("Mar"), 2, 0);
        grid.addComponent(new Label("Mer"), 3, 0);
        grid.addComponent(new Label("Gio"), 4, 0);
        grid.addComponent(new Label("Ven"), 5, 0);
        grid.addComponent(new Label("Sab"), 6, 0);
        grid.addComponent(new Label("Dom"), 7, 0);

        int row=1;
        for(Servizio serv : servizi){
            Label label = new Label(serv.getDescrizione());
            grid.addComponent(label, 0, row);

            for(int col=1; col<8; col++){
                CheckBox cb = new CheckBox();
                grid.addComponent(cb, col, row);
            }

            row++;


        }

        layout.addComponent(layoutDate);
        layout.addComponent(grid);
        return layout;
    }




    /**
     * Crea e ritorna il pannello comandi.
     *
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireDismissListeners(new DismissEvent(bAnnulla, false, false));
            }
        });

        Button bEsegui = new Button("Esegui");
        bEsegui.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        bEsegui.addStyleName(ValoTheme.BUTTON_PRIMARY);    // "primary" not "default"
        bEsegui.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

            }
        });

        layout.addComponent(bAnnulla);
        layout.addComponent(bEsegui);

        return layout;
    }



}
