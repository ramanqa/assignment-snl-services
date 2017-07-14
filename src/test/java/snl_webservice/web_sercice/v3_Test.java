package snl_webservice.web_sercice;

import static com.jayway.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;

import net.minidev.json.JSONObject;

public class v3_Test {

	String access_token;
	int Board_id;
	int player_id;

	@BeforeTest
	public void oath2() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/oauth/token");

		String response = given()
				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
						"90fd72d0063c7bee4f6bd311193ce37cfb49146e6eb09bd086f002e54841546d", "client_secret",
						"ae2cdabb9f4b72efd518939871dba8a39c853ad2cc9800082e06968c1cc134ea")
				.auth().preemptive()
				.basic("90fd72d0063c7bee4f6bd311193ce37cfb49146e6eb09bd086f002e54841546d",
						"ae2cdabb9f4b72efd518939871dba8a39c853ad2cc9800082e06968c1cc134ea")
				.when().then().statusCode(200).post(url).asString();

		JsonPath jsonPath = new JsonPath(response);
		access_token = jsonPath.getString("access_token");
		System.out.println("access_token ..." + access_token);
	}

	@Test(priority = 1)
	public void Board_create_new_board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v3/board/new.json");

		given().auth().oauth2(access_token).contentType(ContentType.JSON).accept(ContentType.JSON).expect()
				.statusCode(200).when().get(url);

		Board_id = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath()
				.getJsonObject("response.board.id");

		System.out.println("Board_id.." + Board_id);
		given().when().then().statusCode(200);
	}

	@Test(priority = 2)
	public void get_list_of_borad() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v3/board.json");

		// get list of board

		try {
			List<String> i = given().auth().oauth2(access_token).expect().statusCode(200).when().get(url).then()
					.extract().jsonPath().getList("response.board.id");
			System.out.println("Board_list.." + i);
		} finally {
			RestAssured.reset();
		}

	}
	
	
	
	
	
	@Test(priority = 3)
	public void player_join_player_to_board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v3/player.json");
		

		// join new player to board
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();

		jsonObject.put("board", Board_id);
		jsonObject1.put("name", "User 1");
		jsonObject.put("player", jsonObject1);
		given().auth().oauth2(access_token).contentType("application/json").body(jsonObject.toString()).when().post(url);
		given().when().then().statusCode(200);

	}

	@Test(priority = 4)
	public void get_id() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v3/board/" + Board_id + ".json");
		 
		player_id = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath().getJsonObject("response.board.players[0].id");
		System.out.println("player_id=" + player_id);
		given().when().then().statusCode(200);

	}

	@Test(priority = 5)
	public void player_get_player_details() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v3/player/" + player_id + ".json");
		
		Map<String, String> mymap = new HashMap<>();

		mymap = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath().getMap("response");
		System.out.println("player_details.." + mymap);
		given().when().then().statusCode(200);
	}

	@Test(priority = 6)
	public void player_update_player_details() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v3/player/" + player_id + ".json");
		 
		JSONObject jsonObject = new JSONObject();

		JSONObject jsonObject1 = new JSONObject();

		jsonObject1.put("name", "user 4");
		jsonObject.put("player", jsonObject1);
		given().auth().oauth2(access_token).contentType("application/json").body(jsonObject.toString()).when().put(url);

		Map<String, String> mymap = new HashMap<>();

		mymap = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath().getMap("response");
		System.out.println("player_details.." + mymap);
		given().when().then().statusCode(200);

	}

	@Test(priority = 7)
	public void Move_player_by_id_in_Board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v3/move/" + Board_id + ".json?player_id=" + player_id + "");
		 

		int roll = given().auth().oauth2(access_token).when().get(url).then().statusCode(200).extract().jsonPath().get("response.roll");
		System.out.println("roll with.." + roll);

		int position = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath().get("response.player.position");
		System.out.println("Position.." + position);
		given().when().then().statusCode(200);

	}

	@Test(priority = 8)
	public void Board_get_details_of_board() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v3/board/" + Board_id + ".json");

		Object obj = given().auth().oauth2(access_token).when().get(url).then().extract().jsonPath().get("response.board");
		System.out.println(obj);
		given().when().then().statusCode(200);

	}

	@Test(priority = 10)
	public void Board_delete_board() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v3/board/" + Board_id + ".json");
		given().auth().oauth2(access_token).when().delete(url).then().statusCode(200);

	}

	@Test(priority = 9)
	public void player_delete_player() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v3/player/" + player_id + ".json");
		 
		given().auth().oauth2(access_token).contentType("application/json").when().delete(url).then().statusCode(200);

	}
}
