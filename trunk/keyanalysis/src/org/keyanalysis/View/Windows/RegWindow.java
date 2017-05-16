package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DB.LoginService;
import org.keyanalysis.Services.DB.StorageService;
import org.keyanalysis.Services.DB.UserService;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RegWindow extends Window {
	private static final long serialVersionUID = -9216254227318947530L;

	public RegWindow() {
		super();
		this.setModal(true);
		this.setDraggable(true);
		this.center();
		final VerticalLayout mainLayout = new VerticalLayout();
		// mainLayout.setWidth(Constants.SIZE_100_PERCENT);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		final HorizontalLayout loginHorizontalLayout = new HorizontalLayout();
		loginHorizontalLayout.setMargin(true);
		loginHorizontalLayout.setId(Messages.getString("RegWindow.0")); //$NON-NLS-1$
		final VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setWidth(null);
		loginLayout.setSpacing(true);
		final TextField usernameTextField = new TextField(Messages.getString("RegWindow.1")); //$NON-NLS-1$
		usernameTextField.setWidth(Messages.getString("RegWindow.2")); //$NON-NLS-1$
		usernameTextField.setHeight(Messages.getString("RegWindow.3")); //$NON-NLS-1$
		usernameTextField.focus();
		loginLayout.addComponent(usernameTextField);
		final PasswordField passwordTextField = new PasswordField(Messages.getString("RegWindow.4")); //$NON-NLS-1$
		passwordTextField.setWidth(Messages.getString("RegWindow.5")); //$NON-NLS-1$
		passwordTextField.setHeight(Messages.getString("RegWindow.6")); //$NON-NLS-1$
		loginLayout.addComponent(passwordTextField);
		final PasswordField passwordTextField2 = new PasswordField(Messages.getString("RegWindow.7")); //$NON-NLS-1$
		passwordTextField2.setWidth(Messages.getString("RegWindow.8")); //$NON-NLS-1$
		passwordTextField2.setHeight(Messages.getString("RegWindow.9")); //$NON-NLS-1$
		loginLayout.addComponent(passwordTextField2);
		final GridLayout buttonGrid = this.createRegButtonLayout(usernameTextField, passwordTextField,
				passwordTextField2);
		loginLayout.addComponent(buttonGrid);
		loginHorizontalLayout.addComponent(loginLayout);
		loginHorizontalLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(loginHorizontalLayout);
		mainLayout.setComponentAlignment(loginHorizontalLayout, Alignment.MIDDLE_CENTER);
		this.setContent(mainLayout);
	}

	private GridLayout createRegButtonLayout(final TextField usernameTextField, final PasswordField passwordTextField,
			final PasswordField passwordTextField2) {
		final GridLayout buttonGrid = new GridLayout(2, 1);
		buttonGrid.setWidth(Messages.getString("RegWindow.10")); //$NON-NLS-1$
		buttonGrid.setSpacing(true);
		final HorizontalLayout labelPanel = new HorizontalLayout();
		buttonGrid.addComponent(labelPanel);
		buttonGrid.setComponentAlignment(labelPanel, Alignment.MIDDLE_LEFT);
		final Button loginButton = new Button(Messages.getString("RegWindow.11")); //$NON-NLS-1$
		loginButton.setWidth(Messages.getString("RegWindow.12")); //$NON-NLS-1$
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2119674789611252424L;

			@Override
			public void buttonClick(final ClickEvent event) {
				usernameTextField
						.setValue(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(usernameTextField.getValue()));
				passwordTextField
						.setValue(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(passwordTextField.getValue()));
				passwordTextField2.setValue(
						org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(passwordTextField2.getValue()));
				if (!Messages.getString("RegWindow.13").equals(usernameTextField.getValue()) && !Messages.getString("RegWindow.14").equals(passwordTextField.getValue()) //$NON-NLS-1$ //$NON-NLS-2$
						&& !Messages.getString("RegWindow.15").equals(passwordTextField2.getValue())) { //$NON-NLS-1$
					final boolean free = UserService.findAUser(usernameTextField.getValue(), new User());
					if (!free) {
						if (passwordTextField.getValue().equals(passwordTextField2.getValue())) {
							UserService.addUser(usernameTextField.getValue(),
									LoginService.hashing(passwordTextField.getValue()), StorageService.addStorage());
							UI.getCurrent().getPage().reload();
						} else {
							(new Notification(Messages.getString("RegWindow.16"), Messages.getString("RegWindow.17"), Type.ERROR_MESSAGE)) //$NON-NLS-1$ //$NON-NLS-2$
									.show(Page.getCurrent());
						}
					} else {
						(new Notification(Messages.getString("RegWindow.18"), Messages.getString("RegWindow.19"), Type.ERROR_MESSAGE)) //$NON-NLS-1$ //$NON-NLS-2$
								.show(Page.getCurrent());
					}
				} else {
					(new Notification(Messages.getString("RegWindow.20"), Messages.getString("RegWindow.21"), Type.ERROR_MESSAGE)) //$NON-NLS-1$ //$NON-NLS-2$
							.show(Page.getCurrent());
				}
			}
		});
		buttonGrid.addComponent(loginButton);
		buttonGrid.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
		return buttonGrid;
	}
}
