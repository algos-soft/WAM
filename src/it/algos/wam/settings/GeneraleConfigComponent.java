package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;

/**
 * Created by gac on 20/03/17.
 * Componente di configurazione del comportamento del Tabellone
 */
@SuppressWarnings("serial")
public class GeneraleConfigComponent extends BaseConfigPanel {

    private static final String KEY_PRIMA_COGNOME = CompanyPrefs.usaPrimaCognome.getCode();
    private static final String KEY_USA_CERTIFICATI = CompanyPrefs.usaGestioneCertificati.getCode();
    private static final String KEY_USA_STATISTICHE = CompanyPrefs.usaStatisticheSuddivise.getCode();

    private CheckBoxField checkCognome;
    private CheckBoxField checkCertificati;
    private CheckBoxField checkStatistiche;

    public GeneraleConfigComponent() {
        super();

        // crea i fields
        createFields();

        // crea la UI
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        HorizontalLayout hl;

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label(CompanyPrefs.usaPrimaCognome.getDescrizione()));
        hl.addComponent(checkCognome);
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label(CompanyPrefs.usaGestioneCertificati.getDescrizione()));
        hl.addComponent(checkCertificati);
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(new Label(CompanyPrefs.usaStatisticheSuddivise.getDescrizione()));
        hl.addComponent(checkStatistiche);
        layout.addComponent(hl);

        layout.addComponent(new Label(""));
        layout.addComponent(createSaveButton());

        setCompositionRoot(layout);
    }// end of method

    // crea e registra i fields
    private void createFields() {

        // create and add fields and other components
        checkCognome = new CheckBoxField();
        checkCertificati = new CheckBoxField();
        checkStatistiche = new CheckBoxField();

        // bind fields to properties
        getGroup().bind(checkCognome, KEY_PRIMA_COGNOME);
        getGroup().bind(checkCertificati, KEY_USA_CERTIFICATI);
        getGroup().bind(checkStatistiche, KEY_USA_STATISTICHE);
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
        return "Generali";
    }// end of method

    /**
     * Item containing form data
     */
    private class DataSetItem extends PropertysetItem implements PrefSetItem {

        public DataSetItem() {
            super();

            boolean primaCognome = CompanyPrefs.usaPrimaCognome.getBool();
            addItemProperty(KEY_PRIMA_COGNOME, new ObjectProperty<>(primaCognome));

            boolean usaCertificati = CompanyPrefs.usaGestioneCertificati.getBool();
            addItemProperty(KEY_USA_CERTIFICATI, new ObjectProperty<>(usaCertificati));

            boolean usaStatistiche = CompanyPrefs.usaStatisticheSuddivise.getBool();
            addItemProperty(KEY_USA_STATISTICHE, new ObjectProperty<>(usaStatistiche));
        }// end of method

        public void persist() {
            boolean primaCognome = (Boolean) getItemProperty(KEY_PRIMA_COGNOME).getValue();
            boolean usaCertificati = (Boolean) getItemProperty(KEY_USA_CERTIFICATI).getValue();
            boolean usaStatistiche = (Boolean) getItemProperty(KEY_USA_STATISTICHE).getValue();

            CompanyPrefs.usaPrimaCognome.put(primaCognome);
            CompanyPrefs.usaGestioneCertificati.put(usaCertificati);
            CompanyPrefs.usaStatisticheSuddivise.put(usaStatistiche);

            Notification.show("Dati salvati");
        }// end of method

    }// end of internal class

}// end of class
