package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DB.LogService;
import org.keyanalysis.Services.DB.LoginService;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
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

public class LoginWindow extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5516574370863045329L;
	User u;

	public LoginWindow() {
		this.setModal(true);
		this.setResizable(false);
		this.setDraggable(true);
		this.center();
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		final HorizontalLayout loginHorizontalLayout = new HorizontalLayout();
		loginHorizontalLayout.setMargin(true);
		loginHorizontalLayout.setId(Messages.getString("LoginWindow.0")); //$NON-NLS-1$
		final VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setWidth(null);
		loginLayout.setSpacing(true);
		final TextField usernameTextField = new TextField(Messages.getString("LoginWindow.1")); //$NON-NLS-1$
		usernameTextField.setWidth(Messages.getString("LoginWindow.2")); //$NON-NLS-1$
		usernameTextField.setHeight(Messages.getString("LoginWindow.3")); //$NON-NLS-1$
		usernameTextField.focus();
		loginLayout.addComponent(usernameTextField);
		final PasswordField passwordTextField = new PasswordField(Messages.getString("LoginWindow.4")); //$NON-NLS-1$
		passwordTextField.setWidth(Messages.getString("LoginWindow.5")); //$NON-NLS-1$
		passwordTextField.setHeight(Messages.getString("LoginWindow.6")); //$NON-NLS-1$
		loginLayout.addComponent(passwordTextField);
		final GridLayout buttonGrid = this.createLoginButtonLayout(usernameTextField, passwordTextField);
		loginLayout.addComponent(buttonGrid);
		loginHorizontalLayout.addComponent(loginLayout);
		loginHorizontalLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		mainLayout.addComponent(loginHorizontalLayout);
		mainLayout.setComponentAlignment(loginHorizontalLayout, Alignment.MIDDLE_CENTER);
		this.setContent(mainLayout);
	}

	private GridLayout createLoginButtonLayout(final TextField usernameTextField,
			final PasswordField passwordTextField) {
		this.u = (User) VaadinSession.getCurrent().getAttribute(Messages.getString("LoginWindow.7")); //$NON-NLS-1$
		final GridLayout buttonGrid = new GridLayout(2, 2);
		buttonGrid.setWidth(Messages.getString("LoginWindow.8")); //$NON-NLS-1$
		buttonGrid.setSpacing(true);
		final HorizontalLayout labelPanel = new HorizontalLayout();
		buttonGrid.addComponent(labelPanel);
		buttonGrid.setComponentAlignment(labelPanel, Alignment.MIDDLE_LEFT);
		final Button loginButton = new Button(Messages.getString("LoginWindow.9")); //$NON-NLS-1$
		loginButton.setWidth(Messages.getString("LoginWindow.10")); //$NON-NLS-1$
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2119674789611252424L;

			@Override
			public void buttonClick(final ClickEvent event) {
				usernameTextField
						.setValue(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(usernameTextField.getValue()));
				passwordTextField
						.setValue(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(passwordTextField.getValue()));
				if (!Messages.getString("LoginWindow.11").equals(usernameTextField.getValue()) && !Messages.getString("LoginWindow.12").equals(passwordTextField.getValue())) { //$NON-NLS-1$ //$NON-NLS-2$
					LoginWindow.this.u = new User();
					final boolean success = LoginService.signIn(usernameTextField.getValue(),
							passwordTextField.getValue(), LoginWindow.this.u);
					if (success) {
						LoginWindow.this.getSession().setAttribute(Messages.getString("LoginWindow.13"), LoginWindow.this.u); //$NON-NLS-1$
						((KeyanalysisUI) UI.getCurrent()).setLoggedUser(LoginWindow.this.u);
						LogService.AddLogEntry(Messages.getString("LoginWindow.14"), null, Messages.getString("LoginWindow.15")); //$NON-NLS-1$ //$NON-NLS-2$
						UI.getCurrent().getPage().reload();
						LoginWindow.this.close();
					} else {
						LoginWindow.this.getSession().setAttribute(Messages.getString("LoginWindow.16"), LoginWindow.this.u); //$NON-NLS-1$
						final User tried = new User();
						tried.setName(usernameTextField.getValue());
						LogService.AddLogEntry(Messages.getString("LoginWindow.17"), tried, Messages.getString("LoginWindow.18")); //$NON-NLS-1$ //$NON-NLS-2$
						(new Notification(Messages.getString("LoginWindow.19"), Messages.getString("LoginWindow.20"), Type.ERROR_MESSAGE)).show(Page.getCurrent()); //$NON-NLS-1$ //$NON-NLS-2$
						// ((KeyanalysisUI)UI.getCurrent()).logOut();
					}
					((KeyanalysisUI) UI.getCurrent()).setLoggedUser(LoginWindow.this.u);
				} else {
					(new Notification(Messages.getString("LoginWindow.21"), Messages.getString("LoginWindow.22"), Type.ERROR_MESSAGE)).show(Page.getCurrent()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		});
		buttonGrid.addComponent(loginButton);
		buttonGrid.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
		final Button link = new Button(Messages.getString("LoginWindow.23"), new ClickListener() { //$NON-NLS-1$
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				LoginWindow.this.close();
				UI.getCurrent().addWindow(new RegWindow());

			}
		});
		buttonGrid.addComponent(link, 1, 1);
		;
		buttonGrid.setComponentAlignment(link, Alignment.MIDDLE_RIGHT);
		return buttonGrid;
	}
}
