package org.keyanalysis.Services;

public class Constants {
	public static final String title = "<h2><b>Keyanalysis</b></h2>";
	public static final String fiftyPerCent = "45%";
	public static final String keyFieldPrompt = "Give a key or leave it empty!";
	public static final int keyMaxLength = 32;
	public static final String uploading = "Uploading...";
	public static final String uploadButton = "File upload";
	public static final String upload = "Upload";
	public static final String download = "Download";
	public static final String uploadDone = "Successful upload";
	public static final String uploadUndone = "Failed to upload";
	public static final int uploadSizeMB = 50;
	public static final double wantedHammingDistance = 0.3;
	public static final String delimiter = "Delimiter character";
	public static final String hasTag = "Has it column identity?";
	public static final String whichTag = "Which column contain the text?";
	public static final String wantedHamming = "Wanted Hamming-distance: (0-0.5)";
	public static final String done = "Done";
	public static final String cancel = "Cancel";
	public static final String atlag = "Avarage:";
	public static final String szoras = "Dispersion:";
	public static final String median = "Median:";
	public static final String compare = "Compare";

	// Panel sorszÃ¡mok
	public static final int textPerAESPanel_Number = 0;
	public static final int textPerSHAPanel_Number = 1;
	public static final int textPerSkeinPanel_Number = 2;
	public static final int AES10Panel_Number = 3;
	public static final int key10Panel_Number = 4;
	public static final int textAESHammingPanel_Number = 5;
	public static final int textSHAHammingPanel_Number = 6;
	public static final int textSkeinHammingPanel_Number = 7;
	public static final int textLengthPanel_Number = 8;
	public static final int entropy_Number = 9;
	public static final int collisions_Number = 10;
	public static final int serpent10Panel_Number = 11;
	public static final int textPerserpentPanel_Number = 12;
	public static final int textSerpentHammingPanel_Number = 13;
	public static final int twofish10Panel_Number = 14;
	public static final int textPerTwofishPanel_Number = 15;
	public static final int textTwofishHammingPanel_Number = 16;
	public static final int blowfish10Panel_Number = 17;
	public static final int textPerblowfishPanel_Number = 18;
	public static final int textblowfishHammingPanel_Number = 19;

	// Panel elnevezï¿½sek
	public static final String textPerAESPanel = "Binary rate of source and AES encode";
	public static final String textPerSerpentPanel = "Binary rate of source and Serpent encode";
	public static final String textPerBlowfishPanel = "Binary rate of source and Blowfish encode";
	public static final String textPerTwofishPanel = "Binary rate of source and Twofish encode";
	public static final String AES10Panel = "Binary rate in AES strings";
	public static final String key10Panel = "Binary rate in key strings";
	public static final String serpentPanel = "Binary rate in Serpent strings";
	public static final String blowfishPanel = "Binary rate in Blowfish strings";
	public static final String twofishPanel = "Binary rate in Twofish strings";
	public static final String textLengthPanel = "Distances of binaries of the source";
	public static final String textAESHammingPanel = "Hamming-distance of source and AES / Distances of binaries of the source";
	public static final String textSerpentHammingPanel = "Hamming-distance of source and Serpent / Distances of binaries of the source";
	public static final String textBlowfishHammingPanel = "Hamming-distance of source and Blowfish / Distances of binaries of the source";
	public static final String textTwofishHammingPanel = "Hamming-distance of source and Twofish / Distances of binaries of the source";
	public static final String textSHAHammingPanel = "Hamming-distance of source and SHA / Distances of binaries of the source";
	public static final String textSkeinHammingPanel = "Hamming-distance of source and Skein / Distances of binaries of the source";
	public static final String entropyPanel = "Entropies";
	public static final String textPerSHAPanel = "Binary rate of source and SHA hashes";
	public static final String textPerSkeinPanel = "Binary rate of source and Skein hashes";
	public static final String collisionsPanel = "Collisions";

	// EntrÃ³pia
	public static final String SourceEntropy = "Shannon entropy of the source: ";
	public static final String AesEntropy = "Shannon entropy of the AES: ";
	public static final String SerpentEntropy = "Shannon entropy of the Serpent: ";
	public static final String BlowfishEntropy = "Shannon entropy of the Blowfish: ";
	public static final String TwofishEntropy = "Shannon entropy of the Twofish: ";
	public static final String ShaEntropy = "Shannon entropy of the SHA: ";
	public static final String SkeinEntropy = "Shannon entropy of the Skein: ";
	public static final String SourceMinEntropy = "Min-entropy of the source: ";
	public static final String AesMinEntropy = "Min-entropy of the AES: ";
	public static final String SerpentMinEntropy = "Min-entropy of the Serpent: ";
	public static final String BlowfishMinEntropy = "Min-entropy of the Blowfish: ";
	public static final String TwofishMinEntropy = "Min-entropy of the Twofish: ";
	public static final String ShaMinEntropy = "Min-entropy of the SHA: ";
	public static final String SkeinMinEntropy = "Min-entropy of the Skein: ";

	// Ütközések

	// HibaÃ¼zenetek
	public static final String histogramError = "There was a problem with creating histogram";
	public static final String errorOccured = "Something was wrong";
	public static final String bigFile = "File is too big";
	public static final String notCorrectKey = "Not correct length!";
	public static final String wrongFileformat = "Wrong file format!";

	// Histogram tÃ­pusok
	public static final String RateHistogramm = "rate";
	public static final String HammingHistogramm = "hamming";

	public static final String aes = "_AES256.txt";
	public static final String sha = "_SHA.txt";
	public static final String skein = "_SKEIN.txt";
	public static final String key = "_KEY.txt";
	public static final String serpent = "_SERPENT.txt";
	public static final String blowfish = "_BLOWFISH.txt";
	public static final String twofish = "_TWOFISH.txt";

	public static final String aes64 = "_AESCutted256.txt";

	public static final String sha64 = "_SHACutted256.txt";
	public static final String sha32 = "_SHACutted128.txt";
	public static final String sha16 = "_SHACutted64.txt";
	public static final String sha8 = "_SHACutted32.txt";

	public static final String skein64 = "_SKEINCutted256.txt";
	public static final String skein32 = "_SKEINCutted128.txt";
	public static final String skein16 = "_SKEINCutted64.txt";
	public static final String skein8 = "_SKEINCutted32.txt";

}
