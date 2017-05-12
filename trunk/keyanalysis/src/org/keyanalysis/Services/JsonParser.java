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
	public static String getString(File file, String wantTag) {
		if (!file.exists() || !file.isFile() || "".equals(wantTag)) {
			return null;
		}
		
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(file));

			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray statuses = (JSONArray) jsonObject.get("statuses");
			
			Iterator<?> iterator = statuses.iterator();
			while (iterator.hasNext()) {
				JSONObject statobj = (JSONObject) iterator.next();
				return (String) statobj.get(wantTag);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
