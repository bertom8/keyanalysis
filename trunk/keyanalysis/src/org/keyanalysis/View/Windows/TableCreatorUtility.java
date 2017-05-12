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
import com.vaadin.server.ThemeResource;
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
	private Filter deletedFilter = new Compare.Equal("deleted", false);
	private TextField searchField;
	private Button addButton;
	private Button editButton;
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
	
	public TableCreatorUtility(final String title, final Class<?> containerClass, final User user, final Window window) {
		this.title = title;
		this.containerClass = containerClass;
		this.user = user;
		this.window = window;
	}
	
	public GridLayout createTableLayout(){
		final GridLayout layout = new GridLayout(1,2);
		layout.setRowExpandRatio(0, 0);
		layout.setRowExpandRatio(1, 1);
		final GridLayout titleGrid = createTitleGrid();
		layout.addComponent(titleGrid,0,0);
		final EntityManager entityManager = CreateService.createEntityManager();
		jpaContainer = JPAContainerFactory.makeNonCached(containerClass, entityManager);
		jpaContainer.addContainerFilter(deletedFilter);
		if (containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			jpaContainer.addContainerFilter(new Compare.Equal("storage", user.getStorage()));
			System.out.println(user.getStorage().getId());
		}
		table = new Table(null,jpaContainer);		
		setTableColumns();	
		table.setSelectable(true);
		table.setMultiSelect(false);
		table.setSizeFull();
		table.setPageLength(0);	
		if (containerClass.isAssignableFrom(User.class)) {
			table.addItemClickListener(getUserItemClickListener());
		}
		if (containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			table.addItemClickListener(getItemItemClickListener());
			addButton.addClickListener(ListenerService.getAddButtonListener());
			copyButton.addClickListener(ListenerService.getCopyButtonListener(table));
		}
		if (!containerClass.isAssignableFrom(Log.class)) {
			//addButton.addClickListener(ListenerService.getAddButtonListener(jpaContainer));	
			//editButton.addClickListener(ListenerService.getEditButtonListener(table,containerClass));
			removeButton.addClickListener(ListenerService.getRemoveButtonListener(table));
		}
		layout.addComponent(table,0,1);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;	
	}

	private GridLayout createTitleGrid(){
		final GridLayout titleGrid = new GridLayout(8,1);
		titleGrid.setSizeFull();
		titleGrid.setHeight("60px");
		final Label titleLabel = new Label(title);
		titleGrid.addComponent(titleLabel, 0, 0);
		titleGrid.setComponentAlignment(titleLabel, Alignment.BOTTOM_LEFT);
		addButton = new Button(new ThemeResource("../images/upload.png"));
		//addButton = new Button("ADD");
		copyButton = new Button(new ThemeResource("../images/add.png"));
		//editButton = new Button(new ThemeResource(Constants.ICON_EDIT_LOCATION));
		removeButton = new Button(new ThemeResource("../images/remove.png"));
		//removeButton = new Button("REMOVE");
		searchField = new TextField();
		searchField.setWidth("250px");
		searchField.setHeight("60px");
		final Button searchButton = new Button(new ThemeResource("../images/search.png"));
		//final Button searchButton = new Button("SEARCH");
		setButtonStyle(addButton,"NEW");
		setButtonStyle(copyButton, "COPY");
		//setButtonStyle(editButton,Constants.ICON_EDIT_CAPTION);
		setButtonStyle(removeButton,"REMOVE");
		setButtonStyle(searchButton,"SEARCH");
		searchButton.setClickShortcut(KeyCode.ENTER);
		if (!containerClass.isAssignableFrom(Log.class)){
			if (((User)VaadinSession.getCurrent().getAttribute("USER")).equals(user)) {
				titleGrid.addComponent(addButton, 2, 0);
				titleGrid.addComponent(removeButton, 3, 0);
			} else {
				titleGrid.addComponent(copyButton, 3, 0);
			}
			/*titleGrid.addComponent(addButton, 1, 0);
			titleGrid.addComponent(copyButton, 2, 0);
			//titleGrid.addComponent(editButton, 2, 0);
			titleGrid.addComponent(removeButton, 3, 0);*/
		}
		searchButton.addClickListener(getSearchClickListener());
		titleGrid.addComponent(searchField, 6, 0);
		titleGrid.addComponent(searchButton, 7, 0);
		setLayoutExpandRatio(titleGrid);
		return titleGrid;
	}
	
	private ItemClickListener getUserItemClickListener(){
		return new ItemClickListener() {
			private static final long serialVersionUID = 37359382987048352L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()){
					window.close();
					User u = new User();
					u.setDeleted((boolean)event.getItem().getItemProperty("deleted").getValue());
					u.setName(event.getItem().getItemProperty("name").getValue().toString());
					u.setPassword(event.getItem().getItemProperty("password").getValue().toString());
					u.setStorage((Storage)event.getItem().getItemProperty("storage").getValue());
					UI.getCurrent().addWindow(new StorageWindow(u));
				}
			}
		};
	}
	
	private ItemClickListener getItemItemClickListener() {
		return new ItemClickListener() {
			private static final long serialVersionUID = -4939393468242858883L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()){
					window.close();
					ProcessService ps = new ProcessService((String)event.getItem().getItemProperty("name").getValue(), (String)event.getItem().getItemProperty("filePath").getValue());
					((KeyanalysisUI)UI.getCurrent()).setProcess(ps);
					((KeyanalysisUI)UI.getCurrent()).getProcess().makeCharts();
					((KeyanalysisUI)UI.getCurrent()).downloadButtonEnable(true);
				}
			}
		};
	}
	
	private void setTableColumns(){
		if (containerClass.isAssignableFrom(User.class)) {
			table.setVisibleColumns(new Object[]{"name"});
			table.setColumnHeaders("Name");
			table.setSortContainerPropertyId("name");
			table.setSortAscending(true);
		}
		if (containerClass.isAssignableFrom(org.keyanalysis.Model.Item.class)) {
			table.setVisibleColumns(new Object[]{"name", "time", "benchmark"});
			table.setColumnHeaders("Name", "Time", "Benchmark");
			table.setSortContainerPropertyId("time");
			table.setSortAscending(false);
		}
		if (containerClass.isAssignableFrom(Log.class)) {
			table.setVisibleColumns(new Object[]{"username","action","time"});
			table.setColumnHeaders("Username","Action","Time");
			table.setSortContainerPropertyId("time");
			table.setSortAscending(false);
		}		
	}
	
	private ClickListener getSearchClickListener(){
		return new ClickListener() {
			private static final long serialVersionUID = 5316425090490307717L;

			@Override
			public void buttonClick(ClickEvent event) {
				jpaContainer.removeAllContainerFilters();
				final Filter[] filterList = new Filter[table.getVisibleColumns().length];
				for(int i=0;i<table.getVisibleColumns().length;++i){
					filterList[i] = new Like(table.getVisibleColumns()[i],"%"+searchField.getValue()+"%",false);
				}
				filter = new Or(filterList);
				jpaContainer.addContainerFilter(filter);
				jpaContainer.addContainerFilter(deletedFilter);
			}
		};
	}
	
	private void setButtonStyle(final Button button, final String desc){
		button.setWidth("60px");
		button.setHeight("60px");
		button.setStyleName("nopadding noborder");
		button.setDescription(desc);
	}
	
	private void setLayoutExpandRatio(final GridLayout titleGrid){
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
