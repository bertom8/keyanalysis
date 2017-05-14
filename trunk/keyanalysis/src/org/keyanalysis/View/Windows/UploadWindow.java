package org.keyanalysis.View.Windows;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.keyanalysis.Model.Item;
import org.keyanalysis.Services.Constants;
import org.keyanalysis.Services.DigestService;
import org.keyanalysis.Services.FileUploaderService;
import org.keyanalysis.Services.ProcessService;
import org.keyanalysis.Services.DB.LogService;
import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UploadWindow extends Window {
	private static final long serialVersionUID = -6573314797754683428L;
	private TextField format = new TextField();
	private TextField delimiter = new TextField();
	private CheckBox hasHeader;
	private TextField key = null;
	private double HammingDist = 0;

	public UploadWindow(final String title) {
		super(title);
		this.createGrid(null);
	}

	public UploadWindow(final String title, final Item item) {
		super(title);
		this.createGrid(item);
	}

	private void createGrid(final Item item) {
		this.setClosable(true);
		this.setResizable(false);
		this.setWindowMode(WindowMode.NORMAL);
		this.setModal(true);
		this.setResponsive(true);
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		this.setContent(layout);

		final FileUploaderService uploaderService = new FileUploaderService(this);
		if (item == null) {
			final Upload up = new Upload();
			up.setId("uploadForm");
			up.setButtonCaption(Constants.upload);
			up.setResponsive(true);
			up.setReceiver(uploaderService);
			up.addStartedListener(uploaderService);
			up.addProgressListener(uploaderService);
			up.addSucceededListener(uploaderService);
			up.addFailedListener(uploaderService);
			up.setSizeFull();

			layout.addComponent(up);
		} else {
			final String pathToFile = VaadinServlet.getCurrent().getServletContext().getRealPath("") + "uploadDatas/"
					+ DigestService.getMD5Hash(item.getName() + Date.from(Instant.now()).getTime()) + "/";
			new File(pathToFile).mkdir();
			uploaderService.setFilename(item.getName());
			uploaderService.setFilePath(pathToFile);
			uploaderService.setDone(true);
			try {
				FileUtils.copyFile(new File(item.getFilePath() + item.getName()),
						new File(pathToFile + item.getName()));
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}

		this.hasHeader = new CheckBox(Constants.hasTag, false);
		this.hasHeader.setVisible(false);
		this.hasHeader.addValueChangeListener(e -> {
			if ((boolean) e.getProperty().getValue() == true) {
				this.format.setValue("");
				this.format.setVisible(true);
				this.format.setEnabled(true);
			} else {
				this.format.setValue("");
				this.format.setVisible(false);
				this.format.setEnabled(false);
			}
		});
		layout.addComponent(this.hasHeader);
		this.format.setVisible(false);
		layout.addComponent(this.format);
		this.delimiter.setVisible(false);
		this.delimiter.setCaption(Constants.delimiter);
		// delimiter.setInputPrompt("");
		layout.addComponent(this.delimiter);

		this.key = new TextField();
		this.key.setValue(null);
		this.key.setNullRepresentation("");
		this.key.setNullSettingAllowed(true);
		this.key.addValidator(new StringLengthValidator(Constants.notCorrectKey, 32, 32, true));
		this.key.setId("keyInput");
		this.key.setImmediate(true);
		this.key.setMaxLength(Constants.keyMaxLength);
		this.key.setWidth("95%");
		this.key.setInputPrompt(Constants.keyFieldPrompt);
		layout.addComponent(this.key);

		layout.addComponent(new Label("<hr/>", ContentMode.HTML));
		final TextField hamming = new TextField(Constants.wantedHamming);
		layout.addComponent(hamming);
		final HorizontalLayout hl = new HorizontalLayout();
		hl.setId("uploadControllerButtons");
		hl.setMargin(true);

		final Button done = new Button(Constants.done, new ClickListener() {
			private static final long serialVersionUID = -9118926051039751583L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (uploaderService.isDone()) {
					UploadWindow.this.key.setValue(
							org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(UploadWindow.this.key.getValue()));
					hamming.setValue(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(hamming.getValue()));
					if (hamming.getValue() != null && !("".equals(hamming.getValue()))) {
						if (!hamming.getValue().matches("[0][\\.|\\,]\\d+")) {
							new Notification("Error", "Give a correct Hamming value!", Type.ERROR_MESSAGE)
									.show(Page.getCurrent());
							return;
						}

						if ((Double.parseDouble(hamming.getValue()) > 0
								&& Double.parseDouble(hamming.getValue()) < 1)) {
							UploadWindow.this.HammingDist = Double.parseDouble(hamming.getValue());
						}
					}

					final ProcessService ps = new ProcessService(uploaderService.getFilename(),
							UploadWindow.this.getWindow(), uploaderService.getFilePath());
					((KeyanalysisUI) UI.getCurrent()).setProcess(ps);
					((KeyanalysisUI) UI.getCurrent()).getProcess().run();
					LogService.AddLogEntry("Successed upload", null, "Item");

					UploadWindow.this.close();
				}
			}
		});
		done.setStyleName("uploadButton");

		final Button cancel = new Button(Constants.cancel, new ClickListener() {
			private static final long serialVersionUID = 1489239772039007250L;

			@Override
			public void buttonClick(final ClickEvent event) {
				if (uploaderService.isDone()) {
					if (new File(uploaderService.getFilePath() + uploaderService.getFilename()).delete()) {
						UploadWindow.this.close();
					} else {
						throw new UnsupportedOperationException();
					}
				}
				UploadWindow.this.close();
			}
		});
		cancel.setStyleName("uploadButton");

		hl.addComponents(cancel, done);
		layout.addComponent(hl);
	}

	public CheckBox getHasHeader() {
		return this.hasHeader;
	}

	public TextField getDelimiter() {
		return this.delimiter;
	}

	public void setDelimiter(final TextField tf) {
		this.delimiter = tf;
	}

	public TextField getKey() {
		return this.key;
	}

	public TextField getFormat() {
		return this.format;
	}

	private UploadWindow getWindow() {
		return this;
	}

	public void setFormat(final TextField tf) {
		this.format = tf;
	}

	public double getHammingDist() {
		return this.HammingDist;
	}
}
