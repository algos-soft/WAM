package it.algos.wam.entity.servizio;


import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.companyentity.WamCompanyEntity_;
import it.algos.wam.entity.companyentity.WamMod;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.form.ModuleForm;
import it.algos.webbase.web.query.CQuery;

import javax.persistence.metamodel.Attribute;
import java.util.List;

/**
 * Modulo del servizio (fisso o variabile) previsto o effettuato
 */
@SuppressWarnings("serial")
public class ServizioMod extends WamMod {

    // indirizzo interno del modulo - etichetta del menu
    public static String MENU_ADDRESS = "Servizi";

    /**
     * Costruttore senza parametri
     * <p>
     * Invoca la superclasse passando i parametri:
     * (obbligatorio) la Entity specifica
     * (facoltativo) etichetta del menu (se manca usa il nome della Entity)
     * (facoltativo) icona del menu (se manca usa un'icona standard)
     */
    public ServizioMod() {
        super(Servizio.class, MENU_ADDRESS, FontAwesome.GEAR);
    }// end of constructor

    /**
     * Crea i campi visibili nella lista (table)
     * <p/>
     * Come default spazzola tutti i campi della Entity <br>
     * Può essere sovrascritto (facoltativo) nelle sottoclassi specifiche <br>
     * Serve anche per l'ordine con cui vengono presentati i campi nella lista <br>
     */
    protected Attribute<?, ?>[] creaFieldsList() {
        return new Attribute[]{
                WamCompanyEntity_.company,
                Servizio_.ordine,
                Servizio_.sigla,
                Servizio_.descrizione,
                Servizio_.durata,
                Servizio_.oraInizio,
                Servizio_.oraFine,
                Servizio_.visibile,
                Servizio_.orario,
                Servizio_.multiplo,
                Servizio_.persone};
    }// end of method

    @Override
    public ModuleForm createForm(Item item) {
        return new ServizioForm(item, this);
    }

    @Override
    protected boolean preDelete(BaseEntity bEntity) {
        return super.preDelete(bEntity);
    }


    @Override
    /**
     * Override di delete per controllare che non ci siano
     * iscrizioni associate ai servizi da cancellare
     */
    public void delete() {
        boolean cont=true;
        final Object[] ids = getTable().getSelectedIds();
        for(Object id : ids){
            BaseEntity entity = getEntityManager().find(getEntityClass(), id);
            if(entity!=null && entity instanceof Servizio){
                Servizio serv = (Servizio)entity;
                List<ServizioFunzione> sfs = serv.getServizioFunzioni();
                for(ServizioFunzione sf:sfs){
                    List iscrizioni = CompanyQuery.queryList(Iscrizione.class, Iscrizione_.servizioFunzione, sf);
                    if(iscrizioni.size()>0){
                        cont=false;
                        break;
                    }
                }
            }
        }

        if(cont){
            super.delete();
        }else{
            Notification.show("Impossibile cancellare i servizi selezionati perché dei volontari si sono già iscritti.", Notification.Type.WARNING_MESSAGE);
        }
    }



}// end of class

