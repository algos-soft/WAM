package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.IntegerField;

/**
 * Created by gac on 07/05/17.
 * Componente di configurazione del controllo frequenza minima
 */

@SuppressWarnings("serial")
public class FrequenzaConfigComponent extends BaseConfigPanel {

    private static final String KEY_USA_FREQUENZA = "usaFrequenza";
    private static final String KEY_TURNI_MINIMI_MENSILI = "turniMinimi";

    private CheckBoxField checkFrequenza;
    private IntegerField numeroTurni;


    public FrequenzaConfigComponent() {
        super();

        // crea i fields
        createFields();

        // crea la UI
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        HorizontalLayout hl;

        layout.addComponent(new Label("Se la frequenza minima viene controllata, un volontario può accedere al login solo se ha effettuato un numero di turni superiore al minimo mensile.<br>Alcuni singoli volontari potrebbero essere esentati dalla frequenza obbligatoria per ragioni organizzative.", ContentMode.HTML));

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label(CompanyPrefs.controllaFrequenza.getDescrizione()));
        hl.addComponent(checkFrequenza);
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label(CompanyPrefs.turniMinimiMensili.getDescrizione()));
        hl.addComponent(numeroTurni);
        layout.addComponent(hl);

        layout.addComponent(new Label(""));
        layout.addComponent(createSaveButton());

        setCompositionRoot(layout);
    }// end of basic constructor

    /**
     * Crea e registra i fields
     * <p>
     * create and add fields and other components
     * bind fields to properties
     */
    private void createFields() {
        // create and add fields and other components
        checkFrequenza = new CheckBoxField();
        numeroTurni = new IntegerField();

        // bind fields to properties
        getGroup().bind(checkFrequenza, KEY_USA_FREQUENZA);
        getGroup().bind(numeroTurni, KEY_TURNI_MINIMI_MENSILI);
    }// end of method

    @Override
    public void loadContent() {
        super.loadContent();
    }// end of method

    public PrefSetItem createItem() {
        return new DataSetItem();
    }// end of method

    @Override
    public String getTitle() {
        return "Frequenza";
    }// end of method

    /**
     * Item containing form data
     */
    private class DataSetItem extends PropertysetItem implements PrefSetItem {

        public DataSetItem() {
            super();

            boolean controllaFrequenza = CompanyPrefs.controllaFrequenza.getBool();
            addItemProperty(KEY_USA_FREQUENZA, new ObjectProperty<>(controllaFrequenza));

            int numeroTurni = CompanyPrefs.turniMinimiMensili.getInt();
            addItemProperty(KEY_TURNI_MINIMI_MENSILI, new ObjectProperty<>(numeroTurni));

        }// end of method

        public void persist() {

            boolean controllaFrequenza = (Boolean) getItemProperty(KEY_USA_FREQUENZA).getValue();
            int numeroTurni = (int) getItemProperty(KEY_TURNI_MINIMI_MENSILI).getValue();

            CompanyPrefs.controllaFrequenza.put(controllaFrequenza);
            CompanyPrefs.turniMinimiMensili.put(numeroTurni);

            Notification.show("Dati salvati");
        }// end of method

    }// end of inner class

}// end of class
