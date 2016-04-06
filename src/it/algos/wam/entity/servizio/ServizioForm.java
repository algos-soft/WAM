package it.algos.wam.entity.servizio;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.query.WamQuery;
import it.algos.wam.tabellone.CServizioEditor;
import it.algos.wam.tabellone.CTabelloneEditor;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.form.AForm;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.module.ModulePop;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 5-04-2016.
 */
public class ServizioForm extends ModuleForm {

    private VerticalLayout layoutFunc;
    private ArrayList<EditorSF> sfEditors;


    public ServizioForm(Item item, ModulePop module) {
        super(item, module);
    }

    @Override
    protected Component createComponent() {

        sfEditors=new ArrayList();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        layout.addComponent(creaCompTitolo());
        layout.addComponent(creaCompDetail());
        //layout.addComponent(creaPanComandi());

        return layout;
    }

    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label(getServizio().getOrario()));
        layout.addComponent(new Label("<strong>" + getServizio().getDescrizione() + "</strong>", ContentMode.HTML));

        return layout;
    }


    /**
     * Crea il componente che visualizza il dettaglio
     *
     * @return il componente dettagli
     */
    private Component creaCompDetail() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        TextField fDescrizione = new TextField("Descrizione");
        fDescrizione.setWidth("20em");
        layout.addComponent(fDescrizione);

        layoutFunc = new VerticalLayout();
        layoutFunc.setCaption("Funzioni previste");
        layoutFunc.setSpacing(true);

        // aggiunge gli editor per le funzioni esistenti
        List<ServizioFunzione> listaSF = getServizio().getServizioFunzioni();
        Collections.sort(listaSF);
        for (ServizioFunzione sf : listaSF) {
            EditorSF editor = new EditorSF(sf);
            layoutFunc.addComponent(editor);
            sfEditors.add(editor);
        }
        layout.addComponent(layoutFunc);

        // aggiunge un bottone per creare nuove funzioni
        Button bNuova = new Button("Aggiungi funzione", FontAwesome.PLUS_CIRCLE);
        bNuova.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditorSF editor = new EditorSF(null);
                layoutFunc.addComponent(editor);
                sfEditors.add(editor);
            }
        });
        layout.addComponent(bNuova);


        return layout;
    }



    private Servizio getServizio(){
        return (Servizio)getEntity();
    }


    @Override
    protected boolean save() {
        boolean saved=false;

        // controlla se questo servizio è registrabile
        String err = checkServizioRegistrabile();
        if (err.isEmpty()) {
            saveServizio();
            saved=true;
        } else {
            String msg = "Impossibile registrare il servizio.\n" + err;
            Notification.show(null, msg, Notification.Type.WARNING_MESSAGE);
        }

        return saved;
    }

    @Override
    public void postCommit() {
    }



    /**
     * Controlla se questo servizio è registrabile
     * @return stringa vuota se registrabile, motivo se non registrabile
     */
    private String checkServizioRegistrabile() {
        String err="";

        // deve avere delle funzioni
        if(sfEditors.size()==0){
            if(!err.isEmpty()){err+="\n";}
            err+="Non ci sono funzioni";
        }

        // le funzioni devono essere tutte specificate
        for(EditorSF editor : sfEditors){
            if (editor.getFunzione()==null){
                if(!err.isEmpty()){err+="\n";}
                err+="Ci sono funzioni non specificate";
                break;
            }
        }

        return err;
    }


    /**
     * Registra il servizio correntemente in editing.
     * Sincronizza i ServizioFunzione esistenti: modifica quelli esistenti,
     * cancella quelli inesistenti e crea quelli nuovi.
     */
    private void saveServizio(){

        // avvia una transazione
        getEntityManager().getTransaction().begin();

        try{

            // modifica quelli esistenti e aggiunge i nuovi
            for(EditorSF editor : sfEditors){
                ServizioFunzione sf = editor.getServizioFunzione();
                if(sf==null){    // se è nuovo lo crea ora
                    sf = new ServizioFunzione(getServizio(), null);
                    //sf.setServizio(getServizio());
                }
                // aggiorna l'entity dall'editor
                sf.setFunzione(editor.getFunzione());
                sf.setObbligatoria(editor.isObbligatoria());

                // se nuovo, lo aggiunge al servizio
                if(sf.getId()==null){
                    getServizio().add(sf);
                }
            }

            // Solo se non sono nuovi:
            // cancella quelli inesistenti nell'editor (sono stati cancellati).
            // Dato che elimina elementi della stessa lista che viene iterata, esegue
            // l'iterazione partendo dal fondo
            for(int i=getServizio().getServizioFunzioni().size()-1 ; i>=0; i--){
                ServizioFunzione sf = getServizio().getServizioFunzioni().get(i);
                if(sf.getId()!=null){   // non considera quelli senza id, sono i nuovi!
                    boolean found=false;
                    for(EditorSF editor : sfEditors){
                        if(editor.getServizioFunzione()!=null){
                            if(editor.getServizioFunzione().equals(sf)){
                                found=true;
                                break;
                            }
                        }
                    }
                    if(!found){
                        getServizio().getServizioFunzioni().remove(i);
                    }
                }

            }

            // crea o aggiorna il servizio
            if(getServizio().getId()==null){
                getEntityManager().persist(getServizio());
            }else{
                getEntityManager().merge(getServizio());
            }

            // conclude la transazione
            getEntityManager().getTransaction().commit();

        }catch (Exception e){
            getEntityManager().getTransaction().rollback();
            e.printStackTrace();;
        }



    }




    /**
     * Editor di una singola funzione del servizio
     */
    private class EditorSF extends HorizontalLayout {

        private ServizioFunzione serFun;
        private CheckBox checkSel;
        private ERelatedComboField comboFunzioni;
        private CheckBox checkObbl;


        public EditorSF(ServizioFunzione serFun) {

            this.serFun = serFun;

            setSpacing(true);

            comboFunzioni = new ERelatedComboField(Funzione.class);
            comboFunzioni.setWidth("12em");
            if (serFun != null) {
                Funzione f = serFun.getFunzione();
                if (f != null) {
                    comboFunzioni.setValue(f.getId());
                }
            }


            checkObbl = new CheckBox("obbligatoria");
            // imposta il checkbox obbligatorio
            if (serFun != null) {
                checkObbl.setValue(this.serFun.isObbligatoria());
            }

            Button bElimina = new Button("Elimina", FontAwesome.TRASH_O);
            bElimina.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {

                    if (serFun != null) {
                        List<Iscrizione> iscrizioni = WamQuery.queryIscrizioniServizioFunzione(getEntityManager(), serFun);

                        if (iscrizioni.size() == 0) {

                            String messaggio = "Vuoi eliminare la funzione " + serFun.getFunzione().getDescrizione() + "?";
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


        }

        public boolean isObbligatoria() {
            return checkObbl.getValue();
        }

        /**
         * Eliminazione effettiva di questo componente e del relativo ServizioFunzione
         */
        private void doDelete() {
            Servizio s = getServizio();
            layoutFunc.removeComponent(this);
            sfEditors.remove(this);
            int a = 87;
        }

        /**
         * Ritorna la funzione correntemente selezionata nel popup
         * @return la funzione selezionata
         */
        public Funzione getFunzione() {
            Funzione f=null;
            Object obj=comboFunzioni.getSelectedBean();
            if(obj!=null && obj instanceof Funzione){
                f=(Funzione)obj;
            }
            return f;
        }

        public ServizioFunzione getServizioFunzione() {
            return serFun;
        }

        /**
         * Ritorna il ServizioFunzione aggiornato in base all'editing corrente.
         * Se il ServizioFunzione è presente lo aggiorna, altrimenti lo crea ora.
         * @return il ServizioFunzione aggiornato
         */
        public ServizioFunzione getServizioFunzioneAggiornato() {
            ServizioFunzione sf=serFun;
            if(sf==null){
                sf = new ServizioFunzione(getServizio(), null);
            }
            sf.setFunzione(getFunzione());
            sf.setObbligatoria(isObbligatoria());
            return serFun;
        }



    }

}
