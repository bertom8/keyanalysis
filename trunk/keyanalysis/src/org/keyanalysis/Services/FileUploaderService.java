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
	KeyanalysisUI ui = (KeyanalysisUI)UI.getCurrent();
	UploadWindow window = null;
	File file;
	String filePath = VaadinServlet.getCurrent().getServletContext().getRealPath("");
	Upload upload;
	String mime = new String();
	private static final long uploadSize = Constants.uploadSizeMB * 1024 * 1024;
	BufferedReader br;
	private String filename;
	boolean done = false;

	public FileUploaderService(Window window) {
		this.window = (UploadWindow) window;
	}

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		this.mime = MIMEType;
		this.filename = filename;
		FileOutputStream fos = null;
		boolean loggedIn = VaadinSession.getCurrent().getAttribute("USER") != null;
		if (loggedIn) {
			File dir = new File(filePath + "uploadDatas");
			if (!dir.exists())
				dir.mkdir();
		} else {
			File dir = new File(filePath + "uploadDatas/tmp");
			if (!dir.exists())
				dir.mkdirs();
		}
		String pathToFile = filePath + "uploadDatas/" + (!loggedIn ? "/tmp/" : "") + DigestService.getMD5Hash(filename + Date.from(Instant.now()).getTime())
							+ "/";
		new File(pathToFile).mkdir();
		filePath = pathToFile;
		file = new File(filePath + filename);
		ui.setFile(file);
		try {
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return fos;
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent event) {
		done = true;
		ui.getProgress().setValue((float) 1.0);
		ui.removeProgress();
		Notification.show(Constants.uploadDone, Notification.Type.HUMANIZED_MESSAGE);
	}

	@Override
	public void uploadFailed(FailedEvent event) {
		Notification.show(Constants.uploadUndone, Notification.Type.ERROR_MESSAGE);
		ui.getProgress().setValue((float) 0);
		ui.getRoot().getContent().removeAllComponents();
	}

	@Override
	public void uploadStarted(StartedEvent event) {
		if (event.getContentLength() > uploadSize) {
			Notification.show(Constants.bigFile, Notification.Type.ERROR_MESSAGE);
			event.getUpload().interruptUpload();
		}
		switch (event.getFilename().substring(event.getFilename().lastIndexOf("."), event.getFilename().length())) {
			case ".txt": 
				break;
			case ".csv": 
				window.getHasHeader().setVisible(true);
				TextField delimit = window.getDelimiter();
				delimit.setVisible(true);
				delimit.setEnabled(true);
				window.setDelimiter(delimit);
				break;
			case ".json": 
				TextField tf1 = window.getFormat();
				tf1.setCaption(Constants.whichTag);
				tf1.setVisible(true);
				tf1.setEnabled(true);
				window.setFormat(tf1);
				break;
			default: 
				Notification.show(Constants.uploadUndone, Constants.wrongFileformat, Notification.Type.ERROR_MESSAGE);
				event.getUpload().interruptUpload();
				break;
		}
		upload = event.getUpload();
		ui.drawProgress(Constants.uploading);
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		if (readBytes > uploadSize) {
			Notification.show(Constants.bigFile, Notification.Type.ERROR_MESSAGE);
			upload.interruptUpload();
		}

		float progressBarPercent = readBytes / contentLength;
		ui.getProgress().setValue(progressBarPercent);
		ui.getProgress().markAsDirty();
	}
	
	public boolean isDone() {
		return done;
	}

	public Thread getThread() {
		return t;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFilePath() {
		return filePath;
	}
}
