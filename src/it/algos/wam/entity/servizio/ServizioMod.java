package it.algos.wam.entity.servizio;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.companyentity.WamModSposta;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.table.ATable;
import it.algos.webbase.web.table.TablePortal;
import it.algos.webbase.web.toolbar.TableToolbar;

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
        boolean cont = true;
        final Object[] ids = getTable().getSelectedIds();
        for (Object id : ids) {
            BaseEntity entity = getEntityManager().find(getEntityClass(), id);
            if (entity != null && entity instanceof Servizio) {
                Servizio serv = (Servizio) entity;
                List<ServizioFunzione> sfs = serv.getServizioFunzioniOrdine();
                for (ServizioFunzione sf : sfs) {
                    List iscrizioni = CompanyQuery.getList(Iscrizione.class, Iscrizione_.servizioFunzione, sf);
                    if (iscrizioni.size() > 0) {
                        cont = false;
                        break;
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        if (cont) {
            super.delete();
        } else {
            Notification.show("Impossibile cancellare i servizi selezionati perché dei volontari si sono già iscritti.", Notification.Type.WARNING_MESSAGE);
        }// end of if/else cycle
    }// end of method


}// end of class

