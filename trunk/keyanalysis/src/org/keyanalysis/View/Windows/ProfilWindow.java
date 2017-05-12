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
		center();
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		User u = ((KeyanalysisUI)UI.getCurrent()).getLoggedUser();
		if (u == null)
			close();
		storage = ((KeyanalysisUI)UI.getCurrent()).getStorage();
		showProfil(u);
	}
	
	/**
	 * Selected user's profil will be shown in the window
	 * @param u
	 */
	public ProfilWindow(User u) {
		super();
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		if (u == null)
			close();
		
		StorageService.getStorage(u, storage);			
		showProfil(u);
	}

	private void showProfil(User u) {
		((KeyanalysisUI)UI.getCurrent()).getStorage();
		
		UserSettingsPanel(u);
	}
	
	private void UserSettingsPanel(User u) {
		setCaption("Profil");
		//setStyleName("hasMainTitle");
		//addStyleName("v-scrollable");
		setWidth("90%");
		setHeight("90%");
		final GridLayout mainLayout = new GridLayout(1,2);
		mainLayout.setColumnExpandRatio(0, 0);
		mainLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		mainLayout.setSizeFull();
		mainLayout.setId("scrollstyleProfil");
		setContent(mainLayout);		
		mainLayout.addComponent(createPasswordChanger(u),0,0);	
		Panel itemspanel = new Panel("Show items");
		itemspanel.setWidth("90%");
		final TableCreatorUtility tcu = new TableCreatorUtility("My items",Item.class,u,this);
		final GridLayout layout = tcu.createTableLayout();
		layout.addStyleName("v-scrollable");
		itemspanel.setContent(layout);
		
		mainLayout.addComponent(itemspanel, 0, 1);
	}
	
	private Panel createPasswordChanger(User u){
		final GridLayout passwordChangeLayout = new GridLayout(2,3);
		final Panel passwordChangePanel = new Panel(passwordChangeLayout);
		passwordChangePanel.setCaption("Jelszóváltás");
		passwordChangePanel.addStyleName("hasMainTitle");
		passwordChangeLayout.setWidth("445px");
		passwordChangeLayout.setHeightUndefined();
		passwordChangePanel.setWidth("450px");
		passwordChangePanel.setHeightUndefined();		
		passwordChangeLayout.setHeight("200");
		PasswordField oldPassField = new PasswordField("Old password:");
		passwordChangeLayout.addComponent(oldPassField, 0, 0);
		PasswordField newPassFirst = new PasswordField("New passwrod twice:");
		passwordChangeLayout.addComponent(newPassFirst, 0, 1);
		PasswordField newPassSecond = new PasswordField("");
		passwordChangeLayout.addComponent(newPassSecond, 1 , 1);
		Button submit = new Button("Save");
		passwordChangeLayout.addComponent(submit, 0, 2);
		passwordChangeLayout.setMargin(new MarginInfo(false, false, false, true));
		submit.addClickListener(submit(u, oldPassField, newPassFirst, newPassSecond ));
			return passwordChangePanel;
		}
	
	private ClickListener submit (User u, PasswordField oldPassField, PasswordField newPassFirst, PasswordField newPassSecond) {
		return new ClickListener(){


			private static final long serialVersionUID = -7292624747068979285L;

			@Override
			public void buttonClick(ClickEvent event) {
				boolean oldPassOK = false;
				boolean changeSuccess = false;
				String oldPass = oldPassField.getValue();
				oldPass = LoginService.hashing(oldPass);
				if (oldPass.equals(u.getPassword())) {
					oldPassOK = true;						
				} else {
					new Notification("Error","Your current password is wrong!",Type.ERROR_MESSAGE).show(Page.getCurrent());
					oldPassOK = false;
				}
				if (((newPassFirst.getValue()).equals(newPassSecond.getValue())) && (oldPassOK == true)) {
					if (newPassFirst.getValue().equals("")) {
						new Notification("Error","Give a new password!",Type.ERROR_MESSAGE).show(Page.getCurrent());
						newPassFirst.addValidator(new StringLengthValidator(
								"Password must not be empty",
							    1, 10, true));
						newPassSecond.addValidator(new StringLengthValidator(
								"Password must not be empty",
							    1, 10, true));
					} else {
						changeSuccess = UserService.changePassword(u.getName(), newPassFirst.getValue());
						if (changeSuccess) {
							new Notification("Password change", "Success!").show(Page.getCurrent());								
						} else {
							new Notification("Password change", "Something was wrong!", Type.ERROR_MESSAGE).show(Page.getCurrent());;
						}
						newPassFirst.removeAllValidators();
						newPassSecond.removeAllValidators();
					}
				} else if (oldPassOK == true){
					new Notification("Error","Please give new password twice correctly!",Type.ERROR_MESSAGE).show(Page.getCurrent());
				}
				oldPassField.setValue("");
				newPassFirst.setValue("");
				newPassSecond.setValue("");
			}};
	}
}
