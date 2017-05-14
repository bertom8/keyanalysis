package org.keyanalysis.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.time.Instant;

import org.keyanalysis.View.KeyanalysisUI;
import org.keyanalysis.View.Windows.UploadWindow;

import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public class FileUploaderService
		implements Receiver, StartedListener, ProgressListener, SucceededListener, FailedListener {

	private static final long serialVersionUID = -4147314482814608185L;
	Thread t = null;
	KeyanalysisUI ui = (KeyanalysisUI) UI.getCurrent();
	UploadWindow window = null;
	File file;
	String filePath = VaadinServlet.getCurrent().getServletContext().getRealPath("");
	Upload upload;
	String mime = new String();
	private static final long uploadSize = Constants.uploadSizeMB * 1024 * 1024;
	BufferedReader br;
	private String filename;
	boolean done = false;

	public FileUploaderService(final Window window) {
		this.window = (UploadWindow) window;
	}

	@Override
	public OutputStream receiveUpload(final String filename, final String MIMEType) {
		this.mime = MIMEType;
		this.filename = filename;
		FileOutputStream fos = null;
		final boolean loggedIn = VaadinSession.getCurrent().getAttribute("USER") != null;
		if (loggedIn) {
			final File dir = new File(this.filePath + "uploadDatas");
			if (!dir.exists()) {
				dir.mkdir();
			}
		} else {
			final File dir = new File(this.filePath + "uploadDatas/tmp");
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		final String pathToFile = this.filePath + "uploadDatas/" + (!loggedIn ? "/tmp/" : "")
				+ DigestService.getMD5Hash(filename + Date.from(Instant.now()).getTime()) + "/";
		new File(pathToFile).mkdir();
		this.filePath = pathToFile;
		this.file = new File(this.filePath + filename);
		this.ui.setFile(this.file);
		try {
			fos = new FileOutputStream(this.file);
		} catch (final java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return fos;
	}

	@Override
	public void uploadSucceeded(final Upload.SucceededEvent event) {
		this.done = true;
		this.ui.getProgress().setValue((float) 1.0);
		this.ui.removeProgress();
		Notification.show(Constants.uploadDone, Notification.Type.HUMANIZED_MESSAGE);
	}

	@Override
	public void uploadFailed(final FailedEvent event) {
		Notification.show(Constants.uploadUndone, Notification.Type.ERROR_MESSAGE);
		this.ui.getProgress().setValue((float) 0);
		this.ui.getRoot().getContent().removeAllComponents();
	}

	@Override
	public void uploadStarted(final StartedEvent event) {
		if (event.getContentLength() > uploadSize) {
			Notification.show(Constants.bigFile, Notification.Type.ERROR_MESSAGE);
			event.getUpload().interruptUpload();
		}
		switch (event.getFilename().substring(event.getFilename().lastIndexOf("."), event.getFilename().length())) {
		case ".txt":
			break;
		case ".csv":
			this.window.getHasHeader().setVisible(true);
			final TextField delimit = this.window.getDelimiter();
			delimit.setVisible(true);
			delimit.setEnabled(true);
			this.window.setDelimiter(delimit);
			break;
		case ".json":
			final TextField tf1 = this.window.getFormat();
			tf1.setCaption(Constants.whichTag);
			tf1.setVisible(true);
			tf1.setEnabled(true);
			this.window.setFormat(tf1);
			break;
		default:
			Notification.show(Constants.uploadUndone, Constants.wrongFileformat, Notification.Type.ERROR_MESSAGE);
			event.getUpload().interruptUpload();
			break;
		}
		this.upload = event.getUpload();
		this.ui.drawProgress(Constants.uploading);
	}

	@Override
	public void updateProgress(final long readBytes, final long contentLength) {
		if (readBytes > uploadSize) {
			Notification.show(Constants.bigFile, Notification.Type.ERROR_MESSAGE);
			this.upload.interruptUpload();
		}

		final float progressBarPercent = readBytes / contentLength;
		this.ui.getProgress().setValue(progressBarPercent);
		this.ui.getProgress().markAsDirty();
	}

	public boolean isDone() {
		return this.done;
	}

	public void setDone(final boolean value) {
		this.done = value;
	}

	public Thread getThread() {
		return this.t;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(final String filepath) {
		this.filePath = filepath;
	}
}
