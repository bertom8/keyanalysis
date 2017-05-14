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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
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
	private final ProgressBar progress = new ProgressBar();
	private ProcessService process = null;
	private File file = null;
	private final NativeSelect lista = null;
	// private Button chooseButton = null;
	private Button loginButton = null;
	private MenuBar profil = null;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = KeyanalysisUI.class)
	public static class Servlet extends VaadinServlet {
		private static final long serialVersionUID = -6490416594396768282L;
	}

	@Override
	protected void init(final VaadinRequest request) {
		this.loggedUser = (User) this.getSession().getAttribute("USER");
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(1800);
		Page.getCurrent().setTitle("Keyanalysis");
		this.createMainUI();
		final Timer ti = new Timer();
		ti.scheduleAtFixedRate(new SchedulerService(), new Date(), 1111111);
		/*
		 * Thread t = new Thread(new SchedulerService()); t.start(); try {
		 * t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
		 */
	}

	/**
	 * 
	 */
	private void createMainUI() {
		Responsive.makeResponsive(this);
		this.createMenu();
		this.addStyleName(ValoTheme.UI_WITH_MENU);
		this.setContent(this.root);
	}

	/**
	 * 
	 */
	private void createMenu() {
		// Title
		final Label title = new Label(Constants.title, ContentMode.HTML);
		title.addContextClickListener(new ContextClickListener() {
			private static final long serialVersionUID = -1268517517501948904L;

			@Override
			public void contextClick(final ContextClickEvent event) {
				if (event.getButton().equals(MouseButton.LEFT)) {
					if (!event.isShiftKey()) {
						KeyanalysisUI.this.getPage().reload();
					}
				}
			}
		});
		title.setId("titleLabel");
		title.addStyleName("valo-menu-title");
		title.setSizeFull();
		title.setResponsive(true);
		this.root.addMenu(title);
		final Button up = new Button(Constants.uploadButton, new ClickListener() {
			private static final long serialVersionUID = 413498238313269838L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().addWindow(new UploadWindow(Constants.upload));
			}
		});
		up.setId("uploadButtonMenu");
		this.root.addMenu(up);

		final Button twitter = new Button("Get Tweets", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final ProcessService ps = new ProcessService("twitter.txt", "");
				KeyanalysisUI.this.setProcess(ps);
				KeyanalysisUI.this.process.makeCharts();
			}
		});
		twitter.setId("getTweetsButton");
		this.root.addMenu(twitter);

		final Button downloadButton = new Button(Constants.download);
		downloadButton.setId("downloadButton");
		downloadButton.setEnabled(false);
		downloadButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 413498238313269838L;

			@Override
			public void buttonClick(final ClickEvent event) {
				KeyanalysisUI.this.getProcess().getFileResource(downloadButton);
			}
		});
		this.root.addMenu(downloadButton);
		final Button compareButton = new Button(Constants.compare, new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().addWindow(new CompareWindow());
			}
		});
		compareButton.setId("compareButton");
		compareButton.setEnabled(false);
		this.root.addMenu(compareButton);

		this.loginButton = new Button("Login", new ClickListener() {
			private static final long serialVersionUID = -8781451780815490925L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().addWindow(new LoginWindow());
			}
		});
		this.loginButton.setId("loginButton");
		if (this.loggedUser == null) {
			this.root.addMenu(this.loginButton);
		} else {
			this.createProfilMenu();
			this.root.getMenu().addComponent(this.profil);
		}
		// root.addMenu(loginButton);
		/*
		 * Button twit = new Button("Twitter", new ClickListener() { private
		 * static final long serialVersionUID = -8781451780815490925L;
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * //org.keyanalysis.Services.TweetSearcher.saveTweetsFromJson(new
		 * File(VaadinServlet.getCurrent().getServletContext().getRealPath("") +
		 * "world6_9.txt")); //long time = new Date(1463318280004L).getTime();
		 * //long time = new Date(1463307961482L).getTime(); long time = new
		 * Date().getTime() - 10800000*21; for (long i = 0; i < 21; i++) {
		 * //org.keyanalysis.Services.TweetSearcher.searchEngTweets(time);
		 * org.keyanalysis.Services.TweetSearcher.searchHunTweets(time); time +=
		 * 10800000; } //System.out.println(new Date(1463318280004L)); } });
		 * root.addMenu(twit);
		 */
		this.root.getMenu().setExpandRatio(title, 2);
		this.root.getMenu().setExpandRatio(up, 1);
		this.root.getMenu().setExpandRatio(twitter, 1);
		this.root.getMenu().setExpandRatio(downloadButton, 1);
		this.root.getMenu().setExpandRatio(compareButton, 1);
		// root.getMenu().setExpandRatio(twit, 1);
	}

	/**
	 * 
	 * @param notice
	 */
	public void drawProgress(final String notice) {
		this.root.getContent().removeAllComponents();
		final CssLayout progressLayout = new CssLayout();
		progressLayout.setId("progressBarLayout");
		progressLayout.setWidth("100%");
		this.progress.setValue((float) 0.0);
		this.progress.markAsDirty();
		this.progress.setImmediate(true);
		progressLayout.addComponent(this.progress);
		this.root.getContent().addComponent(progressLayout);
		this.progress.setId("progressBar");
		this.progress.setWidth(String.valueOf(this.root.getContent().getWidth() * 3));
		this.drawnotice(progressLayout, notice);
		this.drawCancelButton(progressLayout);
	}

	/**
	 * 
	 * @param layout
	 * @param notice
	 */
	private void drawnotice(final Layout layout, final String notice) {
		final Label noticeLabel = new Label(notice);
		noticeLabel.setId("notice");
		layout.addComponent(noticeLabel);
	}

	private void drawCancelButton(final Layout layout) {
		final Button cancel = new Button("Mégse");
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
		this.root.getMenu().removeComponent(this.loginButton);
		this.root.setImmediate(true);
		this.root.getMenu().addComponent(this.profil);
	}

	public void logOut() {
		VaadinSession.getCurrent().close();
		UI.getCurrent().getPage().setUriFragment("");
		this.loggedUser = null;
		UI.getCurrent().getPage().reload();
	}

	public void createProfilMenu() {
		this.profil = new MenuBar();
		// profil.addStyleName("mybarmenu");
		// layout.addComponent(barmenu);

		// A feedback component
		// final Label selection = new Label("-");
		// layout.addComponent(selection);
		final MenuItem mainProfil = this.profil
				.addItem(((User) VaadinSession.getCurrent().getAttribute("USER")).getName(), null);
		mainProfil.addItem("Profil", new Command() {
			private static final long serialVersionUID = 6133202127278652105L;

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				UI.getCurrent().addWindow(new ProfilWindow());

			}
		});
		mainProfil.addItem("Search", new Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				UI.getCurrent().addWindow(new SearchUserWindow());
			}
		});
		mainProfil.addSeparator();
		mainProfil.addItem("Logout", new Command() {
			private static final long serialVersionUID = -6068622063923346676L;

			@Override
			public void menuSelected(final MenuItem selectedItem) {
				KeyanalysisUI.this.changeLoginButton();
				KeyanalysisUI.this.logOut();
			}
		});

		this.changeLoginButton();
	}

	/**
	 * 
	 */
	public void removeProgress() {
		this.progress.setId("progressBarOut");
		this.root.getContent().removeAllComponents();
		this.root.getContent().markAsDirty();
	}

	/**
	 * 
	 * @return
	 */
	public ProgressBar getProgress() {
		return this.progress;
	}

	/**
	 * 
	 * @return
	 */
	public ValoMenuLayout getRoot() {
		return this.root;
	}

	/**
	 * 
	 * @return
	 */
	public ProcessService getProcess() {
		return this.process;
	}

	/**
	 * 
	 * @param process
	 */
	public void setProcess(final ProcessService process) {
		this.process = process;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public NativeSelect getLista() {
		return this.lista;
	}

	public User getLoggedUser() {
		return this.loggedUser;
	}

	public void setLoggedUser(final User loggedUser) {
		this.loggedUser = loggedUser;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public void setStorage(final Storage storage) {
		this.storage = storage;
	}

	public void downloadButtonEnable(final boolean bool) {
		((Button) this.root.getMenu().getComponent(3)).setEnabled(bool);
	}

	public void compareButtonEnable(final boolean bool) {
		((Button) this.root.getMenu().getComponent(4)).setEnabled(bool);
	}
}
