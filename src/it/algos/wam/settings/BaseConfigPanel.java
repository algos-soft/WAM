package it.algos.wam.settings;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public abstract class BaseConfigPanel extends CustomComponent implements ConfigComponent {

	private FieldGroup group;
	protected PrefSetItem item;

	public BaseConfigPanel() {
		super();

//		setMargin(true);
//		setSpacing(true);

		group = new FieldGroup();

	}

	public abstract PrefSetItem createItem();

	/**
	 * Create the button panel
	 */
	protected Button createSaveButton() {
		Button bSave = new Button("Registra");
		bSave.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					group.commit();
					item.persist();
				} catch (CommitException e) {
					e.printStackTrace();
				}
			}
		});

		return bSave;

	}

	public FieldGroup getGroup() {
		return group;
	}

	interface PrefSetItem extends Item {

		/**
		 * Persists the item properties to the storage
		 */
		public void persist();

	}

	@Override
	public void loadContent() {
		item = createItem();
		group.setItemDataSource(item);
	}
	
	@Override
	public Component getUIComponent() {
		return this;
	}

	public PrefSetItem getItem() {
		return item;
	}
}
