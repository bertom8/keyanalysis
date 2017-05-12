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
	private Charts c = new Charts();

	public ProcessService(final String filename, final UploadWindow window, final String newFilePath) {
		if (filename == null || filename.isEmpty()) {
			Notification.show(Constants.errorOccured + filename, Notification.Type.ERROR_MESSAGE);
			return;
		}
		this.keyString = window.getKey().getValue();
		this.window = window;
		this.filename = filename;
		if (keyString != null && !keyString.isEmpty()) {
			key = new SecretKeySpec(keyString.getBytes(), "AES");
		}
		filePath = newFilePath;
		file = new File(filePath + filename);
	}
	
	public ProcessService(final String filename, final String newFilePath) {
		this.filename = filename;
		this.filePath = newFilePath;
		file = new File(filePath + filename);
	}

	@Override
	public void run() {
		if (file.isFile() && file.exists()) {
			// ((KeyanalysisUI)
			// UI.getCurrent()).drawProgress("Feldolgozás...");
			//read();
			//if (!texts.isEmpty())
				work();
		}
	}
	
	public void runWithTweets() {
		if (file.isFile() && file.exists()) {
				encode();
		}
	}

	/**
	 * Read the file
	 * @param filename
	 */
	private void getFile(String filename, ProgressBar progress) {
		if (filename == null || filename.isEmpty()) {
			Notification.show(Constants.errorOccured + filename, Notification.Type.ERROR_MESSAGE);
			return;
		}
		file = new File(filePath  + filename);
	}

	/**
	 * 
	 * @param line
	 * @param firstRow
	 * @return
	 */
	private String read(String line, boolean firstRow) {
		
	    switch (file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()) ) {
	    case ".csv" :
	    	if (firstRow && window.getHasHeader().getValue()) {
	    		columnNumber = readHeader(line);
	    		return null;
	    	}
	    	return readCsv(line);
	    case ".json" :
	    	return readJson(line);
	    case ".txt" : 
	    	return readTxt(line);
	    default:
	    	return null;
	    }
	}
	
	/**
	 * 
	 * @param line
	 * @return
	 */
	private String readTxt(String line) {
		return line;
	}
	
	/**
	 * 
	 * @param line
	 * @return
	 */
	private int readHeader(String line) {
		List<Character> chars = new ArrayList<>();
		char[] c = line.substring(0, line.lastIndexOf(window.getFormat().getValue(), 0)).toCharArray();
		for (int i = 0; i < c.length; i++) {
			chars.add(c[i]);
		}
		return (int)chars.stream().filter(string -> string.equals(window.getDelimiter().getValue())).count();
	}
	
	/**
	 * 
	 * @param line
	 * @return
	 */
	private String readCsv(String line) {
		return line.substring(line.indexOf(line.split(window.getDelimiter().getValue())[columnNumber]), line.length()-1);
	}

	/**
	 * Beolvassuk és értelmezzük, feldolgozzuk a kapott json fájlt a file
	 * Listünkbe
	 */
	private String readJson(String line) {
		//return JsonParser.getString(file, window.getFormat().getValue());
		ReadTweets p = new ReadTweets();
		JsonObject o = p.readFromString(line);
		if (o == null)
			return null;
		String text = o.get(window.getFormat().getValue()).getAsString().replace('\n', ' ').replace('\t', ' ').replace('"', ' ');
		return text;
	}

	/**
	 * Kódolások és hashelések elvégzése; kiíratás fájlokba
	 * This method read and process the file and encrypt it's content
	 */
	private void work() {
		encode();
		
		if (VaadinSession.getCurrent().getAttribute("USER") != null)
			ItemService.addItem(filePath, filename, getAesEntropy()[0]/getSourceEntropy()[0]);
		
		makeCharts();
	}
	
	private void encode() {
		PrintWriter pwAES, pwKey, pwSHA, pwSkein, pwSerpent, pwBlowfish, pwTwofish;
		try {
			pwAES = new PrintWriter(filePath + file.getName() + Constants.aes, "UTF-8");
			pwKey = new PrintWriter(filePath + file.getName() + Constants.key, "UTF-8");
			pwSHA = new PrintWriter(filePath + file.getName() + Constants.sha, "UTF-8");
			pwSkein = new PrintWriter(filePath + file.getName() + Constants.skein, "UTF-8");
			pwSerpent = new PrintWriter(filePath + file.getName() + Constants.serpent, "UTF-8");
			pwBlowfish = new PrintWriter(filePath + file.getName() + Constants.blowfish, "UTF-8");
			pwTwofish = new PrintWriter(filePath + file.getName() + Constants.twofish, "UTF-8");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String next, line;
			boolean firstRow = true;
			int i = 0;
			while((line = reader.readLine()) != null) {
				next = read(line, firstRow);
				firstRow = false;
				if (next == null)
					continue;
				
				String aes = "";
				String serpent = "";
				String blowfish = "";
				String twofish = "";
				if (keyString == null || keyString.isEmpty()) {
					if (window.getHammingDist() != 0) {
						do {
							key = DigestService.generateRandomKeyWithInsideAlg();
							aes = DigestService.encriptAES(next, key);
							try {
								serpent = DigestService.encriptSerpent(next, key);
								blowfish = DigestService.encriptBlowfish(next, key);
								twofish = DigestService.encriptTwofish(next, key);
							} catch (InvalidKeyException e) {
								new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
							}
						}
						while( (Tests.getHammingDistance(new String(key.getEncoded(), "UTF-8"), aes) 
								/ new String(key.getEncoded(), "UTF-8").length()) < window.getHammingDist() );
					}
					else {
						key = DigestService.generateRandomKeyWithInsideAlg();
						aes = DigestService.encriptAES(next, key);
						try {
							serpent = DigestService.encriptSerpent(next, key);
							blowfish = DigestService.encriptBlowfish(next, key);
							twofish = DigestService.encriptTwofish(next, key);
						} catch (InvalidKeyException e) {
							new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
						}
					}
					pwKey.println(Base64.getEncoder().encodeToString(key.getEncoded()));
				} else {
					aes = DigestService.encriptAES(next, new SecretKeySpec(keyString.getBytes(), "AES"));
					try {
						serpent = DigestService.encriptSerpent(next, new SecretKeySpec(keyString.getBytes(), "Serpent"));
						blowfish = DigestService.encriptBlowfish(next, new SecretKeySpec(keyString.getBytes(), "Blowfish"));
						twofish = DigestService.encriptTwofish(next, new SecretKeySpec(keyString.getBytes(), "Twofish"));
					} catch (InvalidKeyException e) {
						new Notification("Rossz kulcs!", Type.ERROR_MESSAGE);
					}
				}
				//String aes = DigestService.encriptAES(next, key);
				System.out.println(i++);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * It creates the charts of the histograms
	 */
	public void makeCharts() {
		c.getCharts((KeyanalysisUI) UI.getCurrent());
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.aes, true),
				Constants.textPerAESPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.sha, true),
				Constants.textPerSHAPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.skein, true),
				Constants.textPerSkeinPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.aes, false),
				Constants.AES10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.key, false),
				Constants.key10Panel_Number, Constants.RateHistogramm);
		// TODO: sha10, skein10
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.aes),
				Constants.textAESHammingPanel_Number, Constants.HammingHistogramm);
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.sha),
				Constants.textSHAHammingPanel_Number, Constants.HammingHistogramm);
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.skein),
				Constants.textSkeinHammingPanel_Number, Constants.HammingHistogramm);
		
		c.createHistogram(Tests.binaryTweetLength(filePath + file.getName()), Constants.textLengthPanel_Number);

		makeEntropyCharts(c);
		makeCollisions(c);
		makeSerpentCharts(c);
		makeBlowfishCharts(c);
		makeTwofishCharts(c);
		
		((KeyanalysisUI)UI.getCurrent()).downloadButtonEnable(true);
	}
	
	/**
	 * It creates the view panel for the entropies
	 * @param c
	 */
	private void makeEntropyCharts(Charts c) {
		c.drawEntropy(getSourceEntropy(), getAesEntropy(), getSerpentEntropy(), getBlowfishEntropy(),
				getTwofishEntropy(), getShaEntropy(), getSkeinEntropy());
	}
	
	/**
	 * It creates the viwe panel for the collisions
	 * @param c
	 */
	private void makeCollisions(Charts c) {
		c.drawCollisions(getCollsNumbers());
	}
	
	/**
	 * It creates the charts of the serpent histogram
	 * @param c
	 */
	private void makeSerpentCharts(Charts c) {
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.serpent, true), Constants.textPerserpentPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.serpent, false), Constants.serpent10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.serpent), Constants.textSerpentHammingPanel_Number, Constants.HammingHistogramm);
	}
	
	/**
	 * It creates the charts for the blowfish histogram
	 * @param c
	 */
	private void makeBlowfishCharts(Charts c) {
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.blowfish, true), Constants.textPerblowfishPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.blowfish, false), Constants.blowfish10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.blowfish), Constants.textblowfishHammingPanel_Number, Constants.HammingHistogramm);
	}
	
	/**
	 * It creates the charts for the twofish histogram
	 * @param c
	 */
	private void makeTwofishCharts(Charts c) {
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.twofish, true), Constants.textPerTwofishPanel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.aranyok(filePath + file.getName(), filePath + file.getName() + Constants.twofish, false), Constants.twofish10Panel_Number, Constants.RateHistogramm);
		c.createHistogram(Tests.binDistance(filePath + file.getName(), filePath + file.getName() + Constants.twofish), Constants.textTwofishHammingPanel_Number, Constants.HammingHistogramm);
	}
	
	/**
	 * 
	 * @return Number of collisions of the splitted encrypted outputs and hashes
	 */
	private HashMap<String, Integer> getCollsNumbers() {
		Tests.createFilesForColls(filePath + file.getName() + Constants.sha);
		Tests.createFilesForColls(filePath + file.getName() + Constants.skein);
		Tests.createFilesForColls(filePath + file.getName() + Constants.aes);
		int shaColls = 0;
		int skeinColls = 0;
		HashMap<String, Integer> colls = new HashMap<>();
		if ((shaColls = Tests.countCollsFromFile(new File(filePath + file.getName() + Constants.sha64))) == 0) {
			colls.put("SHA64", shaColls);
			if ((shaColls = Tests.countCollsFromFile(new File(filePath + file.getName() + Constants.sha32))) == 0) {
				colls.put("SHA32", shaColls);
				shaColls = Tests.countCollsFromFile(new File(filePath +	file.getName() + Constants.sha16));
				colls.put("SHA16", shaColls);
			}
		}
		
		if ((skeinColls = Tests.countCollsFromFile(new File(filePath + file.getName() + Constants.skein64))) == 0) {
				colls.put("SKEIN64", skeinColls);
				if ((skeinColls = Tests.countCollsFromFile(new File(filePath + file.getName() + Constants.skein32))) == 0) {
					colls.put("SKEIN32", skeinColls);
					skeinColls = Tests.countCollsFromFile(new File(filePath +	file.getName() + Constants.skein16));
					colls.put("SKEIN16", skeinColls);
				}
		}
		colls.put("AES", Tests.countCollsFromFile(new File(filePath + file.getName() + Constants.aes64)));
		return colls;
	}

	/**
	 * 
	 * @return the entropy numbers of the source
	 */
	private double[] getSourceEntropy() {
		createTxtFromUploaded(file);
		return Tests.entropy1(filePath + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".txt");
	}

	/**
	 * 
	 * @return the entropy numbers of the aes of current file
	 */
	private double[] getAesEntropy() {
		return Tests.entropy1(filePath + file.getName() + Constants.aes);
	}
	
	/**
	 * 
	 * @return the entropy numbers of the serpent of current file
	 */
	private double[] getSerpentEntropy() {
		return Tests.entropy1(filePath + file.getName() + Constants.serpent);
	}
	/**
	 * 
	 * @return the entropy numbers of the blowfish of current file
	 */
	private double[] getBlowfishEntropy() {
		return Tests.entropy1(filePath + file.getName() + Constants.blowfish);
	}
	
	/**
	 * 
	 * @return the entropy numbers of the twofish of current file
	 */
	private double[] getTwofishEntropy() {
		return Tests.entropy1(filePath + file.getName() + Constants.twofish);
	}

	/**
	 * 
	 * @return The entropy number of the sha hash of current file
	 */
	private double[] getShaEntropy() {
		return Tests.entropy1(filePath + file.getName() + Constants.sha);
	}
	
	/**
	 * 
	 * @return The entropy number of the skein hash of current file
	 */
	private double[] getSkeinEntropy() {
	    return Tests.entropy1(filePath + file.getName() + Constants.skein);
	}
	
	/**
	 * This create a zip package file from the generated files.
	 * @return
	 */
	private File createZip() {
		try {
			FileOutputStream fos = new FileOutputStream(file.getName().substring(0, file.getName().lastIndexOf(".")) + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			List<String> fileNames = new ArrayList<String>();
			fileNames.add( file.getName() + Constants.aes);
			fileNames.add( file.getName() + Constants.sha);
			fileNames.add( file.getName() + Constants.skein);
			fileNames.add( file.getName() + Constants.key);
			fileNames.add( file.getName() + Constants.serpent);
			fileNames.add( file.getName() + Constants.blowfish);
			fileNames.add( file.getName() + Constants.twofish);
			
			for (String name : fileNames) {
				addToZipFile(name, zos);
			}
			
			zos.close();
			fos.close();
			return new File(file.getName().substring(0, file.getName().lastIndexOf(".")) + ".zip");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add a file to the zip format file package
	 * @param fileName
	 * @param zos
	 * @throws IOException
	 */
	private void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {

		File file = new File(filePath + fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * Letöltéshez File Resource megadás
	 * Create an event for a view compontent that is allow the zipping of files and download them.
	 * @param button
	 * @return
	 */
	public boolean getFileResource(Button button) {
		if (file == null)
			return false;
		File zipFile = createZip();
		if (zipFile.exists()) {
			FileDownloader fd = new FileDownloader(new FileResource(zipFile));
			fd.extend(button);
			return true;
		}
		return false;
	}

	/**
	 * It removes that files is used by this class
	 */
	public void endProcess() {
		if (file != null) {
			new File(filePath + file.getName() + Constants.aes)
					.delete();
			new File(filePath + file.getName() + Constants.key)
					.delete();
			new File(filePath + file.getName() + Constants.sha)
					.delete();
			new File(filePath + file.getName() + Constants.skein)
					.delete();
			new File(filePath + file.getName() + Constants.serpent)
					.delete();
			new File(filePath + file.getName() + Constants.blowfish)
			.delete();
			new File(filePath + file.getName() + Constants.twofish)
			.delete();
			
			file.delete();
			new File(filePath).delete();
		}
	}

	/**
	 * It removes a directory that has been gotten by parameter
	 * @param directory
	 */
	public static final void removeDir(String directory) {
		if ( !"".equals(directory) ) {
			File f = new File(directory);
			if (f.exists() && f.isDirectory()) {
				File[] files = f.listFiles();
				for (File one : files) {
					one.delete();
				}
				f.delete();
			}
		}
	}
	
	/**
	 * It copies a directory to another
	 * @param from
	 * @param copyTo
	 */
	public static final void copyDir(String from, String copyTo) {
		if (!"".equals(from) && !"".equals(copyTo)) {
			File f = new File(from);
			if (f.exists() && f.isDirectory()) {
				File f2 = new File(copyTo);
				f2.mkdirs();
				try {
					FileUtils.copyDirectory(f, f2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Count the avarage of the numbers in the List that is in paramter of function
	 * @param list
	 * @return
	 */
	public static final <T extends Number> double avg(List<T> list) {
		Iterator<?> it = list.iterator();
		double sum = 0;
		int i = 0;
		while (it.hasNext()) {
			sum += (double)it.next();
			i++;
		}
		return sum/i;
	}
	
	/**
	 * Count dispersion of the numbers in that List is in the parameter of fuction
	 * @param list
	 * @return
	 */
	public static final <T extends Number> double dispersion(List<T> list) {
		double avg = avg(list);
		List<Double> dif = new ArrayList<Double>();
		Iterator<T> it = list.iterator();
		while (it.hasNext()) {
			dif.add(Math.pow( (double)it.next()-avg,2) );
		}
		return Math.sqrt(avg(dif));
	}
	
	/**
	 * Create a text format document from another format that can be csv or json
	 * @param file
	 */
	private void createTxtFromUploaded(File file) {
		if (file.getName().endsWith(".txt"))
			return;
		BufferedReader br = null;
		PrintWriter writer = null;
		try {
			br = new BufferedReader(new FileReader(file));
			writer = new PrintWriter(filePath +	file.getName().substring(0, file.getName().lastIndexOf(".")) + ".txt");
			String line, next;
			boolean firstRow = true;
			while((line = br.readLine()) != null) {
				next = read(line, firstRow);
				firstRow = false;
				if (next == null)
					continue;
				writer.println(next);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setFilePath(String string) {
		filePath = string;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public Charts getCharts() {
		return c;
	}

	public File getFile() {
		return file;
	}
	
}