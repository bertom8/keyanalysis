package org.keyanalysis.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import com.google.code.externalsorting.ExternalSort;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.keyanalysis.View.KeyanalysisUI;

import com.vaadin.ui.UI;

/**
 * 
 * @author Bereczki Tam�s
 *
 */
public class Tests {

	/**
	 * 
	 * @param h
	 * @param file
	 */
	public static void makeTextFromIntegers(ArrayList<Integer> h, String file) {
		try {
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			Iterator<Integer> it = h.iterator();
			while (it.hasNext()) {
				int tweet = it.next();
				pw.println(tweet);
				// System.out.println(tweet);
			}
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param h
	 * @return
	 */
	public static ArrayList<String> sortList(ArrayList<String> h) {
		Collections.sort(h);
		System.out.println("End of sorting");
		return h;
	}

	/**
	 * 
	 * @param h
	 * @param file
	 */
	public static void makeTextFromStrings(ArrayList<String> h, String file) {
		try {
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			Iterator<String> it = h.iterator();
			while (it.hasNext()) {
				String tweet = it.next();
				pw.println(tweet);
				System.out.println(tweet);
			}
			pw.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static ArrayList<String> readFile(String file) {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader br = null;

		try {

			String line;

			br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				list.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * @return
	 */
	public static ArrayList<Integer> tweethossz(String source) {
		ArrayList<Integer> hossz = new ArrayList<>();
		if (source.isEmpty()) {
			return hossz;
		}
		BufferedReader br = null;

		try {

			String line;

			br = new BufferedReader(new FileReader(source));

			while ((line = br.readLine()) != null) {
				hossz.add(line.length());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return hossz;
	}

	public static ArrayList<Integer> binaryTweetLength(String source) {
		ArrayList<Integer> hossz = new ArrayList<>();
		if (source.isEmpty()) {
			return hossz;
		}
		BufferedReader br = null;

		try {
			String line;

			br = new BufferedReader(new FileReader(source));

			while ((line = br.readLine()) != null) {
				hossz.add(toBinary(line).length());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return hossz;
	}

	/**
	 * 
	 * @param sequence1
	 * @param sequence2
	 * @return Hamming distance
	 */
	public static int getHammingDistance(String sequence1, String sequence2) {
		char[] s1 = sequence1.toCharArray();
		char[] s2 = sequence2.toCharArray();

		int shorter = Math.min(s1.length, s2.length);

		int result = 0;
		for (int i = 0; i < shorter; i++) {
			if (s1[i] != s2[i])
				result++;
		}

		return result;
	}

	/**
	 * Get binary representaion of string
	 * 
	 * @param s
	 * @return binary of string
	 */
	public static String toBinary(String s) {
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}

	/**
	 * 
	 * @param source
	 * @param aesek
	 * @return
	 */
	public static ArrayList<Double> binDistance(ArrayList<String> source, String aesek) {
		ArrayList<Double> tavok = new ArrayList<>();
		if (source.isEmpty() || aesek.isEmpty()) {
			return tavok;
		}
		BufferedReader br2 = null;
		try {
			String stored;
			String hash;
			double hossz;
			Iterator<String> it = source.iterator();
			br2 = new BufferedReader(new FileReader(aesek));

			while (it.hasNext() && (hash = br2.readLine()) != null) {
				stored = it.next();
				hossz = toBinary(stored).length();
				tavok.add(getHammingDistance(toBinary(stored), toBinary(hash)) / hossz);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tavok;
	}

	/**
	 * 
	 * @param source
	 * @param aesek
	 * @return
	 */
	public static ArrayList<Double> binDistance(String source, String aesek) {
		ArrayList<Double> tavok = new ArrayList<>();
		if (source.isEmpty() || aesek.isEmpty()) {
			return tavok;
		}
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
			String stored;
			String hash;
			double hossz;
			br1 = new BufferedReader(new FileReader(source));
			br2 = new BufferedReader(new FileReader(aesek));

			while ((stored = br1.readLine()) != null && (hash = br2.readLine()) != null) {
				hossz = toBinary(stored).length();
				tavok.add(getHammingDistance(toBinary(stored), toBinary(hash)) / hossz);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tavok;
	}

	/**
	 * How many one are in the binary of string
	 * 
	 * @param s
	 * @return number of ones
	 */
	public static int howManyOnes(String s) {
		int count = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '1')
				count++;
		}

		return count;
	}

	/**
	 * How many zeros are in the binary of string
	 * 
	 * @param s
	 * @return number of zeros
	 */
	public static int howManyZeros(String s) {
		int count = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '0')
				count++;
		}
		return count;
	}

	public static ArrayList<Double> aranyok(ArrayList<String> source, String aesek, boolean withTogether) {
		if (withTogether) {
			ArrayList<Double> aranyok = new ArrayList<Double>();
			Iterator<Double> stored = aranyokszamitas(source, aesek, false).iterator();
			Iterator<Double> aes = aranyokszamitas(source, aesek, true).iterator();
			while (stored.hasNext() && aes.hasNext()) {
				aranyok.add(stored.next() / aes.next());
			}
			return aranyok;
		} else {
			return aranyokszamitas(source, aesek, true);
		}
	}

	public static ArrayList<Double> aranyok(String source, String aesek, boolean withTogether) {
		if (withTogether) {
			ArrayList<Double> aranyok = new ArrayList<Double>();
			Iterator<Double> stored = aranyokszamitas(source, aesek, false).iterator();
			Iterator<Double> aes = aranyokszamitas(source, aesek, true).iterator();
			while (stored.hasNext() && aes.hasNext()) {
				aranyok.add(stored.next() / aes.next());
			}
			return aranyok;
		} else {
			return aranyokszamitas(source, aesek, true);
		}
	}

	/**
	 * 
	 * @return
	 */
	private static ArrayList<Double> aranyokszamitas(String source, String aesek, boolean hashed) {
		ArrayList<Double> aranyok = new ArrayList<>();
		if (source.isEmpty() || aesek.isEmpty())
			return aranyok;
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
			String stored;
			String hash;
			br1 = new BufferedReader(new FileReader(source));
			br2 = new BufferedReader(new FileReader(aesek));
			while ((stored = br1.readLine()) != null && (hash = br2.readLine()) != null) {
				if (hashed) {
					double zeros = howManyZeros(toBinary(hash));
					double ones = toBinary(hash).length() - zeros;
					aranyok.add(ones / zeros);
				} else {
					double zeros = howManyZeros(toBinary(stored));
					double ones = toBinary(stored).length() - zeros;
					aranyok.add(ones / zeros);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return aranyok;
	}

	private static ArrayList<Double> aranyokszamitas(ArrayList<String> source, String aesek, boolean hashed) {
		ArrayList<Double> aranyok = new ArrayList<>();
		if (source.isEmpty() || aesek.isEmpty())
			return aranyok;
		BufferedReader br2 = null;
		try {
			String stored;
			String hash;
			Iterator<String> it = source.iterator();
			br2 = new BufferedReader(new FileReader(aesek));
			while (it.hasNext() && (hash = br2.readLine()) != null) {
				stored = it.next();
				if (hashed) {
					double zeros = howManyZeros(toBinary(hash));
					double ones = toBinary(hash).length() - zeros;
					aranyok.add(ones / zeros);
				} else {
					double zeros = howManyZeros(toBinary(stored));
					double ones = toBinary(stored).length() - zeros;
					aranyok.add(ones / zeros);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return aranyok;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private static ArrayList<String> get16Characters(String s) {
		ArrayList<String> a = new ArrayList<>();
		int i;
		for (i = 0; i < s.length() - 16; i = i + 16) {
			a.add(s.substring(i, i + 16));

		}
		if (i < s.length()) {
			String sub = null;
			while (i < s.length()) {
				sub += s.charAt(i);
				i++;
			}
			a.add(sub);
		}
		return a;
	}

	private static ArrayList<String> get32CharactersAndAES(String s) {
		ArrayList<String> a = new ArrayList<>();
		int i;
		for (i = 0; i < s.length() - 32; i = i + 32) {
			// System.out.println(s.substring(i, i+32));
			a.add(DigestService.encriptAES(s.substring(i, i + 32)));

		}
		if (i < s.length()) {
			String sub = "";
			while (i < s.length()) {
				sub += s.charAt(i);
				i++;
			}
			a.add(DigestService.encriptAES(sub));
		}
		return a;
	}

	private static ArrayList<String> get32Characters(String s) {
		ArrayList<String> a = new ArrayList<>();
		int i;
		for (i = 0; i < s.length() - 32; i = i + 32) {
			// System.out.println(s.substring(i, i+32));
			a.add(s.substring(i, i + 32));

		}
		if (i < s.length()) {
			String sub = "";
			while (i < s.length()) {
				sub += s.charAt(i);
				i++;
			}
			a.add(sub);
		}
		return a;
	}

	private static ArrayList<String> get64Characters(String s) {
		ArrayList<String> a = new ArrayList<>();
		int i;
		for (i = 0; i < s.length() - 64; i = i + 64) {
			a.add(s.substring(i, i + 64));
		}
		if (i < s.length()) {
			String sub = "";
			while (i < s.length()) {
				sub += s.charAt(i);
				i++;
			}
			a.add(sub);
		}
		return a;
	}

	private static ArrayList<String> get96Characters(String s) {
		ArrayList<String> a = new ArrayList<>();
		int i;
		for (i = 0; i < s.length() - 160; i = i + 160) {
			a.add(s.substring(i, i + 160));
		}
		if (i < s.length()) {
			String sub = "";
			while (i < s.length()) {
				sub += s.charAt(i);
				i++;
			}
			a.add(sub);
		}
		return a;
	}

	public static void kovetoBlokkparok() {
		ArrayList<String> tweets = readFile("storedtweets.txt");
		ArrayList<String> aes = new ArrayList<>();
		Iterator<String> it = tweets.iterator();
		//int i = 0;
		int j = 0;
		while (it.hasNext() && j < 20000000) {
			if (j % 90000 == 0 || j == 4008239) {
				appendFile(aes, "160charstweets.txt");
				aes.clear();
			}
			// ArrayList<String> subAes = get32CharactersAndAES(it.next());
			ArrayList<String> subAes = get96Characters(it.next());
			Iterator<String> it2 = subAes.iterator();

			while (it2.hasNext()) {
				aes.add(it2.next());
			}
			System.out.println(j++);
		}
	}

	public static void kovetoBlokkokElemzes() {
		ArrayList<String> tweets = readFile("kovetobpaes.txt");
		ArrayList<Integer> counts = new ArrayList<>();
		int num = 0;
		Iterator<String> it = tweets.iterator();
		String prev = "";
		while (it.hasNext()) {
			String next = it.next();
			if (prev == next)
				num++;
			else {
				counts.add(num);
				num = 0;
			}
			prev = next;
		}
		makeTextFromIntegers(counts, "kovetobpaes_nums.txt");
		System.out.println(tweets.size());
		System.out.println(counts.size());
	}

	private static void appendFile(ArrayList<String> h, String file) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			out.println();
			Iterator<String> it = h.iterator();
			while (it.hasNext()) {
				String txt = it.next();
				out.println(txt);
				System.out.println(txt);
			}
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	/**
	 * 
	 * @return
	 */
	public static ArrayList<Double> binDistancePer128Bit() {
		ArrayList<Double> tavok = new ArrayList<>();
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		int i = 0;
		try {
			String stored;
			String hossz;

			br1 = new BufferedReader(new FileReader("aes.txt"));
			br2 = new BufferedReader(new FileReader("storedtweets.txt"));

			while ((stored = br1.readLine()) != null && (hossz = br2.readLine()) != null) {
				ArrayList<String> charsOf16 = get16Characters(toBinary(stored));
				Iterator<String> it = charsOf16.iterator();
				while (it.hasNext() && i <= 4006000) {

					String next = it.next();
					tavok.add((getHammingDistance(toBinary(next), toBinary("egik5utjgp8mi1xv"))
							/ toBinary(hossz).length())
							+ (getHammingDistance(toBinary(next), toBinary("egik5utjgp8mi1xv"))
									% toBinary(hossz).length() * 0.01));

					System.out.println(i++);
				}
				// System.out.println(i++);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tavok;
	}

	public static String cutHash(String s, int wantBit, boolean fromBegin) {
		String cutted = null;
		if (s.isEmpty())
			return cutted;
		if (wantBit % 4 == 0) {
			if (fromBegin) {
				cutted = s.substring(0, wantBit / 4);
			} else {
				int length = s.length();
				cutted = s.substring(length - wantBit / 4, length);
			}
		}
		return cutted;
	}

	public static ArrayList<String> cutting(String file, int bit, boolean fromBegin) {
		ArrayList<String> newHashes = new ArrayList<>();
		BufferedReader br1 = null;
		int i = 0;
		File f = new File(file);
		if (!f.exists() || f.isDirectory()) {
			return newHashes;
		}
		try {
			String stored;

			br1 = new BufferedReader(new FileReader(file));

			while ((stored = br1.readLine()) != null) {
				String s = cutHash(stored, bit, fromBegin);
				newHashes.add(s);
				System.out.println(i++);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return newHashes;
	}

	public static int countColl(String file, ArrayList<Integer> szamok) {
		int count = 0;
		ArrayList<String> utkozok = new ArrayList<>();
		BufferedReader br1 = null;
		//int i = 0;
		File f = new File(file);
		if (!f.exists() || f.isDirectory()) {
			return count;
		}
		try {
			String num;
			int n;

			br1 = new BufferedReader(new FileReader(file));

			while ((num = br1.readLine()) != null) {
				// System.out.println((num.substring(0, num.lastIndexOf("
				// ")).substring(num.lastIndexOf(" ")-4).trim()));
				// n = Integer.valueOf((num.substring(0, num.lastIndexOf("
				// ")).substring(num.lastIndexOf(" ")-4).trim()));
				n = Integer.valueOf(num.substring(0, 4).trim());
				if (n > 1) {
					szamok.add(n);
					utkozok.add(num);
				}
				if (n > 1)
					count += n - 1;
				System.out.println(n);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		Iterator<String> it = utkozok.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		return count;
	}

	private static String cutShiftedHash(String s, int fromChar, int byChar) {
		String cutted = null;
		if (s.isEmpty())
			return cutted;
		cutted = s.substring(fromChar, fromChar + byChar);
		return cutted;
	}

	public static void shift32bit(String algorithm, int chars) {
		ArrayList<String> h = readFile(algorithm + ".txt");
		/*
		 * int length = 0; switch (algorithm) { case "md5hash": length = 128;
		 * break; case "sha1hash": length = 160; break; case "ripemd160hash":
		 * length = 160; break; case "tigerhash": length = 192; break; case
		 * "tiger2hash": length = 192; break; case "sha2hash": length = 256;
		 * break; case "gosthash": length = 256; break; case "sha512hash":
		 * length = 512; break; case "sha3hash": length = 512; break; case
		 * "whirlpoolhash": length = 512; break; case "skeinhash": length = 512;
		 * break; default: break; }
		 */
		int i = 0;
		Iterator<String> it = h.iterator();
		while (it.hasNext()) {
			String s = it.next();
			// for( int i = 8; i < s.length()+1; i = i+8) {
			h.set(i, cutShiftedHash(s, chars, 6));
			// }
			System.out.println(i++);
		}

		makeTextFromStrings(h, algorithm + "cut_" + chars * 4 + ".txt");
	}

	private static boolean appendFile(String str, String file) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			out.println();
			out.println(str);
			System.out.println(str);
			return true;
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		return false;
	}

	public static SecretKey kulcsmeghatarozas(String str) {
		SecretKey key = null;
		File f = new File(str);
		if (f.exists() && !f.isDirectory()) {
			key = kulcsellenorzes(str);
			while (key == null) {
				key = kulcsellenorzes(str);
			}
		}

		return key;
	}

	private static SecretKey kulcsellenorzes(String destOfFile) {
		ArrayList<String> kulcsok = new ArrayList<>();
		ArrayList<String> aeses = new ArrayList<>();
		int i = 0;
		String txt;
		SecretKey key = null;
		BufferedReader br = null;
		Random r = new Random();

		try {
			// File.createTempFile("kulcsmeghat", ".txt");
			br = new BufferedReader(new FileReader(destOfFile));

			while ((txt = br.readLine()) != null && i < 100000) {
				String binary_txt = toBinary(txt);
				int onesOfBinaryTxt = howManyOnes(binary_txt);
				String aes;
				do {
					// double arany = binary_txt.length() / 256 *
					// onesOfBinaryTxt;
					// key = DigestService.generateRandomKey((int)arany,r);
					key = DigestService.generateRandomKey(onesOfBinaryTxt, r);
					//String keyString = Base64.getEncoder().encodeToString(key.getEncoded());
					// String hashedKey =
					// Hex.encodeHexString(hash(keyString.getBytes()));
					/*
					 * for (int j = 0; j < 20000; j++) keyString =
					 * DigestService.getSHA2Hash(keyString);
					 * //System.out.println(keyString); String keyBinary = new
					 * BigInteger(keyString, 16).toString(2); if
					 * (keyBinary.length() < 256) { int dif = 256 -
					 * keyBinary.length(); for ( int j = 0; j < dif; j++)
					 * keyBinary += '1'; } String newKey = ""; for(int j = 0; j
					 * <= keyBinary.length() - 8; j+=8) { int k =
					 * Integer.parseInt(keyBinary.substring(j, j+8), 2); newKey
					 * += (char) k; }
					 */
					// key = new SecretKeySpec(hash(keyString.getBytes()),
					// "AES");
					key = new SecretKeySpec(hash(txt.getBytes()), "AES");
					// System.out.println(new String( Hex.decodeHex(
					// keyString.toCharArray() ) ));
					/*
					 * double arany =
					 * howManyOnes(toBinary(Base64.getEncoder().encodeToString(
					 * key.getEncoded()))) /
					 * howManyZeros(toBinary(Base64.getEncoder().encodeToString(
					 * key.getEncoded()))); if (arany < 0.45 || arany > 0.55) {
					 * System.out.println("Wrong 1/0: " +
					 * Base64.getEncoder().encodeToString(key.getEncoded()));
					 * return null; }
					 */
					aes = DigestService.encriptAES(txt, key);
					/*
					 * System.out.println(aes);
					 * System.out.println(getHammingDistance(txt, aes));
					 * System.out.println(txt.length());
					 * System.out.println((getHammingDistance(txt, aes) /
					 * txt.length()));
					 */
				} while ((getHammingDistance(txt, aes) / txt.length()) < 0.3);
				kulcsok.add(Base64.getEncoder().encodeToString(key.getEncoded()));
				aeses.add(aes);
				System.out.println(i++);
				/*
				 * if ((getHammingDistance(txt, aes) / toBinary(txt).length()) <
				 * 0.3) { System.out.println("Wrong Distance: " +
				 * Base64.getEncoder().encodeToString(key.getEncoded())); return
				 * null; }
				 */
				/*
				 * else {
				 * appendFile(Base64.getEncoder().encodeToString(key.getEncoded(
				 * )), "kulcsmeghat.txt"); }
				 */
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			makeTextFromStrings(kulcsok, "generaltkulcsok_1hez9.txt");
			makeTextFromStrings(aeses, "generaltaesek_1hez9.txt");
		}

		return key;
	}
	
	/*public static void entropyFromPython(ArrayList<String> list) {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.exec("import sys\nsys.path.append('pathToModiles if they're not there by default')\nimport yourModule");
		// execute a function that takes a string and returns a string
		PyObject someFunc = interpreter.get("funcName");
		org.python.core.PyArray as = new 
		PyObject result = someFunc.__call__(new PyString("Test!"));
		String realResult = (String) result.__tojava__(String.class);
	}*/
	
	public static double entropy1(ArrayList<String> list) {
		double e = 0;
		for (String s : list) {
			e = Entropy.getShannonEntropy(s);
		}
		return e;
	}
	
	public static double[] entropy1(String file) {
		BufferedReader br = null;
		double[] en = new double[2];
		en[1] = Double.POSITIVE_INFINITY;
		try {
			String text;
			br = new BufferedReader(new FileReader(file));
			while ((text = br.readLine()) != null) {
				en[0] += Entropy.getShannonEntropy(text);
				//en[0] *= text.getBytes().length/2;
				if (en[0] < en[1]) {
					en[1] = en[0];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return en;
	}

	public static double[] entropy(ArrayList<String> list) {
		double[] entropy = new double[2];
		entropy[0] = 0;
		entropy[1] = Double.POSITIVE_INFINITY;
		long count = 0;
		long ones = 0;
		if (list.isEmpty())
			return null;

		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			char[] binaryOfText = toBinary(it.next()).toCharArray();
			count += binaryOfText.length;
			double numOfOnes = 0;
			for (int i = 0; i < binaryOfText.length; i++) {
				if (binaryOfText[i] == '1')
					numOfOnes++;
			}
			ones += numOfOnes;
			/*
			double p1 = numOfOnes / (double)binaryOfText.length;
			double p0 = ((double)binaryOfText.length - numOfOnes) / (double)binaryOfText.length;
			double n = -((p0 * (Math.log(p0) / Math.log(2))) + (p1 * (Math.log(p1) / Math.log(2))));
			entropy[0] += -n;
			if (n < entropy[1])
				entropy[1] = n;*/
		}
		double p1 = (double)ones / (double)count;
		double p0 = ((double)count - (double)ones) / (double)count;
		double n = -((p0 * (Math.log(p0) / Math.log(2))) + (p1 * (Math.log(p1) / Math.log(2))));
		entropy[0] += -n;
		if (n < entropy[1])
			entropy[1] = n;
		entropy[0] = -entropy[0];
		return entropy;
	}

	public static double[] entropy(String file) {
		double[] entropy = new double[2];
		entropy[0] = 0;
		entropy[1] = Double.POSITIVE_INFINITY;
		long count = 0;
		long ones = 0;
		if (file.isEmpty())
			return null;

		BufferedReader br = null;
		try {
			String text;
			br = new BufferedReader(new FileReader(file));
			while ((text = br.readLine()) != null) {
				char[] binaryOfText = toBinary(text).toCharArray();
				count += binaryOfText.length;
				double numOfOnes = 0;
				for (int i = 0; i < binaryOfText.length; i++) {
					if (binaryOfText[i] == '1')
						numOfOnes++;
				}
				ones += numOfOnes;
				
				double p1 = numOfOnes / (double)binaryOfText.length;
				double p0 = ((double)binaryOfText.length - numOfOnes) / (double)binaryOfText.length;
				double n = -((p0 * (Math.log(p0) / Math.log(2))) + (p1 * (Math.log(p1) / Math.log(2))));
				//entropy[0] += -n;
				if (n < entropy[1])
					entropy[1] = n;
			}
			double p1 = (double)ones / (double)count;
			double p0 = ((double)count - (double)ones) / (double)count;
			double n = -((p0 * (Math.log(p0) / Math.log(2))) + (p1 * (Math.log(p1) / Math.log(2))));
			entropy[0] += -n;
			if (n < entropy[1])
				entropy[1] = n;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		entropy[0] = -entropy[0];
		return entropy;
	}

	public static double minentropy(String file) {
		double[] entropies = new double[9216];
		BufferedReader br1 = null;
		int countRows = 0;
		try {
			String tweet;
			br1 = new BufferedReader(new FileReader("generaltak/generaltkulcsok.txt"));

			while ((tweet = br1.readLine()) != null) {
				char[] betuk = toBinary(tweet).toCharArray();
				if (betuk.length > 0)
					for (int i = 0; i < betuk.length; i++) {
						if (betuk[i] == '1') {
							entropies[i]++;
						}
					}
				countRows++;
				System.out.println(tweet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br1 != null)
					br1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		double maxEntrophy = Double.NEGATIVE_INFINITY;
		double minEntrophy = Double.POSITIVE_INFINITY;
		double sumEntrophy = 0;
		for (int i = 0; i < entropies.length; i++) {
			if (entropies[i] != 0) {
				entropies[i] /= countRows;
				double n = -(Math.log(entropies[i]) / Math.log(2));
				if (n < minEntrophy)
					minEntrophy = entropies[i];
				if (n > maxEntrophy)
					maxEntrophy = entropies[i];
				sumEntrophy += -(entropies[i] * (Math.log(entropies[i]) / Math.log(2)));
				System.out.println(entropies[i]);
			}
		}
		// makeTextFromIntegers(entropies, "entropies.txt");
		System.out.println("max-entropy: " + maxEntrophy);
		System.out.println("min-entropy: " + minEntrophy);
		System.out.println("entropy: " + sumEntrophy);
		return minEntrophy;
	}

	public static byte[] hash(byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(null, salt, 2000, 256);
		// Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}
	
	public static void createFilesForColls(String src) {
		if (src == null | "".equals(src))
			return;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(src));
			String encripted;
			while ((encripted = br.readLine()) != null) {
					//SHA/Skein
					PrintWriter chars64 = null;
					PrintWriter chars32 = null;
					PrintWriter chars16 = null;
					if (src.endsWith(Constants.sha)) {
						chars64 = new PrintWriter(
							((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath()
							+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.sha64);
						chars32 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath()
								+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.sha32);
						chars16 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath()
								+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.sha16);
						//BufferedWriter chars8 = new BufferedWriter(new FileWriter(
						//		VaadinServlet.getCurrent().getServletContext().getRealPath("") + Constants.sha8));
					}
					else if (src.endsWith(Constants.skein)) {
						chars64 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath()
								+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.skein64);
						chars32 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath() 
									+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.skein32);
						chars16 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath()
									+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.skein16);
					}
					else {
						chars64 = new PrintWriter(
								((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath() 
								+ ((KeyanalysisUI)UI.getCurrent()).getProcess().getFile().getName() + Constants.aes64);
					}
					if (src.endsWith(Constants.aes) ) {
						Iterator<String> it = get64Characters(encripted).iterator();
						while(it.hasNext()) {
							chars64.println(it.next());
						}
						chars64.close();
					}
					else {
						Iterator<String> it = get64Characters(encripted).iterator();
						while(it.hasNext()) {
							chars64.println(it.next());
						}
						it = get32Characters(encripted).iterator();
						while(it.hasNext()) {
							chars32.println(it.next());
						}
						it = get16Characters(encripted).iterator();
						while(it.hasNext()) {
							chars16.println(it.next());
						}
						chars64.close();
						chars32.close();
						chars16.close();
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int countCollsFromFile(File file) {
		file = sortFile(file);
		int colls = 0;
		if (file == null || !file.exists() || file.isDirectory())
			return colls;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine() != null ? br.readLine() : null;
			String next;
			while ((next = br.readLine()) != null) {
				if (line.equals(next)) {
					colls++;
				}
				line = next;
				next = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return colls;
	}
	
	private static File sortFile(File file) {
		File newFile = new File(((KeyanalysisUI)UI.getCurrent()).getProcess().getFilePath() + 
				file.getName().substring(0, file.getName().lastIndexOf(".")) + "sorted.txt" );
		try {
			ExternalSort.sort(file, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFile;
	}
}
