package com.googlerestapi.com.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;

import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class SNLTestV3 {
	String accessToken;
	int boardId;
	int playerId;
	SNLMain obj;

	@BeforeTest
	public void beforeTest() throws ParseException {

		String response1 = RestAssured.given()
				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
						"0ead79c23eefa98695ea9007ca49e403440d557d3888ed7db1bda86ae8ad3c67", "client_secret",
						"d598b0984e13e16e78a9b731af0df9c786574de4718b155a473951fb6fcd5589")
				.auth().preemptive()
				.basic("0ead79c23eefa98695ea9007ca49e403440d557d3888ed7db1bda86ae8ad3c67",
						"d598b0984e13e16e78a9b731af0df9c786574de4718b155a473951fb6fcd5589")
				.when().post("http://10.0.1.86/snl/oauth/token").asString();
		JsonPath jsonPath = new JsonPath(response1);
		accessToken = jsonPath.getString("access_token");

	     obj = new SNLMain();

		Response response = RestAssured.given().auth().oauth2(accessToken).param("response.status", "1").when()
				.get("http://10.0.1.86/snl/rest/v3/board/new.json");
		response.then().assertThat().statusCode(200);
		boardId = obj.getBoardId(response);

	}

	@Test(priority = 1)
	public void testNewBoard() {
		Response response = RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/board/" + boardId + ".json");
		response.then().assertThat().statusCode(200);
		response.then().body("response.board.id", equalTo(boardId));
		ArrayList listBoardResponse = RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/board.json").jsonPath()
				.get("response.board.id");
		assertEquals(listBoardResponse.contains(boardId), true);

	}

	@Test(priority = 2)
	public void testAddNewPlayer() {
		String json = obj.getNewPlayerJsonString(boardId);
		Response response = RestAssured.given().auth().oauth2(accessToken).contentType(ContentType.JSON).body(json).when()
				.post("http://10.0.1.86/snl/rest/v3/player.json");
		response.then().assertThat().statusCode(200);
		playerId = response.jsonPath().getInt("response.player.id");
		RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/player/" + playerId + ".json").then()
				.body("response.player.id", equalTo(playerId)).body("response.player.board_id", equalTo(boardId))
				.body("response.player.position", equalTo(0)).assertThat().statusCode(200);
		/*
		 * check new player from board
		 * 
		 */
		JsonPath jsonPath = RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/board/" + boardId + ".json")
				.jsonPath();
		ArrayList listPlayers = (ArrayList) jsonPath.get("response.board.players");
		int id = obj.getPlayerIdFromBoardListPlayers(listPlayers, playerId);
		assertEquals(id, playerId);
	}

	@Test(priority = 6)
	public void testDeleteBoard() {
		Response response = RestAssured.given().auth().oauth2(accessToken).when().delete("http://10.0.1.86/snl/rest/v3/board/" + boardId + ".json");
		response.then().assertThat().statusCode(200);
		response.then().body("response.success", equalTo("OK"));
		RestAssured.given().auth().oauth2(accessToken).when().delete("http://10.0.1.86/snl/rest/v3/board/" + boardId + ".json").then().statusCode(500);
		ArrayList listBoardResponse = RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/board.json").jsonPath()
				.get("response.board.id");
		assertEquals(listBoardResponse.contains(boardId), false);

	}

	@Test(priority = 3)
	public void testUpdatePlayerDetails() { //
		RestAssured.given().auth().oauth2(accessToken).when().get("http://10.0.1.86/snl/rest/v3/player/" + playerId + ".json").then()
				.body("response.player.board_id", equalTo(boardId)).statusCode(200)
				.body("response.player.name", equalTo("manu")).assertThat().statusCode(200);
		String updateJson = obj.getUpdatePlayerJsonObject();
		RestAssured.given().auth().oauth2(accessToken).contentType(ContentType.JSON).body(updateJson).when()
				.put("http://10.0.1.86/snl/rest/v3/player/" + playerId + ".json").then()
				.body("response.player.name", equalTo("swapnil")).assertThat().statusCode(200);
	}

	@Test(priority = 5)
	public void testDeletePlayerDetails() {
		RestAssured.given().auth().oauth2(accessToken).when().delete("http://10.0.1.86/snl/rest/v3/player/" + playerId + ".json").then()
				.body("response.success", equalTo("OK")).assertThat().statusCode(200);

	}

	@Test(priority = 4)
	public void testMovePlayer() {
		Response response = RestAssured.given().auth().oauth2(accessToken).when()
				.get("http://10.0.1.86/snl/rest/v3/move/" + boardId + ".json?player_id=" + playerId);
		response.then().assertThat().statusCode(200);
		int position = response.jsonPath().get("response.player.position");
		assertNotEquals(position, 0);
	}
}
