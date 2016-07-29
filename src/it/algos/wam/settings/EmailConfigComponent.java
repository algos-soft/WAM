package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.EmailField;
import it.algos.webbase.web.field.TextField;
import it.algos.webbase.web.lib.LibSession;

@SuppressWarnings("serial")
/**
 * Componente di configurazione email
 */
public class EmailConfigComponent extends BaseConfigPanel {

	private static final String KEY_SENDER = "sender";
	private static final String KEY_BACKUP_EMAIL = "emailbackup";
	private static final String KEY_BACKUP_ADDRESS = "emailbackupaddress";


	private TextField senderField;
	private CheckBoxField sendBackupMailField;
	private TextField backupEmailAddressField;


	public EmailConfigComponent() {
		super();

		// crea i fields
		createFields();
		
		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		layout.addComponent(senderField);
		layout.addComponent(new Spacer());
		layout.addComponent(sendBackupMailField);
		layout.addComponent(backupEmailAddressField);

		addComponent(layout);
		addComponent(createButtonPanel());

	}
	
	// crea e registra i fields
	private void createFields(){

		// create and add fields and other components

		// sender adress
		senderField = new EmailField("Indirizzo del mittente");
		senderField.setDescription("L'indirizzo che risulta come mittente di tutte le email in uscita. I destinatari potrebbero rispondere a questo indirizzo.");

		// execute backup email
		sendBackupMailField = new CheckBoxField("Invia una copia di tutte le email");
		sendBackupMailField.setDescription("Invia una copia di tutte le email in uscita a una casella di backup");

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
		return new DataSetItem();
	}

	/**
	 * Item containing form data
	 */
	private class DataSetItem extends PropertysetItem implements PrefSetItem {

		public DataSetItem() {
			super();
			
			WamCompany company = WamCompany.getCurrent();
			String sender = company.getSenderAddress();
			sender = (sender==null? "" : sender);
			boolean doBackup=company.isSendMailToBackup();
			String backAddr = company.getBackupMailbox();
			backAddr = (backAddr==null? "" : backAddr);

			addItemProperty(KEY_SENDER, new ObjectProperty<String>(sender));
			addItemProperty(KEY_BACKUP_EMAIL, new ObjectProperty<Boolean>(doBackup));
			addItemProperty(KEY_BACKUP_ADDRESS, new ObjectProperty<String>(backAddr));


		}

		public void persist() {
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

				WamCompany company = WamCompany.getCurrent();

				String sender = (String)getItemProperty(KEY_SENDER).getValue();
				company.setSenderAddress(sender);

				boolean doBackup = (Boolean)getItemProperty(KEY_BACKUP_EMAIL).getValue();
				company.setSendMailToBackup(doBackup);

				String backAddr = (String)getItemProperty(KEY_BACKUP_ADDRESS).getValue();
				company.setBackupMailbox(backAddr);

				company.save();

			}

		}

	}

	@Override
	public String getTitle() {
		return "Impostazione email";
	}

}
