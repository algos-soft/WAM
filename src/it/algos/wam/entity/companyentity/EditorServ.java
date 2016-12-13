package it.algos.wam.entity.companyentity;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.lib.LibWam;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;

import java.util.List;

/**
 * Created by gac on 05 nov 2016.
 * Editor di una singola funzione del servizio
 */
public class EditorServ extends EditorWam {

    private Servizio servizio;
    private ServizioFunzione servFunz;


    public EditorServ(ServFunzListener formChiamante, Servizio servizio) {
        this(formChiamante, servizio, (ServizioFunzione) null);
    }// end of constructor


    public EditorServ(ServFunzListener formChiamante, Servizio servizio, ServizioFunzione servFunz) {
        super(formChiamante);
        this.servizio = servizio;

        if (servFunz == null) {
            servFunz = new ServizioFunzione(servizio, null);
        }// end of if cycle

        this.servFunz = servFunz;
        this.init();
    }// end of constructor


    protected void init() {
        //--crea prima tutti i componenti
        super.init();

        //--assembla i vari elementi grafici
        addComponent(bIcona);
        addComponent(comboFunzioni);
        addComponent(checkObbligatorio);
        addComponent(bElimina);

        setComponentAlignment(comboFunzioni, Alignment.MIDDLE_LEFT);
        setComponentAlignment(checkObbligatorio, Alignment.MIDDLE_LEFT);
        setComponentAlignment(bElimina, Alignment.MIDDLE_LEFT);
    }// end of method


    protected void creaComponenti() {
        super.creaComponenti();
        this.creaCheckbox();
    }// end of method

    /**
     * Crea il bottone per la selezione dell'icona
     */
    protected void creaBottoneIcona() {
        super.creaBottoneIcona();
        setIconButton(servFunz);
        if (servFunz != null) {
            syncIconaColor(servFunz.isObbligatoria());
        }// end of if cycle
    }// end of method

    /**
     * Crea il combo di selezione della funzione
     * Elimina la funzione madre nel comboBox delle funzioni dipendenti
     */
    protected void creaComboBox() {
        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());

        comboFunzioni.sort(Funzione_.sigla);
        comboFunzioni.setWidth("25em");
        comboFunzioni.setDescription("Funzione associate a questo servizio");
        comboFunzioni.setNullSelectionAllowed(false);

        if (servFunz != null && servFunz.getFunzione() != null) {
            comboFunzioni.setValue(servFunz.getFunzione().getId());
        }// end of if cycle

        comboFunzioni.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                servFunz.setFunzione(LibWam.getFunzione(event));
                setIconButton(LibWam.getServizioFunzione(event));
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Crea il checkbox per la obbligatorietà della funzione
     * Opzionale, usato solo da ServizioForm e non da FunzioneForm
     */
    private void creaCheckbox() {
        checkObbligatorio = new CheckBox("obb.");
        if (servFunz != null && servFunz.getFunzione() != null) {
            checkObbligatorio.setValue(servFunz.isObbligatoria());
        }// end of if cycle

        checkObbligatorio.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncIconaColor((boolean) valueChangeEvent.getProperty().getValue());
                syncObbligatoria((boolean) valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    /**
     * Crea il bottone per eliminare la funzione
     */
    protected void creaBottoneElimina() {
        super.creaBottoneElimina();

        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (servFunz != null) {
                    List<Iscrizione> iscrizioni= (List<Iscrizione>)CompanyQuery.getList(Iscrizione.class, Iscrizione_.servizioFunzione,servFunz);
                    if (iscrizioni.size() == 0) {
                        String messaggio = "Vuoi eliminare la funzione " + servFunz.getFunzione().getCode() + "?";
                        new ConfirmDialog(null, messaggio, new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog, boolean confirmed) {
                                if (confirmed) {
                                    doDelete();// elimino componente e relativo ServizioFunzione
                                }// end of if cycle
                            }// end of inner inner method
                        }).show(); // end of anonymous inner class
                    } else {
                        Notification.show(null, "Questa funzione ha già delle iscrizioni, non si può cancellare", Notification.Type.WARNING_MESSAGE);
                    }// end of if/else cycle
                } else {  // ServizioFunzione null, procedo alla eliminazione del componente
                    doDelete();
                }// end of if/else cycle
            }// end of inner method
        });// end of anonymous inner class
    }// end of method


    public boolean isObbligatoria() {
        return checkObbligatorio.getValue();
    }

    private void syncIconaColor(boolean obbligatoria) {
        if (bIcona != null) {
            if (obbligatoria) {
                bIcona.setStyleName("rosso");
            } else {
                bIcona.setStyleName("verde");
            }// end of if/else cycle
        }// end of if cycle
    }// end of method

    private void syncObbligatoria(boolean obbligatoria) {
        if (servFunz != null) {
            servFunz.setObbligatoria(obbligatoria);
        }// end of if cycle
    }// end of method


    /**
     * Ritorna la funzione correntemente selezionata nel popup
     *
     * @return la funzione selezionata
     */
    public Funzione getFunzione() {
        Funzione f = null;
        Object obj = comboFunzioni.getSelectedBean();
        if (obj != null && obj instanceof Funzione) {
            f = (Funzione) obj;
        }
        return f;
    }

    public ServizioFunzione getServizioFunzione() {
        return servFunz;
    }

    /**
     * Ritorna il ServizioFunzione aggiornato in base all'editing corrente.
     * Se il ServizioFunzione è presente lo aggiorna, altrimenti lo crea ora.
     *
     * @return il ServizioFunzione aggiornato
     */
    public ServizioFunzione getServizioFunzioneAggiornato() {
        ServizioFunzione sf = servFunz;
        if (sf == null) {
            sf = new ServizioFunzione(servizio, null);
        }
        sf.setFunzione(getFunzione());
        sf.setObbligatoria(isObbligatoria());
        return servFunz;
    }


    /**
     * Eliminazione effettiva di questo componente
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    protected void doDelete() {
        ((ServFunzListener) formChiamante).doDelete(this);
    }// end of method

    /**
     * Assegna un'icona al bottone
     */
    private void setIconButton(ServizioFunzione serFunz) {
        if (serFunz != null && serFunz.getFunzione() != null) {
            bIcona.setCaption(serFunz.getFunzione().getIconHtml());
        } else {
            bIcona.setCaption("");
        }// end of if/else cycle
    }// end of method


}// end of class

