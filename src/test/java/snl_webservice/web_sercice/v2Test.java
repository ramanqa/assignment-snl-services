package snl_webservice.web_sercice;

import static com.jayway.restassured.RestAssured.authentication;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

import net.minidev.json.JSONObject;

public class v2Test {
	int Board_id;
	int player_id;

	@Test(priority = 1)
	public void Board_create_new_board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v2/board/new.json");
		authentication = basic("su", "root_pass");
		given().expect().statusCode(200).when().get(url);

		Board_id = given().when().get(url).then().extract().jsonPath().getInt("response.board.id");

		System.out.println("Board_id.." + Board_id);

	}

	@Test(priority = 2)
	public void get_list_of_borad() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v2/board.json");

		// get list of board

		authentication = basic("su", "root_pass");

		try {
			List<String> i = expect().statusCode(200).when().get(url).then().extract().jsonPath()
					.getList("response.board.id");
			System.out.println("Board_list.." + i);
		} finally {
			RestAssured.reset();
		}

	}

	@Test(priority = 3)
	public void player_join_player_to_board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v2/player.json");
		authentication = basic("su", "root_pass");

		// join new player to board
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();

		jsonObject.put("board", Board_id);
		jsonObject1.put("name", "User 1");
		jsonObject.put("player", jsonObject1);
		given().contentType("application/json").body(jsonObject.toString()).when().post(url);

	}

	@Test(priority = 4)
	public void get_id() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v2/board/" + Board_id + ".json");
		authentication = basic("su", "root_pass");
		player_id = given().when().get(url).then().extract().jsonPath().getJsonObject("response.board.players[0].id");
		System.out.println("player_id=" + player_id);
		
		

	}

	@Test(priority = 5)
	public void player_get_player_details() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v2/player/" + player_id + ".json");
		authentication = basic("su", "root_pass");
		Map<String, String> mymap = new HashMap<>();

		mymap = given().when().get(url).then().extract().jsonPath().getMap("response");
		System.out.println("player_details.." + mymap);
	}

	@Test(priority = 6)
	public void player_update_player_details() throws MalformedURLException {
		URL url = new URL("http://10.0.1.86/snl/rest/v2/player/" + player_id + ".json");
		authentication = basic("su", "root_pass");
		JSONObject jsonObject = new JSONObject();

		JSONObject jsonObject1 = new JSONObject();

		jsonObject1.put("name", "user 4");
		jsonObject.put("player", jsonObject1);
		given().contentType("application/json").body(jsonObject.toString()).when().put(url);

		Map<String, String> mymap = new HashMap<>();

		mymap = given().when().get(url).then().extract().jsonPath().getMap("response");
		System.out.println("player_details.." + mymap);

	}

	@Test(priority = 7)
	public void Move_player_by_id_in_Board() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v2/move/" + Board_id + ".json?player_id=" + player_id + "");
		authentication = basic("su", "root_pass");

		int roll = given().when().get(url).then().extract().jsonPath().get("response.roll");
		System.out.println("roll with.." + roll);

		
		

		
		int position = given().when().get(url).then().extract().jsonPath().get("response.player.position");
		System.out.println("Position.." + position);

	}

	@Test(priority=8)
	public void Board_get_details_of_board() throws MalformedURLException{
		URL url=new URL("http://10.0.1.86/snl/rest/v2/board/"+Board_id+".json");
		
		Object obj=given().when().get(url).then().extract().jsonPath().get("response.board");
		System.out.println(obj);
		
	}
	
	@Test(priority=10)
	public void Board_delete_board() throws MalformedURLException{
		URL url=new URL("http://10.0.1.86/snl/rest/v2/board/"+Board_id+".json");
		given().when().delete(url);
		
		
		
		
	}
	
	@Test(priority = 9)
	public void player_delete_player() throws MalformedURLException {

		URL url = new URL("http://10.0.1.86/snl/rest/v2/player/" + player_id + ".json");
		authentication = basic("su", "root_pass");
		given().contentType("application/json").when().delete(url);

	}


}
