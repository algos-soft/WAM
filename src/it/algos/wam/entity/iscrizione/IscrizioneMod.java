package it.algos.wam.entity.iscrizione;

import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;

import javax.persistence.metamodel.Attribute;

/**
 * Created by gac on 17/02/17.
 * Gestione (minimale) del modulo specifico
 */
public class IscrizioneMod extends WamMod {

    /**
     * Costruttore senza parametri
     * <p/>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public IscrizioneMod() {
        super(Iscrizione.class);
    }// end of constructor




}// end of class
