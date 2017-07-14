package com.snl.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Boardv1Service {
	Response response = null;
	String jsonAsString = null;
	private JSONParser jsonParser;
	public String id;
	String id2;
	public String id3;

	/**
	 * Constructor to set basic path
	 */
	public Boardv1Service() {
		RestAssured.baseURI = "http://10.0.1.86/snl/";
		RestAssured.basePath = "/rest/v1/";
	}

	/**
	 * call GET method to create new board
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Response service_call_to_create_new_board() throws ParseException {
		response = given().when().get("/board/new.json").then().contentType(ContentType.JSON).extract().response();
		jsonAsString = response.asString();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonAsString);
		JSONObject msg = (JSONObject) jsonObject.get("response");
		JSONObject msg2 = (JSONObject) msg.get("board");
		id = msg2.get("id").toString();
		return response;
	}

	/**
	 * call Get method to obtain list of boards
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Response service_call_to_see_the_list_of_boards() throws ParseException {

		response = given().get("/board.json");
		return response;

	}

	/**
	 * call POST method to add new player to board
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response service_call_to_add_new_player() {
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
		response = given().contentType("application/json").body(jsonObject).when().post("/player.json");
		id2 = (response.getBody().jsonPath().getJsonObject("response.player.id")).toString();
		return response;
	}

	/**
	 * call Get method to move a player
	 * 
	 * @return
	 */
	public Response service_call_to_move_a_player() {
		response = given().get("/move/" + id + ".json?player_id=" + id2);
		return response;
	}

	/**
	 * call Get method to move an absent player
	 * 
	 * @return
	 */
	public Response service_call_to_move_absent_player() {
		response = given().get("/move/1.json?player_id=1");
		return response;
	}

	/**
	 * call GET method to obtain player details
	 * 
	 * @return
	 */
	public Response service_call_to_obtain_player_details() {
		response = given().get("/player/" + id2 + ".json");
		return response;

	}

	/**
	 * call PUT method to update player details
	 * 
	 * @return
	 */
	public Response service_call_to_update_player_details() {
		InputStream input1 = this.getClass().getClassLoader().getResourceAsStream("test2.json");
		jsonParser = new JSONParser();
		JSONObject jsonObject1 = null;
		try {
			jsonObject1 = (JSONObject) jsonParser.parse(new InputStreamReader(input1, "UTF-8"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		response = given().contentType("application/json").body(jsonObject1).when().put("/player/" + id2 + ".json");
		return response;

	}

	/**
	 * call DELETE method to delete player details
	 * 
	 * @return
	 */
	public Response servie_call_to_delete_player_details() {
		response = given().delete("/player/" + id2 + ".json");
		return response;
	}

	/**
	 * call GET method to obtain board with specific id
	 * 
	 * @return
	 */
	public Response servie_call_to_obtain_board_with_id() {
		response = given().get("/board/" + id + ".json");
		return response;
	}

	/**
	 * call PUT method to reset board with specific id
	 * 
	 * @return
	 */
	public Response service_call_to_reset_board() {
		response = given().put("/board/" + id + ".json").andReturn();
		return response;
	}

	/**
	 * call DELETE method to delete board with specific id
	 * 
	 * @return
	 */
	public Response service_call_to_delete_board() {
		response = given().delete("/board/" + id + ".json");
		return response;
	}

	/**
	 * call DELETE method to delete board already absent
	 * 
	 * @return
	 */
	public Response service_call_to_delete_absent_board() {
		response = given().delete("/board/" + id + ".json");
		return response;
	}
}
