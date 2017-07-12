package com.training;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class OauthTest {
	String accessToken;

	@BeforeClass
	public void auth() {

		String response = given()
				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
						"5400b11dc3aab7d3daa63c7aeb0cfbde54eb56beda0a4bae45aeafdd550560ea", "client_secret",
						"1b680fc380a285b46ea222e5751839ecc81bcbf31786708e0b9d4b916b53a973")
				.auth().preemptive()
				.basic("5400b11dc3aab7d3daa63c7aeb0cfbde54eb56beda0a4bae45aeafdd550560ea",
						"1b680fc380a285b46ea222e5751839ecc81bcbf31786708e0b9d4b916b53a973")
				.when().post("http://10.0.1.86/snl/oauth/token").asString();

		JsonPath jsonPath = new JsonPath(response);
		accessToken = jsonPath.getString("access_token");

		RestAssured.baseURI = "http://10.0.1.86/snl";

	}

	@Test
	public void checking_basic_Authentication() {

		given().auth().oauth2(accessToken).get().then().statusCode(200);
	}
		@Test
	public void testing_board_status()
	{
		/**
		 * GET - Get a list of boards
		 */
	given().auth().oauth2(accessToken).get("/rest/v1/board.json").then().statusCode(200);
	
		/**
		 * GET - create a new board
		 */
		given().auth().oauth2(accessToken).get("/rest/v1/board/new.json").then().statusCode(200);
	
		/**
		 * GET - Get a details of a board
		 */
		given().auth().oauth2(accessToken).get("/rest/v1/board/8.json").then().statusCode(200);
		
		/**
		 * PUT - Reset board, delete players
		 */
		given().auth().oauth2(accessToken).put("/rest/v1/board/8.json").then().statusCode(200);
		/**
		 * DELETE - Destroy board
		 */

		given().auth().oauth2(accessToken).delete("/rest/v1/board/24.json").then().statusCode(200);

	}
	@Test
	public void testing_player_Status() throws UnsupportedEncodingException, IOException, ParseException {

		/**
		 * POST - Join new player to a board
		 */
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("db.json");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(input, "UTF-8"));
		given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject).when().post("/rest/v1/player.json").then()
				.statusCode(200);
		/**
		 * GET - get player details
		 */
		given().auth().oauth2(accessToken).get("/rest/v1/player/70.json").then().statusCode(200);

		/**
		 * PUT - update player details
		 */
		InputStream input1 = this.getClass().getClassLoader().getResourceAsStream("ab.json");
		JSONObject jsonObject1 = (JSONObject) jsonParser.parse(new InputStreamReader(input1, "UTF-8"));
		given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject1).when().put("/rest/v1/player/70.json").then()
				.statusCode(200);
		given().auth().oauth2(accessToken).get("/rest/v1/player/70.json").then().statusCode(200);
		/**
		 * DELETE - quit player from game and destroy player
		 */
		given().auth().oauth2(accessToken).delete("/rest/v1/player/72.json").then().statusCode(200);

	}
	@Test
	public void testing_roll_dice_movement_status()
	{
		/**
		 * GET - roll dice and move player on board
		 */
		
				given().auth().oauth2(accessToken).get("/rest/v1/move/42.json?player_id=70").then().statusCode(200);
	}

}
