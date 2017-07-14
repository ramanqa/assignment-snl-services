package com.web_snl.SNL_project;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.filter.ValueNode.JsonNode;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import static com.jayway.jsonpath.JsonPath.parse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import static com.jayway.restassured.RestAssured.*;
import java.net.URLConnection;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.filter.ValueNode.JsonNode;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import static com.jayway.jsonpath.JsonPath.parse;
//import com.maven_Rest.RestFrame.run;
import org.json.simple.parser.JSONParser;
import static com.jayway.restassured.RestAssured.*;

import org.json.simple.parser.JSONParser;

import com.jayway.restassured.authentication.OAuth2Scheme;

public class V3_Test {
	public String accessToken;
	public int board_id;
	public int player_id;

	public void board(int x)
	{
		board_id=x;
	}
	public void player(int x)
	{
		player_id=x;
	}

	@BeforeTest
	public void create_token() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/oauth/token");
		String response = given()
				.parameters("username", "su", "password", "root_pass","grant_type", "client_credentials",
						"client_id", "b2f9361625140f2e8e63813bfd61b9a2aea08b07a9a4b122b12f4e49c820422f",
						"client_secret", "efa565d36ce910299c2fe824c473fdb909b6bb5a6ab1201cd039c4691879518d")
				.auth().preemptive()
				.basic("b2f9361625140f2e8e63813bfd61b9a2aea08b07a9a4b122b12f4e49c820422f",
						"efa565d36ce910299c2fe824c473fdb909b6bb5a6ab1201cd039c4691879518d")
				.when().then().statusCode(200).post(url).asString();
		

		JsonPath jsonPath = new JsonPath(response);
		accessToken = jsonPath.getString("access_token");
		
		System.out.println("access.."+accessToken);
		
	}
	
	@Test(priority=1)
	public void create_board() throws IOException
	{
					String s=   given()
		            .auth().oauth2(accessToken)
		            .contentType(ContentType.JSON)
		            .accept(ContentType.JSON)
		            .when()
		            .get("http://10.0.1.86/snl/rest/v3/board/new.json").then().statusCode(200).extract().response().asString();
		       
		       Integer board_id = parse(s).read("$.response.board.id");
		       board(board_id);
		       System.out.println("&&&&&"+board_id);
	}
	
	
	@Test(priority=2)
	public void create_player() throws IOException
	{
				 String s=   given()
	            .auth().oauth2(accessToken)
	            .contentType(ContentType.JSON)
	            .accept(ContentType.JSON).body("{\"board\":\""+board_id+"\", \"player\": {\"name\": \"nikhi\"}}")
	            .post("http://10.0.1.86/snl/rest/v3/player.json").then().statusCode(200).extract().response().asString();
				Integer player_id = parse(s).read("$.response.player.id");
		        player(player_id);
		        System.out.println("$$$$$"+player_id);
	}
	
	@Test(priority=4)
	public void move() throws IOException
	{
		     	     String s=   given()
		            .auth().oauth2(accessToken)
		            .contentType(ContentType.JSON)
		            .accept(ContentType.JSON)
		            .when()
		            .get("http://10.0.1.86/snl/rest/v3/move/"+board_id+".json?player_id="+player_id+"").then().statusCode(200).extract().response().asString();
		             Integer position= parse(s).read("$.response.player.position");
		      
	}
	
	@Test(priority=3)
	public void list_of_board() throws IOException
	{
					String s=   given()
		            .auth().oauth2(accessToken)
		            .contentType(ContentType.JSON)
		            .accept(ContentType.JSON)
		            .get("http://10.0.1.86/snl/rest/v3/board.json").then().statusCode(200).extract().response().asString();
    }
	
	//@Test(priority=5)
	public void delete_player()
	{
		 System.out.println("!!!!  "+player_id);
		 given()
        .auth().oauth2(accessToken)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .delete("http://10.0.1.86/snl/rest/v3/player/" + player_id + ".json").then().statusCode(200);
	}
	
	//@Test(priority=6)
	public void delete_board()
	{
		 //System.out.println("!!!!  "+board_id);
		 given()
        .auth().oauth2(accessToken)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .delete("http://10.0.1.86/snl/rest/v3/board/" + board_id + ".json").then().statusCode(200);
	}
	@Test(priority=4)
	public void update_player() throws MalformedURLException
	{
		URL url = new URL("http://10.0.1.86/snl/rest/v3/player/" + player_id + ".json");
		JSONObject o = new JSONObject();
		JSONObject o1 = new JSONObject();
		o1.put("name", "sharma");
		o.put("player", o1);
		given().auth().oauth2(accessToken).contentType("application/json").body(o.toString()).when().put(url);
		Map<String, String> mymap = new HashMap<String, String>();
		mymap = given().auth().oauth2(accessToken).when().get(url).then().extract().jsonPath().getMap("response");
		System.out.println("player_details.." + mymap);
		given().when().then().statusCode(200);
	}
}
















