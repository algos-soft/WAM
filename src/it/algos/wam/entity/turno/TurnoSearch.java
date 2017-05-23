package it.algos.wam.entity.turno;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import it.algos.wam.entity.companyentity.WamSearch;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.entity.BaseEntity_;
import it.algos.webbase.web.field.ArrayComboField;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.lib.LibText;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.SortProperty;
import it.algos.webbase.web.search.SearchManager;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.*;

import static it.algos.wam.entity.iscrizione.Iscrizione_.turno;

/**
 * Created by gac on 26/04/17
 * .
 */
public class TurnoSearch extends WamSearch {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public TurnoSearch(ModulePop module) {
        super(module);
    }// end of constructor

    @Override
    protected void init() {
        super.init();

        addDateListener(Turno_.inizio.getName(), Turno_.fine.getName());
    }// end of method

    @SuppressWarnings("rawtypes")
    @Override
    protected void createFields(FormLayout layout) {
        super.createFields(layout);

        createFieldsAddizionali(layout);
    }// end of method

    private void createFieldsAddizionali(FormLayout layout) {
        Field field;

        field = creaPopVolontari();
        addField(Volontario_.cognome, field);
        layout.addComponent(field);

        field = new CheckBoxField("Assegnato", false);
        addField(Iscrizione_.tsCreazione, field);
        layout.addComponent(field);

        field = new CheckBoxField("Valido", false);
        addField(Iscrizione_.esisteProblema, field);
        layout.addComponent(field);

        field = new CheckBoxField("Completo", false);
        addField(Iscrizione_.nota, field);
        layout.addComponent(field);
    }// end of method


    /**
     * Crea un popup dei valori unici per la property 'code'.
     */
    @SuppressWarnings("rawtypes")
    private Field creaPopVolontari() {
        SortProperty sort = new SortProperty(Volontario_.cognome, true);
        List<Volontario> options = (List<Volontario>) CompanyQuery.getList(Volontario.class, sort);
        return new ArrayComboField(options, "Volontario");
    }// end of method


    protected void addDateListener(String nameFieldIniziale, String nameFieldFinale) {
        Field fieldIniziale = getField(nameFieldIniziale);
        Field fieldFinale = getField(nameFieldFinale);

        if (fieldIniziale != null && fieldFinale != null) {
            fieldIniziale.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    syncDateInizialeFinale(fieldIniziale, fieldFinale);
                }// end of inner method
            });// end of anonymous inner class

            fieldFinale.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                    syncDateInizialeFinale(fieldFinale, fieldIniziale);
                }// end of inner method
            });// end of anonymous inner class
        }// end of if cycle

    }// end of method


    /**
     * Sincronizza i due campi della data iniziale e finale
     * Se entrambe sono vuote, non fa nulla
     * Se entrambe sono valide, non fa nulla
     * Se quella da cui arriva è valida e l'altra è vuota, duplica il valore (serve per filtrare un solo giorno)
     */
    protected void syncDateInizialeFinale(Field fieldIniziale, Field fieldFinale) {
        Object valueIniziale = fieldIniziale.getValue();
        Object valueFinale = fieldFinale.getValue();

        if (valueIniziale != null && valueFinale == null) {
            fieldFinale.setValue(valueIniziale);
        }// end of if cycle

    }// end of method

    /**
     * Attributes used in this search
     * Di default prende dal modulo
     * Può essere sovrascritto se c'è un Search specifico
     *
     * @return a list containing all the attributes used in this search
     */
    @Override
    protected Attribute<?, ?>[] getAttributesList() {
        return new Attribute[]{
                Turno_.servizio,
                Turno_.inizio,
                Turno_.fine,
                Turno_.titoloExtra,
                Turno_.localitaExtra
        };//end of brace
    }// end of method


    /**
     * crea un filtro per un Field
     * Usa il valore corrente del field
     *
     * @param attr l'attribute per recuperare il field dalla mappa
     * @return il filtro creato
     */
    protected Container.Filter createFilter(Attribute attr) {
        if (attr.getName().equals(Turno_.inizio.getName())) {
            return createInizioFilter(attr);
        }// end of if cycle

        if (attr.getName().equals(Turno_.fine.getName())) {
            Attribute attrFine = getAttr(Turno_.inizio.getName());
            return createFineFilter(attrFine, (Date) getFieldValue(attr));
        }// end of if cycle

        // volontario
        if (attr.getName().equals(Volontario_.cognome.getName())) {
            return createVolontarioFilter(attr);
        }// end of if cycle

        // almeno una iscrizione
        if (attr.getName().equals(Iscrizione_.tsCreazione.getName())) {
            return createSingolaIscrizioneFilter(attr);
        }// end of if cycle

        // iscrizioni valide
        if (attr.getName().equals(Iscrizione_.esisteProblema.getName())) {
            return createIscrizioniValideFilter(attr);
        }// end of if cycle

        // tutte le iscrizioni possibili
        if (attr.getName().equals(Iscrizione_.nota.getName())) {
            return createIscrizioneFullFilter(attr);
        }// end of if cycle

        return super.createFilter(attr);
    }// end of method


    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createVolontarioFilter(Attribute attr) {
        Container.Filter outFilter = null;
        Field field = getFieldMap().get(attr);
        Volontario volontario = (Volontario) field.getValue();
        List<Iscrizione> listaIscrizioni = (List<Iscrizione>) CompanyQuery.getList(Iscrizione.class, Iscrizione_.volontario, volontario);
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter tmpFilter = null;
        Turno turno;
        Long turnoID;

        for (Iscrizione isc : listaIscrizioni) {
            turno = isc.getTurno();
            turnoID = turno.getId();
            tmpFilter = new Compare.Equal(BaseEntity_.id.getName(), turnoID);
            lista.add(tmpFilter);
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createSingolaIscrizioneFilter(Attribute attr) {
        Container.Filter outFilter = null;
        List<Turno> listaTurni = (List<Turno>) CompanyQuery.getList(Turno.class);
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter tmpFilter = null;
        Long turnoID;

        for (Turno turno : listaTurni) {
            if (turno.isAssegnato()) {
                turnoID = turno.getId();
                tmpFilter = new Compare.Equal(BaseEntity_.id.getName(), turnoID);
                lista.add(tmpFilter);
            }// end of if cycle
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createIscrizioniValideFilter(Attribute attr) {
        Container.Filter outFilter = null;
        List<Turno> listaTurni = (List<Turno>) CompanyQuery.getList(Turno.class);
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter tmpFilter = null;
        Long turnoID;

        for (Turno turno : listaTurni) {
            if (turno.isValido()) {
                turnoID = turno.getId();
                tmpFilter = new Compare.Equal(BaseEntity_.id.getName(), turnoID);
                lista.add(tmpFilter);
            }// end of if cycle
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createIscrizioneFullFilter(Attribute attr) {
        Container.Filter outFilter = null;
        List<Turno> listaTurni = (List<Turno>) CompanyQuery.getList(Turno.class);
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter tmpFilter = null;
        Long turnoID;

        for (Turno turno : listaTurni) {
            if (turno.isCompleto()) {
                turnoID = turno.getId();
                tmpFilter = new Compare.Equal(BaseEntity_.id.getName(), turnoID);
                lista.add(tmpFilter);
            }// end of if cycle
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

}// end of class
