package it.algos.wam.login;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.multiazienda.CompanySessionLib;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.lib.LibSession;
import it.algos.webbase.web.login.DefaultLoginForm;
import it.algos.webbase.web.login.UserIF;

import java.util.List;

/**
 * Created by alex on 25-05-2016.
 * .
 */
public class WamLoginForm extends DefaultLoginForm {

    private ERelatedComboField userCombo;
    private UserIF user;

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

    @Override
    public Component createUsernameComponent() {
        userCombo = new ERelatedComboField(Volontario.class, "Utente");
        userCombo.sort(Volontario_.cognome, Volontario_.nome);

        //--filtro. Solo quelli attivi
        Container.Filter filter = new Compare.Equal(Volontario_.attivo.getName(), true);
        JPAContainer filterableContainer = (JPAContainer) userCombo.getContainerDataSource();
        filterableContainer.addContainerFilter(filter);

        userCombo.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                getPassField().clear();
            }
        });

        return userCombo;
    }

    @Override
    public void setUsername(String name) {
        Volontario v = Volontario.queryByNick(name);
        if (v != null) {
            userCombo.setValue(v.getId());
        } else {
            userCombo.setValue(null);   // no selection
        }
    }


    @Override
    /**
     * @return the selected user
     */
    public UserIF getSelectedUser() {
        UserIF user = null;

        if (this.user != null) {
            user = this.user;
        } else {
            Object obj = userCombo.getSelectedBean();
            if (obj != null) {
                if (obj instanceof UserIF) {
                    user = (UserIF) obj;
                }
            }
        }
        return user;
    }

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
