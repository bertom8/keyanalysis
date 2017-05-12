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
	    final GridLayout buttonGrid = createLoginButtonLayout(usernameTextField, passwordTextField);
	    loginLayout.addComponent(buttonGrid);
	    loginHorizontalLayout.addComponent(loginLayout);
	    loginHorizontalLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
	    mainLayout.addComponent(loginHorizontalLayout);
	    mainLayout.setComponentAlignment(loginHorizontalLayout, Alignment.MIDDLE_CENTER);
	    setContent(mainLayout);	
	}
	
	private GridLayout createLoginButtonLayout(final TextField usernameTextField, final PasswordField passwordTextField){
		u = (User) VaadinSession.getCurrent().getAttribute("USER");
		final GridLayout buttonGrid = new GridLayout(2,2);
		buttonGrid.setWidth("300px");
		buttonGrid.setSpacing(true);
		final HorizontalLayout labelPanel = new HorizontalLayout();
		buttonGrid.addComponent(labelPanel);
		buttonGrid.setComponentAlignment(labelPanel, Alignment.MIDDLE_LEFT);
		final Button loginButton = new Button("LOGIN");
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
				if (!"".equals(usernameTextField.getValue()) && !"".equals(passwordTextField.getValue())){
					//TODO: getSafeValues(usernameTextField.getValue())
					u = new User();
					final boolean success = LoginService.signIn(usernameTextField.getValue(), passwordTextField.getValue(),u);
					if (success){						
						getSession().setAttribute("USER", u);
						((KeyanalysisUI)UI.getCurrent()).setLoggedUser(u);
						LogService.AddLogEntry("Login Success", null, "Login");
						UI.getCurrent().getPage().reload();
						close();
					} else {
						getSession().setAttribute("USER", u);
						User tried = new User();
						tried.setName(usernameTextField.getValue());
						LogService.AddLogEntry("Login failed", tried, "Login");
						(new Notification("LOGIN ERROR","Error happend",Type.ERROR_MESSAGE)).show(Page.getCurrent());
						//((KeyanalysisUI)UI.getCurrent()).logOut();
					}
					((KeyanalysisUI)UI.getCurrent()).setLoggedUser(u);
				} else {
					(new Notification("LOGIN ERROR","Missing datas",Type.ERROR_MESSAGE)).show(Page.getCurrent());
				}
			}		
		});
		buttonGrid.addComponent(loginButton);
		buttonGrid.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);
		Button link = new Button("Registration", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
				UI.getCurrent().addWindow(new RegWindow());	
				
			}
		});
		buttonGrid.addComponent(link, 1, 1);;
		buttonGrid.setComponentAlignment(link, Alignment.MIDDLE_RIGHT);
		return buttonGrid;
	}
}
