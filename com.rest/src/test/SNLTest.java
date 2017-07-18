package com.googlerestapi.com.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class SNLTest {
	int boardId;
	int playerId;
	SNLMain obj;
@BeforeTest
public void newBoard() throws ParseException
{
     obj = new SNLMain();
	 Response response = RestAssured.given().param("response.status", "1").when().get("http://10.0.1.86/snl/rest/v1/board/new.json");
      response.then().assertThat().statusCode(200);
		boardId = obj.getBoardId(response);
}
@Test(priority =1)
public void testNewBoard()
{    
	Response response = RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/"+boardId+".json");
	response.then().assertThat().statusCode(200);
	response.then().body("response.board.id",equalTo(boardId));
	ArrayList listBoardResponse = RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board.json").jsonPath().get("response.board.id");
	assertEquals(listBoardResponse.contains(boardId),true);
	
	}
@Test(priority=2)
public void testAddNewPlayer()
{
	String json = obj.getNewPlayerJsonString(boardId);
	Response response = 	RestAssured.given().contentType(ContentType.JSON).body(json).when().post("http://10.0.1.86/snl/rest/v1/player.json");
	   response.then().assertThat().statusCode(200); 
	playerId =  response.jsonPath().getInt("response.player.id");
	 RestAssured.get("http://10.0.1.86/snl/rest/v1/player/"+playerId+".json")
	 .then()
	 .body("response.player.id", equalTo(playerId))
	 .body("response.player.board_id", equalTo(boardId))
	 .body("response.player.position", equalTo(0))
	 .assertThat().statusCode(200);
/*
 * check new player from board
 * 
 */
	JsonPath jsonPath = RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/"+boardId+".json").jsonPath();
    ArrayList listPlayers  = (ArrayList)jsonPath.get("response.board.players");
    int id = obj.getPlayerIdFromBoardListPlayers(listPlayers,playerId);
 	assertEquals(id,playerId);
	}

@Test(priority=6)
public void testDeleteBoard()
{
	Response response = RestAssured.when().delete("http://10.0.1.86/snl/rest/v1/board/"+boardId+".json");
	response.then().assertThat().statusCode(200);
	response.then().body("response.success", equalTo("OK"));
    RestAssured.when().delete("http://10.0.1.86/snl/rest/v1/board/"+boardId+".json").then().statusCode(500);
    ArrayList listBoardResponse = RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board.json").jsonPath().get("response.board.id");
	assertEquals(listBoardResponse.contains(boardId),false);
    
}
@Test(priority=3)
public void testUpdatePlayerDetails()
{                            //
RestAssured.when().get("http://10.0.1.86/snl/rest/v1/player/"+playerId+".json").then()
 .body("response.player.board_id", equalTo(boardId)).statusCode(200)
 .body("response.player.name", equalTo("manu"))
 .assertThat().statusCode(200);
String updateJson = obj.getUpdatePlayerJsonObject();
RestAssured.given().contentType(ContentType.JSON).body(updateJson).when().put("http://10.0.1.86/snl/rest/v1/player/"+playerId+".json").then()
.body("response.player.name", equalTo("swapnil"))
.assertThat().statusCode(200);
}

@Test(priority=5)
public void testDeletePlayerDetails()
{
 RestAssured.when().delete("http://10.0.1.86/snl/rest/v1/player/"+playerId+".json").then().body("response.success", equalTo("OK"))
 .assertThat().statusCode(200);
  
}
@Test(priority=4)
public void testMovePlayer()
{
Response response = RestAssured.when().get("http://10.0.1.86/snl/rest/v1/move/"+boardId+".json?player_id="+playerId);
response.then().assertThat().statusCode(200);
int position = response.jsonPath().get("response.player.position");
assertNotEquals(position, 0);	
	}
}
