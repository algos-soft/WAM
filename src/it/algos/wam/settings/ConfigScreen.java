package it.algos.wam.settings;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.TabSheet;

import java.util.ArrayList;

@SuppressWarnings("serial")

/**
 * Schermata generale delle preferenze ad uso dell'Admin
 * Created by alex on 1-08-2016.
 */
public class ConfigScreen extends TabSheet implements View {

    private ArrayList<ConfigComponent> configComponents;

    public ConfigScreen() {
        super();
        setSizeFull();

        configComponents = new ArrayList<>();
        configComponents.add(new GeneraleConfigComponent());
        configComponents.add(new NotificheConfigComponent());
        configComponents.add(new EmailConfigComponent());
        configComponents.add(new IscrizioniConfigComponent());
        configComponents.add(new FrequenzaConfigComponent());
        configComponents.add(new TabelloneConfigComponent());

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
