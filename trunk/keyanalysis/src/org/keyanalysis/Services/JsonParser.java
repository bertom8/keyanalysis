package org.keyanalysis.Services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
	public static String getString(final File file, final String wantTag) {
		if (!file.exists() || !file.isFile() || "".equals(wantTag)) {
			return null;
		}

		final JSONParser parser = new JSONParser();
		try {

			final Object obj = parser.parse(new FileReader(file));

			final JSONObject jsonObject = (JSONObject) obj;

			final JSONArray statuses = (JSONArray) jsonObject.get("statuses");

			final Iterator<?> iterator = statuses.iterator();
			while (iterator.hasNext()) {
				final JSONObject statobj = (JSONObject) iterator.next();
				return (String) statobj.get(wantTag);
			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
