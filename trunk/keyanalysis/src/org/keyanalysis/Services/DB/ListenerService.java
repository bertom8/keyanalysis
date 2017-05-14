package org.keyanalysis.Services.DB;

import java.sql.Date;
import java.time.Instant;

import org.keyanalysis.Model.Item;
import org.keyanalysis.Model.User;
import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.DigestService;
import org.keyanalysis.Services.ProcessService;
import org.keyanalysis.View.Windows.UploadWindow;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class ListenerService {
	/*
	 * Listener service of the remove button
	 */
	@SuppressWarnings("unchecked")
	public static <T> ClickListener getRemoveButtonListener(final Table table) {
		final JPAContainer<T> jpa = (JPAContainer<T>) table.getContainerDataSource();
		return new ClickListener() {
			private static final long serialVersionUID = -1441002219219016234L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (table.getValue() == null) {
					new Notification("ERROR", "No selected item").show(Page.getCurrent());
				} else {
					final Object itemId = table.getValue();

					ConfirmDialog.show(UI.getCurrent(), "Delete", "Are you sure you want to delete the seleceted item?",
							"OK", "CANCEL", new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(final ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										jpa.getItem(itemId).getItemProperty("deleted").setValue(true);
										jpa.commit();
										jpa.refreshItem(itemId);
										ProcessService.removeDir(
												(String) jpa.getItem(itemId).getItemProperty("filePath").getValue());
										LogService.AddLogEntry("Removing the " + itemId + " item", null, "Item");
										new Notification("Done!", "Item deleted").show(Page.getCurrent());
									}
								}
							});
				}

			}
		};
	}

	/**
	 * Listener service of the copy button
	 * 
	 * @param table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> ClickListener getCopyButtonListener(final Table table) {
		final JPAContainer<T> jpa = (JPAContainer<T>) table.getContainerDataSource();
		return new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (table.getValue() == null) {
					new Notification("ERROR", "No selected item").show(Page.getCurrent());
				} else {
					final Object itemId = table.getValue();
					final Item item = (Item) jpa.getItem(itemId).getEntity();
					item.setStorage(((User) VaadinSession.getCurrent().getAttribute("USER")).getStorage());
					ProcessService.copyDir(item.getFilePath(),
							item.getFilePath().substring(0, item.getFilePath().lastIndexOf("uploadDatas/") + 12)
									+ DigestService.getMD5Hash(item.getName() + Date.from(Instant.now()).getTime()));
					ItemService.addItem(item);
				}

			}
		};
	}

	public static ClickListener getAddButtonListener() {
		return new ClickListener() {
			private static final long serialVersionUID = -1568226672186565732L;

			@Override
			public void buttonClick(final ClickEvent event) {
				UI.getCurrent().addWindow(new UploadWindow(Constants.upload));

			}
		};
	}

	@SuppressWarnings("unchecked")
	public static ClickListener getEditButtonListener(final Table table) {
		final JPAContainer<Item> jpa = (JPAContainer<Item>) table.getContainerDataSource();
		return new ClickListener() {
			private static final long serialVersionUID = -1568226672186565732L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (table.getValue() == null) {
					new Notification("ERROR", "No selected item").show(Page.getCurrent());
				} else {
					UI.getCurrent()
							.addWindow(new UploadWindow(Constants.upload, jpa.getItem(table.getValue()).getEntity()));
				}
			}
		};
	}

}
