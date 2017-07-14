package com.training;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

public class SnakeandLadderVersion1Test {
	Integer boardid;
	Integer playerid1;
	Integer playerid2;

	@BeforeClass
	public void setBaseUri() {

		RestAssured.baseURI = "http://10.0.1.86/snl";
	}

	@Test
	public void testing_1_board_status() {
		/**
		 * GET - Get a list of boards
		 */
		given().get("/rest/v1/board.json").then().statusCode(200);

		/**
		 * GET - create a new board
		 */
		boardid = given().get("/rest/v1/board/new.json").then().statusCode(200).extract().path("response.board.id");
		/**
		 * GET - Get a details of a board
		 */
		given().get("/rest/v1/board/" + boardid + ".json").then().statusCode(200);
	}

	@Test
	public void testing_2_player_Status() throws UnsupportedEncodingException, IOException, ParseException {

		/**
		 * POST - Join new player to a board
		 */
		String playerDetails1 = "{ \"board\":\"" + boardid + "\",  \"player\": {\"name\": \"anjali\" } }";
		playerid1 = given().contentType("application/json").body(playerDetails1).when().post("/rest/v1/player.json")
				.then().statusCode(200).extract().response().path("response.player.id");
		String playerDetails2 = "{ \"board\":\"" + boardid + "\",  \"player\": {\"name\": \"pranjali\" } }";
		playerid2 = given().contentType("application/json").body(playerDetails2).when().post("/rest/v1/player.json")
				.then().statusCode(200).extract().response().path("response.player.id");

		/**
		 * GET - get player details
		 */
		given().get("/rest/v1/player/" + playerid1 + ".json").then().statusCode(200);
		given().get("/rest/v1/player/" + playerid2 + ".json").then().statusCode(200);
		/**
		 * PUT - update player details
		 */
		String updatedPlayer = "{\"player\": {\"name\": \"anu\" } }";
		given().contentType("application/json").body(updatedPlayer).when().put("/rest/v1/player/" + playerid2 + ".json")
				.then().statusCode(200);
	}

	@Test
	public void testing_3_roll_dice_movement_status() {
		/**
		 * GET - roll dice and move player on board
		 */

		given().get("/rest/v1/move/" + boardid + ".json?player_id=" + playerid1 + "").then().statusCode(200);
	}

	@Test
	public void z_deleting_board_and_players() {
		/**
		 * DELETE - quit player from game and destroy player
		 */
		given().delete("/rest/v1/player/" + playerid2 + ".json").then().statusCode(200);
		/**
		 * PUT - Reset board, delete players
		 */
		given().put("/rest/v1/board/" + boardid + ".json").then().statusCode(200);
		/**
		 * DELETE - Destroy board
		 */
		given().delete("/rest/v1/board/" + boardid + ".json");
	}

}
