package org.keyanalysis.View.Windows;

import javax.persistence.EntityManager;

import org.keyanalysis.Model.Log;
import org.keyanalysis.Model.Storage;
import org.keyanalysis.Model.User;
import org.keyanalysis.Services.ProcessService;
import org.keyanalysis.Services.DB.CreateService;
import org.keyanalysis.Services.DB.ListenerService;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class TableCreatorUtility {
	private final String title;
	private final Class<?> containerClass;
	private Table table;
	private JPAContainer<?> jpaContainer;
	private Filter filter;
	private final Filter deletedFilter = new Compare.Equal("deleted", false);
	private TextField searchField;
	private Button addButton;
	private Button rerunButton;
	private Button copyButton;
	private Button removeButton;
	private User user;
	private Window window;

	public TableCreatorUtility(final String title, final Class<?> containerClass) {
		this.title = title;
		this.containerClass = containerClass;
	}

	public TableCreatorUtility(final String title, final Class<?> containerClass, final Window window) {
		this.title = title;
		this.containerClass = containerClass;
		this.window = window;
	}

	public TableCreatorUtility(final String title, final Class<?> containerClass, final User user,
			final Window window) {
		this.title = title;
		this.containerClass = containerClass;
		this.user = user;
		this.window = window;
	}

	public GridLayout createTableLayout() {
		final GridLayout layout = new GridLayout(1, 2);
		layout.setRowExpandRatio(0, 0);
		layout.setRowExpandRatio(1, 1);
		final GridLayout titleGrid = this.createTitleGrid();
		layout.addComponent(titleGrid, 0, 0);
		final EntityManager entityManager = CreateService.createEntityManager();
		this.jpaContainer = JPAContainerFactory.makeNonCached(this.containerClass, entityManager);
		this.jpaContainer.addContainerFilter(this.deletedFilter);
		if (this.containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			this.jpaContainer.addContainerFilter(new Compare.Equal("storage", this.user.getStorage()));
			// System.out.println(user.getStorage().getId());
		}
		this.table = new Table(null, this.jpaContainer);
		this.setTableColumns();
		this.table.setSelectable(true);
		this.table.setMultiSelect(false);
		this.table.setSizeFull();
		this.table.setPageLength(0);
		if (this.containerClass.isAssignableFrom(User.class)) {
			this.table.addItemClickListener(this.getUserItemClickListener());
		}
		if (this.containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			this.table.addItemClickListener(this.getItemItemClickListener());
			this.addButton.addClickListener(ListenerService.getAddButtonListener());
			this.copyButton.addClickListener(ListenerService.getCopyButtonListener(this.table));
			this.rerunButton.addClickListener(ListenerService.getEditButtonListener(this.table));
		}
		if (!this.containerClass.isAssignableFrom(Log.class)) {
			// addButton.addClickListener(ListenerService.getAddButtonListener(jpaContainer));
			this.removeButton.addClickListener(ListenerService.getRemoveButtonListener(this.table));
		}
		layout.addComponent(this.table, 0, 1);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	private GridLayout createTitleGrid() {
		final GridLayout titleGrid = new GridLayout(8, 1);
		titleGrid.setSizeFull();
		titleGrid.setHeight("60px");
		final Label titleLabel = new Label(this.title);
		titleGrid.addComponent(titleLabel, 0, 0);
		titleGrid.setComponentAlignment(titleLabel, Alignment.BOTTOM_LEFT);
		this.addButton = new Button("Add");
		this.copyButton = new Button("Copy");
		this.rerunButton = new Button("Rerun");
		this.removeButton = new Button("Remove");
		this.searchField = new TextField();
		this.searchField.setWidth("250px");
		this.searchField.setHeight("60px");
		final Button searchButton = new Button("Search");
		this.setButtonStyle(this.addButton, "NEW");
		this.setButtonStyle(this.copyButton, "COPY");
		this.setButtonStyle(this.rerunButton, "RERUN");
		this.setButtonStyle(this.removeButton, "REMOVE");
		this.setButtonStyle(searchButton, "SEARCH");
		searchButton.setClickShortcut(KeyCode.ENTER);
		if (!this.containerClass.isAssignableFrom(Log.class)) {
			if (((User) VaadinSession.getCurrent().getAttribute("USER")).equals(this.user)) {
				titleGrid.addComponent(this.addButton, 1, 0);
				titleGrid.addComponent(this.rerunButton, 2, 0);
				titleGrid.addComponent(this.removeButton, 3, 0);
			} else {
				titleGrid.addComponent(this.copyButton, 3, 0);
			}
		}
		searchButton.addClickListener(this.getSearchClickListener());
		titleGrid.addComponent(this.searchField, 6, 0);
		titleGrid.addComponent(searchButton, 7, 0);
		this.setLayoutExpandRatio(titleGrid);
		return titleGrid;
	}

	private ItemClickListener getUserItemClickListener() {
		return new ItemClickListener() {
			private static final long serialVersionUID = 37359382987048352L;

			@Override
			public void itemClick(final ItemClickEvent event) {
				if (event.isDoubleClick()) {
					TableCreatorUtility.this.window.close();
					final User u = new User();
					u.setDeleted((boolean) event.getItem().getItemProperty("deleted").getValue());
					u.setName(event.getItem().getItemProperty("name").getValue().toString());
					u.setPassword(event.getItem().getItemProperty("password").getValue().toString());
					u.setStorage((Storage) event.getItem().getItemProperty("storage").getValue());
					UI.getCurrent().addWindow(new StorageWindow(u));
				}
			}
		};
	}

	private ItemClickListener getItemItemClickListener() {
		return new ItemClickListener() {
			private static final long serialVersionUID = -4939393468242858883L;

			@Override
			public void itemClick(final ItemClickEvent event) {
				if (event.isDoubleClick()) {
					TableCreatorUtility.this.window.close();
					final ProcessService ps = new ProcessService(
							(String) event.getItem().getItemProperty("name").getValue(),
							(String) event.getItem().getItemProperty("filePath").getValue());
					((KeyanalysisUI) UI.getCurrent()).setProcess(ps);
					((KeyanalysisUI) UI.getCurrent()).getProcess().makeCharts();
					((KeyanalysisUI) UI.getCurrent()).downloadButtonEnable(true);
				}
			}
		};
	}

	private void setTableColumns() {
		if (this.containerClass.isAssignableFrom(User.class)) {
			this.table.setVisibleColumns(new Object[] { "name" });
			this.table.setColumnHeaders("Name");
			this.table.setSortContainerPropertyId("name");
			this.table.setSortAscending(true);
		}
		if (this.containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			this.table.setVisibleColumns(new Object[] { "name", "time", "benchmark" });
			this.table.setColumnHeaders("Name", "Time", "Benchmark");
			this.table.setSortContainerPropertyId("time");
			this.table.setSortAscending(false);
		}
		if (this.containerClass.isAssignableFrom(Log.class)) {
			this.table.setVisibleColumns(new Object[] { "username", "action", "time" });
			this.table.setColumnHeaders("Username", "Action", "Time");
			this.table.setSortContainerPropertyId("time");
			this.table.setSortAscending(false);
		}
	}

	private ClickListener getSearchClickListener() {
		return new ClickListener() {
			private static final long serialVersionUID = 5316425090490307717L;

			@Override
			public void buttonClick(final ClickEvent event) {
				TableCreatorUtility.this.jpaContainer.removeAllContainerFilters();
				final Filter[] filterList = new Filter[TableCreatorUtility.this.table.getVisibleColumns().length];
				for (int i = 0; i < TableCreatorUtility.this.table.getVisibleColumns().length; ++i) {
					filterList[i] = new Like(TableCreatorUtility.this.table.getVisibleColumns()[i],
							"%" + TableCreatorUtility.this.searchField.getValue() + "%", false);
				}
				TableCreatorUtility.this.filter = new Or(filterList);
				TableCreatorUtility.this.jpaContainer.addContainerFilter(TableCreatorUtility.this.filter);
				TableCreatorUtility.this.jpaContainer.addContainerFilter(TableCreatorUtility.this.deletedFilter);
			}
		};
	}

	private void setButtonStyle(final Button button, final String desc) {
		button.setWidth("60px");
		button.setHeight("60px");
		button.setStyleName("nopadding noborder");
		button.setDescription(desc);
	}

	private void setLayoutExpandRatio(final GridLayout titleGrid) {
		titleGrid.setColumnExpandRatio(0, 1);
		titleGrid.setColumnExpandRatio(1, 0);
		titleGrid.setColumnExpandRatio(2, 0);
		titleGrid.setColumnExpandRatio(3, 0);
		titleGrid.setColumnExpandRatio(4, 0);
		titleGrid.setColumnExpandRatio(5, 0);
		titleGrid.setColumnExpandRatio(6, 0);
		titleGrid.setColumnExpandRatio(7, 0);
	}
}
