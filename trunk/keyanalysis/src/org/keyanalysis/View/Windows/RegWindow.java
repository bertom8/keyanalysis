package org.keyanalysis.View.Windows;

import org.keyanalysis.Model.User;
import org.keyanalysis.Services.DB.LoginService;
import org.keyanalysis.Services.DB.StorageService;
import org.keyanalysis.Services.DB.UserService;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class RegWindow extends Window {
	private static final long serialVersionUID = -9216254227318947530L;

	public RegWindow() {
		super();
		setModal(true);
		setDraggable(true);
		center();
		final VerticalLayout mainLayout = new VerticalLayout();
	    //mainLayout.setWidth(Constants.SIZE_100_PERCENT);
	    mainLayout.setMargin(true);
	    mainLayout.setSpacing(true);
	    final HorizontalLayout loginHorizontalLayout = new HorizontalLayout();
	    loginHorizontalLayout.setMargin(true);
	    loginHorizontalLayout.setId("ID_LOGIN");
	    final VerticalLayout loginLayout = new VerticalLayout();
	    loginLayout.setWidth(null);
	    loginLayout.setSpacing(true);
	    final TextField usernameTextField = new TextField("USERNAME");
	    usernameTextField.setWidth("300px");
	    usernameTextField.setHeight("30px");
	    usernameTextField.focus();
	    loginLayout.addComponent(usernameTextField);
	    final PasswordField passwordTextField = new PasswordField("PASSWORD");
	    passwordTextField.setWidth("300px");
	    passwordTextField.setHeight("30px");
	    loginLayout.addComponent(passwordTextField);
	    final PasswordField passwordTextField2 = new PasswordField("PASSWORD AGAIN");
	    passwordTextField2.setWidth("300px");
	    passwordTextField2.setHeight("30px");
	    loginLayout.addComponent(passwordTextField2);
	    final GridLayout buttonGrid = createRegButtonLayout(usernameTextField, passwordTextField, passwordTextField2);
	    loginLayout.addComponent(buttonGrid);
	    loginHorizontalLayout.addComponent(loginLayout);
	    loginHorizontalLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
	    mainLayout.addComponent(loginHorizontalLayout);
	    mainLayout.setComponentAlignment(loginHorizontalLayout, Alignment.MIDDLE_CENTER);
	    setContent(mainLayout);	
	}

	private GridLayout createRegButtonLayout(TextField usernameTextField, PasswordField passwordTextField, PasswordField passwordTextField2) {
		final GridLayout buttonGrid = new GridLayout(2,1);
		buttonGrid.setWidth("300px");
		buttonGrid.setSpacing(true);
		final HorizontalLayout labelPanel = new HorizontalLayout();
		buttonGrid.addComponent(labelPanel);
		buttonGrid.setComponentAlignment(labelPanel, Alignment.MIDDLE_LEFT);
		final Button loginButton = new Button("Registration");
		loginButton.setWidth("150px");
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addClickListener(new ClickListener(){

			private static final long serialVersionUID = -2119674789611252424L;

			@Override
			public void buttonClick(ClickEvent event) {
				usernameTextField.setValue(
						org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(usernameTextField.getValue()));
				passwordTextField.setValue(
						org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(passwordTextField.getValue()));
				passwordTextField2.setValue(
						org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(passwordTextField2.getValue()));
				if (!"".equals(usernameTextField.getValue()) && !"".equals(passwordTextField.getValue()) && !"".equals(passwordTextField2.getValue())) {
					final boolean free = UserService.findAUser(usernameTextField.getValue(), new User());
					if (!free) {
						if (passwordTextField.getValue().equals(passwordTextField2.getValue())) {
							UserService.addUser(usernameTextField.getValue(), LoginService.hashing(passwordTextField.getValue()), StorageService.addStorage());
							UI.getCurrent().getPage().reload();
						}
						else {
							(new Notification("USERNAME/PASS ERROR","Error happend",Type.ERROR_MESSAGE)).show(Page.getCurrent());
						}
					} else {
						(new Notification("USERNAME/PASS ERROR","Error happend2",Type.ERROR_MESSAGE)).show(Page.getCurrent());
					}
				} else {
					(new Notification("USERNAME/PASS ERROR","Missing datas",Type.ERROR_MESSAGE)).show(Page.getCurrent());
				}
			}		
		});
		buttonGrid.addComponent(loginButton);
		buttonGrid.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
		return buttonGrid;
	}
}
