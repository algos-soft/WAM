package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.CheckBoxField;
import it.algos.webbase.web.field.IntegerField;

@SuppressWarnings("serial")

/**
 * Componente di configurazione delle notifiche ad uso dell'Admin
 */
public class NotificheConfigComponent extends BaseConfigPanel {

	private static final String KEY_INVIA_NOTIFICA = "invianotifica";
	private static final String KEY_ORE_PRIMA = "oreprima";

	private CheckBoxField fldInviaNotificaInizioTurno;
	private IntegerField fldNotificaQuanteOrePrima;

	public NotificheConfigComponent() {
		super();

		// crea i fields
		createFields();
		
		// crea la UI
//		VerticalLayout layout = new VerticalLayout();
//
//		layout.addComponent(fldInviaNotificaInizioTurno);
//		layout.addComponent(fldNotificaQuanteOrePrima);

//		addComponent(layout);
//		addComponent(createButtonPanel());

		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		layout.addComponent(fldInviaNotificaInizioTurno);
		layout.addComponent(fldNotificaQuanteOrePrima);
		layout.addComponent(new Spacer());
		layout.addComponent(createSaveButton());

		setCompositionRoot(layout);


	}
	
	// crea e registra i fields
	private void createFields(){

		// create and add fields and other components

		// invia notifica inizio turno
		fldInviaNotificaInizioTurno = new CheckBoxField("Invia una notifica di inizio turno");
		fldInviaNotificaInizioTurno.setDescription("Invia una notifica via email al volontario prima dell'inizio del turno");

		// notifica inizio turno quante ore prima
		fldNotificaQuanteOrePrima = new IntegerField("Quante ore prima");
		fldNotificaQuanteOrePrima.setDescription("Quante ore prima dell'inizio del turno va inviata la notifica");


		// bind fields to properties
		getGroup().bind(fldInviaNotificaInizioTurno, KEY_INVIA_NOTIFICA);
		getGroup().bind(fldNotificaQuanteOrePrima, KEY_ORE_PRIMA);

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

			boolean doInviaNotifInizioTurno=CompanyPrefs.inviaNotificaInizioTurno.getBool();
			int orePrima = CompanyPrefs.quanteOrePrimaNotificaInizioTurno.getInt();

			addItemProperty(KEY_INVIA_NOTIFICA, new ObjectProperty<Boolean>(doInviaNotifInizioTurno));
			addItemProperty(KEY_ORE_PRIMA, new ObjectProperty<Integer>(orePrima));


		}

		public void persist() {
			Object obj;
			boolean cont = true;
			
			// se invia notifica attivo ci devono essere le ore prima
			boolean inviaNotifica = (boolean)getItemProperty(KEY_INVIA_NOTIFICA).getValue();
			if (inviaNotifica) {
				Integer oreprima = (Integer)getItemProperty(KEY_ORE_PRIMA).getValue();
				if (oreprima<=0) {
					Notification.show("Indicare quante ore prima va inviata la notifica.");
					cont=false;
				}
			}
			
			if (cont) {

				boolean doInviaNotifInizioTurno = (Boolean)getItemProperty(KEY_INVIA_NOTIFICA).getValue();
				int orePrima = (int)getItemProperty(KEY_ORE_PRIMA).getValue();
				CompanyPrefs.inviaNotificaInizioTurno.put(doInviaNotifInizioTurno);
				CompanyPrefs.quanteOrePrimaNotificaInizioTurno.put(orePrima);

				Notification.show("Dati salvati");

			}

		}

	}

	@Override
	public String getTitle() {
		return "Invio notifiche";
	}

}
