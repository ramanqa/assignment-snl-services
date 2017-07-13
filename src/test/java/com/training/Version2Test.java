package com.training;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Version2Test {
	Integer boardid;
	Integer playerid1;

	
	@BeforeClass
	public void setBaseUri() {

		RestAssured.baseURI = "http://10.0.1.86/snl";
		
	}

	@Test
	public void checking_basic_Authentication() {
            given().auth().basic("su", "root_pass").get().then().statusCode(200);
	}
	@Test
	public void testing_board_status()
	{
		/**
		 * GET - Get a list of boards
		 */
	given().auth().basic("su", "root_pass").get("/rest/v2/board.json").then().statusCode(200);
	
		/**
		 * GET - create a new board
		 */
	
		boardid = given().auth().basic("su", "root_pass").get("/rest/v2/board/new.json").then().statusCode(200).extract().path("response.board.id");

		/**
		 * GET - Get a details of a board
		 */
		
		given().auth().basic("su", "root_pass").get("/rest/v2/board/" + boardid + ".json").then().statusCode(200);
		

	}

	@Test
	public void testing_player_Status() throws UnsupportedEncodingException, IOException, ParseException {

		/**
		 * POST - Join new player to a board
		 */
	
		
		String playerDetails1 = "{ \"board\":\"" + boardid + "\",  \"player\": {\"name\": \"anjali\" } }";

		playerid1 = given().auth().basic("su", "root_pass").contentType("application/json").body(playerDetails1).when().post("/rest/v2/player.json")
				.then().statusCode(200).extract().response().path("response.player.id");
		/**
		 * GET - get player details
		 */
		
		given().auth().basic("su", "root_pass").get("/rest/v2/player/" + playerid1 + ".json").then().statusCode(200);
		/**
		 * PUT - update player details
		 */
		
		String updatedPlayer = "{\"player\": {\"name\": \"anu\" } }";
		given().auth().basic("su", "root_pass").contentType("application/json").body(updatedPlayer).when().put("/rest/v2/player/" + playerid1 + ".json")
				.then().statusCode(200);
		
	
	}
	@Test
	public void testing_roll_dice_movement_status()
	{
		/**
		 * GET - roll dice and move player on board
		 */
		
		given().auth().basic("su", "root_pass").get("/rest/v2/move/" + boardid + ".json?player_id=" + playerid1 + "").then().statusCode(200);
	}
	@Test
	public void z_deleting_board_and_players() {
		/**
		 * DELETE - quit player from game and destroy player
		 */
		given().auth().basic("su", "root_pass").delete("/rest/v2/player/" + playerid1 + ".json").then().statusCode(200);
		/**
		 * PUT - Reset board, delete players
		 */
		given().auth().basic("su", "root_pass").put("/rest/v2/board/" + boardid + ".json").then().statusCode(200);
		/**
		 * DELETE - Destroy board
		 */
		given().auth().basic("su", "root_pass").delete("/rest/v2/board/" + boardid + ".json");
	}

	

}
