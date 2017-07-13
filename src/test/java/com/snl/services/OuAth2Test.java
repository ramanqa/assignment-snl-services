package com.snl.services;
import static io.restassured.RestAssured.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class OuAth2Test {
	String accessToken;
	Response response = null;
	String jsonAsString = null;
	private JSONParser jsonParser;
	String id;
	String id2;
	 
	 	@SuppressWarnings("deprecation")
		@BeforeTest
	 	public void authorisation() {
	 		Response response = given().parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
	 						"8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8", "client_secret",
	 					"bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
	 				.auth().preemptive()
	 				.basic("8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8",
	 						"bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
	 				.when().post("http://10.0.1.86/snl/oauth/token");
	 
	 		JsonPath jsonPath = new JsonPath(response.asString());
	 		accessToken = jsonPath.getString("access_token");
	 		System.out.println(accessToken);
	 		RestAssured.baseURI = "http://10.0.1.86/snl/";
			RestAssured.basePath = "/rest/v3/";
	 }
	 	@Test(priority = 2)
		public void test_methods_for_board_list3() {

			response = given().auth().oauth2(accessToken).get("/board.json");
			Assert.assertEquals(response.statusCode(), 200);
			Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
			// System.out.println(response.asString());
			response = given().auth().oauth2(accessToken).when().get("/board.json").then().contentType(ContentType.JSON).extract().response();
			String jsonAsString2 = response.asString();
			// System.out.println(response.asString());
			Assert.assertFalse(jsonAsString.contains(jsonAsString2));

		}

		/**
		 * test response for new board
		 * @throws ParseException 
		 */
		@Test(priority = 1)
		public void test_response_for_new_board3() throws ParseException {
			response = given().auth().oauth2(accessToken).get("/board/new.json");
			Assert.assertEquals(response.statusCode(), 200);
			Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
			response = given().auth().oauth2(accessToken).when().get("/board/new.json").then().contentType(ContentType.JSON).extract().response();
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
		public void test_response_board_with_id3() {
			response = given().auth().oauth2(accessToken).get("/board/"+id+".json");
			Assert.assertEquals(response.statusCode(), 200);
			Assert.assertEquals(response.statusLine(), "HTTP/1.1 200 OK");
			// System.out.println(response.asString());

			response = given().auth().oauth2(accessToken).put("/board/7.json").andReturn();
			// System.out.println(response.asString());

		
			  response = given().auth().oauth2(accessToken).delete("/board/310.json");
			  Assert.assertEquals(response.statusCode(), 500);//on deleting the board internal error will occur on accessing status
			//  System.out.println(response.andReturn().asString());
			
		}

		/**
		 * Test of joining new player to board
		 */
		@SuppressWarnings("unchecked")
		@Test(priority = 3)
		public void test_response_for_new_player3() {
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
		response =	given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject).when().post("/player.json");
		Assert.assertEquals(response.statusCode(), 200);
		 id2=(response.getBody().jsonPath().getJsonObject("response.player.id")).toString();
		}

		/**
		 * tests for service call on player
		 * @throws ParseException 
		 */
		@Test(priority = 5)
		public void test_response_for_player_at_id3() throws ParseException {
			response=given().auth().oauth2(accessToken).get("/player/"+id2+".json");
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
			response= given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject1).when().put("/player/"+id2+".json");
			Assert.assertEquals(response.statusCode(), 200);
			 response=given().auth().oauth2(accessToken).delete("/player/"+id2+".json");
			//System.out.println(response.asString());
		}
		/**
		 * Moving player with id
		 */
		@Test(priority=4)
		public void test_response_move_player3(){
			response= given().auth().oauth2(accessToken).get("/move/"+id+".json?player_id="+id2);
			Assert.assertEquals(response.statusCode(), 200);
		}
	}
