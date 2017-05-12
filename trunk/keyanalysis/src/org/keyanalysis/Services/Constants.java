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
	public static final String textPerAESPanel = "Forrás és AES titkosításainak 1/0 aránya";
	public static final String textPerSerpentPanel = "Forrás és Serpent titkosításainak 1/0 aránya";
	public static final String textPerBlowfishPanel = "Forrás és Blowfish titkosításainak 1/0 aránya";
	public static final String textPerTwofishPanel = "Forrás és Twofish titkosításainak 1/0 aránya";
	public static final String AES10Panel = "AES-ben lévõ 1/0 arány";
	public static final String key10Panel = "Kulcsokban lévõ 1/0 arány";
	public static final String serpentPanel = "Serpentben lévõ 1/0 arány";
	public static final String blowfishPanel = "Blowfishben lévõ 1/0 arány";
	public static final String twofishPanel = "Twofishben lévõ 1/0 arány";
	public static final String textLengthPanel = "Források bináris hosszai";
	public static final String textAESHammingPanel = "Forrás és AES-eik hamming távolsága / Forrás bináris hossza";
	public static final String textSerpentHammingPanel = "Forrás és Serpentjeik hamming távolsága / Forrás bináris hossza";
	public static final String textBlowfishHammingPanel = "Forrás és Blowfisheik hamming távolsága / Forrás bináris hossza";
	public static final String textTwofishHammingPanel = "Forrás és Twofisheik hamming távolsága / Forrás bináris hossza";
	public static final String textSHAHammingPanel = "Forrás és SHA-ik Hamming távolsága / Forrás bináris hossza";
	public static final String textSkeinHammingPanel = "Forrás és Skein-ik Hamming távolsága / Forrás bináris hossza";
	public static final String entropyPanel = "Entropies";
	public static final String textPerSHAPanel = "Forrás és SHA hasheinek 1/0 aránya";
	public static final String textPerSkeinPanel = "Forrás és Skein hasheinek 1/0 aránya";
	public static final String collisionsPanel = "Collisions";

	// EntrÃ³pia
	public static final String SourceEntropy = "A forrás Shannon entrópiája: ";
	public static final String AesEntropy = "Az AES Shannon entrópiája: ";
	public static final String SerpentEntropy = "Az Serpent Shannon entrópiája: ";
	public static final String BlowfishEntropy = "Az Blowfish Shannon entrópiája: ";
	public static final String TwofishEntropy = "Az Twofish Shannon entrópiája: ";
	public static final String ShaEntropy = "Az SHA Shannon entrópiája: ";
	public static final String SkeinEntropy = "A Skein Shannon entrópiája: ";
	public static final String SourceMinEntropy = "A forrás minimum entrópiája: ";
	public static final String AesMinEntropy = "Az AES minimum entrópiája: ";
	public static final String SerpentMinEntropy = "Az Serpent minimum entrópiája: ";
	public static final String BlowfishMinEntropy = "Az Blowfish minimum entrópiája: ";
	public static final String TwofishMinEntropy = "Az Twofish minimum entrópiája: ";
	public static final String ShaMinEntropy = "Az SHA minimum entrópiája: ";
	public static final String SkeinMinEntropy = "A Skein minimum entrópiája: ";
	
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
