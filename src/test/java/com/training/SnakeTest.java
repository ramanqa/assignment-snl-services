package com.training;




import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.response.Response;

public class SnakeTest {

	@Test
	public void verify_board()
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().get("/rest/v1/board.json").then().statusCode(200);
		//given().get("/rest/v1/board/new.json").then().statusCode(200);
		given().get("/rest/v1/board/105.json").then().statusCode(200);
		//given().put("/rest/v1/board/105.json").then().statusCode(200);
		
	}
	@Test
	public void verify_player() throws UnsupportedEncodingException, IOException, ParseException
	
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		JSONParser parser = new JSONParser();	
		Object obj = parser.parse(new FileReader("src/test/resources/db.json"));
		JSONObject jsonObject = (JSONObject) obj;

		given().contentType("application/json").body(jsonObject).when().post("/rest/v1/player.json").then()		
	.statusCode(200);
	}
	

	@Test
	public void update_player() throws FileNotFoundException, IOException, ParseException
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().get("/rest/v1/player/44.json").then().statusCode(200);
		JSONParser parser = new JSONParser();
	Object obj = parser.parse(new FileReader("src/test/resources/dat.json"));
		JSONObject jsonObject = (JSONObject) obj;
		
		given().contentType("application/json").body(jsonObject).when().put("/rest/v1/player/44.json").then()		
		.statusCode(200);
	}
	
	@Test
	public void move_player()
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().get("/rest/v1/move/105.json?player_id=44").then().statusCode(200);

	}
}

