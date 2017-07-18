package snl_Services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.containsString;
import static org.testng.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class TestSnlServicesVer_2 {
	
	static String boardID,pathParam,playerID,playerpath,playerName;
	
	@Test(priority=1)
	public void testBoardList_GET(){
		
		RestAssured.given().param("response").when().get("http://10.0.1.86/snl/rest/v2/board.json").then().assertThat().statusCode(401);
		RestAssured.expect().statusCode(200).when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board.json");
		
	}
	
	@Test(priority=0)
	public void testCreateNewBoard_GET() throws Exception{
		

		RestAssured.expect().when().get("http://10.0.1.86/snl/rest/v2/board/new.json").then().assertThat().statusCode(401);
		RestAssured.expect().statusCode(200).when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board/new.json");
		JsonPath res =RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/new.json").then().extract().jsonPath();
		 boardID=res.getString("response.board.id");
		pathParam=boardID.concat(".json"); 
		System.out.println("Board id is  "+boardID);
		
	}
	
	@Test(priority=2)
	public void testNewBoardWithID_GET(){
		
		RestAssured.expect().when().get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(401);
		RestAssured.given().param("response.board.id", boardID).when().with().authentication().basic("su", "root_pass").
		get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(200);
		RestAssured.expect().body("response.board.players.size()", equalTo(0)).when().get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam);
		
	}
	
	@Test(priority=3)
	public void testNewBoardWithID_PUT(){
		
		RestAssured.expect().when().put("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(401);
		RestAssured.given().when().with().authentication().basic("su", "root_pass").put("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(200);

		JsonPath res =RestAssured.when().put("http://10.0.1.86/snl/rest/v1/board/{id}",pathParam).then().extract().jsonPath();
		 String players=res.getString("response.board.players");
		 System.out.println(players);
		 assertEquals(players, null);;
	}
	
	@Test(priority=10)
	public void testNewBoardWithID_DELETE(){
		
		RestAssured.expect().when().delete("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(401);
		RestAssured.given().parameters("success", "OK").when().with().authentication().basic("su", "root_pass")
		.delete("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(200);
		RestAssured.expect().body("response.message", containsString("Invalid board id")).when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam);
		RestAssured.expect().body("response.status", equalTo(-1)).when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam);
	} 
	
	@SuppressWarnings("unchecked")
	@Test(priority=4)
	public void testPlayer_POST() throws Exception{
	
		
		JSONObject board=new JSONObject();
		board.put("board", boardID);
		JSONObject player=new JSONObject();
		player.put("name", "TestPlayer");
		board.put("player", player);
		RestAssured.expect().when().post("http://10.0.1.86/snl/rest/v2/player.json").then().assertThat().statusCode(401);
		Response response=RestAssured.given().body(board).when().with().authentication().basic("su", "root_pass").post("http://10.0.1.86/snl/rest/v2/player.json").then().extract().response();
		response.then().assertThat().statusCode(200);
		
		JsonPath jsonpath =response.then().extract().jsonPath();
		 playerID=jsonpath.getString("response.player.id");
		 System.out.println(playerID);
		 
		 JsonPath path= RestAssured.expect().when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().extract().jsonPath();
		String actualPlayerID=path.getString("response.board.players[0].id");
		 playerName=path.getString("response.board.players[0].name");
		assertEquals(actualPlayerID,playerID);
		assertEquals(playerName,"TestPlayer"); 
		
	}
	
	@Test(priority=6)
	public void testPlayerWithID_GET(){
		
		RestAssured.expect().when().get("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(401);
		playerpath=playerID.concat(".json");
		RestAssured.given().parameters("response.player.id", playerID, "response.player.board_id",boardID,"response.player.name","TestPlayer").when().
		with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(200);
	}
	
	@SuppressWarnings("unchecked")
	@Test(priority=7)
	public void testPlayerWithID_PUT(){
		
		RestAssured.expect().when().put("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(401);
		JSONObject player=new JSONObject();
		JSONObject name=new JSONObject();
		name.put("name", "Player1");
		player.put("player", name);
		RestAssured.given().body(player).when().with().authentication().basic("su", "root_pass").put("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(200);
		
		JsonPath path= RestAssured.expect().when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().extract().jsonPath();
		playerName=path.getString("response.player.name");
		assertEquals(playerName,"Player1");
	}
	
	@Test(priority=9)
	public void testPlayerWithID_DELETE(){
		
		RestAssured.expect().when().delete("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(401);
		RestAssured.given().when().with().authentication().basic("su", "root_pass").delete("http://10.0.1.86/snl/rest/v2/player/{id}",playerpath).then().assertThat().statusCode(200);
		RestAssured.given().parameters("response.board.id",boardID, "response.board.players", null).when().with().authentication().basic("su", "root_pass")
		.get("http://10.0.1.86/snl/rest/v2/board/{id}",pathParam).then().assertThat().statusCode(200);
	}
	
	@Test(priority=8)
	public void testMove_GET(){
		
		RestAssured.expect().when().get("http://10.0.1.86/snl/rest/v2/move/{boardid}?player_id={player_id}",pathParam,playerID).then().assertThat().statusCode(401);
		RestAssured.given().parameters("response.player.id", playerID, "response.player.board_id",boardID,"response.player.name",playerName)
		.when().with().authentication().basic("su", "root_pass").get("http://10.0.1.86/snl/rest/v2/move/{boardid}?player_id={player_id}",pathParam,playerID).then().assertThat().statusCode(200);
		
		RestAssured.expect().body("response.roll",lessThan(7),"response.player.position",lessThan(26)).when().with().authentication().basic("su", "root_pass")
		.get("http://10.0.1.86/snl/rest/v2/move/{boardid}?player_id={player_id}",pathParam,playerID);
		
	}
	
}
