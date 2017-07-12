package com.training;

import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import static com.jayway.restassured.RestAssured.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.jayway.restassured.RestAssured;
//import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class SnakeandLadderVersion1Test {

	@BeforeClass
	public void setBaseUri() {

		RestAssured.baseURI = "http://10.0.1.86/snl";
	}

	@Test
	public void testing_board_status() {
		/**
		 * GET - Get a list of boards
		 */
		Response res = given().get("/rest/v1/board.json");
		Assert.assertEquals(res.statusCode(), 200);
		/**
		 * GET - create a new board
		 */
		Response res1 = given().get("/rest/v1/board/new.json");
		Assert.assertEquals(res1.statusCode(), 200);
		/**
		 * GET - Get a details of a board
		 */
		Response res2 = given().get("/rest/v1/board/6.json");
		Assert.assertEquals(res2.statusCode(), 200);
		/**
		 * PUT - Reset board, delete players
		 */
		given().put("/rest/v1/board/6.json").then().statusCode(200);
		/**
		 * DELETE - Destroy board
		 */

		given().delete("/rest/v1/board/4.json");

	}

	@Test
	public void testing_player_Status() throws UnsupportedEncodingException, IOException, ParseException {

		/**
		 * POST - Join new player to a board
		 */
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("db.json");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(input, "UTF-8"));
		given().contentType("application/json").body(jsonObject).when().post("/rest/v1/player.json").then()
				.statusCode(200);
		/**
		 * GET - get player details
		 */
		given().get("/rest/v1/player/35.json").then().statusCode(200);

		/**
		 * PUT - update player details
		 */
		InputStream input1 = this.getClass().getClassLoader().getResourceAsStream("ab.json");
		JSONObject jsonObject1 = (JSONObject) jsonParser.parse(new InputStreamReader(input1, "UTF-8"));
		given().contentType("application/json").body(jsonObject1).when().put("/rest/v1/player/35.json").then()
				.statusCode(200);
		given().get("/rest/v1/player/35.json").then().statusCode(200);
		/**
		 * DELETE - quit player from game and destroy player
		 */
		given().delete("/rest/v1/player/33.json").then().statusCode(200);

	}
	@Test
	public void testing_roll_dice_movement_status()
	{
		/**
		 * GET - roll dice and move player on board
		 */
		
				given().get("/rest/v1/move/40.json?player_id=35").then().statusCode(200);
	}

}
