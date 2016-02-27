package it.algos.wam.entity.milite;


import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.wamcompany.WamCompany_;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.TablePortal;

import javax.persistence.metamodel.Attribute;

/**
 * Modulo del Milite
 */
@SuppressWarnings("serial")
public class MiliteMod extends ModulePop {

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public MiliteMod() {
        super(Milite.class, FontAwesome.GEAR);
    }// end of constructor

    /**
     * Crea i campi visibili
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * NON garantisce l'ordine con cui vengono presentati i campi nella scheda <br>
     * Può mostrare anche il campo ID, oppure no <br>
     * Se si vuole differenziare tra Table, Form e Search, <br>
     * sovrascrivere creaFieldsList, creaFieldsForm e creaFieldsSearch <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{WamCompany_.company, Milite_.nome, Milite_.cognome, Milite_.dataNascita, Milite_.telefonoCellulare};
    }// end of method


    /**
     * Create the Table Portal
     *
     * @return the TablePortal
     */
    @Override
    public TablePortal createTablePortal() {
        return new MiliteTablePortal(this);
    }// end of method

}// end of class

