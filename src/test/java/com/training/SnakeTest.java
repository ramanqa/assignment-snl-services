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
	Integer id;
	Integer player_id;
	String path;
	@Test (priority=1)
	public void snake_verify_board()
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		given().get("/rest/v1/board.json").then().statusCode(200);
		 id= given().get("/rest/v1/board/new.json").getBody().jsonPath().getJsonObject("response.board.id");
		 path= "/rest/v1/board/"+id+".json";
		given().get(path).then().statusCode(200);
		System.out.println(id);
		
		
		
	}

	@Test (priority=2)
	public void snake_verify_player() throws UnsupportedEncodingException, IOException, ParseException
	
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		
		String son= "{ \"board\":\""+id+"\",  \"player\": {\"name\": \"shivani\" } }";
			 
		Response res=given().contentType("application/json").body(son).when().post("/rest/v1/player.json");
		player_id=res.getBody().jsonPath().getJsonObject("response.player.id");
		res.then().statusCode(200);
		
		System.out.println(player_id);
	}
	

@Test (priority=3)
	public void snake_update_player() throws FileNotFoundException, IOException, ParseException
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		String path2= "/rest/v1/player/"+player_id+".json";
		given().get(path2).then().statusCode(200);

		
		String json= "{\"player\":{\"name\": \"kanika\"}}";
		given().contentType("application/json").body(json).when().put(path2).then()		
		.statusCode(200);
	}

	@Test (priority=4)
	public void snake_move_player()
	{
		RestAssured.baseURI="http://10.0.1.86/snl";
		String path3= "/rest/v1/move/"+id+".json?player_id="+player_id;
		given().get(path3).then().statusCode(200);

	
}
	@Test(priority=5)
	public void delete_board()
	{
		given().put(path).then().statusCode(200);
		given().delete(path).then().statusCode(200);
	}
	}

