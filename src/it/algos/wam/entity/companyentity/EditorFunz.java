package it.algos.wam.entity.companyentity;

import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.entity.BaseEntity;

import java.util.List;

/**
 * Created by gac on 19 ott 2016.
 * Editor di una singola funzione dipendente della funzione
 * Layout orizzontale con:
 * - icona
 * - popup delle funzioni della company
 * - bottone di cancellazione
 */
public class EditorFunz extends HorizontalLayout {

    private Funzione funzione;
    private Button bIcona;
    private ERelatedComboField comboFunzioni;
    private CheckBox checkObbligatorio;
    private Button bElimina;
    private boolean usaFieldObbligatorio;
    private FunzioneListener formChiamante;


    public EditorFunz(FunzioneListener formChiamante, Funzione funzione, boolean usaFieldObbligatorio) {
        this.formChiamante = formChiamante;
        this.funzione = funzione;
        this.usaFieldObbligatorio = usaFieldObbligatorio;
        this.init();
    }// end of constructor

    protected void init() {
        setSpacing(true);

        //--crea prima tutti i componenti
        creaComponenti();

        //--assembla i vari elementi grafici
        addComponent(bIcona);
        addComponent(comboFunzioni);
        if (usaFieldObbligatorio) {
            addComponent(checkObbligatorio);
        }// end of if cycle
        addComponent(bElimina);

    }// end of method

    protected void creaComponenti() {
        this.creaBottoneIcona();
        this.creaComboBox();
        this.creaCheckbox();
        this.creaBottoneElimina();
    }// end of method

    /**
     * Crea il bottone per la selezione dell'icona
     */
    private void creaBottoneIcona() {
        bIcona = new Button();
        bIcona.setHtmlContentAllowed(true);
        bIcona.addStyleName("blue");
        bIcona.setWidth("3em");
        bIcona.setDescription("Icona grafica rappresentativa della funzione");
        setbIcona(funzione);

    }// end of method

    /**
     * Crea il combo di selezione della funzione
     * Elimina la funzione madre nel comboBox delle funzioni dipendenti
     */
    private void creaComboBox() {
        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());

        comboFunzioni.sort(Funzione_.sigla);
        comboFunzioni.setWidth("25em");
        comboFunzioni.setDescription("Funzione dipendente che viene abilitata automaticamente per il volontario");

        if (funzione != null) {
            comboFunzioni.setValue(funzione.getId());
        }// end of if cycle

        comboFunzioni.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                setbIcona(LibWam.getFunzione(event));
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea il checkbox per la obbligatorietà della funzione
     * Opzionale, usato solo da ServizioForm e non da FunzioneForm
     */
    private void creaCheckbox() {
        checkObbligatorio = new CheckBox("obb.");
        checkObbligatorio.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncIconaColor((boolean) valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

    /**
     * Crea il bottone per eliminare la funzione
     */
    private void creaBottoneElimina() {
        bElimina = new Button("", FontAwesome.TRASH_O);
        bElimina.setDescription("Elimina la funzione dipendente");

        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                doDelete();
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    public void syncIconaColor(boolean obbligatoria) {
        if (bIcona != null) {
            if (obbligatoria) {
                bIcona.setStyleName("rosso");
            } else {
                bIcona.setStyleName("verde");
            }// end of if/else cycle
        }// end of if cycle
    }// end of method

    /**
     * Eliminazione effettiva di questo componente
     * La relativa Funzione, deve essere regolata in FunzioneForm
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    private void doDelete() {
        formChiamante.doDeleteFunz(this);
    }// end of method


    /**
     * Assegna un'icona al bottone
     */
    private void setbIcona(Funzione funz) {
        if (funz != null) {
            bIcona.setCaption(funz.getIconHtml());
        } else {
            bIcona.setCaption("");
        }// end of if/else cycle
    }// end of method


    /**
     * Ritorna la funzione correntemente selezionata nel popup
     *
     * @return la funzione selezionata
     */
    public Funzione getFunzione() {
        Object obj;
        Funzione funz = null;

        obj = comboFunzioni.getSelectedBean();
        if (obj != null && obj instanceof Funzione) {
            funz = (Funzione) obj;
        }// end of if cycle

        return funz;
    }// end of method

}// end of class
