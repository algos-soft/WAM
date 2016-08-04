package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.IntegerField;

@SuppressWarnings("serial")
/**
 * Componente di configurazione del comportamento del Tabellone
 */
public class TabelloneConfigComponent extends BaseConfigPanel {

    private static final String KEY_ORE_ALERT = "oreAlert";
    private static final String KEY_ORE_WARNING = "oreWarning";

    private ArrayComboField comboOreAlert;
    private ArrayComboField comboOreWarning;


    public TabelloneConfigComponent() {
        super();

        // crea i fields
        createFields();

        // crea la UI
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(new Label("Un turno si considera <strong>incompleto</strong> quando " +
                "non tutte <br>le funzioni obbligatorie sono coperte", ContentMode.HTML));

        HorizontalLayout hl;

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label("Un turno incompleto diventa giallo"));
        hl.addComponent(comboOreWarning);
        hl.addComponent(new Label("ore prima dell'inizio (warning)"));
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label("Un turno incompleto diventa rosso"));
        hl.addComponent(comboOreAlert);
        hl.addComponent(new Label("ore prima dell'inizio (alert)"));
        layout.addComponent(hl);

        layout.addComponent(createSaveButton());

        setCompositionRoot(layout);

    }

    // crea e registra i fields
    private void createFields() {

        // create and add fields and other components
        comboOreAlert = new ArrayComboField(new Integer[]{12, 24, 36, 48, 72, 96});
        comboOreAlert.setCaption(null);
        comboOreAlert.setWidth("6em");

        comboOreWarning = new ArrayComboField(new Integer[]{24, 36, 48, 72, 96, 120, 144});
        comboOreWarning.setCaption(null);
        comboOreWarning.setWidth("6em");

        // bind fields to properties
        getGroup().bind(comboOreAlert, KEY_ORE_ALERT);
        getGroup().bind(comboOreWarning, KEY_ORE_WARNING);

    }

    @Override
    public void loadContent() {
        super.loadContent();
    }

    public PrefSetItem createItem() {
        return new DataSetItem();
    }

    /**
     * Item containing form data
     */
    private class DataSetItem extends PropertysetItem implements PrefSetItem {

        public DataSetItem() {
            super();

            int alertOrePrima = CompanyPrefs.turnoAlertOrePrima.getInt();
            addItemProperty(KEY_ORE_ALERT, new ObjectProperty<>(alertOrePrima));

            int warningOrePrima = CompanyPrefs.turnoWarningOrePrima.getInt();
            addItemProperty(KEY_ORE_WARNING, new ObjectProperty<>(warningOrePrima));

        }

        public void persist() {

            int alertOrePrima = (int) getItemProperty(KEY_ORE_ALERT).getValue();
            int warningOrePrima = (int) getItemProperty(KEY_ORE_WARNING).getValue();

            if(warningOrePrima>alertOrePrima){
                CompanyPrefs.turnoAlertOrePrima.put(alertOrePrima);
                CompanyPrefs.turnoWarningOrePrima.put(warningOrePrima);

                Notification.show("Dati salvati");

            }else{
                Notification.show("Le ore di Warning devono essere superiori alle ore di Alert", Notification.Type.ERROR_MESSAGE);
            }


        }

    }

    @Override
    public String getTitle() {
        return "Tabellone";
    }


}
