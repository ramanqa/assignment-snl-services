package com.snl.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.io.InputStream;
//import static org.hamcrest.Matchers.*;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class RestServiceTestForBoard2 {
	Response response = null;
	String jsonAsString = null;
	private JSONParser jsonParser;
	String id;
	String id2;

	/**
	 * Setup basic path
	 */
	@BeforeClass
	public static void setupURL() {
		RestAssured.baseURI = "http://10.0.1.86/snl/";
		RestAssured.basePath = "/rest/v2/";
	}

	/**
	 * test Get method response
	 */
	@Test(priority = 2)
	public void test_methods_for_board_list2() {

		response =  given().auth().preemptive().basic("su", "root_pass").get("/board.json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		// System.out.println(response.asString());
		response =  given().auth().preemptive().basic("su", "root_pass").when().get("/board.json").then().contentType(ContentType.JSON).extract().response();
		String jsonAsString2 = response.asString();
		// System.out.println(response.asString());
		Assert.assertFalse(jsonAsString.contains(jsonAsString2));

	}

	/**
	 * test response for new board
	 * @throws ParseException 
	 */
	@Test(priority = 1)
	public void test_response_for_new_board2() throws ParseException {
		response =  given().auth().preemptive().basic("su", "root_pass").get("/board/new.json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		response =  given().auth().preemptive().basic("su", "root_pass").when().get("/board/new.json").then().contentType(ContentType.JSON).extract().response();
		jsonAsString = response.asString();
		// System.out.println(response.asString());
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonAsString);
		JSONObject msg = (JSONObject) jsonObject.get("response");
		JSONObject msg2 = (JSONObject) msg.get("board");
		id = msg2.get("id").toString();
	//	System.out.println(id);
	}

	/**
	 * check response for board with specific id
	 */
	@Test(priority = 6)
	public void test_response_board_with_id2() {
		response =  given().auth().preemptive().basic("su", "root_pass").get("/board/"+id+".json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		// System.out.println(response.asString());

		response =  given().auth().preemptive().basic("su", "root_pass").put("/board/7.json").andReturn();
		// System.out.println(response.asString());

	
		  response = given().auth().preemptive().basic("su", "root_pass").delete("/board/310.json");
		  Assert.assertEquals(response.statusCode(), 500);//on deleting the board internal error will occur on accessing status
		//  System.out.println(response.andReturn().asString());
		
	}

	/**
	 * Test of joining new player to board
	 */
	@SuppressWarnings("unchecked")
	@Test(priority = 3)
	public void test_response_for_new_player2() {
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("test.json");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(input, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		jsonObject.put("board", id);
			//System.out.println(jsonObject.toJSONString());
	response =	 given().auth().preemptive().basic("su", "root_pass").contentType("application/json").body(jsonObject).when().post("/player.json");
	Assert.assertEquals(response.statusCode(), 200);
	 id2=(response.getBody().jsonPath().getJsonObject("response.player.id")).toString();
	 System.out.println(id2);
	}

	/**
	 * tests for service call on player
	 * @throws ParseException 
	 */
	@Test(priority = 5)
	public void test_response_for_player_at_id2() throws ParseException {
		response= given().auth().preemptive().basic("su", "root_pass").get("/player/"+id2+".json");
		Assert.assertEquals(response.statusCode(), 200);
	//	System.out.println(response.asString());
		InputStream input1 = this.getClass().getClassLoader().getResourceAsStream("test2.json");
		jsonParser  = new JSONParser();
		JSONObject jsonObject1 = null;
		try {
			jsonObject1 = (JSONObject) jsonParser.parse(new InputStreamReader(input1, "UTF-8"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	response=  given().auth().preemptive().basic("su", "root_pass").contentType("application/json").body(jsonObject1).when().put("/player/"+id2+".json");
		Assert.assertEquals(response.statusCode(), 200);
		 response=given().delete("/player/"+id2+".json");
		//System.out.println(response.asString());
	}
	/**
	 * Moving player with id
	 */
	@Test(priority=4)
	public void test_response_move_player2(){
		response=  given().auth().preemptive().basic("su", "root_pass").get("/move/"+id+".json?player_id="+id2);
		Assert.assertEquals(response.statusCode(), 200);
	}
}
