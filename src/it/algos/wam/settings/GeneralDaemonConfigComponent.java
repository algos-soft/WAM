package it.algos.wam.settings;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import it.algos.wam.WAMApp;
import it.algos.wam.daemons.WamScheduler;
import it.algos.webbase.web.component.Spacer;
import it.algos.webbase.web.field.CheckBoxField;

import javax.servlet.ServletContext;

@SuppressWarnings("serial")
public class GeneralDaemonConfigComponent extends BaseConfigPanel implements View {

	private static final String KEY_SERVICE_START = "servicestart";



	private Label serviceStatus;
	private Button bStartDaemon;
	private Button bStopDaemon;
	private CheckBoxField checkbox;

	public GeneralDaemonConfigComponent() {
		super();

		// crea e registra i fields
		creaFields();
		
		serviceStatus = new Label("", ContentMode.HTML);

		bStartDaemon = new Button("Attiva il servizio");
		bStartDaemon.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WamScheduler.getInstance().start();
				refreshStatus();
			}
		});

		bStopDaemon = new Button("Ferma il servizio");
		bStopDaemon.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WamScheduler.getInstance().stop();
				refreshStatus();
			}
		});

		// crea la UI
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);

		String title = "<b>Servizio di controllo iscrizioni</b><p>";
		title += "All'inizio di ogni ora esegue i controlli previsti per <br>"
				+ "ogni azienda ed invia le eventuali notifiche.";
		Label infoLabel = new Label(title, ContentMode.HTML);
		layout.addComponent(infoLabel);
		layout.addComponent(serviceStatus);
		layout.addComponent(bStartDaemon);
		layout.addComponent(bStopDaemon);
		layout.addComponent(checkbox);
		layout.addComponent(new Spacer());
		layout.addComponent(createSaveButton());

		setCompositionRoot(layout);

		refreshStatus();

	}
	
	// crea e registra i fields
	private void creaFields(){
		
		// check box servizio attivo
		checkbox=new CheckBoxField("Attiva il servizio all'avvio del server");

		// bind fields to properties
		getGroup().bind(checkbox, KEY_SERVICE_START);

	}
	

	private void refreshStatus() {

		boolean serviceIsOn = false;
		ServletContext svc= WAMApp.getServletContext();

		Object obj = svc.getAttribute(WamScheduler.DAEMON_NAME);
		if ((obj != null) && (obj instanceof Boolean)) {
			boolean flag = (Boolean) obj;
			if (flag) {
				serviceIsOn = true;
			}
		}

		if (serviceIsOn) {
			serviceStatus.setValue("Il servizio è <b>ATTIVO</b>");
			bStartDaemon.setEnabled(false);
			bStopDaemon.setEnabled(true);
		} else {
			serviceStatus.setValue("Il servizio è <b>FERMO<b>");
			bStartDaemon.setEnabled(true);
			bStopDaemon.setEnabled(false);
		}

	}



	@Override
	public Component getUIComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return "Servizio di controllo iscrizioni";
	}
	
	
	public PrefSetItem createItem() {
		return new DaemonSetItem();
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		loadContent();
	}

	/**
	 * Item containing form data
	 */
	private class DaemonSetItem extends PropertysetItem implements PrefSetItem {

		public DaemonSetItem() {
			super();

			boolean startup=ManagerPrefs.startDaemonAtStartup.getBool();
			addItemProperty(KEY_SERVICE_START, new ObjectProperty<Boolean>(startup));
		}
		
		public void persist() {
			Object obj = getItemProperty(KEY_SERVICE_START).getValue();
			ManagerPrefs.startDaemonAtStartup.put(obj);
		}

	}





}
