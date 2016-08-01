package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;

@SuppressWarnings("serial")
public class SMTPServerConfigComponent extends BaseConfigPanel implements View {

	private static final String KEY_HOST = "smtp";
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USE_AUTH = "useauth";
	private static final String KEY_PORT = "port";

	private Field<?> smtpField;
	private Field<?> portField;
	private Field<?> useAuthField;
	private Field<?> smtpUserField;
	private Field<?> smtpPasswordField;

	public SMTPServerConfigComponent() {
		super();

		
		// crea i fields
		createFields();
		
		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(false);

		layout.addComponent(new Label("Configurazione SMTP server (utilizzato per tutta la posta in uscita)"));

		FormLayout fieldLayout = new FormLayout();
		fieldLayout.addComponent(smtpField);
		fieldLayout.addComponent(portField);
		fieldLayout.addComponent(useAuthField);
		fieldLayout.addComponent(smtpUserField);
		fieldLayout.addComponent(smtpPasswordField);

		layout.addComponent(fieldLayout);
		layout.addComponent(createSaveButton());

		setCompositionRoot(layout);


	}
	
	// crea e registra i fields
	private void createFields(){
		// create and add fields and other components
		smtpField = new TextField("SMTP server");
		smtpField.setWidth("20em");
		portField = new IntegerField("Porta");
		useAuthField = new CheckBoxField("Usa autenticazione SMTP");
		smtpUserField = new TextField("SMTP username");
		smtpUserField.setWidth("14em");
		smtpPasswordField = new PasswordField("SMTP password");
		smtpPasswordField.setWidth("14em");


		// bind fields to properties
		getGroup().bind(smtpField, KEY_HOST);
		getGroup().bind(smtpUserField, KEY_USER);
		getGroup().bind(smtpPasswordField, KEY_PASSWORD);
		getGroup().bind(useAuthField, KEY_USE_AUTH);
		getGroup().bind(portField, KEY_PORT);

	}
	
	
	public PrefSetItem createItem() {
		return new SMTPSetItem();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		loadContent();
	}

	/**
	 * Item containing form data
	 */
	private class SMTPSetItem extends PropertysetItem implements PrefSetItem {

		public SMTPSetItem() {
			super();
			
			addItemProperty(KEY_HOST, new ObjectProperty<String>(ManagerPrefs.smtpServer.getString()));
			addItemProperty(KEY_USER, new ObjectProperty<String>(ManagerPrefs.smtpUserName.getString()));
			addItemProperty(KEY_PASSWORD, new ObjectProperty<String>(ManagerPrefs.smtpPassword.getString()));
			addItemProperty(KEY_PORT, new ObjectProperty<Integer>(ManagerPrefs.smtpPort.getInt()));
			addItemProperty(KEY_USE_AUTH, new ObjectProperty<Boolean>(ManagerPrefs.smtpUseAuth.getBool()));

		}

		public void persist() {
			Object obj;
			boolean cont = true;
			
			
			if (cont) {

				obj = getItemProperty(KEY_HOST).getValue();
				ManagerPrefs.smtpServer.put(obj);

				obj = getItemProperty(KEY_USER).getValue();
				ManagerPrefs.smtpUserName.put(obj);

				obj = getItemProperty(KEY_PASSWORD).getValue();
				ManagerPrefs.smtpPassword.put(obj);
				
				obj = getItemProperty(KEY_PORT).getValue();
				ManagerPrefs.smtpPort.put(obj);
				
				obj = getItemProperty(KEY_USE_AUTH).getValue();
				ManagerPrefs.smtpUseAuth.put(obj);
				
			}

		}

	}

	@Override
	public String getTitle() {
		return "Configurazione server SMTP";
	}


}
