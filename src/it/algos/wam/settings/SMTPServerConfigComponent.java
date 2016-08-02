package it.algos.wam.settings;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import it.algos.webbase.web.dialog.EditDialog;
import it.algos.webbase.web.email.EmailService;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.IntegerField;
import it.algos.webbase.web.field.PasswordField;
import it.algos.webbase.web.field.TextField;
import org.apache.commons.mail.EmailException;

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

		HorizontalLayout hr = new HorizontalLayout();
		hr.setSpacing(true);
		hr.addComponent(createSaveButton());
		hr.addComponent(new Button("Test", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent clickEvent) {
				try {
					getGroup().commit();
					doTest();
				} catch (FieldGroup.CommitException e) {
					e.printStackTrace();
				}
			}
		}));
		layout.addComponent(hr);

		setCompositionRoot(layout);


	}

	/**
	 * Invia una email di test
	 */
	private void doTest() {
		String host=getHost();
		if(host==null || host.equals("")){
			Notification.show("Manca l'indirizzo del server SMTP");
			return;
		}

		int port=getPort();
		if(port<=0){
			Notification.show("Manca la porta SMTP");
			return;
		}

		if(getUseAuth()){
			String user=getUsername();
			if(user.equals("")){
				Notification.show("Manca lo username per l'autenticazione");
				return;
			}
			String pass=getPassword();
			if(pass.equals("")){
				Notification.show("Manca la password per l'autenticazione");
				return;
			}
		}


		new EditDialog("Test email", "invia a:", new EditDialog.EditListener() {
			@Override
			public void onClose() {

			}

			@Override
			public void onClose(String address) {
				boolean useAuth=getUseAuth();
				String username=getUsername();
				String password=getPassword();
				try {
					EmailService.sendMail(host, port, useAuth, username, password, "noreply@wam.it", address, null, null, "WAM test e-mail", "questa è una email di test inviata da WAM", false, null);
					Notification.show("e-mail inviata");
				} catch (EmailException e) {
					Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}).show();

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
			boolean cont = true;
			
			
			if (cont) {

				ManagerPrefs.smtpServer.put(getHost());
				ManagerPrefs.smtpUserName.put(getUsername());
				ManagerPrefs.smtpPassword.put(getPassword());
				ManagerPrefs.smtpPort.put(getPort());
				ManagerPrefs.smtpUseAuth.put(getUseAuth());
				
			}

		}

	}

	@Override
	public String getTitle() {
		return "Configurazione server SMTP";
	}

	private String getHost(){
		return (String)getItem().getItemProperty(KEY_HOST).getValue();
	}

	private String getUsername(){
		return (String)getItem().getItemProperty(KEY_USER).getValue();
	}

	private String getPassword(){
		return (String)getItem().getItemProperty(KEY_PASSWORD).getValue();
	}

	private int getPort(){
		return (int)getItem().getItemProperty(KEY_PORT).getValue();
	}

	private boolean getUseAuth(){
		return (boolean)getItem().getItemProperty(KEY_USE_AUTH).getValue();
	}





}
