package org.mytestng.assign4;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class JsonReader {
	
	public List<Version> getListOfVersions() {

		try {
			List<Version> versionList = new ArrayList();
			JSONObject jsonFile = (JSONObject) (new JSONParser()).parse(new FileReader("src/test/myResources/JsonInput.json"));
			JSONArray versionArray = new JSONArray();
			versionArray = (JSONArray) jsonFile.get("versions");
			Map<String, String> versionMap = new HashMap();

			for (int i = 0; i < versionArray.size(); i++) {

				JSONObject singleObject = (JSONObject) versionArray.get(i);
				versionMap.put("version",  (String) singleObject.get("version"));
				versionMap.put("newBoardUrl", (String) singleObject.get("newBoardUrl"));
				versionMap.put("newPlayerUrl",  (String) singleObject.get("newPlayerUrl"));
				versionMap.put("getParticularBoardUrl",  (String) singleObject.get("getParticularBoardUrl"));
				versionMap.put("movePlayerUrl",  (String) singleObject.get("movePlayerUrl"));
				versionMap.put("deletePlayerUrl",  (String) singleObject.get("deletePlayerUrl"));
				versionMap.put("deleteBoardUrl",  (String) singleObject.get("deleteBoardUrl"));


				versionList.add(new Version(versionMap));

			}
			return versionList;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

}
	public static void main(String[] a)
	{
		JsonReader jsonReader = new  JsonReader();
		System.out.println(jsonReader.getListOfVersions().get(1).getDeleteBoardUrl());
	}
	
}
