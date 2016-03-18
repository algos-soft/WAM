package it.algos.wam.tabellone;

import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.web.dialog.ConfirmDialog;

import javax.persistence.EntityManager;

/**
 * Componente per presentare e modificare un Servizio nel Tabellone.
 * Created by alex on 18-03-2016.
 */
public class CServizioEditor extends CTabelloneEditor {

    private Servizio servizio;

    public CServizioEditor(Servizio servizio, EntityManager entityManager) {
        super(entityManager);
        this.servizio = servizio;

        addComponent(creaCompTitolo());
        addComponent(creaPanComandi());


    }


    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        layout.addComponent(new Label(servizio.getOrario()));
        layout.addComponent(new Label("<strong>" + servizio.getDescrizione() + "</strong>", ContentMode.HTML));

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

        Button bElimina = new Button("Elimina servizio");
        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ConfirmDialog dialog=new ConfirmDialog("Elimina servizio","Sei sicuro?",new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog, boolean confirmed) {
                        if(confirmed){
                            servizio.delete(entityManager);
                            fireDismissListeners(new DismissEvent(bElimina, false, true));
                        }
                    }
                });
                dialog.show();
            }
        });

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireDismissListeners(new DismissEvent(bAnnulla, false, false));
            }
        });

        Button bRegistra = new Button("Registra");
        bRegistra.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        bRegistra.addStyleName(ValoTheme.BUTTON_PRIMARY);    // "primary" not "default"
        bRegistra.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
//                if(saveTurno()){
//                    fireDismissListeners(new DismissEvent(bRegistra, true, false));
//                }
                fireDismissListeners(new DismissEvent(bRegistra, true, false));
            }
        });

        // controllo se il servizio è persisted
        if(servizio.getId()!=null){
            layout.addComponent(bElimina);
        }
        layout.addComponent(bAnnulla);
        layout.addComponent(bRegistra);

        return layout;
    }



}
