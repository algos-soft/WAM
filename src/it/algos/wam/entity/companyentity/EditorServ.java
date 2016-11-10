package it.algos.wam.entity.companyentity;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.funzione.Funzione_;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.multiazienda.ERelatedComboField;

/**
 * Created by gac on 05 nov 2016.
 * Editor di una singola funzione del servizio
 */
public class EditorServ extends EditorWam {

    private Servizio servizio;
    private ServizioFunzione serFun;
//    private Button bIcona;
//    private CheckBox checkSel;
//    private ERelatedComboField comboFunzioni;
//    private CheckBox checkObbligatorio;
//    private Button bElimina;
//    private FunzioneListener formChiamante;


    public EditorServ(FunzioneListener formChiamante, Servizio servizio) {
        this(formChiamante, servizio, (ServizioFunzione) null);
    }// end of constructor


    public EditorServ(FunzioneListener formChiamante, Servizio servizio, ServizioFunzione serFun) {
        super(formChiamante);
        this.servizio = servizio;
        this.serFun = serFun;
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
        setIconButton(serFun);
        if (serFun != null) {
            syncIconaColor(serFun.isObbligatoria());
        }// end of if cycle
    }// end of method

//    /**
//     * Crea il combo di selezione della funzione
//     */
//    private void creaComboBox() {
//        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());
//
//        comboFunzioni.sort(Funzione_.sigla);
//        comboFunzioni.setWidth("25em");
//        comboFunzioni.setDescription("Funzione dipendente che viene abilitata automaticamente per il volontario");
//        comboFunzioni.setNullSelectionAllowed(false);
//
//        // escludi la funzione corrente
//        Container.Filter filter = new Compare.Equal(Funzione_.id.getName(), formChiamante.getFunzione().getId());
//        comboFunzioni.getFilterableContainer().addContainerFilter(new Not(filter));
//
//        if (funzione != null) {
//            comboFunzioni.setValue(funzione.getId());
//        }// end of if cycle
//
//        comboFunzioni.addListener(new Listener() {
//            @Override
//            public void componentEvent(Event event) {
//                setbIcona(LibWam.getFunzione(event));
//            }// end of inner method
//        });// end of anonymous inner class
//    }// end of method

    /**
     * Crea il combo di selezione della funzione
     * Elimina la funzione madre nel comboBox delle funzioni dipendenti
     */
    protected void creaComboBox() {
        final ServizioFunzione serFunzFinal = serFun;
        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());

        comboFunzioni.sort(Funzione_.sigla);
        comboFunzioni.setWidth("25em");
        comboFunzioni.setDescription("Funzione associate a questo servizio");
        comboFunzioni.setNullSelectionAllowed(false);

        if (serFun != null) {
            comboFunzioni.setValue(serFun.getFunzione().getId());
        }// end of if cycle

