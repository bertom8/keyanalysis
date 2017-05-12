package org.keyanalysis.View;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public class ValoMenuLayout extends CssLayout {
	private static final long serialVersionUID = 5476296142599673447L;
	private HorizontalLayout menu = new HorizontalLayout();
	private CssLayout content = new CssLayout();

	public ValoMenuLayout() {
		setSizeFull();
		menu.setPrimaryStyleName("valo-menu");
		menu.setWidth("100%");
		menu.setHeight("9%");
		menu.setResponsive(true);
		menu.setId("menu");
		content.setPrimaryStyleName("valo-content");
		content.addStyleName("v-scrollable");
		content.setSizeFull();
		addComponents(menu, content);
	}

	public ComponentContainer getContentContainer() {
		return content;
	}

	public void addMenu(Component menu) {
		if (!menu.getStyleName().equals("valo-menu-title"))
			menu.addStyleName("valo-menu-part");
		this.menu.addComponent(menu);
	}

	public HorizontalLayout getMenu() {
		return menu;
	}

	public void setMenu(HorizontalLayout menu) {
		this.menu = menu;
	}

	public CssLayout getContent() {
		return content;
	}

	public void setContent(CssLayout content) {
		this.content = content;
	}
}
