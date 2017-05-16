package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.User;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

public class SearchUserWindow extends Window {
	private static final long serialVersionUID = -6842717644878825964L;

	public SearchUserWindow() {
		super();
		this.setCaption(Messages.getString("SearchUserWindow.0")); //$NON-NLS-1$
		// setStyleName(Constants.STYLE_MAIN_TITLE);
		this.setSizeFull();
		final TableCreatorUtility tcu = new TableCreatorUtility(Messages.getString("SearchUserWindow.1"), User.class, this); //$NON-NLS-1$
		final GridLayout layout = tcu.createTableLayout();
		layout.setId(Messages.getString("SearchUserWindow.2")); //$NON-NLS-1$
		this.setContent(layout);
	}
}
