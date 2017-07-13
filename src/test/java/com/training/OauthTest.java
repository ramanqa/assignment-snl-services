package com.training;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.lessThan;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class OauthTest {
	String accessToken;
	Integer boardid;
	Integer playerid1;

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
		//testing load time
		given().auth().oauth2(accessToken).get("rest/v3/board.json").then().statusCode(200).and().time(lessThan(1000L));

	}

	@Test
	public void testing_board_status() {
		/**
		 * GET - Get a list of boards
		 */
		given().auth().oauth2(accessToken).get("/rest/v3/board.json").then().statusCode(200);

		/**
		 * GET - create a new board
		 */
		boardid = given().auth().oauth2(accessToken).get("/rest/v3/board/new.json").then().statusCode(200).extract()
				.path("response.board.id");

		/**
		 * GET - Get a details of a board
		 */
		given().auth().oauth2(accessToken).get("/rest/v3/board/" + boardid + ".json").then().statusCode(200);

	}

	@Test
	public void testing_player_Status() throws UnsupportedEncodingException, IOException, ParseException {

		/**
		 * POST - Join new player to a board
		 */
		
		String playerDetails1 = "{ \"board\":\"" + boardid + "\",  \"player\": {\"name\": \"anjali\" } }";

		playerid1 = given().auth().oauth2(accessToken).contentType("application/json").body(playerDetails1).when().post("/rest/v3/player.json")
				.then().statusCode(200).extract().response().path("response.player.id");
		/**
		 * GET - get player details
		 */
		given().auth().oauth2(accessToken).get("/rest/v3/player/" + playerid1 + ".json").then().statusCode(200);

		/**
		 * PUT - update player details
		 */
		String updatedPlayer = "{\"player\": {\"name\": \"anu\" } }";
		given().auth().oauth2(accessToken).contentType("application/json").body(updatedPlayer).when().put("/rest/v3/player/" + playerid1 + ".json")
				.then().statusCode(200);
		
	}

	@Test
	public void testing_roll_dice_movement_status() {
		/**
		 * GET - roll dice and move player on board
		 */
		given().auth().oauth2(accessToken).get("/rest/v3/move/" + boardid + ".json?player_id=" + playerid1 + "").then().statusCode(200);

	}
	@Test
	public void z_deleting_board_and_players() {
		/**
		 * DELETE - quit player from game and destroy player
		 */
		given().auth().oauth2(accessToken).delete("/rest/v3/player/" + playerid1 + ".json").then().statusCode(200);
		/**
		 * PUT - Reset board, delete players
		 */
		given().auth().oauth2(accessToken).put("/rest/v3/board/" + boardid + ".json").then().statusCode(200);
		/**
		 * DELETE - Destroy board
		 */
		given().auth().oauth2(accessToken).delete("/rest/v3/board/" + boardid + ".json");
	}

}
