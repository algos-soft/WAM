package it.algos.wam.login;

import com.vaadin.ui.Component;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.multiazienda.ERelatedComboField;
import it.algos.webbase.web.login.DefaultLoginForm;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by alex on 25-05-2016.
 */
public class WamLoginForm extends DefaultLoginForm {

    ERelatedComboField userCombo;

    public WamLoginForm() {
        //addComponent();
    }

    @Override
    public Component createUsernameComponent() {
        userCombo = new ERelatedComboField(Volontario.class, "Utente");
        return userCombo;
    }

    @Override
    public void setUsername(String name) {
        //super.setUsername(name);
    }


    @Override
    /**
     * @return the selected user
     */
    public UserIF getSelectedUser(){
        UserIF user=null;
        Object obj = userCombo.getValue();
        if(obj!=null){
            if(obj instanceof UserIF){
                user = (UserIF)obj;
            }
        }
        return user;
    }

}
