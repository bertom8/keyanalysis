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
		this.setSizeFull();
		this.menu.setPrimaryStyleName("valo-menu");
		this.menu.setWidth("100%");
		this.menu.setHeight("9%");
		this.menu.setResponsive(true);
		this.menu.setId("menu");
		this.content.setPrimaryStyleName("valo-content");
		this.content.addStyleName("v-scrollable");
		this.content.setSizeFull();
		this.addComponents(this.menu, this.content);
	}

	public ComponentContainer getContentContainer() {
		return this.content;
	}

	public void addMenu(final Component menu) {
		if (!menu.getStyleName().equals("valo-menu-title")) {
			menu.addStyleName("valo-menu-part");
		}
		this.menu.addComponent(menu);
	}

	public HorizontalLayout getMenu() {
		return this.menu;
	}

	public void setMenu(final HorizontalLayout menu) {
		this.menu = menu;
	}

	public CssLayout getContent() {
		return this.content;
	}

	public void setContent(final CssLayout content) {
		this.content = content;
	}
}
