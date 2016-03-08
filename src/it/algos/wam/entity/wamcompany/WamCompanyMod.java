package it.algos.wam.entity.wamcompany;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.webbase.domain.company.BaseCompanyForm;
import it.algos.webbase.domain.company.BaseCompanyModule;
import it.algos.webbase.domain.company.BaseCompany_;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.metamodel.Attribute;

/**
 * Company module
 */
public class WamCompanyMod extends ModulePop {


    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Croce";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public WamCompanyMod() {
        super(WamCompany.class, MENU_ADDRESS, FontAwesome.BARS);
    }// end of constructor


    /**
     * Crea i campi visibili nella scheda (form)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsForm() {
        return new Attribute[]{
                BaseCompany_.companyCode,
                BaseCompany_.name,
                BaseCompany_.contact,
                BaseCompany_.email,
                WamCompany_.vaiSubitoTabellone
        };
    }// end of method

}// end of class

