package it.algos.wam.login;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.login.AbsUserProfileForm;
import it.algos.webbase.web.login.ChangePasswordDialog;
import it.algos.webbase.web.login.UserIF;

/**
 * Created by alex on 04/08/16.
 */
public class WamUserProfileForm extends AbsUserProfileForm{

    private TextField nomeField;
    private TextField cognomeField;
    private EmailField emailField;
    Volontario volontario;

    public WamUserProfileForm() {

        nomeField = new TextField("Nome");
        nomeField.setWidth("20em");
        nomeField.setRequired(true);
        nomeField.setRequiredError("Il nome deve essere compilato");
        cognomeField = new TextField("Cognome");
        cognomeField.setWidth("20em");
        cognomeField.setRequired(true);
        cognomeField.setRequiredError("Il cognome deve essere compilato");
        emailField = new EmailField("Email");

        Button button = new Button("Cambia password");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                changePassword();
            }
        });

        addComponent(nomeField);
        addComponent(cognomeField);
        addComponent(emailField);
        addComponent(new Spacer());
        addComponent(button);

        nomeField.focus();
    }

    @Override
    public void setUser(UserIF user) {
        if(user==null || !(user instanceof Volontario)){
            return;
        }
        volontario = (Volontario)user;
        nomeField.setValue(volontario.getNome());
        cognomeField.setValue(volontario.getCognome());
        emailField.setValue(volontario.getEmail());
    }


    @Override
    public UserIF getUser() {
        return volontario;
    }

    @Override
    protected void onConfirm() {
        try {
            nomeField.validate();
            cognomeField.validate();
            emailField.validate();

            volontario.setNome(nomeField.getValue());
            volontario.setCognome(cognomeField.getValue());
            volontario.setEmail(emailField.getValue());
            volontario.save();

            super.onConfirm();
        }catch (Exception e){
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
}
