package it.algos.wam.entity.servizio;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import it.algos.wam.entity.companyentity.WamModSposta;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

import java.util.Comparator;
import java.util.List;

/**
 * Modulo del servizio (fisso o variabile) previsto o effettuato
 */
public class ServizioMod extends WamModSposta {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Servizi";

    // icona (eventuale) del modulo
    public static Resource ICON = FontAwesome.TASKS;

    // etichetta del campo - usata sia nella lista che nel form
    public static String LABEL_COLOR = "Gruppo";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public ServizioMod() {
        super(Servizio.class, MENU_ADDRESS, ICON);
    }// end of constructor


    @Override
    public ModuleForm createForm(Item item) {
        return new ServizioForm(item, this);
    }// end of method


    @Override
    public ATable createTable() {
        return new ServizioTable(this);
    }// end of method


    @Override
    public TablePortal createTablePortal() {
        TablePortal portaleServizio = new ServizioTablePortal(this);
        portaleServizio.delCmd(TableToolbar.CMD_SEARCH);

        return portaleServizio;
    }// end of method


    @Override
    protected boolean preDelete(BaseEntity bEntity) {
        return super.preDelete(bEntity);
    }


    /**
     * Override di delete per controllare che non ci siano
     * iscrizioni associate ai servizi da cancellare
     */
    @Override
    public void delete() {
        boolean cancella = true;
        Servizio serv = null;
        List<ServizioFunzione> servFunzLista = null;
        int num = 0;

        for (Object id : getTable().getSelectedIds()) {
            serv = getServ(id);
            if (serv != null) {
                servFunzLista = serv.getServizioFunzioniOrdine();
                for (ServizioFunzione servFunz : servFunzLista) {
                    num = AQuery.count(Iscrizione.class, Iscrizione_.servizioFunzione, servFunz);
                    if (num > 0) {
                        cancella = false;
                        break;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        if (cancella) {
            super.delete();
        } else {
            if (getTable().getSelectedIds().length == 1) {
                Notification.show("Impossibile cancellare il servizio selezionato perché alcuni volontari lo hanno già effettuato.", Notification.Type.WARNING_MESSAGE);
            } else {
                Notification.show("Impossibile cancellare i servizi selezionati perché alcuni volontari li hanno già effettuati.", Notification.Type.WARNING_MESSAGE);
            }// end of if/else cycle
        }// end of if/else cycle
    }// end of method

    /**
     * Ricalcola il valore del parametro 'ordine''
     */
    @Override
    protected void fixOrdine() {
        List<Servizio> lista = null;
        Servizio serv;

        WamCompany company = (WamCompany) CompanySessionLib.getCompany();
        if (company != null) {
            lista = Servizio.getListByCompany(company);

            lista.sort(new Comparator<Servizio>() {
                @Override
                public int compare(Servizio o1, Servizio o2) {
                    Integer ordQuesto = o1.getOrdine();
                    Integer ordAltro = o2.getOrdine();
                    return ordQuesto.compareTo(ordAltro);
                }// end of inner method
            });// end of anonymous inner class

            for (int k = 0; k < lista.size(); k++) {
                serv = lista.get(k);
                serv.setOrdine(k + 1);
                serv.save();
            }// end of for cycle
        }// end of if cycle

        getTable().refresh();
    }// end of method

    /**
     * Tavola dei servizi per funzione
     */
    public Table getTavola(Funzione funzione) {
        Table tavola = null;
        List<Servizio> lista = null;
        WamCompany company = (WamCompany) CompanySessionLib.getCompany();

        if (company != null) {
            lista = Servizio.getListByCompany(company);
        }// end of if cycle

        return tavola;
    }// end of method

    private Servizio getServ(Object id) {
        Servizio servizio = null;
        BaseEntity entity = getEntityManager().find(Servizio.class, id);

        if (entity != null) {
            servizio = (Servizio) entity;
        }// end of if cycle

        return servizio;
    }// end of method

}// end of class

