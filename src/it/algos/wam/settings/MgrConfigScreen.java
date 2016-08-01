package it.algos.wam.settings;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Accordion;

import java.util.ArrayList;

/**
 * Schermata generale delle preferenze per il manager
 * Created by alex on 1-08-2016.
 */
public class MgrConfigScreen extends Accordion implements View {

    private ArrayList<ConfigComponent> configComponents;

    public MgrConfigScreen() {
        super();
        setSizeFull();

        configComponents = new ArrayList<>();
        configComponents.add(new SMTPServerConfigComponent());
//        configComponents.add(new MgrNotificheConfigComponent());

        for (ConfigComponent comp : configComponents) {
            addTab(comp.getUIComponent(), comp.getTitle());
            comp.loadContent();
        }

        addSelectedTabChangeListener(new SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                // potrei recuperare solo il singolo ma ricarico tutti
                for (ConfigComponent comp : configComponents) {
                    comp.loadContent();
                }
            }
        });

    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
