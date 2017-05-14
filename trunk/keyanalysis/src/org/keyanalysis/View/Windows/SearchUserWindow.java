package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.User;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Window;

public class SearchUserWindow extends Window {
	private static final long serialVersionUID = -6842717644878825964L;

	public SearchUserWindow() {
		super();
		this.setCaption("Search");
		// setStyleName(Constants.STYLE_MAIN_TITLE);
		this.setSizeFull();
		final TableCreatorUtility tcu = new TableCreatorUtility("Search", User.class, this);
		final GridLayout layout = tcu.createTableLayout();
		layout.setId("scrollstyleSearch");
		this.setContent(layout);
	}
}
