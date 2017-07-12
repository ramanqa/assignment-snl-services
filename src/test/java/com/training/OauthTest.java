package com.training;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

import static com.jayway.restassured.RestAssured.*;

import com.jayway.restassured.response.Response;

public class OauthTest {
	String accessToken;
    @BeforeTest
	public void auth(){
	 String response =
	            given()
	                .parameters("username", "su", "password", "root_pass", 
	                           "grant_type", "client_credentials", 
	                           "client_id","8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8",  "client_secret", "bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
	                .auth()
	                .preemptive()
	                .basic("8c499de42c8907350a69b418bdc2bc96ba57c7d9e7a3fcc61592b7dd036a47d8","bf3f128dd9240eb6067cce29e2db3b5814befb7e9afaac2aa2032995d584de35")
	            .when()
	                .post("http://10.0.1.86/snl/oauth/token")
	                .asString();

	    JsonPath jsonPath = new JsonPath(response);
	   accessToken = jsonPath.getString("access_token");
	

	   
}
   @Test
   public void verify_board(){
	   
	   RestAssured.baseURI="http://10.0.1.86/snl/";
	 given().auth().oauth2(accessToken).get("rest/v1/board.json").then().statusCode(200);
	given().auth().oauth2(accessToken).get("/rest/v1/board/new.json").then().statusCode(200);
	  given().auth().oauth2(accessToken).get("/rest/v1/board/79.json").then().statusCode(200);
   }
	@Test
	public void verify_player() throws UnsupportedEncodingException, IOException, ParseException
	
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		JSONParser parser = new JSONParser();	
		Object obj = parser.parse(new FileReader("src/test/resources/db.json"));
		JSONObject jsonObject = (JSONObject) obj;

		given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject).when().post("/rest/v1/player.json").then()		
	.statusCode(200);
	}
	@Test
	public void update_player() throws FileNotFoundException, IOException, ParseException
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().get("/rest/v1/player/62.json").then().statusCode(200);
		JSONParser parser = new JSONParser();
	Object obj = parser.parse(new FileReader("src/test/resources/dat.json"));
		JSONObject jsonObject = (JSONObject) obj;
		
		given().auth().oauth2(accessToken).contentType("application/json").body(jsonObject).when().put("/rest/v1/player/62.json").then()		
		.statusCode(200);
	}
	
	@Test
	public void move_player()
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().auth().oauth2(accessToken).get("/rest/v1/move/79.json?player_id=62").then().statusCode(200);

	}
	
}