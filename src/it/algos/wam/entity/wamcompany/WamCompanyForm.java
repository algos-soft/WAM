package it.algos.wam.entity.wamcompany;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import it.algos.wam.bootstrap.BootService;
import it.algos.wam.entity.turno.Turno_;
import it.algos.wam.lib.LibWam;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibBean;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;

/**
 * Created by webbase templates.
 */
public class WamCompanyForm extends ModuleForm {

    private static String RIENTRO = "  ---> ";
    private boolean usaCreaDatiProva;
    private boolean usaCreaDatiBase;
    private boolean usaCreaTurniVuoti;
    private boolean usaRiempiTurni;

    private CheckBoxField box1;
    private CheckBoxField box2;
    private CheckBoxField box3;
    private CheckBoxField box4;

    private Label label2;
    private Label label3;
    private Label label4;

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
     * Attributes used in this form
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Form specifico
     *
     * @return a list containing all the attributes used in this form
     */
    protected Attribute<?, ?>[] getAttributesList() {
        if (LibSession.isDeveloper()) {
            return new Attribute[]{
                    WamCompany_.companyCode,
                    WamCompany_.organizzazione,
                    BaseCompany_.name,
                    WamCompany_.presidente,
                    BaseCompany_.contact,
                    BaseCompany_.email,
                    BaseCompany_.address1,
                    BaseCompany_.note,
            };//end of brace
        } else {
            return new Attribute[]{
                    WamCompany_.organizzazione,
                    BaseCompany_.name,
                    WamCompany_.presidente,
                    BaseCompany_.contact,
                    BaseCompany_.email,
                    BaseCompany_.address1,
                    BaseCompany_.note,
            };//end of brace
        }// end of if/else cycle
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
        layout.setMargin(true);
        layout.setSpacing(false);

        layout.addComponent(super.createComponent());
        layout.addComponent(this.creaCheckBox());
        layout.addComponent(this.creaCheckBox2());
        layout.addComponent(this.creaCheckBox3());
        layout.addComponent(this.creaCheckBox4());

        return layout;
    }// end of method

    /**
     * Crea il CheckBox aggiuntivo
     */
    private Component creaCheckBox() {
        HorizontalLayout layout = new HorizontalLayout();
        WamCompanyForm form = this;

        if (isNewRecord()) {
            box1 = new CheckBoxField("Creazione dei dati di prova per questa croce");
            box1.setValue(false);
            box1.setVisible(true);
            box1.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    usaCreaDatiProva = (boolean) event.getProperty().getValue();
                    syncCheckBox(form, event);
                }// end of inner method
            });// end of anonymous inner class
            layout.addComponent(box1);
        }// end of if cycle

        return layout;
    }// end of method

    /**
     * Crea il secondo CheckBox aggiuntivo
     */
    private Component creaCheckBox2() {
        HorizontalLayout layout = new HorizontalLayout();

        if (isNewRecord()) {
            usaCreaDatiBase = true;
            box2 = new CheckBoxField("Funzioni, servizi e volontari");
            box2.setValue(usaCreaDatiBase);
            box2.setVisible(false);
            label2 = new Label(RIENTRO);
            label2.setVisible(false);
            box2.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    usaCreaDatiBase = (boolean) event.getProperty().getValue();
                }// end of inner method
            });// end of anonymous inner class
            layout.addComponent(label2);
            layout.addComponent(box2);
        }// end of if cycle

        return layout;
    }// end of method

    /**
     * Crea il terzo CheckBox aggiuntivo
     */
    private Component creaCheckBox3() {
        HorizontalLayout layout = new HorizontalLayout();

        if (isNewRecord()) {
            usaCreaTurniVuoti = true;
            box3 = new CheckBoxField("Predisposizione dei turni");
            box3.setValue(usaCreaDatiBase);
            box3.setVisible(false);
            label3 = new Label(RIENTRO);
            label3.setVisible(false);
            box3.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    usaCreaTurniVuoti = (boolean) event.getProperty().getValue();
                }// end of inner method
            });// end of anonymous inner class
            layout.addComponent(label3);
            layout.addComponent(box3);
        }// end of if cycle

        return layout;
    }// end of method

    /**
     * Crea il terzo CheckBox aggiuntivo
     */
    private Component creaCheckBox4() {
        HorizontalLayout layout = new HorizontalLayout();

        if (isNewRecord()) {
            usaRiempiTurni = false;
            box4 = new CheckBoxField("Inserimento delle iscrizioni nei turni");
            box4.setValue(usaRiempiTurni);
            box4.setVisible(false);
            label4 = new Label(RIENTRO);
            label4.setVisible(false);
            box4.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    usaRiempiTurni = (boolean) event.getProperty().getValue();
                }// end of inner method
            });// end of anonymous inner class
            layout.addComponent(label4);
            layout.addComponent(box4);
        }// end of if cycle

        return layout;
    }// end of method

    private void syncCheckBox(WamCompanyForm form, Property.ValueChangeEvent evento) {
        if (box2 != null && box3 != null) {
            if (usaCreaDatiProva) {
                label2.setVisible(true);
                box2.setVisible(true);
                label3.setVisible(true);
                box3.setVisible(true);
                label4.setVisible(true);
                box4.setVisible(true);
            } else {
                label2.setVisible(false);
                box2.setVisible(false);
                label3.setVisible(false);
                box3.setVisible(false);
                label4.setVisible(false);
                box4.setVisible(false);
            }// fine del blocco if-else
        }// fine del blocco if
    }// end of method

    /**
     * Saves the current values to the storage.
     *
     * @return true if saved successfully
     */
    @Override
    protected boolean save() {
        return super.save();
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

        if (isNewRecord() && usaCreaDatiProva && usaCreaDatiBase) {
            BootService.initCompany(company, usaCreaTurniVuoti, usaRiempiTurni);
        }// end of if cycle

        if (isNewRecord()) {
            fireCompanyAdded(company);
        }// end of if cycle

    }// end of method


    /**
     * La company (croce) è stata creata/cambiata.
     * Spedisce un avviso a tutti i listener.
     */
    protected void fireCompanyAdded(WamCompany company) {
        UI ui = getModule().getUI();
        WamUI wamUI;
        WamCompanyMod mod;

        if (ui instanceof WamUI) {
            wamUI = (WamUI) ui;
            wamUI.fireCompanyAdded(company);
            wamUI.fireCompanyChanged(company);
        }// fine del blocco if
        CompanySessionLib.setCompany(company);
        Page.getCurrent().reload();
    }// end of method

}// end of form class
