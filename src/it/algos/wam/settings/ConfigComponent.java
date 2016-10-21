package it.algos.wam.settings;

import com.vaadin.ui.Component;

/**
 * Componente di base per le pagine di configurazione
 */
public interface ConfigComponent {

    /**
     * Returns the main UI component
     */
    Component getUIComponent();

    /**
     * Returns the title of the component
     */
    String getTitle();

    /**
     * Reads the data from the storage and updates the UI components
     */
    void loadContent();

}
