package com.qainfotech.tap.training.snlServices;

import org.testng.Assert;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.mainsnlServices.BoardDetailsV1;

import static org.assertj.core.api.Assertions.*;
import static org.testng.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SNLServicesTestV1 {

	JSONParser parser = new JSONParser();
	BoardDetailsV1 b;
	@BeforeTest
	public void setBaseUri() {
		b=new BoardDetailsV1();
		RestAssured.baseURI = "http://10.0.1.86/snl";

		// RestAssured.baseURI = "http://10.0.1.86/snl";
	}

	

	@Test
	public void a_testStatusCodeJSON() {

		// String s=RestAssured.given().param("response.status",
		// "1").when().get("http://10.0.1.86/snl/rest/v1/board.json").getBody().asString();
		// System.out.println(s);
		RestAssured.given().param("response.status", "1").when().get("http://10.0.1.86/snl/rest/v1/board.json").then()
				.assertThat().statusCode(200);
	}

	@Test
	public void b_testStatusCodeXML() {
		// String s= RestAssured.given().param("response.status",
		// "1").when().get("http://10.0.1.86/snl/rest/v1/board.xml").asString();
		// System.out.println(s);
		RestAssured.given().param("response.status", "1").when().get("http://10.0.1.86/snl/rest/v1/board.xml").then()
				.assertThat().statusCode(200);

	}

	@Test
	public void c_test_new_board() throws ParseException {

		long id = b.getID();
		RestAssured.get("http://10.0.1.86/snl/rest/v1/board/" + id + ".json").then().assertThat().statusCode(200);

	}

	@Test
	public void d_test_get_details_of_board() throws ParseException {
		long id = b.getID();
		RestAssured.given().param("response.status", "1").when()
				.get("http://10.0.1.86/snl/rest/v1/board/" + id + ".json").then().assertThat().statusCode(200);

	}

	@Test
	public void e_test_DELETEBOARD_Ver_1() throws ParseException {

		long id = b.getID();
		RestAssured.delete("http://10.0.1.86/snl/rest/v1/board/" + id + ".json");
		// RestAssured.given().param("response.status", "1").when()
		// .get("http://10.0.1.86/snl/rest/v1/board/" + id +
		// ".json").then().assertThat().statusCode(404);

		long idreturn = 1;
		idreturn = b.parseComplete(id);
		assertThat(idreturn == 0).isTrue();

	}

	@Test
	public void f_test_player_entry() throws ParseException, IOException {

		long ids[]=b.get_player_id();
		long playerid=ids[1];
		long boardid= ids[0];
		
		
		Response r = RestAssured.get("http://10.0.1.86/snl/rest/v1/board/"+boardid+".json");
		r.then().assertThat().statusCode(200);
		
		
		//JsonPath path= RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/{id}",pathParam).then().extract().jsonPath();
		//JsonPath path= RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/"+boardid+".json").then().extract().jsonPath();
		 long playerIdresponse =r.jsonPath().getLong("response.board.players[0].id");
		String playernameresponse=r.jsonPath().getString("response.board.players[0].name");
		assertEquals(playerIdresponse, playerid);
		assertEquals(playernameresponse, "max");
		
		
		
		
	}
@Test
 public void g_test_delete_player() throws ParseException{
	
	long ids[]=b.get_player_id();
	long playerid=ids[1];
	long boardid=ids[0];
	
	Response res= RestAssured.delete("/rest/v1/player/"+playerid+".json");
	res.then().extract().response();
	res.then().assertThat().statusCode(200);
	
	//Response r = RestAssured.get("http://10.0.1.86/snl/rest/v1/board/"+boardid+".json");
	RestAssured.get("/rest/v1/player/"+playerid+".json").then().assertThat().statusCode(404);

	
	
}


@Test
public void h_test_player_moves() throws ParseException{

	long ids[]=b.get_player_id();
	long playerid=ids[1];
	long boardid=ids[0];
	System.out.println(boardid);
	System.out.println(playerid);
	Response res = RestAssured.get("/rest/v1/move/"+boardid+".json?player_id="+playerid);
	res.then().assertThat().statusCode(200);

	
}


}


