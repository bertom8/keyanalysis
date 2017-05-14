package org.keyanalysis.Services;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import biz.source_code.base64Coder.Base64Coder;
import gnu.crypto.cipher.Blowfish;
import gnu.crypto.cipher.Serpent;
import gnu.crypto.cipher.Twofish;
import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public final class DigestService {
	private static Cipher cipher;
	// private static SecretKey sKey = new
	// SecretKeySpec("egik5utjgp8mi1xv".getBytes(), "AES");
	private static SecretKey sKey = new SecretKeySpec("egik5utjgp8mi1xv94lx9af82gh0neds".getBytes(), "AES");

	public static SecretKey generateRandomKeyWithInsideAlg() {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyGenerator.init(256);
		return keyGenerator.generateKey();
	}

	public static SecretKey generateRandomKey() {
		String s = "";
		final Random r = new Random(System.nanoTime());
		int i = 0;
		while (i <= 32 * 8) {
			final char b = ((r.nextInt(10) % 2) == 0) ? '1' : '0';
			s = s + b;
			i++;
		}
		// s = s.substring(0, 192);

		final byte[] bi = new BigInteger(s, 2).toByteArray();
		// System.out.println(bi.length);
		final byte[] bytearray = new byte[24];
		for (int j = 0; j < bytearray.length; j++) {
			bytearray[j] = bi[j];
		}
		// System.out.println(Base64.getEncoder().encodeToString(bytearray));
		return new SecretKeySpec(bytearray, "AES");
	}

	public static SecretKey generateRandomKey(final int numberOfOnes, final Random r) {
		String s = "";
		// Random r = new Random();
		int ones = 0;
		while (ones <= numberOfOnes) {
			final boolean b1 = ((r.nextInt(10) % 2) == 0);
			char c;
			if (b1) {
				c = '1';
				ones++;
			} else {
				c = '0';
			}
			s = s + c;
		}

		while (ones <= 32 * 8 + 8) {
			final boolean b = r.nextBoolean();
			if (b) {
				s = s + '1';
			} else {
				s = s + '0';
			}
			ones++;
		}
		final byte[] bi = new BigInteger(s, 2).toByteArray();
		// System.out.println(bi.length);
		final byte[] bytearray = new byte[32];
		for (int j = 0; j < 32; j++) {
			bytearray[j] = bi[j];
		}
		// System.out.println(Base64.getEncoder().encodeToString(bytearray));
		return new SecretKeySpec(bytearray, "AES");
		/*
		 * KeyGenerator keyGenerator = null; try { keyGenerator =
		 * KeyGenerator.getInstance("AES"); } catch (NoSuchAlgorithmException e)
		 * { e.printStackTrace(); } keyGenerator.init(256); return
		 * keyGenerator.generateKey();
		 */
	}

	public static String encriptAES(final String source) {
		// For random generated key
		/*
		 * KeyGenerator keyGenerator = null; try { keyGenerator =
		 * KeyGenerator.getInstance("AES"); } catch (NoSuchAlgorithmException e)
		 * { e.printStackTrace(); } keyGenerator.init(128); sKey =
		 * keyGenerator.generateKey();
		 */
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		final byte[] plainTextByte = source.getBytes();
		try {
			cipher.init(Cipher.ENCRYPT_MODE, sKey);
		} catch (final InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] encryptedByte = null;
		try {
			encryptedByte = cipher.doFinal(plainTextByte);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		final Base64.Encoder encoder = Base64.getEncoder();
		final String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}

	public static String encriptSerpent(final String source, final SecretKey key)
			throws InvalidKeyException, UnsupportedEncodingException {
		String text = new String(source.getBytes("UTF8"), StandardCharsets.UTF_8);
		byte[] plainText = null;
		byte[] encryptedText;
		final Serpent serpent = new Serpent();
		// create a key
		final byte[] keyBytes = key.getEncoded();
		final Object keyObject = serpent.makeKey(keyBytes, 16);
		// make the length of the text a multiple of the block size
		if ((text.getBytes("UTF8").length % 32) != 0) {
			while ((text.getBytes("UTF8").length % 32) != 0) {
				text += " ";
			}
		}
		// initialize byte arrays for plain/encrypted text
		try {
			plainText = text.getBytes("UTF8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		encryptedText = new byte[plainText.length];
		// encrypt text in 8-byte chunks
		for (int i = 0; i < plainText.length; i += 32) {
			serpent.encrypt(plainText, i, encryptedText, i, keyObject, 16);
		}
		final String encryptedString = Base64Coder.encodeLines(encryptedText);
		return encryptedString;
	}

	public static String encriptTwofish(final String source, final SecretKey key)
			throws InvalidKeyException, UnsupportedEncodingException {
		String text = new String(source.getBytes("UTF8"), StandardCharsets.UTF_8);
		byte[] plainText = null;
		byte[] encryptedText;
		final Twofish twofish = new Twofish();
		// create a key
		final byte[] keyBytes = key.getEncoded();
		final Object keyObject = twofish.makeKey(keyBytes, 16);
		// make the length of the text a multiple of the block size
		if ((text.getBytes("UTF8").length % 16) != 0) {
			while ((text.getBytes("UTF8").length % 16) != 0) {
				text += " ";
			}
		}
		// initialize byte arrays for plain/encrypted text
		try {
			plainText = text.getBytes("UTF8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		encryptedText = new byte[plainText.length];
		// encrypt text in 8-byte chunks
		for (int i = 0; i < Array.getLength(plainText); i += 16) {
			twofish.encrypt(plainText, i, encryptedText, i, keyObject, 16);
		}
		final String encryptedString = Base64Coder.encodeLines(encryptedText);
		return encryptedString;
	}

	public static String encriptBlowfish(final String source, final SecretKey key)
			throws InvalidKeyException, UnsupportedEncodingException {
		String text = new String(source.getBytes("UTF8"), StandardCharsets.UTF_8);
		byte[] plainText = null;
		byte[] encryptedText;
		final Blowfish blowfish = new Blowfish();
		// create a key
		final byte[] keyBytes = key.getEncoded();
		final Object keyObject = blowfish.makeKey(keyBytes, 16);
		// make the length of the text a multiple of the block size
		if ((text.getBytes("UTF8").length % 16) != 0) {
			while ((text.getBytes("UTF8").length % 16) != 0) {
				text += " ";
			}
		}
		// initialize byte arrays for plain/encrypted text
		try {
			plainText = text.getBytes("UTF8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		encryptedText = new byte[plainText.length];
		// encrypt text in 8-byte chunks
		for (int i = 0; i < Array.getLength(plainText); i += 16) {
			blowfish.encrypt(plainText, i, encryptedText, i, keyObject, 16);
		}
		final String encryptedString = Base64Coder.encodeLines(encryptedText);
		return encryptedString;
	}

	public static String encriptAES(final String source, final SecretKey key) {
		// For random generated key
		/*
		 * KeyGenerator keyGenerator = null; try { keyGenerator =
		 * KeyGenerator.getInstance("AES"); } catch (NoSuchAlgorithmException e)
		 * { e.printStackTrace(); } keyGenerator.init(128); sKey =
		 * keyGenerator.generateKey();
		 */
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		final byte[] plainTextByte = source.getBytes();
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (final InvalidKeyException e) {
			e.printStackTrace();
		}
		byte[] encryptedByte = null;
		try {
			encryptedByte = cipher.doFinal(plainTextByte);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		final Base64.Encoder encoder = Base64.getEncoder();
		final String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}

	public static String decriptAES(final String source) {
		final Base64.Decoder decoder = Base64.getDecoder();
		final byte[] encryptedTextByte = decoder.decode(source);
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		byte[] decryptedByte = null;
		try {
			decryptedByte = cipher.doFinal(encryptedTextByte);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		final String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	public static String decriptAES(final String source, final SecretKey key) {
		final Base64.Decoder decoder = Base64.getDecoder();
		final byte[] encryptedTextByte = decoder.decode(source);
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		byte[] decryptedByte = null;
		try {
			decryptedByte = cipher.doFinal(encryptedTextByte);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		final String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	public static String getMD5Hash(final String source) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.reset();
		md.update(source.getBytes());
		final byte[] digest = md.digest();
		final BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}

	public static String getSHA1Hash(final String source) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		final byte[] result = md.digest(source.getBytes());
		final StringBuffer sb = new StringBuffer();
		for (final byte element : result) {
			sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String getSHA2_512Hash(final String source) {
		Security.addProvider(new BouncyCastleProvider());
		MessageDigest mda = null;
		try {
			mda = MessageDigest.getInstance("SHA-512", "BC");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		final byte[] digest = mda.digest(source.getBytes());
		return toHexString(digest);
		// return Hex.encodeHexString(digest);
		// return org.bouncycastle.util.encoders.Hex.toHexString(digest);
	}

	public static String getSHA2Hash(final String source) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		final byte[] result = md.digest(source.getBytes());
		final StringBuffer sb = new StringBuffer();
		for (final byte element : result) {
			sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String getSHA3Hash(final String source) {
		final DigestSHA3 md = new DigestSHA3(512); // same as DigestSHA3 md =
													// new
													// SHA3.Digest256();
		md.update(source.getBytes());
		final byte[] digest = md.digest();
		return Hex.encodeHexString(digest);
		// return org.bouncycastle.util.encoders.Hex.toHexString(digest);
	}

	public static String getRIPEMD160Hash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("ripemd160");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
			// System.out.println(checksum.getFormattedValue());
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getWhirlpoolHash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("whirlpool");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
			// System.out.println(checksum.getFormattedValue());
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getHavalHash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("haval");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getTigerHash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("tiger");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getGostHash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("gost");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getTiger2Hash(final String source) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("tiger2");
			checksum.update(source.getBytes());
			return checksum.getFormattedValue();
		} catch (final NoSuchAlgorithmException nsae) {
		}
		return null;
	}

	public static String getSkeinHash(final String source) {
		final byte[] result = new byte[64];
		Skein512.hash(source.getBytes(), result);
		return toHexString(result);
		// return org.bouncycastle.util.encoders.Hex.toHexString(result);
	}

	private static String toHexString(final byte[] bytes) {
		final StringBuilder hexString = new StringBuilder();
		for (final byte b : bytes) {
			final String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
