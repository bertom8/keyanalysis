package org.keyanalysis.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.keyanalysis.Services.DB.ItemService;
import org.keyanalysis.View.Charts;
import org.keyanalysis.View.KeyanalysisUI;
import org.keyanalysis.View.Windows.UploadWindow;

import com.google.gson.JsonObject;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public class ProcessService implements Runnable {
	private File file;
	private String keyString = null;
	private SecretKey key = null;
	private UploadWindow window = null;
	private String filePath = VaadinServlet.getCurrent().getServletContext().getRealPath("");
	private String filename;
	private int columnNumber;
	private final Charts c = new Charts();

	public ProcessService(final String filename, final UploadWindow window, final String newFilePath) {
		if (filename == null || filename.isEmpty()) {
			Notification.show(Constants.errorOccured + filename, Notification.Type.ERROR_MESSAGE);
			return;
		}
		this.keyString = window.getKey().getValue();
		this.window = window;
		this.filename = filename;
		if (this.keyString != null && !this.keyString.isEmpty()) {
			this.key = new SecretKeySpec(this.keyString.getBytes(), "AES");
		}
		this.filePath = newFilePath;
		this.file = new File(this.filePath + filename);
	}

	public ProcessService(final String filename, final String newFilePath) {
		this.filename = filename;
		this.filePath = newFilePath;
		this.file = new File(this.filePath + filename);
	}

	@Override
	public void run() {
		if (this.file.isFile() && this.file.exists()) {
			// ((KeyanalysisUI)
			// UI.getCurrent()).drawProgress("Feldolgozás...");
			// read();
			// if (!texts.isEmpty())
			this.work();
		}
	}

	public void runWithTweets() {
		if (this.file.isFile() && this.file.exists()) {
			this.encode();
		}
	}

	/**
	 * Read the file
	 * 
	 * @param filename
	 */
	private void getFile(final String filename, final ProgressBar progress) {
		if (filename == null || filename.isEmpty()) {
			Notification.show(Constants.errorOccured + filename, Notification.Type.ERROR_MESSAGE);
			return;
		}
		this.file = new File(this.filePath + filename);
	}

	/**
	 * 
	 * @param line
	 * @param firstRow
	 * @return
	 */
	private String read(final String line, final boolean firstRow) {

		switch (this.file.getName().substring(this.file.getName().lastIndexOf("."), this.file.getName().length())) {
		case ".csv":
			if (firstRow && this.window.getHasHeader().getValue()) {
				this.columnNumber = this.readHeader(line);
				return null;
			}
			return this.readCsv(line);
		case ".json":
			return this.readJson(line);
		case ".txt":
			return this.readTxt(line);
		default:
			return null;
		}
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private String readTxt(final String line) {
		return line;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private int readHeader(final String line) {
		final List<Character> chars = new ArrayList<>();
		final char[] c = line.substring(0, line.lastIndexOf(this.window.getFormat().getValue(), 0)).toCharArray();
		for (final char element : c) {
			chars.add(element);
		}
		return (int) chars.stream().filter(string -> string.equals(this.window.getDelimiter().getValue())).count();
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private String readCsv(final String line) {
		return line.substring(line.indexOf(line.split(this.window.getDelimiter().getValue())[this.columnNumber]),
				line.length() - 1);
	}

	/**
	 * Beolvassuk és értelmezzük, feldolgozzuk a kapott json fájlt a file
	 * Listünkbe
	 */
	private String readJson(final String line) {
		// return JsonParser.getString(file, window.getFormat().getValue());
		final ReadTweets p = new ReadTweets();
		final JsonObject o = p.readFromString(line);
		if (o == null) {
			return null;
		}
		final String text = o.get(this.window.getFormat().getValue()).getAsString().replace('\n', ' ')
				.replace('\t', ' ').replace('"', ' ');
		return text;
	}

	/**
	 * Kódolások és hashelések elvégzése; kiíratás fájlokba This method read and
	 * process the file and encrypt it's content
	 */
	private void work() {
		this.createTxtFromUploaded(this.file);
		this.encode();

		if (VaadinSession.getCurrent().getAttribute("USER") != null) {
			ItemService.addItem(this.filePath, this.filename, this.getAesEntropy()[0] / this.getSourceEntropy()[0]);
		}

		this.makeCharts();
	}

	private void encode() {
		PrintWriter pwAES, pwKey, pwSHA, pwSkein, pwSerpent, pwBlowfish, pwTwofish;
		try {
			pwAES = new PrintWriter(this.filePath + this.file.getName() + Constants.aes, "UTF-8");
			pwKey = new PrintWriter(this.filePath + this.file.getName() + Constants.key, "UTF-8");
			pwSHA = new PrintWriter(this.filePath + this.file.getName() + Constants.sha, "UTF-8");
			pwSkein = new PrintWriter(this.filePath + this.file.getName() + Constants.skein, "UTF-8");
			pwSerpent = new PrintWriter(this.filePath + this.file.getName() + Constants.serpent, "UTF-8");
			pwBlowfish = new PrintWriter(this.filePath + this.file.getName() + Constants.blowfish, "UTF-8");
			pwTwofish = new PrintWriter(this.filePath + this.file.getName() + Constants.twofish, "UTF-8");
			final BufferedReader reader = new BufferedReader(new FileReader(this.file));
			String next, line;
			boolean firstRow = true;
			while ((line = reader.readLine()) != null) {
				next = this.read(line, firstRow);
				firstRow = false;
				if (next == null) {
					continue;
				}

				String aes = "";
				String serpent = "";
				String blowfish = "";
				String twofish = "";
				if (this.keyString == null || this.keyString.isEmpty()) {
					if (this.window.getHammingDist() != 0) {
						do {
							this.key = DigestService.generateRandomKeyWithInsideAlg();
							aes = DigestService.encriptAES(next, this.key);
							try {
								serpent = DigestService.encriptSerpent(next, this.key);
								blowfish = DigestService.encriptBlowfish(next, this.key);
								twofish = DigestService.encriptTwofish(next, this.key);
							} catch (final InvalidKeyException e) {
								new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
							}
						} while ((Tests.getHammingDistance(new String(this.key.getEncoded(), "UTF-8"), aes)
								/ new String(this.key.getEncoded(), "UTF-8").length()) < this.window.getHammingDist());
					} else {
						this.key = DigestService.generateRandomKeyWithInsideAlg();
						aes = DigestService.encriptAES(next, this.key);
						try {
							serpent = DigestService.encriptSerpent(next, this.key);
							blowfish = DigestService.encriptBlowfish(next, this.key);
							twofish = DigestService.encriptTwofish(next, this.key);
						} catch (final InvalidKeyException e) {
							new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
						}
					}
					pwKey.println(Base64.getEncoder().encodeToString(this.key.getEncoded()));
				} else {
					aes = DigestService.encriptAES(next, new SecretKeySpec(this.keyString.getBytes(), "AES"));
					try {
						serpent = DigestService.encriptSerpent(next,
								new SecretKeySpec(this.keyString.getBytes(), "Serpent"));
						blowfish = DigestService.encriptBlowfish(next,
								new SecretKeySpec(this.keyString.getBytes(), "Blowfish"));
						twofish = DigestService.encriptTwofish(next,
								new SecretKeySpec(this.keyString.getBytes(), "Twofish"));
					} catch (final InvalidKeyException e) {
						new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
					}
				}
				// String aes = DigestService.encriptAES(next, key);
				pwAES.println(aes);
				pwSerpent.println(serpent);
				pwBlowfish.println(blowfish);
				pwTwofish.println(twofish);
				pwSHA.println(DigestService.getSHA2_512Hash(aes));
				pwSkein.println(DigestService.getSkeinHash(aes));
			}
			reader.close();
			pwAES.close();
			pwKey.close();
			pwSHA.close();
			pwSkein.close();
			pwSerpent.close();
			pwBlowfish.close();
			pwTwofish.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * It creates the charts of the histograms
	 */
	public void makeCharts() {
		this.c.getCharts((KeyanalysisUI) UI.getCurrent());
		this.c.createHistogram(Tests.aranyok(this.filePath + this.file.getName(),
				this.filePath + this.file.getName() + Constants.aes, true), Constants.textPerAESPanel_Number,
				Constants.RateHistogramm);
		this.c.createHistogram(Tests.aranyok(this.filePath + this.file.getName(),
				this.filePath + this.file.getName() + Constants.sha, true), Constants.textPerSHAPanel_Number,
				Constants.RateHistogramm);
		this.c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.skein, true),
				Constants.textPerSkeinPanel_Number, Constants.RateHistogramm);
		this.c.createHistogram(Tests.aranyok(this.filePath + this.file.getName(),
				this.filePath + this.file.getName() + Constants.aes, false), Constants.AES10Panel_Number,
				Constants.RateHistogramm);
		this.c.createHistogram(Tests.aranyok(this.filePath + this.file.getName(),
				this.filePath + this.file.getName() + Constants.key, false), Constants.key10Panel_Number,
				Constants.RateHistogramm);
		// TODO: sha10, skein10
		this.c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.aes),
				Constants.textAESHammingPanel_Number, Constants.HammingHistogramm);
		this.c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.sha),
				Constants.textSHAHammingPanel_Number, Constants.HammingHistogramm);
		this.c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.skein),
				Constants.textSkeinHammingPanel_Number, Constants.HammingHistogramm);

		this.c.createHistogram(Tests.binaryTweetLength(this.filePath + this.file.getName()),
				Constants.textLengthPanel_Number);

		this.makeEntropyCharts(this.c);
		this.makeCollisions(this.c);
		this.makeSerpentCharts(this.c);
		this.makeBlowfishCharts(this.c);
		this.makeTwofishCharts(this.c);

		((KeyanalysisUI) UI.getCurrent()).downloadButtonEnable(true);
		((KeyanalysisUI) UI.getCurrent()).compareButtonEnable(true);
	}

	/**
	 * It creates the view panel for the entropies
	 * 
	 * @param c
	 */
	private void makeEntropyCharts(final Charts c) {
		c.drawEntropy(this.getSourceEntropy(), this.getAesEntropy(), this.getSerpentEntropy(),
				this.getBlowfishEntropy(), this.getTwofishEntropy(), this.getShaEntropy(), this.getSkeinEntropy());
	}

	/**
	 * It creates the viwe panel for the collisions
	 * 
	 * @param c
	 */
	private void makeCollisions(final Charts c) {
		c.drawCollisions(this.getCollsNumbers());
	}

	/**
	 * It creates the charts of the serpent histogram
	 * 
	 * @param c
	 */
	private void makeSerpentCharts(final Charts c) {
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.serpent, true),
				Constants.textPerserpentPanel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.serpent, false),
				Constants.serpent10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.serpent),
				Constants.textSerpentHammingPanel_Number, Constants.HammingHistogramm);
	}

	/**
	 * It creates the charts for the blowfish histogram
	 * 
	 * @param c
	 */
	private void makeBlowfishCharts(final Charts c) {
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.blowfish, true),
				Constants.textPerblowfishPanel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.blowfish, false),
				Constants.blowfish10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.blowfish),
				Constants.textblowfishHammingPanel_Number, Constants.HammingHistogramm);
	}

	/**
	 * It creates the charts for the twofish histogram
	 * 
	 * @param c
	 */
	private void makeTwofishCharts(final Charts c) {
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.twofish, true),
				Constants.textPerTwofishPanel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.aranyok(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.twofish, false),
				Constants.twofish10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(
				Tests.binDistance(this.filePath + this.file.getName(),
						this.filePath + this.file.getName() + Constants.twofish),
				Constants.textTwofishHammingPanel_Number, Constants.HammingHistogramm);
	}

	/**
	 * 
	 * @return Number of collisions of the splitted encrypted outputs and hashes
	 */
	private HashMap<String, Integer> getCollsNumbers() {
		Tests.createFilesForColls(this.filePath + this.file.getName() + Constants.sha);
		Tests.createFilesForColls(this.filePath + this.file.getName() + Constants.skein);
		Tests.createFilesForColls(this.filePath + this.file.getName() + Constants.aes);
		int shaColls = 0;
		int skeinColls = 0;
		final HashMap<String, Integer> colls = new HashMap<>();
		if ((shaColls = Tests
				.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.sha64))) == 0) {
			colls.put("SHA64", shaColls);
			if ((shaColls = Tests
					.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.sha32))) == 0) {
				colls.put("SHA32", shaColls);
				shaColls = Tests.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.sha16));
				colls.put("SHA16", shaColls);
			}
		}

		if ((skeinColls = Tests
				.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.skein64))) == 0) {
			colls.put("SKEIN64", skeinColls);
			if ((skeinColls = Tests
					.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.skein32))) == 0) {
				colls.put("SKEIN32", skeinColls);
				skeinColls = Tests
						.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.skein16));
				colls.put("SKEIN16", skeinColls);
			}
		}
		colls.put("AES", Tests.countCollsFromFile(new File(this.filePath + this.file.getName() + Constants.aes64)));
		return colls;
	}

	/**
	 * 
	 * @return the entropy numbers of the source
	 */
	private double[] getSourceEntropy() {
		return Tests.entropy1(
				this.filePath + this.file.getName().substring(0, this.file.getName().lastIndexOf(".")) + ".txt");
	}

	/**
	 * 
	 * @return the entropy numbers of the aes of current file
	 */
	private double[] getAesEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.aes);
	}

	/**
	 * 
	 * @return the entropy numbers of the serpent of current file
	 */
	private double[] getSerpentEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.serpent);
	}

	/**
	 * 
	 * @return the entropy numbers of the blowfish of current file
	 */
	private double[] getBlowfishEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.blowfish);
	}

	/**
	 * 
	 * @return the entropy numbers of the twofish of current file
	 */
	private double[] getTwofishEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.twofish);
	}

	/**
	 * 
	 * @return The entropy number of the sha hash of current file
	 */
	private double[] getShaEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.sha);
	}

	/**
	 * 
	 * @return The entropy number of the skein hash of current file
	 */
	private double[] getSkeinEntropy() {
		return Tests.entropy1(this.filePath + this.file.getName() + Constants.skein);
	}

	/**
	 * This create a zip package file from the generated files.
	 * 
	 * @return
	 */
	private File createZip() {
		try {
			final FileOutputStream fos = new FileOutputStream(
					this.file.getName().substring(0, this.file.getName().lastIndexOf(".")) + ".zip");
			final ZipOutputStream zos = new ZipOutputStream(fos);

			final List<String> fileNames = new ArrayList<>();
			fileNames.add(this.file.getName() + Constants.aes);
			fileNames.add(this.file.getName() + Constants.sha);
			fileNames.add(this.file.getName() + Constants.skein);
			fileNames.add(this.file.getName() + Constants.key);
			fileNames.add(this.file.getName() + Constants.serpent);
			fileNames.add(this.file.getName() + Constants.blowfish);
			fileNames.add(this.file.getName() + Constants.twofish);

			for (final String name : fileNames) {
				this.addToZipFile(name, zos);
			}

			zos.close();
			fos.close();
			return new File(this.file.getName().substring(0, this.file.getName().lastIndexOf(".")) + ".zip");

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add a file to the zip format file package
	 * 
	 * @param fileName
	 * @param zos
	 * @throws IOException
	 */
	private void addToZipFile(final String fileName, final ZipOutputStream zos) throws IOException {

		final File file = new File(this.filePath + fileName);
		final FileInputStream fis = new FileInputStream(file);
		final ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		final byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * Letöltéshez File Resource megadás Create an event for a view compontent
	 * that is allow the zipping of files and download them.
	 * 
	 * @param button
	 * @return
	 */
	public boolean getFileResource(final Button button) {
		if (this.file == null) {
			return false;
		}
		final File zipFile = this.createZip();
		if (zipFile.exists()) {
			final FileDownloader fd = new FileDownloader(new FileResource(zipFile));
			fd.extend(button);
			return true;
		}
		return false;
	}

	/**
	 * It removes that files is used by this class
	 */
	public void endProcess() {
		if (this.file != null) {
			new File(this.filePath + this.file.getName() + Constants.aes).delete();
			new File(this.filePath + this.file.getName() + Constants.key).delete();
			new File(this.filePath + this.file.getName() + Constants.sha).delete();
			new File(this.filePath + this.file.getName() + Constants.skein).delete();
			new File(this.filePath + this.file.getName() + Constants.serpent).delete();
			new File(this.filePath + this.file.getName() + Constants.blowfish).delete();
			new File(this.filePath + this.file.getName() + Constants.twofish).delete();

			this.file.delete();
			new File(this.filePath).delete();
		}
	}

	/**
	 * It removes a directory that has been gotten by parameter
	 * 
	 * @param directory
	 */
	public static final void removeDir(final String directory) {
		if (!"".equals(directory)) {
			final File f = new File(directory);
			if (f.exists() && f.isDirectory()) {
				final File[] files = f.listFiles();
				for (final File one : files) {
					one.delete();
				}
				f.delete();
			}
		}
	}

	/**
	 * It copies a directory to another
	 * 
	 * @param from
	 * @param copyTo
	 */
	public static final void copyDir(final String from, final String copyTo) {
		if (!"".equals(from) && !"".equals(copyTo)) {
			final File f = new File(from);
			if (f.exists() && f.isDirectory()) {
				final File f2 = new File(copyTo);
				f2.mkdirs();
				try {
					FileUtils.copyDirectory(f, f2);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Count the avarage of the numbers in the List that is in paramter of
	 * function
	 * 
	 * @param list
	 * @return
	 */
	public static final <T extends Number> double avg(final List<T> list) {
		final Iterator<?> it = list.iterator();
		double sum = 0;
		int i = 0;
		while (it.hasNext()) {
			sum += (double) it.next();
			i++;
		}
		return sum / i;
	}

	/**
	 * Count dispersion of the numbers in that List is in the parameter of
	 * fuction
	 * 
	 * @param list
	 * @return
	 */
	public static final <T extends Number> double dispersion(final List<T> list) {
		final double avg = avg(list);
		final List<Double> dif = new ArrayList<>();
		final Iterator<T> it = list.iterator();
		while (it.hasNext()) {
			dif.add(Math.pow((double) it.next() - avg, 2));
		}
		return Math.sqrt(avg(dif));
	}

	/**
	 * Create a text format document from another format that can be csv or json
	 * 
	 * @param file
	 */
	private void createTxtFromUploaded(final File file) {
		if (file.getName().endsWith(".txt")) {
			return;
		}
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			br = new BufferedReader(new FileReader(file));
			writer = new PrintWriter(
					this.filePath + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".txt");
			String line, next;
			boolean firstRow = true;
			while ((line = br.readLine()) != null) {
				next = this.read(line, firstRow);
				firstRow = false;
				if (next == null) {
					continue;
				}
				writer.println(next);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				writer.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFilePath(final String string) {
		this.filePath = string;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public Charts getCharts() {
		return this.c;
	}

	public File getFile() {
		return this.file;
	}

}