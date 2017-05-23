package it.algos.wam.entity.iscrizione;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.servizio.Servizio_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione_;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.turno.Turno_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ESearchManager;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.query.SortProperty;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;

import javax.persistence.metamodel.Attribute;
import java.util.*;

/**
 * Created by gac on 17/05/17.
 * .
 */
public class IscrizioneSearch extends ESearchManager {

    /**
     * Constructor
     *
     * @param module the reference module
     */
    public IscrizioneSearch(ModulePop module) {
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
        //--devo mettere un box di scelta data/date
        return new Attribute[]{
                Servizio_.sigla,
                Funzione_.code,
                Iscrizione_.volontario,
                Turno_.inizio,
                Turno_.fine,
                Iscrizione_.esisteProblema,
                Iscrizione_.notificaInviata
        };//end of brace
    }// end of method

    /**
     * Crea un campo del tipo corrispondente all'attributo dato.
     * I campi vengono creati del tipo grafico previsto nella Entity.
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Field creaField(Attribute attr) {
        if (attr.getName().equals(Servizio_.sigla.getName())) {
            return creaPopSiglaServizio();
        } else {
            if (attr.getName().equals(Funzione_.code.getName())) {
                return creaPopCodeFunzione();
            } else {
                return super.creaField(attr);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method


    /**
     * Crea un popup dei valori unici per la property 'sigla'.
     */
    @SuppressWarnings("rawtypes")
    private Field creaPopSiglaServizio() {
        SortProperty sort = new SortProperty(Servizio_.ordine, true);
        List<String> options = CompanyQuery.getListStr(Servizio.class, Servizio_.sigla, sort);
        return new ComboBox("Servizio", options);
    }// end of method

    /**
     * Crea un popup dei valori unici per la property 'code'.
     */
    @SuppressWarnings("rawtypes")
    private Field creaPopCodeFunzione() {
        SortProperty sort = new SortProperty(Funzione_.ordine, true);
        List<String> options = CompanyQuery.getListStr(Funzione.class, Funzione_.code, sort);
        return new ComboBox("Funzione", options);
    }// end of method



    /**
     * crea un filtro per un Field
     * Usa il valore corrente del field
     *
     * @param attr l'attribute per recuperare il field dalla mappa
     * @return il filtro creato
     */
    protected Container.Filter createFilter(Attribute attr) {
        if (attr.getName().equals(Servizio_.sigla.getName())) {
            return createServizioFilter(attr);
        }// end of if cycle

        if (attr.getName().equals(Funzione_.code.getName())) {
            return createFunzioneFilter(attr);
        }// end of if cycle

        if (attr.getName().equals(Turno_.inizio.getName())) {
            return createInizioFilter(attr);
        }// end of if cycle

        if (attr.getName().equals(Turno_.fine.getName())) {
            return createFineFilter(attr);
        }// end of if cycle

        return super.createFilter(attr);
    }// end of method


    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createServizioFilter(Attribute attr) {
        Field field = getFieldMap().get(attr);
        Object value = field.getValue();
        Servizio serv = (Servizio) CompanyQuery.getEntity(Servizio.class, Servizio_.sigla, value);
        List<ServizioFunzione> listaServFunz = (List<ServizioFunzione>) CompanyQuery.getList(ServizioFunzione.class, ServizioFunzione_.servizio, serv);

        return createOrFilter(listaServFunz);
    }// end of method

    /**
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createFunzioneFilter(Attribute attr) {
        Field field = getFieldMap().get(attr);
        Object value = field.getValue();
        Funzione funz = (Funzione) CompanyQuery.getEntity(Funzione.class, Funzione_.code, value);
        List<ServizioFunzione> listaServFunz = (List<ServizioFunzione>) CompanyQuery.getList(ServizioFunzione.class, ServizioFunzione_.funzione, funz);

        return createOrFilter(listaServFunz);
    }// end of method


    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createInizioFilter(Attribute attr) {
        Container.Filter outFilter = null;
        Field field = getFieldMap().get(attr);
        Object value = field.getValue();
        Container.Filter  filter = new Compare.GreaterOrEqual(attr.getName(), value);
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter tmpFilter = null;

        List<Turno> listaTurni = (List<Turno>) CompanyQuery.getList(Turno.class, filter);

        for (Turno turno : listaTurni) {
            tmpFilter = new Compare.GreaterOrEqual(Iscrizione_.turno.getName(), turno);
            lista.add(tmpFilter);
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

    /**
     * Create a filter for String searches.
     *
     * @param attr the StaticMetamodel attribute
     */
    private Container.Filter createFineFilter(Attribute attr) {
        return null;
    }// end of method

    /**
     * Create a filter for String searches.
     */
    private Container.Filter createOrFilter(List<ServizioFunzione> listaServFunz) {
        ArrayList<Container.Filter> lista = new ArrayList<>();
        Container.Filter outFilter = null;
        Container.Filter filter = null;

        for (ServizioFunzione servFunz : listaServFunz) {
            filter = new Compare.Equal(Iscrizione_.servizioFunzione.getName(), servFunz);
            lista.add(filter);
        }// end of for cycle

        Container.Filter[] aFilters = lista.toArray(new Container.Filter[0]);
        outFilter = new Or(aFilters);

        return outFilter;
    }// end of method

}// end of class
