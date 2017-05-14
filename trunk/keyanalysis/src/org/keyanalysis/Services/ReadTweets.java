package org.keyanalysis.Services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author Bereczki Tamás
 *
 */
public class ReadTweets {
	private final JsonParser parser = new JsonParser();

	public JsonObject readFromString(String str) {
		if (str.length() < 3) {
			return null;
		}
		if (!str.endsWith("}")) {
			str = str.substring(0, str.lastIndexOf('}') + 1);
		}
		return this.parser.parse(str).getAsJsonObject();
	}

}
