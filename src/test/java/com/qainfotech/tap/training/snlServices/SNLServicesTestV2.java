package com.qainfotech.tap.training.snlServices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.qainfotech.tap.training.mainsnlServices.BoardDetailsV2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SNLServicesTestV2 {

	BoardDetailsV2 b;
	
	@BeforeTest
	public void before()
	{
		b= new BoardDetailsV2();
		RestAssured.baseURI = "http://10.0.1.86/snl";
		RestAssured.given().auth().basic("su", "root_pass").when().get("/rest/v2/board.json").then().statusCode(200);
		//RestAssured.authentication=basic("su","root_pass");
	}

	
	
	

	@Test
	public void a_testStatusCodeJSON() {

		// String s=RestAssured.given().param("response.status",
		// "1").when().get("http://10.0.1.86/snl/rest/v2/board.json").getBody().asString();
		// System.out.println(s);
		RestAssured.given().auth().basic("su", "root_pass").when().get("http://10.0.1.86/snl/rest/v2/board.json").then()
				.assertThat().statusCode(200);
	}

	@Test
	public void b_testStatusCodeXML() {
		// String s= RestAssured.given().param("response.status",
		// "1").when().get("http://10.0.1.86/snl/rest/v2/board.xml").asString();
		// System.out.println(s);
		RestAssured.given().auth().basic("su", "root_pass").when().get("http://10.0.1.86/snl/rest/v2/board.xml").then()
				.assertThat().statusCode(200);

	}

	@Test
	public void c_test_new_board() throws ParseException {

		long id = b.getID();
		RestAssured.given().auth().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board/" + id + ".json").then().assertThat().statusCode(200);

	}

	@Test
	public void d_test_get_details_of_board() throws ParseException {
		long id = b.getID();
		RestAssured.given().auth().basic("su", "root_pass").when()
				.get("http://10.0.1.86/snl/rest/v2/board/" + id + ".json").then().assertThat().statusCode(200);

	}

	@Test
	public void e_test_DELETEBOARD() throws ParseException {

		long id = b.getID();
		//System.out.println(id);
		RestAssured.given().auth().basic("su", "root_pass").when().delete("http://10.0.1.86/snl/rest/v2/board/" + id + ".json");
		
		//RestAssured.given().auth().basic("su", "root_pass").when().get("http://10.0.1.86/snl/rest/v2/board/" + id + ".json").then().assertThat().statusCode(200);
		long idreturn = 1;
		idreturn = b.parseComplete(id);
		assertThat(idreturn == 0).isTrue();

	}

	@Test
	public void f_test_player_entry() throws ParseException, IOException {

		long ids[]=b.get_player_id();
		long playerid=ids[1];
		long boardid= ids[0];
		
		
		Response r = RestAssured.given().auth().basic("su", "root_pass").when().get("http://10.0.1.86/snl/rest/v2/board/"+boardid+".json");
		r.then().assertThat().statusCode(200);
		
		
		//JsonPath path= RestAssured.when().get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().extract().jsonPath();
		//JsonPath path= RestAssured.when().get("http://10.0.1.86/snl/rest/v2/board/"+boardid+".json").then().extract().jsonPath();
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
	
	Response res= RestAssured.given().auth().basic("su", "root_pass").when().delete("/rest/v2/player/"+playerid+".json");
	res.then().extract().response();
	res.then().assertThat().statusCode(200);
	
	//Response r = RestAssured.get("http://10.0.1.86/snl/rest/v2/board/"+boardid+".json");
	RestAssured.given().auth().basic("su", "root_pass").when().get("/rest/v2/player/"+playerid+".json").then().assertThat().statusCode(404);

	
	
}
 

@Test
public void h_test_player_moves() throws ParseException{

	long ids[]=b.get_player_id();
	long playerid=ids[1];
	long boardid=ids[0];
	//System.out.println(boardid);
	//System.out.println(playerid);
	Response res = RestAssured.given().auth().basic("su", "root_pass").when().get("/rest/v2/move/"+boardid+".json?player_id="+playerid);
	res.then().assertThat().statusCode(200);

	
}

	
	
	
	
}
