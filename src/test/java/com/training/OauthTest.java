package com.training;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.response.Response;

public class OauthTest {
	String accessToken;
	Integer id;
	Integer player_id;
	String path;

	@BeforeTest
	public void auth() {
		String response = given()
				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
						"8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8", "client_secret",
						"bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
				.auth().preemptive()
				.basic("8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8",
						"bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
				.when().post("http://10.0.1.86/snl/oauth/token").asString();

		JsonPath jsonPath = new JsonPath(response);
		accessToken = jsonPath.getString("access_token");

	}

	@Test(priority = 1)
	public void Oauth_verify_board() {

		RestAssured.baseURI = "http://10.0.1.86/snl/";
		given().auth().oauth2(accessToken).get("rest/v3/board.json").then().statusCode(200);
		Response res = given().auth().oauth2(accessToken).get("/rest/v3/board/new.json");
		res.then().statusCode(200);
		id = res.getBody().jsonPath().getJsonObject("response.board.id");
		path = "/rest/v3/board/" + id + ".json";

		given().auth().oauth2(accessToken).get(path).then().statusCode(200);

	}

	@Test(priority = 2)
	public void Oauth_verify_player() throws UnsupportedEncodingException, IOException, ParseException

	{
		RestAssured.baseURI = "http://10.0.1.86/snl";
		String son = "{ \"board\":\"" + id + "\",  \"player\": {\"name\": \"shivani\" } }";
		Response res = given().auth().oauth2(accessToken).contentType("application/json").body(son).when()
				.post("/rest/v3/player.json");
		res.then().statusCode(200);
		player_id = res.getBody().jsonPath().getJsonObject("response.player.id");
	}

	@Test(priority = 3)
	public void Oauth_update_player() throws FileNotFoundException, IOException, ParseException {
		RestAssured.baseURI = "http://10.0.1.86/snl";
		String path2 = "/rest/v3/player/" + player_id + ".json";
		given().auth().oauth2(accessToken).contentType("application/json").get(path2).then().statusCode(200);
		String json = "{\"player\":{\"name\": \"kanika\"}}";
		given().auth().oauth2(accessToken).contentType("application/json").body(json).when().put(path2).then()
				.statusCode(200);
	}

	@Test(priority = 4)
	public void Oauth_move_player() {
		RestAssured.baseURI = "http://10.0.1.86/snl";
		String path3 = "/rest/v3/move/" + id + ".json?player_id=" + player_id;
		given().auth().oauth2(accessToken).get(path3).then().statusCode(200);

	}

	@Test(priority = 5)
	public void delete_board() {
		given().auth().oauth2(accessToken).put(path).then().statusCode(200);
		given().auth().oauth2(accessToken).delete(path).then().statusCode(200);
	}
}
