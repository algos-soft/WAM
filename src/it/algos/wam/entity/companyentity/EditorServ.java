package it.algos.wam.entity.companyentity;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.query.WamQuery;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.RelatedComboField;

import java.util.List;

/**
 * Created by gac on 05 nov 2016.
 * Editor di una singola funzione del servizio
 */
public class EditorServ extends HorizontalLayout {

    private Servizio servizio;
    private ServizioFunzione serFun;
    private CheckBox checkSel;
    private ERelatedComboField comboFunzioni;
    private CheckBox checkObbl;
    private Button iconButton;
    private FunzioneListener formChiamante;

    public EditorServ(FunzioneListener formChiamante, Servizio servizio) {
        this(formChiamante, servizio, (ServizioFunzione) null);
    }// end of constructor

    public EditorServ(FunzioneListener formChiamante, Servizio servizio, ServizioFunzione serFun) {
        this.formChiamante = formChiamante;
        this.servizio = servizio;

        setSpacing(true);

        //@todo aggiunta gac
        if (serFun == null) {
            serFun = new ServizioFunzione(servizio, null);
        }
        this.serFun = serFun;
        final ServizioFunzione serFunzFinal = serFun;
        //@todo aggiunta gac
        if (true) {
            iconButton = new Button();
            iconButton.setHtmlContentAllowed(true);
            iconButton.addStyleName("bfunzione");
            iconButton.setWidth("3em");
            if (serFun != null) {
                syncIconaColor(serFun.isObbligatoria());
            }
//                if (serFun.isObbligatoria()) {
//                    iconButton.setStyleName("rosso");
//                } else {
//                    iconButton.addStyleName("verde");
//                }// end of if/else cycle

            addComponent(iconButton);
            if (serFun != null) {
                Funzione funz = serFun.getFunzione();
                setIconButton(funz);
            }
        }// end of if cycle


        // combo di selezione della funzione
//        BaseCompany company = servizio.getCompany();
//        if (company == null) {
//            company = WamCompany.find((long) fCompanyCombo.getValue());
//        }// end of if cycle
//        comboFunzioni = new ERelatedComboField(Funzione.class, company);
        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());

        comboFunzioni.sort(Funzione_.sigla);
        comboFunzioni.setWidth("25em");
        if (serFun != null) {
            Funzione f = serFun.getFunzione();
            if (f != null) {
                comboFunzioni.setValue(f.getId());
            }// end of if cycle
        }// end of if cycle
        comboFunzioni.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                Object obj = event.getSource();
                Object value;
                RelatedComboField combo;
                Funzione funz;
                if (obj instanceof RelatedComboField) {
                    combo = (RelatedComboField) obj;
                    value = combo.getValue();
                    if (value instanceof Long) {
                        funz = Funzione.find((Long) value);
                        setIconButton(funz);
                        serFunzFinal.setFunzione(funz);
                    }// end of if cycle
                }// end of if cycle
            }// end of inner method
        });// end of anonymous inner class

        checkObbl = new CheckBox("obb.");
        // imposta il checkbox obbligatorio
        if (serFun != null) {
            checkObbl.setValue(this.serFun.isObbligatoria());
        }
        checkObbl.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncIconaColor((boolean) valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class

        Button bElimina = new Button("", FontAwesome.TRASH_O);
        final ServizioFunzione servFunzFinal = serFun;
        bElimina.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if (servFunzFinal != null) {
                    List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniServizioFunzione(null, servFunzFinal);

                    if (iscrizioni.size() == 0) {

                        String messaggio = "Vuoi eliminare la funzione " + servFunzFinal.getFunzione().getSigla() + "?";
                        new ConfirmDialog(null, messaggio, new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog, boolean confirmed) {
                                if (confirmed) {
                                    doDelete();// elimino componente e relativo ServizioFunzione
                                }
                            }
                        }).show();

                    } else {
                        Notification.show(null, "Questa funzione ha già delle iscrizioni, non si può cancellare", Notification.Type.WARNING_MESSAGE);
                    }

                } else {  // ServizioFunzione null, procedo alla eliminazione del componente
                    doDelete();
                }

            }
        });


        addComponent(comboFunzioni);
        addComponent(checkObbl);
        addComponent(bElimina);
        setComponentAlignment(comboFunzioni, Alignment.MIDDLE_LEFT);
        setComponentAlignment(checkObbl, Alignment.MIDDLE_LEFT);
        setComponentAlignment(bElimina, Alignment.MIDDLE_LEFT);

    }// end of constructor

    public boolean isObbligatoria() {
        return checkObbl.getValue();
    }

    public void syncIconaColor(boolean obbligatoria) {
        if (iconButton != null) {
            if (obbligatoria) {
                iconButton.setStyleName("rosso");
            } else {
                iconButton.setStyleName("verde");
            }// end of if/else cycle
        }// end of if cycle
    }


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
        return serFun;
    }

    /**
     * Ritorna il ServizioFunzione aggiornato in base all'editing corrente.
     * Se il ServizioFunzione è presente lo aggiorna, altrimenti lo crea ora.
     *
     * @return il ServizioFunzione aggiornato
     */
    public ServizioFunzione getServizioFunzioneAggiornato() {
        ServizioFunzione sf = serFun;
        if (sf == null) {
            sf = new ServizioFunzione(servizio, null);
        }
        sf.setFunzione(getFunzione());
        sf.setObbligatoria(isObbligatoria());
        return serFun;
    }


    /**
     * Assegna un'icona al bottone
     */
    private void setIconButton(Funzione funz) {
        if (funz != null) {
            iconButton.setCaption(funz.getIconHtml());
        } else {
            iconButton.setCaption("");
        }// end of if/else cycle
    }// end of inner method

    /**
     * Eliminazione effettiva di questo componente
     * La relativa Funzione, deve essere regolata in FunzioneForm
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    public void doDelete() {
        formChiamante.doDeleteServ(this);
    }// end of method

}// end of class

