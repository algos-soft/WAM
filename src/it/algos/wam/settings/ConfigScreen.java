package it.algos.wam.settings;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Accordion;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ConfigScreen extends Accordion implements View {

    private ArrayList<ConfigComponent> configComponents;

    public ConfigScreen() {
        super();
        setSizeFull();

        configComponents = new ArrayList<>();
        configComponents.add(new NotificheConfigComponent());
        configComponents.add(new EmailConfigComponent());
        configComponents.add(new PermessiConfigComponent());

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
