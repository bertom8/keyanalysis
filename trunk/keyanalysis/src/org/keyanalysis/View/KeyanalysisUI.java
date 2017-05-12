package org.keyanalysis.View;

import java.io.File;
import java.util.Date;
import java.util.Timer;

import javax.servlet.annotation.WebServlet;

import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;
import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.ProcessService;
import org.keyanalysis.Services.SchedulerService;
import org.keyanalysis.View.Windows.CompareWindow;
import org.keyanalysis.View.Windows.LoginWindow;
import org.keyanalysis.View.Windows.ProfilWindow;
import org.keyanalysis.View.Windows.SearchUserWindow;
import org.keyanalysis.View.Windows.UploadWindow;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Bereczki Tamás
 *
 */
@Theme("keyanalysis")
@JavaScript({ "http://d3js.org/d3.v4.0.0-alpha.45.min.js", "http://code.jquery.com/jquery-1.8.2.js",
		"http://code.jquery.com/ui/1.9.0/jquery-ui.js" })
public class KeyanalysisUI extends UI {
	private User loggedUser;
	private Storage storage;
	private static final long serialVersionUID = 3203206028748256045L;
	private final ValoMenuLayout root = new ValoMenuLayout();
	private ProgressBar progress = new ProgressBar();
	private ProcessService process = null;
	private File file = null;
	private NativeSelect lista = null;
	//private Button chooseButton = null;
	private Button loginButton = null;
	private MenuBar profil = null;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = KeyanalysisUI.class)
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = -6490416594396768282L;
	}

	@Override
	protected void init(VaadinRequest request) {
		loggedUser = (User) getSession().getAttribute("USER");
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1800);
		Page.getCurrent().setTitle("Keyanalysis");
		createMainUI();
		Timer ti = new Timer();
		ti.scheduleAtFixedRate(new SchedulerService(), new Date(), 1111111);
		/*Thread t = new Thread(new SchedulerService());
		t.start();
		try { 
			t.join(); 
		} catch (InterruptedException e) {
			 e.printStackTrace(); 
		}*/
	}

	/**
	 * 
	 */
	private void createMainUI() {
		Responsive.makeResponsive(this);
		createMenu();
		addStyleName(ValoTheme.UI_WITH_MENU);
		setContent(root);
	}

	/**
	 * 
	 */
	private void createMenu() {
		// Title
		Label title = new Label(Constants.title, ContentMode.HTML);
		title.addContextClickListener(new ContextClickListener() {
			private static final long serialVersionUID = -1268517517501948904L;

			@Override
			public void contextClick(ContextClickEvent event) {
				if (event.getButton().equals(MouseButton.LEFT)) {
					if (!event.isShiftKey()) {
						getPage().reload();
					}
				}
			}
		});
		title.setId("titleLabel");
		title.addStyleName("valo-menu-title");
		title.setSizeFull();
		title.setResponsive(true);
		root.addMenu(title);
		Button up = new Button(Constants.uploadButton, new ClickListener() {
			private static final long serialVersionUID = 413498238313269838L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new UploadWindow(Constants.upload));
			}
		});
		up.setId("uploadButtonMenu");
		root.addMenu(up);
		
		Button twitter = new Button("Get Tweets", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ProcessService ps = new ProcessService("twitter.txt", "");
				setProcess(ps);
				process.makeCharts();
			}
		});
		twitter.setId("getTweetsButton");
		root.addMenu(twitter);
		
		Button downloadButton = new Button(Constants.download);
		downloadButton.setId("downloadButton");
		downloadButton.setEnabled(false);
		downloadButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 413498238313269838L;

			public void buttonClick(ClickEvent event) {
				getProcess().getFileResource(downloadButton);
			}
		});
		root.addMenu(downloadButton);
		Button compareButton = new Button(Constants.compare, new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new CompareWindow());
			}
		});
		compareButton.setId("compareButton");
		root.addMenu(compareButton);
		
		
		loginButton = new Button("Login", new ClickListener() {
			private static final long serialVersionUID = -8781451780815490925L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new LoginWindow());		
			}
		});
		loginButton.setId("loginButton");
		if ( loggedUser == null) {
			root.addMenu(loginButton);
		} else {
			createProfilMenu();
			root.getMenu().addComponent(profil);
		}
		//root.addMenu(loginButton);
		/*Button twit = new Button("Twitter", new ClickListener() {
			private static final long serialVersionUID = -8781451780815490925L;

			@Override
			public void buttonClick(ClickEvent event) {
				//org.keyanalysis.Services.TweetSearcher.saveTweetsFromJson(new File(VaadinServlet.getCurrent().getServletContext().getRealPath("") + "world6_9.txt"));
				//long time = new Date(1463318280004L).getTime();
				//long time = new Date(1463307961482L).getTime();
				long time = new Date().getTime() - 10800000*21;
				for (long i = 0; i < 21; i++) {
					//org.keyanalysis.Services.TweetSearcher.searchEngTweets(time);
					org.keyanalysis.Services.TweetSearcher.searchHunTweets(time);
					time += 10800000;
				}
				//System.out.println(new Date(1463318280004L));
			}
		});
		root.addMenu(twit);*/
		root.getMenu().setExpandRatio(title, 2);
		root.getMenu().setExpandRatio(up, 1);
		root.getMenu().setExpandRatio(twitter, 1);
		root.getMenu().setExpandRatio(downloadButton, 1);
		root.getMenu().setExpandRatio(compareButton, 1);
		//root.getMenu().setExpandRatio(twit, 1);
	}

	/**
	 * 
	 * @param notice
	 */
	public void drawProgress(String notice) {
		root.getContent().removeAllComponents();
		CssLayout progressLayout = new CssLayout();
		progressLayout.setId("progressBarLayout");
		progressLayout.setWidth("100%");
		progress.setValue((float) 0.0);
		progress.markAsDirty();
		progress.setImmediate(true);
		progressLayout.addComponent(progress);
		root.getContent().addComponent(progressLayout);
		progress.setId("progressBar");
		progress.setWidth(String.valueOf(root.getContent().getWidth() * 3));
		drawnotice(progressLayout, notice);
		drawCancelButton(progressLayout);
	}

	/**
	 * 
	 * @param layout
	 * @param notice
	 */
	private void drawnotice(Layout layout, String notice) {
		Label noticeLabel = new Label(notice);
		noticeLabel.setId("notice");
		layout.addComponent(noticeLabel);
	}

	private void drawCancelButton(Layout layout) {
		Button cancel = new Button("Mégse");
		cancel.setId("cancelButton");
		/*
		 * cancel.addClickListener(new ClickListener() { private static final
		 * long serialVersionUID = 4327227460118307235L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * FileUploaderService fup = ((FileUploaderService) ((Upload)
		 * root.getMenu().getComponent(1)) .getReceiver()); if (fup.isDone()) {
		 * fup.getThread().interrupt(); getProcess().endProcess(); } else {
		 * ((Upload) root.getMenu().getComponent(1)).interruptUpload(); }
		 * root.getContent().removeAllComponents(); } });
		 */
		layout.addComponent(cancel);
	}
	
	public void changeLoginButton() {
		root.getMenu().removeComponent(loginButton);
		root.setImmediate(true);
		root.getMenu().addComponent(profil);
	}
	
	public void logOut(){		
		VaadinSession.getCurrent().close();
		UI.getCurrent().getPage().setUriFragment("");
		loggedUser=null;
		UI.getCurrent().getPage().reload();
	}
	
	public void createProfilMenu() {
		profil = new MenuBar();
		//profil.addStyleName("mybarmenu");
		//layout.addComponent(barmenu);

		// A feedback component
		//final Label selection = new Label("-");
		//layout.addComponent(selection);
		MenuItem mainProfil = profil.addItem(((User)VaadinSession.getCurrent().getAttribute("USER")).getName(), null);
		mainProfil.addItem("Profil", new Command() {
			private static final long serialVersionUID = 6133202127278652105L;

			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().addWindow(new ProfilWindow());
				
			}
		});
		mainProfil.addItem("Search", new Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().addWindow(new SearchUserWindow());		
			}
		});
		mainProfil.addSeparator();
		mainProfil.addItem("Logout", new Command() {
			private static final long serialVersionUID = -6068622063923346676L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				changeLoginButton();
				logOut();				
			}
		});
		
		changeLoginButton();
	}

	/**
	 * 
	 */
	public void removeProgress() {
		progress.setId("progressBarOut");
		root.getContent().removeAllComponents();
		root.getContent().markAsDirty();
	}

	/**
	 * 
	 * @return
	 */
	public ProgressBar getProgress() {
		return progress;
	}

	/**
	 * 
	 * @return
	 */
	public ValoMenuLayout getRoot() {
		return root;
	}

	/**
	 * 
	 * @return
	 */
	public ProcessService getProcess() {
		return process;
	}

	/**
	 * 
	 * @param process
	 */
	public void setProcess(ProcessService process) {
		this.process = process;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public NativeSelect getLista() {
		return lista;
	}
	
	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public void downloadButtonEnable(boolean bool) {
		((Button)root.getMenu().getComponent(3)).setEnabled(bool);
	}
}

