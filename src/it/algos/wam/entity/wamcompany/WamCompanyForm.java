package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.bootstrap.BootService;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.module.ModulePop;

/**
 * Created by webbase templates.
 */
public class WamCompanyForm extends ModuleForm {

    private boolean usaCreaDatiStandard = false;

    /**
     * The form used to edit an item.
     * <p>
     * Invoca la superclasse passando i parametri:
     *
     * @param item   (facoltativo) singola istanza della classe
     * @param module (obbligatorio) di riferimento
     */
    public WamCompanyForm(Item item, ModulePop module) {
        super(item, module);
    }// end of constructor


    /**
     * Populate the map to bind item properties to fields.
     * Create and add a field for each property declared for this form
     * <p>
     * Implementazione di default nella superclasse.<br>
     * I campi vengono recuperati dal Modello.<br>
     * I campi vengono creati del tipo grafico previsto nella Entity.<br>
     * Se si vuole aggiungere un campo (solo nel form e non nel Modello),<br>
     * usare il metodo sovrascritto nella sottoclasse
     * invocando prima (o dopo) il metodo della superclasse.
     * Se si vuole un layout completamente diverso sovrascrivere
     * senza invocare il metodo della superclasse
     */
    @Override
    public void createFields() {
        super.createFields();
    }// end of method


    /**
     * Create the detail component (the upper part containing the fields).
     * <p>
     * Usa il FormLayout che ha le label a sinsitra dei campi (standard)
     * Se si vogliono le label sopra i campi, sovrascivere questo metodo e usare un VerticalLayout
     *
     * @return the detail component containing the fields
     */
    @Override
    protected Component createComponent() {
        VerticalLayout layout = new VerticalLayout();

        layout.addComponent(super.createComponent());
        layout.addComponent(this.creaCheckBox());

        return layout;
    }// end of method


    /**
     * Crea il CheckBox aggiuntivo
     */
    private Component creaCheckBox() {
        VerticalLayout vertLayout = new VerticalLayout();
        CheckBoxField fieldCheck = null;
        vertLayout.setMargin(true);
        vertLayout.setSpacing(true);

        if (isNewRecord()) {
            fieldCheck = new CheckBoxField("Creazione dei dati iniziali per questa croce");
            fieldCheck.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    usaCreaDatiStandard = (boolean) event.getProperty().getValue();
                }// end of inner method
            });// end of anonymous inner class
            vertLayout.addComponent(fieldCheck);
        }// end of if cycle

        return vertLayout;
    }// end of method

    /**
     * Invoked after the item has been saved.
     * Chance for subclasses to override.
     *
     * @param fields the field group
     */
    @Override
    protected void onPostSave(FieldGroup fields) {
        BeanItem bi = LibBean.fromItem(getItem());
        WamCompany company = (WamCompany) bi.getBean();

        if (usaCreaDatiStandard) {
            BootService.initCompany(company);
        }// end of if cycle
    }// end of method

//    protected boolean save() {
//        BeanItem bi = LibBean.fromItem(getItem());
//        WamCompany company = (WamCompany) bi.getBean();
//
//        if (usaCreaDatiStandard) {
//            BootService.initCompany(company);
//        }// end of if cycle
//
//        return super.save();
//    }// end of method

}// end of form class
