package it.algos.wam.settings;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;

@SuppressWarnings("serial")

/**
 * Componente di configurazione dei permessi utente ad uso dell'Admin
 */
public class IscrizioniConfigComponent extends BaseConfigPanel {

    private static final String KEY_CHECK_MINUTI_DOPO = "checkMinutiDopo";
    private static final String KEY_COMBO_MINUTI_DOPO = "comboMinutiDopo";
    private static final String KEY_CHECK_ORE_PRIMA = "checkOrePrima";
    private static final String KEY_COMBO_ORE_PRIMA = "comboOrePrima";


    private CheckBoxField checkMinutiDopo;
    private ArrayComboField comboMinutiDopo;
    private CheckBoxField checkOrePrima;
    private ArrayComboField comboOrePrima;

    private CheckBoxField checkNessunaLimitazione;


    public IscrizioniConfigComponent() {
        super();

        // crea i fields
        createFields();


        // crea la UI
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        Label label;
        label = new Label("Puoi limitare la possibilità di un volontario di cancellarsi dopo che si è iscritto a un turno.<br>" +
                "Se sono trascorsi i termini, solo un amministratore potrà cancellare l'iscrizione.", ContentMode.HTML);
        layout.addComponent(label);

        label = new Label("Il volontario si può cancellare:");
        layout.addComponent(label);


        HorizontalLayout hl;

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(checkMinutiDopo);
        hl.addComponent(new Label("entro"));
        hl.addComponent(comboMinutiDopo);
        hl.addComponent(new Label("minuti dalla iscrizione"));
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(checkOrePrima);
        hl.addComponent(new Label("fino a"));
        hl.addComponent(comboOrePrima);
        hl.addComponent(new Label("ore prima dell'inizio del turno"));
        layout.addComponent(hl);

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        hl.addComponent(checkNessunaLimitazione);
        hl.addComponent(new Label("sempre"));
        layout.addComponent(hl);

        layout.addComponent(createSaveButton());

        setCompositionRoot(layout);

    }

    // crea e registra i fields
    private void createFields() {

        // create and add fields and other components
        checkMinutiDopo = new CheckBoxField();
        comboMinutiDopo = new ArrayComboField(new Integer[]{5, 10, 15, 30, 45, 60, 90, 120});

        checkOrePrima = new CheckBoxField();
        comboOrePrima = new ArrayComboField(new Integer[]{6, 8, 12, 24, 36, 48});

        checkMinutiDopo.setCaption(null);
        comboMinutiDopo.setCaption(null);
        checkOrePrima.setCaption(null);
        comboOrePrima.setCaption(null);

        comboMinutiDopo.setWidth("6em");
        comboOrePrima.setWidth("6em");

        checkMinutiDopo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean value = (boolean) valueChangeEvent.getProperty().getValue();
                if (value) {
                    checkOrePrima.setValue(false);
                    checkNessunaLimitazione.setValue(false);
                }
            }
        });

        checkOrePrima.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean value = (boolean) valueChangeEvent.getProperty().getValue();
                if (value) {
                    checkMinutiDopo.setValue(false);
                    checkNessunaLimitazione.setValue(false);
                }
            }
        });

        // questo campo serve solo per togliere la spunta a tutti gli altri
        checkNessunaLimitazione = new CheckBoxField();
        checkNessunaLimitazione.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                boolean value = (boolean) valueChangeEvent.getProperty().getValue();
                if (value) {
                    checkOrePrima.setValue(false);
                    checkMinutiDopo.setValue(false);
                }
            }
        });



        // bind fields to properties
        getGroup().bind(checkMinutiDopo, KEY_CHECK_MINUTI_DOPO);
        getGroup().bind(comboMinutiDopo, KEY_COMBO_MINUTI_DOPO);
        getGroup().bind(checkOrePrima, KEY_CHECK_ORE_PRIMA);
        getGroup().bind(comboOrePrima, KEY_COMBO_ORE_PRIMA);

    }

    @Override
    public void loadContent() {
        super.loadContent();
    }

    public PrefSetItem createItem() {
        return new NotificheSetItem();
    }

    /**
     * Item containing form data
     */
    private class NotificheSetItem extends PropertysetItem implements PrefSetItem {

        public NotificheSetItem() {
            super();

            int cancMode = CompanyPrefs.modoCancellazione.getInt();
            switch (cancMode) {
                case Iscrizione.MODE_CANC_PRE:
                    addItemProperty(KEY_CHECK_ORE_PRIMA, new ObjectProperty<>(true));
                    addItemProperty(KEY_CHECK_MINUTI_DOPO, new ObjectProperty<>(false));
                    break;
                case Iscrizione.MODE_CANC_POST:
                    addItemProperty(KEY_CHECK_ORE_PRIMA, new ObjectProperty<>(false));
                    addItemProperty(KEY_CHECK_MINUTI_DOPO, new ObjectProperty<>(true));
                    break;
                default:
                    addItemProperty(KEY_CHECK_ORE_PRIMA, new ObjectProperty<>(false));
                    addItemProperty(KEY_CHECK_MINUTI_DOPO, new ObjectProperty<>(false));
                    break;
            }

            int orePrima = CompanyPrefs.cancOrePrimaInizioTurno.getInt();
            addItemProperty(KEY_COMBO_ORE_PRIMA, new ObjectProperty<>(orePrima));
            int minutiDopo = CompanyPrefs.cancMinutiDopoIscrizione.getInt();
            addItemProperty(KEY_COMBO_MINUTI_DOPO, new ObjectProperty<>(minutiDopo));


        }

        public void persist() {

            int mode= Iscrizione.MODE_CANC_NONE;
            boolean checkPre = (boolean)getItemProperty(KEY_CHECK_ORE_PRIMA).getValue();
            if(checkPre){
                mode= Iscrizione.MODE_CANC_PRE;
            }
            boolean checkPost = (boolean)getItemProperty(KEY_CHECK_MINUTI_DOPO).getValue();
            if(checkPost){
                mode= Iscrizione.MODE_CANC_POST;
            }
            CompanyPrefs.modoCancellazione.put(mode);

            int orePre = (int)getItemProperty(KEY_COMBO_ORE_PRIMA).getValue();
            CompanyPrefs.cancOrePrimaInizioTurno.put(orePre);

            int minutiPost = (int)getItemProperty(KEY_COMBO_MINUTI_DOPO).getValue();
            CompanyPrefs.cancMinutiDopoIscrizione.put(minutiPost);

            Notification.show("Dati salvati");


        }

    }

    @Override
    public String getTitle() {
        return "Iscrizioni e cancellazioni";
    }

}
