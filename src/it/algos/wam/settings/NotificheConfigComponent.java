package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;

@SuppressWarnings("serial")
public class NotificheConfigComponent extends BaseConfigPanel {

	private static final String KEY_SENDER = "sender";
	private static final String KEY_BACKUP_EMAIL = "emailbackup";
	private static final String KEY_BACKUP_ADDRESS = "emailbackupaddress";


	private TextField senderField;
	private CheckBoxField sendBackupMailField;
	private TextField backupEmailAddressField;


	public NotificheConfigComponent() {
		super();
		//addStyleName("yellowBg");

		// crea i fields
		createFields();
		
		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		FormLayout fl = new FormLayout();
		fl.setSpacing(true);
		fl.addComponent(sendBackupMailField);
		fl.addComponent(backupEmailAddressField);
		fl.addComponent(senderField);
		layout.addComponent(fl);

		addComponent(layout);
		addComponent(createButtonPanel());

		// sincronizza i checks
		//syncMailChecks();


	}
	
	// crea e registra i fields
	private void createFields(){

		// create and add fields and other components

		// sender adress
		senderField = new EmailField("Indirizzo mittente");
		senderField.setDescription("L'indirizzo che risulta come mittente di tutte le email in uscita. I destinatari potrebbero rispondere a questo indirizzo.");

		// execute backup email
		sendBackupMailField = new CheckBoxField("Invia una copia di tutte le email");
		sendBackupMailField.setDescription("Invia una copia di tutte le email in uscita a un proprio indirizzo");

		// backup email address
		backupEmailAddressField = new EmailField("Indirizzo email per copie");
		backupEmailAddressField.setDescription("L'indirizzo al quale inviare le copie delle email in uscita");

		// bind fields to properties
		getGroup().bind(senderField, KEY_SENDER);
		getGroup().bind(sendBackupMailField, KEY_BACKUP_EMAIL);
		getGroup().bind(backupEmailAddressField, KEY_BACKUP_ADDRESS);

	}

	@Override
	public void loadContent() {
		super.loadContent();
	}

	public PrefSetItem createItem() {
		return new NotificheSetItem();
	}

	/**
	 * Item containing form data
	 */
	private class NotificheSetItem extends PropertysetItem implements PrefSetItem {

		public NotificheSetItem() {
			super();
			
//			addItemProperty(KEY_SENDER, new ObjectProperty<String>(CompanyPrefs.senderEmailAddress.getString()));
//			addItemProperty(KEY_BACKUP_EMAIL, new ObjectProperty<Boolean>(CompanyPrefs.backupEmail.getBool()));
//			addItemProperty(KEY_BACKUP_ADDRESS, new ObjectProperty<String>(CompanyPrefs.backupEmailAddress.getString()));

			// PROVVISORIO!!
			addItemProperty(KEY_SENDER, new ObjectProperty<String>("info@algos.it"));
			addItemProperty(KEY_BACKUP_EMAIL, new ObjectProperty<Boolean>(true));
			addItemProperty(KEY_BACKUP_ADDRESS, new ObjectProperty<String>("backup@algos.it"));


		}

		public void persist() {
			Object obj;
			boolean cont = true;
			
			// se backup email attivo ci deve essere l'indirizzo
			boolean bkemail = (boolean)getItemProperty(KEY_BACKUP_EMAIL).getValue();
			if (bkemail) {
				String bkaddress = (String)getItemProperty(KEY_BACKUP_ADDRESS).getValue();
				if (bkaddress.equals("")) {
					Notification.show("Inserire l'indirizzo email per copie");
					cont=false;
				}
			}
			
			if (cont) {

				obj = getItemProperty(KEY_SENDER).getValue();
//				CompanyPrefs.senderEmailAddress.put(obj);

				obj = getItemProperty(KEY_BACKUP_EMAIL).getValue();
//				CompanyPrefs.backupEmail.put(obj);

				obj = getItemProperty(KEY_BACKUP_ADDRESS).getValue();
//				CompanyPrefs.backupEmailAddress.put(obj);

			}

		}

	}

	@Override
	public String getTitle() {
		return "Configurazione notifiche";
	}

}
