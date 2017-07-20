import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import net.minidev.json.JSONObject;

public class version1 {
	String boardID;
	String playerID;
	String playerName;
	String b_id;
	

     @BeforeClass
	public void setBaseUri () {

	    RestAssured.baseURI = "http://10.0.1.86/snl/";
	  }

	 

	 @Test(priority=1)
	  public void boardtestStatusCode () {
	    
		  //given();
		
		RestAssured.given().when().get("/rest/v1/board.json").then().body("response.board[8].id", equalTo("7079"));
		 //RestAssured.given().when().get("/rest/v1/board.json").then().assertThat().body("response.board[8].id", "7079");
		RestAssured.given().when().get("/rest/v1/board.json").then().body("response.status", equalTo("1"));
 }
	 
	
     @Test(priority=2)
	  public void  newboard() {
    	
		 Response res = RestAssured.given().when().get("/rest/v1/board/new.json").then().extract().response();
		 System.out.println(res.asString());
		 
		 com.jayway.restassured.path.json.JsonPath path =  RestAssured.when().get("/rest/v1/board/new.json").then().extract().jsonPath() ;
		 System.out.println(path);
		 
		 boardID = path.getString("response.board.id");
		 b_id = boardID.concat(".JSON");
		 System.out.println("board id of new board is"+boardID);
	}
     // Response res = RestAssured.given().when().get("/rest/v1/board/7566.json").then().assertThat().b
	  //System.out.println("detailsofboard" +res.asString());
	 

    @Test(priority=3)
	  public void  detailsofboard() {
	    
		  //RestAssured.given().when().get("/rest/v1/board/3704.json").then().statusCode(200);
    	  System.out.println(b_id);
		
		  com.jayway.restassured.path.json.JsonPath path =  RestAssured.given().when().get("/rest/v1/board/7566.json").then().extract().jsonPath();
	    	boardID = path.getString("response.board.id");
	    	
		
		  
		 // System.out.println("details of board" +res.asString());
		  }
	  
    //@Test
    /*public void resetboard(){
    	RestAssured.given().when().delete("/rest/v1/board/7065.json");
    }
    */
    
    //@Test(prio
    public void newplayer(){

    	 RestAssured.given().
    	body(" {\"board\":\"6014\",“\"player\":{\"name\":\"vibhuti gupta\"}}" ).
    	when().
    	contentType(ContentType.JSON).
    	post("/rest/v1/player.json").then().

    	assertThat().statusCode(200);

    	//ystem.out.println(res.asString());



    	}
    

  

    @Test(priority=4)
    public void newPlayer_POST() throws Exception{
    	//System.out.println("board id of new player is" +boardID);
		JSONObject board1=new JSONObject();
		board1.put("board", "7272");
		System.out.println(boardID);
		JSONObject player=new JSONObject();
		player.put("name", "VibhutiGupta");
		board1.put("player", player);
		Response response = RestAssured.given().body(board1).when().post("/rest/v1/player.json").then().extract().response();
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
    	com.jayway.restassured.path.json.JsonPath path =  RestAssured.given().when().get("/rest/v1/player/2898.json").then().extract().jsonPath();
    	playerID = path.getString("response.player.id");
    	System.out.println("player id of player is" +playerID);
    	}

    	@Test(priority=6)
    	public void updateplayerdetails(){
    	Response res = (Response) RestAssured.given().when().contentType(ContentType.JSON).body("{\"player\":{\"name\": \"vanika singhal\"}}").put("/rest/v1/player/2898.json");
    	//System.out.println(res.asString());

    	}

    	/*@Test(priority=7)
    	public void deleteplayer(){
    	RestAssured.given().when().delete("/rest/v1/player/2895.json");

    	}*/
	
    	 @Test
    	public void moveplayer(){
    		 Response res = RestAssured.given().when().contentType(ContentType.JSON).get("/rest/V1/move/[board_id].json?player_id=2895");
    		
    		
    	}

}
