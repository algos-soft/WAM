package it.algos.wam.tabellone;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.query.WamQuery;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente per impostare il generatore/eliminatore di turni vuoti
 */
public class CTurniGenerator extends CTabelloneEditor {

    public CTurniGenerator(EntityManager entityManager) {
        super(entityManager);

        addComponent(creaCompTitolo());
        addComponent(creaCompDettaglio());
        addComponent(creaPanComandi());

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

        return grid;
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
