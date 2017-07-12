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

public class RestServiceTestForBoard1 {
	Response response = null;
	String jsonAsString = null;
	private JSONParser jsonParser;

	/**
	 * Setup basic path
	 */
	@BeforeClass
	public static void setupURL() {
		RestAssured.baseURI = "http://10.0.1.86/snl/";
		RestAssured.basePath = "/rest/v1/";
	}

	/**
	 * test Get method response
	 */
	@Test(priority = 2)
	public void test_methods_for_board_list() {

		response = given().get("/board.json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		// System.out.println(response.asString());
		response = when().get("/board.json").then().contentType(ContentType.JSON).extract().response();
		String jsonAsString2 = response.asString();
		// System.out.println(response.asString());
		Assert.assertFalse(jsonAsString.contains(jsonAsString2));

	}

	/**
	 * test response for new board
	 */
	@Test(priority = 1)
	public void test_response_for_new_board() {
		response = given().get("/board/new.json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		response = when().get("/board/new.json").then().contentType(ContentType.JSON).extract().response();
		jsonAsString = response.asString();
		// System.out.println(response.asString());
	}

	/**
	 * check response for board with specific id
	 */
	@Test(priority = 3)
	public void test_response_board_with_id() {
		response = given().get("/board/7.json");
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
		// System.out.println(response.asString());

		response = given().put("/board/7.json").andReturn();
		// System.out.println(response.asString());

	
		  response = given().delete("/board/310.json");
		  Assert.assertEquals(response.statusCode(), 500);//on deleting the board internal error will occur on accessing status
		  System.out.println(response.andReturn().asString());
		
	}

	/**
	 * Test of joining new player to board
	 */
	@Test(priority = 4)
	public void test_response_for_new_player() {
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
	response =	given().contentType("application/json").body(jsonObject).when().post("/player.json");
	Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * tests for service call on player
	 */
	@Test(priority = 5)
	public void test_response_for_player_at_id() {
		response=given().get("/player/68.json");
		Assert.assertEquals(response.statusCode(), 200);
		System.out.println(response.asString());
		
		InputStream input1 = this.getClass().getClassLoader().getResourceAsStream("test2.json");
		jsonParser  = new JSONParser();
		JSONObject jsonObject1 = null;
		try {
			jsonObject1 = (JSONObject) jsonParser.parse(new InputStreamReader(input1, "UTF-8"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		response= given().contentType("application/json").body(jsonObject1).when().put("/player/69.json");
		Assert.assertEquals(response.statusCode(), 200);
		 response=given().delete("/player/71.json");
		System.out.println(response.asString());
	}
	/**
	 * Moving player with id
	 */
	@Test(priority=6)
	public void test_response_move_player(){
		response= given().get("/move/50.json?player_id=68");
		Assert.assertEquals(response.statusCode(), 200);
		
		response= given().get("/move/1.json?player_id=1");
		Assert.assertEquals(response.statusCode(), 500);//player not present at id 1
	}
}
