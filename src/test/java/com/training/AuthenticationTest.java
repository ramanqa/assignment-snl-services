package com.training;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.response.Response;

public class AuthenticationTest {
	Integer id;
	Integer player_id;
	String path;

	@BeforeTest
	public void load() {
		RestAssured.baseURI = "http://10.0.1.86/snl";

	}

	@Test(priority = 1)
	public void auth_verify_board() {

		given().auth().preemptive().basic("su", "root_pass").when().get("/rest/v2/board.json").then().statusCode(200);
		Response res = given().auth().preemptive().basic("su", "root_pass").when().get("/rest/v2/board/new.json");
		id = res.getBody().jsonPath().getJsonObject("response.board.id");
		res.then().statusCode(200);
		path = "/rest/v2/board/" + id + ".json";
		given().auth().preemptive().basic("su", "root_pass").when().get(path).then().statusCode(200);

		System.out.println(res.asString());
	}

	@Test(priority = 2)
	public void auth_verify_player() throws UnsupportedEncodingException, IOException, ParseException

	{

		String son = "{ \"board\":\"" + id + "\",  \"player\": {\"name\": \"shivani\" } }";

		Response res = given().auth().preemptive().basic("su", "root_pass").when().contentType("application/json")
				.body(son).when().post("/rest/v2/player.json");
		player_id = res.getBody().jsonPath().getJsonObject("response.player.id");
		res.then().statusCode(200);
	}

	@Test(priority = 3)
	public void auth_update_player() throws FileNotFoundException, IOException, ParseException {
		String path2 = "/rest/v2/player/" + player_id + ".json";

		given().auth().preemptive().basic("su", "root_pass").get(path2).then().statusCode(200);

		String json = "{\"player\":{\"name\": \"kanika\"}}";
		given().auth().preemptive().basic("su", "root_pass").when().contentType("application/json").body(json).when()
				.put(path2).then().statusCode(200);
	}

	@Test(priority = 4)
	public void auth_move_player() {
		String path3 = "/rest/v2/move/" + id + ".json?player_id=" + player_id;
		given().auth().preemptive().basic("su", "root_pass").when().get(path3).then().statusCode(200);

	}

	@Test(priority = 5)
	public void delete_board() {
		given().auth().preemptive().basic("su", "root_pass").when().put(path).then().statusCode(200);
		given().auth().preemptive().basic("su", "root_pass").when().delete(path).then().statusCode(200);

	}
}
/* board id used is 78 */
