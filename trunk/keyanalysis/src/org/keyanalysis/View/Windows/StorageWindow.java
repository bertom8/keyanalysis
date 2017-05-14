package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.Item;
import org.keyanalysis.Model.User;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

public class StorageWindow extends Window {
	private static final long serialVersionUID = 7682042281277310996L;

	public StorageWindow(final User u) {
		super();
		this.setCaption("Show items");
		// setStyleName(Constants.STYLE_MAIN_TITLE);
		this.setResizable(false);
		this.center();
		this.setSizeFull();
		final TableCreatorUtility tcu = new TableCreatorUtility("Items", Item.class, u, this);
		final GridLayout layout = tcu.createTableLayout();
		layout.setId("scrollstyleItems");
		this.setContent(layout);
	}
}
