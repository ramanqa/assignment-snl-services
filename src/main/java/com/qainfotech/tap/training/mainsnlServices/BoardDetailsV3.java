package com.qainfotech.tap.training.mainsnlServices;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.path.json.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BoardDetailsV3 {

	
	String accessToken;
	JSONParser parser = new JSONParser();
	
	public String get_Token(){
		
		String response=RestAssured.given()
				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
						"f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33", "client-Secret",
						"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
				.auth().preemptive()
				.basic("f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33",
						"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
				.when().post("http://10.0.1.86/snl/oauth/token").asString();
		JsonPath jsonPath = new JsonPath(response);
		accessToken = jsonPath.getString("access_token");
		//System.out.println(accessToken);
		return accessToken;
		
	}
	public long getID() throws ParseException {
		String s = RestAssured.given().given().auth().oauth2(accessToken).when()
				.get("http://10.0.1.86/snl/rest/v3/board/new.json").getBody().asString();
		// System.out.println(s);

		JSONObject jo = (JSONObject) parser.parse(s);
		JSONObject jos = (JSONObject) jo.get("response");
		JSONObject josb = (JSONObject) jos.get("board");

		long id = (long) josb.get("id");
		// System.out.println("############# " + id);
		return id;
	}
	public long[] get_player_id() throws ParseException{
		long playerID;
		long boardid = getID();
		/**to create a json object*/
		
		JSONObject obj = new JSONObject();
		obj.put("board", boardid);
		JSONObject player = new JSONObject();
		player.put("name", "max");
		obj.put("player", player);
		String body= obj.toJSONString();
		//System.out.println(body);
		
		
		Response response=RestAssured.given().auth().oauth2(accessToken).body(body).contentType(ContentType.JSON).when().post("http://10.0.1.86/snl/rest/v3/player.json");
		response.then().extract().response();
		response.then().assertThat().statusCode(200);
		
		//Response response =RestAssured.given().contentType(ContentType.JSON).body(body).when().post("http://10.0.1.86/snl/rest/v3/player.json");	 
		playerID =  response.jsonPath().getInt("response.player.id");
		//System.out.println(playerID);
		long array[]= new long[2];
		array[0]=boardid;
		array[1]=playerID;
		return array;
		
	}

	public long parseComplete(long idcheck) throws ParseException {
		long returnid = 0;
		RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/board/new.json")
				.getBody().asString();
		String s2 = RestAssured.given().auth().oauth2(accessToken).when()
				.get("http://10.0.1.86/snl/rest/v3/board.json").getBody().asString();
		JSONObject joo = (JSONObject) parser.parse(s2);
		JSONObject joo1 = (JSONObject) joo.get("response");
		JSONArray jaa1 = (JSONArray) joo1.get("board");

		for (int i = 0; i < jaa1.size(); i++) {
			JSONObject obj = (JSONObject) jaa1.get(i);
			long id = (long) obj.get("id");
			if (idcheck == id) {
				returnid = id;
			} else {
				returnid = 0;
			}

		}
		return returnid;
	}

	
}
