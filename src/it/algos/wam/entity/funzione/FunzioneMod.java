package it.algos.wam.entity.funzione;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.companyentity.WamModSposta;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.lib.LibArray;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import javax.persistence.metamodel.Attribute;
import java.util.Comparator;
import java.util.List;

/**
 * Gestione (minimale) del modulo specifico
 */
public class FunzioneMod extends WamModSposta {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Funzioni";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.CHECK_SQUARE_O;


    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public FunzioneMod() {
        super(Funzione.class, MENU_ADDRESS, ICON);
    }// end of constructor


    /**
     * Returns the form used to edit an item. <br>
     * The concrete subclass must override for a specific Form.
     *
     * @param item singola istanza della classe
     * @return the Form
     */
    @Override
    public ModuleForm createForm(Item item) {
        return new FunzioneForm(item, this);
    }// end of method


    /**
     * Crea una Table già filtrata sulla company corrente
     * The concrete subclass must override for a specific Table.
     *
     * @return the Table
     */
    @Override
    public ATable createTable() {
        return new FunzioneTable(this);
    }// end of method


    @Override
    public TablePortal createTablePortal() {
        TablePortal portaleFunzione = new FunzioneTablePortal(this);
        portaleFunzione.delCmd(TableToolbar.CMD_SEARCH);

        return portaleFunzione;
    }// end of method


    /**
     * Crea i campi visibili nella scheda (search)
     * <p>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella scheda <br>
     */
    @Override
    protected Attribute<?, ?>[] creaFieldsSearch() {
        return super.addCompanyField(
                Funzione_.sigla,
                Funzione_.descrizione);
    }// end of method


    /**
     * Override di delete per controllare che non ci siano
     * servizi associati alla/e funzione/i da cancellare
     */
    @Override
    public void delete() {
        boolean cancella = true;

        for (Object id : getTable().getSelectedIds()) {
            if (getFunz(id) != null && LibArray.isValido(getFunz(id).getServizioFunzioni())) {
                cancella = false;
                break;
            }// end of if cycle
        }// end of for cycle

        if (cancella) {
            super.delete();
        } else {
            if (getTable().getSelectedIds().length == 1) {
                Notification.show("Impossibile cancellare la funzione selezionata perché è già utilizzata in alcuni servizi.", Notification.Type.WARNING_MESSAGE);
            } else {
                Notification.show("Impossibile cancellare le funzioni selezionate perché sono già utilizzate in alcuni servizi.", Notification.Type.WARNING_MESSAGE);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

    /**
     * Ricalcola il valore del parametro 'ordine''
     */
    @Override
    protected void fixOrdine() {
        List<Funzione> lista = null;
        Funzione funz;

        WamCompany company = (WamCompany) CompanySessionLib.getCompany();
        if (company != null) {
            lista = Funzione.getListByCompany(company);

            lista.sort(new Comparator<Funzione>() {
                @Override
                public int compare(Funzione o1, Funzione o2) {
                    Integer ordQuesto = o1.getOrdine();
                    Integer ordAltro = o2.getOrdine();
                    return ordQuesto.compareTo(ordAltro);
                }// end of inner method
            });// end of anonymous inner class

            for (int k = 0; k < lista.size(); k++) {
                funz = lista.get(k);
                funz.setOrdine(k + 1);
                funz.save();
            }// end of for cycle
        }// end of if cycle

        getTable().refresh();
    }// end of method

    private Funzione getFunz(Object id) {
        Funzione funzione = null;
        BaseEntity entity = getEntityManager().find(Funzione.class, id);

        if (entity != null) {
            funzione = (Funzione) entity;
        }// end of if cycle

        return funzione;
    }// end of method

}// end of class

