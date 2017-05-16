package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.Item;
import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DB.LoginService;
import org.keyanalysis.Services.DB.StorageService;
import org.keyanalysis.Services.DB.UserService;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ProfilWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4061387347200916019L;
	private Storage storage;

	/**
	 * Own profil will be shown in the window
	 */
	public ProfilWindow() {
		super();
		this.center();
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		final User u = ((KeyanalysisUI) UI.getCurrent()).getLoggedUser();
		if (u == null) {
			this.close();
		}
		this.storage = ((KeyanalysisUI) UI.getCurrent()).getStorage();
		this.showProfil(u);
	}

	/**
	 * Selected user's profil will be shown in the window
	 * 
	 * @param u
	 */
	public ProfilWindow(final User u) {
		super();
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		if (u == null) {
			this.close();
		}

		StorageService.getStorage(u, this.storage);
		this.showProfil(u);
	}

	private void showProfil(final User u) {
		((KeyanalysisUI) UI.getCurrent()).getStorage();

		this.UserSettingsPanel(u);
	}

	private void UserSettingsPanel(final User u) {
		this.setCaption(Messages.getString("ProfilWindow.0")); //$NON-NLS-1$
		// setStyleName("hasMainTitle");
		// addStyleName("v-scrollable");
		this.setWidth(Messages.getString("ProfilWindow.1")); //$NON-NLS-1$
		this.setHeight(Messages.getString("ProfilWindow.2")); //$NON-NLS-1$
		final GridLayout mainLayout = new GridLayout(1, 2);
		mainLayout.setColumnExpandRatio(0, 0);
		mainLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		mainLayout.setSizeFull();
		mainLayout.setId(Messages.getString("ProfilWindow.3")); //$NON-NLS-1$
		this.setContent(mainLayout);
		mainLayout.addComponent(this.createPasswordChanger(u), 0, 0);
		final Panel itemspanel = new Panel(Messages.getString("ProfilWindow.4")); //$NON-NLS-1$
		itemspanel.setWidth(Messages.getString("ProfilWindow.5")); //$NON-NLS-1$
		final TableCreatorUtility tcu = new TableCreatorUtility(Messages.getString("ProfilWindow.6"), Item.class, u, this); //$NON-NLS-1$
		final GridLayout layout = tcu.createTableLayout();
		layout.addStyleName(Messages.getString("ProfilWindow.7")); //$NON-NLS-1$
		itemspanel.setContent(layout);

		mainLayout.addComponent(itemspanel, 0, 1);
	}

	private Panel createPasswordChanger(final User u) {
		final GridLayout passwordChangeLayout = new GridLayout(2, 3);
		final Panel passwordChangePanel = new Panel(passwordChangeLayout);
		passwordChangePanel.setCaption(Messages.getString("ProfilWindow.8")); //$NON-NLS-1$
		passwordChangePanel.addStyleName(Messages.getString("ProfilWindow.9")); //$NON-NLS-1$
		passwordChangeLayout.setWidth(Messages.getString("ProfilWindow.10")); //$NON-NLS-1$
		passwordChangeLayout.setHeightUndefined();
		passwordChangePanel.setWidth(Messages.getString("ProfilWindow.11")); //$NON-NLS-1$
		passwordChangePanel.setHeightUndefined();
		passwordChangeLayout.setHeight(Messages.getString("ProfilWindow.12")); //$NON-NLS-1$
		final PasswordField oldPassField = new PasswordField(Messages.getString("ProfilWindow.13")); //$NON-NLS-1$
		passwordChangeLayout.addComponent(oldPassField, 0, 0);
		final PasswordField newPassFirst = new PasswordField(Messages.getString("ProfilWindow.14")); //$NON-NLS-1$
		passwordChangeLayout.addComponent(newPassFirst, 0, 1);
		final PasswordField newPassSecond = new PasswordField(Messages.getString("ProfilWindow.15")); //$NON-NLS-1$
		passwordChangeLayout.addComponent(newPassSecond, 1, 1);
		final Button submit = new Button(Messages.getString("ProfilWindow.16")); //$NON-NLS-1$
		passwordChangeLayout.addComponent(submit, 0, 2);
		passwordChangeLayout.setMargin(new MarginInfo(false, false, false, true));
		submit.addClickListener(this.submit(u, oldPassField, newPassFirst, newPassSecond));
		return passwordChangePanel;
	}

	private ClickListener submit(final User u, final PasswordField oldPassField, final PasswordField newPassFirst,
			final PasswordField newPassSecond) {
		return new ClickListener() {

			private static final long serialVersionUID = -7292624747068979285L;

			@Override
			public void buttonClick(final ClickEvent event) {
				boolean oldPassOK = false;
				boolean changeSuccess = false;
				String oldPass = oldPassField.getValue();
				oldPass = LoginService.hashing(oldPass);
				if (oldPass.equals(u.getPassword())) {
					oldPassOK = true;
				} else {
					new Notification(Messages.getString("ProfilWindow.17"), Messages.getString("ProfilWindow.18"), Type.ERROR_MESSAGE) //$NON-NLS-1$ //$NON-NLS-2$
							.show(Page.getCurrent());
					oldPassOK = false;
				}
				if (((newPassFirst.getValue()).equals(newPassSecond.getValue())) && (oldPassOK == true)) {
					if (newPassFirst.getValue().equals(Messages.getString("ProfilWindow.19"))) { //$NON-NLS-1$
						new Notification(Messages.getString("ProfilWindow.20"), Messages.getString("ProfilWindow.21"), Type.ERROR_MESSAGE).show(Page.getCurrent()); //$NON-NLS-1$ //$NON-NLS-2$
						newPassFirst.addValidator(new StringLengthValidator(Messages.getString("ProfilWindow.22"), 1, 10, true)); //$NON-NLS-1$
						newPassSecond
								.addValidator(new StringLengthValidator(Messages.getString("ProfilWindow.23"), 1, 10, true)); //$NON-NLS-1$
					} else {
						changeSuccess = UserService.changePassword(u.getName(), newPassFirst.getValue());
						if (changeSuccess) {
							new Notification(Messages.getString("ProfilWindow.24"), Messages.getString("ProfilWindow.25")).show(Page.getCurrent()); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							new Notification(Messages.getString("ProfilWindow.26"), Messages.getString("ProfilWindow.27"), Type.ERROR_MESSAGE) //$NON-NLS-1$ //$NON-NLS-2$
									.show(Page.getCurrent());
							;
						}
						newPassFirst.removeAllValidators();
						newPassSecond.removeAllValidators();
					}
				} else if (oldPassOK == true) {
					new Notification(Messages.getString("ProfilWindow.28"), Messages.getString("ProfilWindow.29"), Type.ERROR_MESSAGE) //$NON-NLS-1$ //$NON-NLS-2$
							.show(Page.getCurrent());
				}
				oldPassField.setValue(Messages.getString("ProfilWindow.30")); //$NON-NLS-1$
				newPassFirst.setValue(Messages.getString("ProfilWindow.31")); //$NON-NLS-1$
				newPassSecond.setValue(Messages.getString("ProfilWindow.32")); //$NON-NLS-1$
			}
		};
	}
}
