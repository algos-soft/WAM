package it.algos.wam.entity.funzione;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * Created by gac on 09/05/17.
 * .
 */
public class FunzioneSearch extends SearchManager {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public FunzioneSearch(ModulePop module) {
        super(module);
    }// end of constructor

    /**
     * Attributes used in this search
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Search specifico
     *
     * @return a list containing all the attributes used in this search
     */
    @Override
    protected Attribute<?, ?>[] getAttributesList() {
        return LibWam.addCompanyField(
                Funzione_.code,
                Funzione_.sigla,
                Funzione_.descrizione
        );//end of bracket
    }// end of method

    /**
     * Crea un campo del tipo corrispondente all'attributo dato.
     * I campi vengono creati del tipo grafico previsto nella Entity.
     */
    @SuppressWarnings("rawtypes")
    protected Field creaField(Attribute attr) {
        if (attr.getName().equals(Funzione_.sigla.getName())) {
            return creaPopSigla();
        } else {
            return super.creaField(attr);
        }// end of if/else cycle
    }// end of method


    /**
     * Crea un popup dei valori unici per la property 'sigla'.
     */
    @SuppressWarnings("rawtypes")
    private Field creaPopSigla() {
        List<String> options = CompanyQuery.getListStr(Funzione.class, Funzione_.sigla);
        return new ComboBox(LibText.primaMaiuscola(Funzione_.sigla.getName()), options);
    }// end of method

}// end of class