        comboFunzioni.addListener(new Listener() {
            @Override
            public void componentEvent(Event event) {
                serFunzFinal.setFunzione(LibWam.getFunzione(event));
                setIconButton(LibWam.getServizioFunzione(event));
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

//    /**
//     * Crea il bottone per la selezione dell'icona
//     */
//    private void creaBottoneIcona() {
//        bIcona = new Button();
//        bIcona.setHtmlContentAllowed(true);
//        bIcona.addStyleName("bfunzione");
//        bIcona.setWidth("3em");
//        bIcona.setDescription("Icona grafica rappresentativa della funzione");
//
//        if (serFun != null) {
//            syncIconaColor(serFun.isObbligatoria());
//        }// end of if cycle
////        setbIcona(funzione);
//    }// end of method

//    /**
//     * Crea il bottone per eliminare la funzione
//     */
//    private void creaBottoneElimina() {
//        bElimina = new Button("", FontAwesome.TRASH_O);
//        bElimina.setDescription("Elimina la funzione dipendente");
//
//        bElimina.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                doDelete();
//            }// end of inner method
//        });// end of anonymous inner class
//    }// end of method


    /**
     * Crea il checkbox per la obbligatorietà della funzione
     * Opzionale, usato solo da ServizioForm e non da FunzioneForm
     */
    private void creaCheckbox() {
        checkObbligatorio = new CheckBox("obb.");
        if (serFun != null) {
            checkObbligatorio.setValue(serFun.isObbligatoria());
        }// end of if cycle

        checkObbligatorio.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                syncIconaColor((boolean) valueChangeEvent.getProperty().getValue());
                syncObbligatoria((boolean) valueChangeEvent.getProperty().getValue());
            }// end of inner method
        });// end of anonymous inner class
    }// end of method

//    public EditorServ(FunzioneListener formChiamante, Servizio servizio, ServizioFunzione serFun) {
//        this.formChiamante = formChiamante;
//        this.servizio = servizio;
//
//        setSpacing(true);
//
//        //@todo aggiunta gac
//        if (serFun == null) {
//            serFun = new ServizioFunzione(servizio, null);
//        }
//        this.serFun = serFun;
//        final ServizioFunzione serFunzFinal = serFun;
//        //@todo aggiunta gac
//        if (true) {
//            iconButton = new Button();
//            iconButton.setHtmlContentAllowed(true);
//            iconButton.addStyleName("bfunzione");
//            iconButton.setWidth("3em");
//            if (serFun != null) {
//                syncIconaColor(serFun.isObbligatoria());
//            }
////                if (serFun.isObbligatoria()) {
////                    iconButton.setStyleName("rosso");
////                } else {
////                    iconButton.addStyleName("verde");
////                }// end of if/else cycle
//
//            addComponent(iconButton);
//            if (serFun != null) {
//                Funzione funz = serFun.getFunzione();
//                setIconButton(funz);
//            }
//        }// end of if cycle
//
//
//        // combo di selezione della funzione
////        BaseCompany company = servizio.getCompany();
////        if (company == null) {
////            company = WamCompany.find((long) fCompanyCombo.getValue());
////        }// end of if cycle
////        comboFunzioni = new ERelatedComboField(Funzione.class, company);
//        comboFunzioni = new ERelatedComboField(Funzione.class, formChiamante.getCompany());
//
//        comboFunzioni.sort(Funzione_.sigla);
//        comboFunzioni.setWidth("25em");
//        if (serFun != null) {
//            Funzione f = serFun.getFunzione();
//            if (f != null) {
//                comboFunzioni.setValue(f.getId());
//            }// end of if cycle
//        }// end of if cycle
//        comboFunzioni.addListener(new Listener() {
//            @Override
//            public void componentEvent(Event event) {
//                Object obj = event.getSource();
//                Object value;
//                RelatedComboField combo;
//                Funzione funz;
//                if (obj instanceof RelatedComboField) {
//                    combo = (RelatedComboField) obj;
//                    value = combo.getValue();
//                    if (value instanceof Long) {
//                        funz = Funzione.find((Long) value);
//                        setIconButton(funz);
//                        serFunzFinal.setFunzione(funz);
//                    }// end of if cycle
//                }// end of if cycle
//            }// end of inner method
//        });// end of anonymous inner class
//
//        checkObbl = new CheckBox("obb.");
//        // imposta il checkbox obbligatorio
//        if (serFun != null) {
//            checkObbl.setValue(this.serFun.isObbligatoria());
//        }
//        checkObbl.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
//                syncIconaColor((boolean) valueChangeEvent.getProperty().getValue());
//            }// end of inner method
//        });// end of anonymous inner class
//
//        Button bElimina = new Button("", FontAwesome.TRASH_O);
//        final ServizioFunzione servFunzFinal = serFun;
//        bElimina.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//
//                if (servFunzFinal != null) {
//                    List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniServizioFunzione(null, servFunzFinal);
//
//                    if (iscrizioni.size() == 0) {
//
//                        String messaggio = "Vuoi eliminare la funzione " + servFunzFinal.getFunzione().getSigla() + "?";
//                        new ConfirmDialog(null, messaggio, new ConfirmDialog.Listener() {
//                            @Override
//                            public void onClose(ConfirmDialog dialog, boolean confirmed) {
//                                if (confirmed) {
//                                    doDelete();// elimino componente e relativo ServizioFunzione
//                                }
//                            }
//                        }).show();
//
//                    } else {
//                        Notification.show(null, "Questa funzione ha già delle iscrizioni, non si può cancellare", Notification.Type.WARNING_MESSAGE);
//                    }
//
//                } else {  // ServizioFunzione null, procedo alla eliminazione del componente
//                    doDelete();
//                }
//
//            }
//        });
//
//
//        addComponent(comboFunzioni);
//        addComponent(checkObbl);
//        addComponent(bElimina);
//        setComponentAlignment(comboFunzioni, Alignment.MIDDLE_LEFT);
//        setComponentAlignment(checkObbl, Alignment.MIDDLE_LEFT);
//        setComponentAlignment(bElimina, Alignment.MIDDLE_LEFT);
//
//    }// end of constructor

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
        if (serFun != null) {
            serFun.setObbligatoria(obbligatoria);
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
     * Eliminazione effettiva di questo componente
     * La relativa Funzione, deve essere regolata in FunzioneForm
     * La relativa ServizioFunzione, deve essere regolata in ServizioForm
     */
    public void doDelete() {
        formChiamante.doDeleteServ(this);
    }// end of method

    /**
     * Assegna un'icona al bottone
     */
    private void setIconButton(ServizioFunzione serFunz) {
        if (serFunz != null) {
            bIcona.setCaption(serFunz.getFunzione().getIconHtml());
        } else {
            bIcona.setCaption("");
        }// end of if/else cycle
    }// end of method


}// end of class

