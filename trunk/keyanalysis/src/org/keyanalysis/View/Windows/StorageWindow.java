package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.Item;
import org.keyanalysis.Model.User;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

public class StorageWindow extends Window {
	private static final long serialVersionUID = 7682042281277310996L;

	public StorageWindow(final User u) {
		super();
		this.setCaption(Messages.getString("StorageWindow.0")); //$NON-NLS-1$
		this.setResizable(false);
		this.center();
		this.setSizeFull();
		final TableCreatorUtility tcu = new TableCreatorUtility(Messages.getString("StorageWindow.1"), Item.class, u, this); //$NON-NLS-1$
		final GridLayout layout = tcu.createTableLayout();
		layout.setId(Messages.getString("StorageWindow.2")); //$NON-NLS-1$
		this.setContent(layout);
	}
}
