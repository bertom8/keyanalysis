package org.keyanalysis.View.Windows;

import javax.persistence.EntityManager;

import org.keyanalysis.Model.Item;
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
	private final Filter deletedFilter = new Compare.Equal(Messages.getString("TableCreatorUtility.0"), false); //$NON-NLS-1$
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
			this.jpaContainer.addContainerFilter(
					new Compare.Equal(Messages.getString("TableCreatorUtility.1"), this.user.getStorage())); //$NON-NLS-1$
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
		titleGrid.setHeight(Messages.getString("TableCreatorUtility.2")); //$NON-NLS-1$
		final Label titleLabel = new Label(this.title);
		titleGrid.addComponent(titleLabel, 0, 0);
		titleGrid.setComponentAlignment(titleLabel, Alignment.BOTTOM_LEFT);
		this.addButton = new Button(Messages.getString("TableCreatorUtility.3")); //$NON-NLS-1$
		// this.addButton.setWidth("120px");
		this.copyButton = new Button(Messages.getString("TableCreatorUtility.4")); //$NON-NLS-1$
		// this.copyButton.setWidth("120px");
		this.rerunButton = new Button(Messages.getString("TableCreatorUtility.5")); //$NON-NLS-1$
		// this.rerunButton.setWidth("120px");
		this.removeButton = new Button(Messages.getString("TableCreatorUtility.6")); //$NON-NLS-1$
		// this.removeButton.setWidth("120px");
		this.searchField = new TextField();
		this.searchField.setWidth(Messages.getString("TableCreatorUtility.7")); //$NON-NLS-1$
		this.searchField.setHeight(Messages.getString("TableCreatorUtility.8")); //$NON-NLS-1$
		final Button searchButton = new Button(Messages.getString("TableCreatorUtility.9")); //$NON-NLS-1$
		this.setButtonStyle(this.addButton, Messages.getString("TableCreatorUtility.10")); //$NON-NLS-1$
		this.setButtonStyle(this.copyButton, Messages.getString("TableCreatorUtility.11")); //$NON-NLS-1$
		this.setButtonStyle(this.rerunButton, Messages.getString("TableCreatorUtility.12")); //$NON-NLS-1$
		this.setButtonStyle(this.removeButton, Messages.getString("TableCreatorUtility.13")); //$NON-NLS-1$
		this.setButtonStyle(searchButton, Messages.getString("TableCreatorUtility.14")); //$NON-NLS-1$
		searchButton.setClickShortcut(KeyCode.ENTER);
		if (this.containerClass.isAssignableFrom(Item.class)) {
			if (((User) VaadinSession.getCurrent().getAttribute(Messages.getString("TableCreatorUtility.15"))) //$NON-NLS-1$
					.equals(this.user)) {
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
					u.setDeleted((boolean) event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.16")) //$NON-NLS-1$
							.getValue());
					u.setName(event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.17")).getValue() //$NON-NLS-1$
							.toString());
					u.setPassword(event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.18")) //$NON-NLS-1$
							.getValue().toString());
					u.setStorage((Storage) event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.19")) //$NON-NLS-1$
							.getValue());
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
							(String) event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.20")) //$NON-NLS-1$
									.getValue(),
							(String) event.getItem().getItemProperty(Messages.getString("TableCreatorUtility.21")) //$NON-NLS-1$
									.getValue());
					((KeyanalysisUI) UI.getCurrent()).setProcess(ps);
					((KeyanalysisUI) UI.getCurrent()).getProcess().makeCharts();
					((KeyanalysisUI) UI.getCurrent()).downloadButtonEnable(true);
				}
			}
		};
	}

	private void setTableColumns() {
		if (this.containerClass.isAssignableFrom(User.class)) {
			this.table.setVisibleColumns(new Object[] { Messages.getString("TableCreatorUtility.22") }); //$NON-NLS-1$
			this.table.setColumnHeaders(Messages.getString("TableCreatorUtility.23")); //$NON-NLS-1$
			this.table.setSortContainerPropertyId(Messages.getString("TableCreatorUtility.24")); //$NON-NLS-1$
			this.table.setSortAscending(true);
		}
		if (this.containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			this.table.setVisibleColumns(new Object[] { Messages.getString("TableCreatorUtility.25"), //$NON-NLS-1$
					Messages.getString("TableCreatorUtility.26"), Messages.getString("TableCreatorUtility.27") }); //$NON-NLS-1$ //$NON-NLS-2$
			this.table.setColumnHeaders(Messages.getString("TableCreatorUtility.28"), //$NON-NLS-1$
					Messages.getString("TableCreatorUtility.29"), Messages.getString("TableCreatorUtility.30")); //$NON-NLS-1$ //$NON-NLS-2$
			this.table.setSortContainerPropertyId(Messages.getString("TableCreatorUtility.31")); //$NON-NLS-1$
			this.table.setSortAscending(false);
		}
		if (this.containerClass.isAssignableFrom(Log.class)) {
			this.table.setVisibleColumns(new Object[] { Messages.getString("TableCreatorUtility.32"), //$NON-NLS-1$
					Messages.getString("TableCreatorUtility.33"), Messages.getString("TableCreatorUtility.34") }); //$NON-NLS-1$ //$NON-NLS-2$
			this.table.setColumnHeaders(Messages.getString("TableCreatorUtility.35"), //$NON-NLS-1$
					Messages.getString("TableCreatorUtility.36"), Messages.getString("TableCreatorUtility.37")); //$NON-NLS-1$ //$NON-NLS-2$
			this.table.setSortContainerPropertyId(Messages.getString("TableCreatorUtility.38")); //$NON-NLS-1$
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
							Messages.getString("TableCreatorUtility.39") //$NON-NLS-1$
									+ TableCreatorUtility.this.searchField.getValue()
									+ Messages.getString("TableCreatorUtility.40"), //$NON-NLS-1$
							false);
				}
				TableCreatorUtility.this.filter = new Or(filterList);
				TableCreatorUtility.this.jpaContainer.addContainerFilter(TableCreatorUtility.this.filter);
				TableCreatorUtility.this.jpaContainer.addContainerFilter(TableCreatorUtility.this.deletedFilter);
			}
		};
	}

	private void setButtonStyle(final Button button, final String desc) {
		button.setWidth(Messages.getString("TableCreatorUtility.41")); //$NON-NLS-1$
		button.setHeight(Messages.getString("TableCreatorUtility.42")); //$NON-NLS-1$
		button.setStyleName(Messages.getString("TableCreatorUtility.43")); //$NON-NLS-1$
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
