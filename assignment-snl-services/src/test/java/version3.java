import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class version3 {
	
	 
	
	String boardID;
	String b_id;
    String playerID;
    Object accessToken;
    
    
    @BeforeClass
     public void setBaseUri () {

     RestAssured.baseURI = "http://10.0.1.86/snl/";
    }
    
    
    @BeforeMethod
    public void token(){
    	String res =  RestAssured.given()
 	                .parameters("username", "su", "password", "root_pass", 
 	                           "grant_type", "client_credentials", 
 	                           "client_id", "72e40f12f43480ee043dd67a61f0ea349346787317eba584758104d33b4b9347", "client_secret", "2fbf73be7c2280d83930e14b5b3cd55ed7c59ab68dfbdd7e49d985c6ed4f432f")
 	                .auth()
 	                .preemptive()
 	                .basic("72e40f12f43480ee043dd67a61f0ea349346787317eba584758104d33b4b9347","2fbf73be7c2280d83930e14b5b3cd55ed7c59ab68dfbdd7e49d985c6ed4f432f")
 	                .when()
 	                .post("oauth/token")
 	                .asString();

    	  JsonPath jsonpath = new JsonPath(res);
    	  accessToken =  jsonpath.getString("access_token");
 	   
  }
   

@Test(priority=1)
  public void boardtestStatusCode () {
    
	  //given();
	//RestAssured.given().authentication().preemptive().basic("su", "root_pass").when().get("/rest/v1/board.json").then().body("response.board[0].id", equalTo("3646"));
	//RestAssured.given().when().get("/rest/v1/board.xml").then().body("response.status", equalTo("1"));
	RestAssured.given().auth().oauth2("accessToken").when().get("/rest/v1/board.json").then().assertThat().statusCode(200);
}

@Test(priority=2)
public void  newboard() {
	
	 RestAssured.given().auth().oauth2("accessToken").when().get("/rest/v1/board/new.json").then().statusCode(200);
	 com.jayway.restassured.path.json.JsonPath path =  RestAssured.when().get("/rest/v1/board/new.json").then().extract().jsonPath() ;
	 System.out.println(path);
	 
	 boardID = path.getString("response.board.id");
	 b_id = boardID.concat(".JSON");
	 System.out.println("board id of new board is"+boardID);
}

@Test(priority=3)
public void  detailsofboard() {
  
	  //RestAssured.given().when().get("/rest/v1/board/3704.json").then().statusCode(200);
	  Response res = RestAssured.given().auth().oauth2("accesstoken").when().get("/rest/v1/board/7066.json");
	  
	  System.out.println("details of board" +res.asString());
	  }

@Test(priority=4)
  public void newPlayer_POST() throws Exception{
  	System.out.println("board id of new player is" +boardID);
		JSONObject board1=new JSONObject();
		board1.put("board", "7272");
		System.out.println(boardID);
		JSONObject player=new JSONObject();
		player.put("name", "VibhutiGupta");
		board1.put("player", player);
		Response response = RestAssured.given().auth().oauth2("accesstoken").body(board1).when().post("/rest/v1/player.json").then().extract().response();
		response.then().assertThat().statusCode(200);
		
		/*com.jayway.restassured.path.json.JsonPath jsonpath = response.then().contentType(ContentType.JSON).extract().jsonPath();
		 playerID=jsonpath.getString("response.player.id");
		 System.out.println(playerID);*/
		 
		/*JsonPath path= RestAssured.when().get("http://10.0.1.86/snl/rest/v1/board/{id}",pathParam).then().extract().jsonPath();
		String actualPlayerID=path.getString("response.board.players[0].id");
		 playerName=path.getString("response.board.players[0].name");
		assertEquals(actualPlayerID,playerID);
		assertEquals(playerName,"TestPlayer");*/
		
}

@Test(priority=5)
public void getplayerdetails()
{
	 RestAssured.given().when().get("/rest/v1/player/2898.json").then().assertThat().statusCode(200);
    com.jayway.restassured.path.json.JsonPath path =  RestAssured.given().auth().oauth2("accesstoken").when().get("/rest/v1/player/2898.json").then().extract().jsonPath();
    playerID = path.getString("response.player.id");
    System.out.println("player id of player is" +playerID);
}

@Test(priority=6)
public void updateplayerdetails(){
Response res = (Response) RestAssured.given().auth().oauth2("accesstoken").when().contentType(ContentType.JSON).body("{\"player\":{\"name\": \"vanika singhal\"}}").put("/rest/v1/player/2898.json");
//System.out.println(res.asString());

}

}
