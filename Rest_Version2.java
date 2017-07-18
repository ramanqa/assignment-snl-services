package Test;
import static org.assertj.core.api.Assertions.assertThat;
//version1 
import static org.hamcrest.CoreMatchers.*;
import static org.testng.Assert.assertNotEquals;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javax.annotation.Resources;
import javax.xml.ws.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.PreemptiveBasicAuthScheme;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

public class Rest_Version2 {
	String Board_id=" ";
	String Player_id=" ";
	String Board=" ";
	String Player=" ";
	@BeforeClass
	public void setBaseUri() {

		RestAssured.baseURI = "http://10.0.1.86/snl";
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName("su");
		authScheme.setPassword("root_pass");
		RestAssured.authentication = authScheme;
	}
	
	//BOARD
	@Test(priority=0)
	public void Create_a_new_board() {
		com.jayway.restassured.response.Response res=RestAssured.given().param("response.status", "1").when().get("/rest/v2/board/new.json");
		res.then().assertThat().statusCode(200);
		JsonPath obj1=res.jsonPath();
		
		Board=obj1.getString("response.board.id");
		Board_id=Board.concat(".json");
		
		assertThat(Board!=null);
	
	}


	@Test(priority=1)
	public void Get_a_list_of_Boards() {
		
		com.jayway.restassured.response.Response res=RestAssured.given().param("response.status", "1").when().get("/rest/v2/board.json");
		res.then().assertThat().statusCode(200);
		JsonPath obj1=res.jsonPath();
		
		assertThat(obj1.getString("response.status").equals(1));
	}
	
	
	@Test(priority=2)
	public void Get_Board_Details()  {
		com.jayway.restassured.response.Response res=RestAssured.given().param("response.status", "1").when().get("/rest/v2/board/{id}", Board_id);
		res.then()
		.assertThat().statusCode(200);
		JsonPath obj1= res.jsonPath();
		assertThat(obj1.getString("response.board.id").equals("Board"));


	}
	
	@Test(priority=3)
	public void Put_Board_Details()  {
		com.jayway.restassured.response.Response res=RestAssured.given().param("response.status", "1").when().put("/rest/v2/board/{id}", Board_id);
		res.then()
		.assertThat().statusCode(200);
		JsonPath obj1= res.jsonPath();
		assertThat(obj1.getString("response.board.turn").equals("1"));

	}
	
	@Test(priority=10)
	public void Destroy_Boards() {
		
		String id;
		int count=0;
	com.jayway.restassured.response.Response res=RestAssured.given().when().delete("/rest/v2/board/{id}", Board_id);
	res.then().assertThat().statusCode(200);
	
	com.jayway.restassured.response.Response res1=RestAssured.given().param("response.status", "1").when().get("/rest/v1/board.json");

	JsonPath jobj= res1.jsonPath();

	for(int i=0;i<count;i++)
	{
		
		assertThat(jobj.getString("response.board.id[count]")!=Board);
	}
	}
	
/*	@Test
	public void Delete_Board_version1() throws ParseException, net.minidev.json.parser.ParseException {

		com.jayway.restassured.response.Response res = RestAssured.given().when().get("/rest/v1/board.json").then().contentType(ContentType.JSON)
				.extract().response();

		String data = res.asString();

		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(data);
		JSONObject resp = (JSONObject) obj.get("response");
		// System.out.println( resp.get("status"));
		JSONArray gg = (JSONArray) resp.get("board");
		System.out.println(((JSONObject) gg.get(0)).get("id"));
		for (int i = 0; i < 5; i++) {
			String idd = ((JSONObject) gg.get(i)).get("id").toString();
			RestAssured.given().param("response.status", "1").when().delete("/rest/v1/board/" + idd + ".json").then()
					.assertThat().statusCode(200);
			System.out.println("delete system crash");
		}

	}*/
	
	//PLAYER

	/*@Test
	public void Join_new_Player_version1() throws IOException {
		URL file = Resources.getResource("file.json");
		String myJson = Resources.toString(file, Charsets.UTF_8);

		System.out.println(myJson);
		RestAssured.given().contentType(ContentType.JSON).body(myJson).when().post("/rest/v1/player.json").then()
				.assertThat().statusCode(200);

	}*/
	@Test(priority=5)
	public void Join_new_Player_to_board() throws IOException {
	
		JSONObject ob1=new JSONObject();
		ob1.put("board",Board);
		
		JSONObject ob2 =new JSONObject();
		ob2.put("name","ABCD");
		
		ob1.put("player", ob2);
		
		//System.out.println(ob1);
		/*RestAssured.given().body(ob1).when().post("/rest/v1/player.json").then()
			.assertThat().statusCode(200);*/	
		
		JsonPath jsonpath=RestAssured.given().body(ob1).when().post("/rest/v2/player.json").then().extract().jsonPath();
		Player_id=jsonpath.getString("response.player.id").concat(".json");
		Player=jsonpath.getString("response.player.id");
		System.out.println(Player_id);
		assertThat(jsonpath.getString("response.player.name").equals("ABCD"));
		assertThat(jsonpath.getString("response.player.position").equals("0"));
		
	}


	@Test(priority=6)
	public void Get_Player_details() {

		com.jayway.restassured.response.Response res=RestAssured.given().param("response.status", "1").when().get("/rest/v2/player/{id}",Player_id);
		res.then().assertThat().statusCode(200);
		JsonPath jobj= res.jsonPath();
		//JsonPath jsonpath=RestAssured.given().body(ob1).when().post("/rest/v1/player.json").then().extract().jsonPath();
		assertThat(jobj.getString("response.player").contains("id"));
		assertThat(jobj.getString("response.player").contains("board_id"));
		assertThat(jobj.getString("response.player").contains("name"));
		assertThat(jobj.getString("response.player").contains("position"));
	
	
	}
	
	@Test(priority=7)
	public void Put_Player_details() {

	JSONObject ob1=new JSONObject();
		
		ob1.put("name","Ashika");
		
		JSONObject ob2= new JSONObject();
		ob2.put("player", ob1);
		//System.out.println(ob2);
		System.out.println(Player_id);
		com.jayway.restassured.response.Response res=RestAssured.given().body(ob2).when().put("/rest/v2/player/{id}",Player_id);
		res.then().assertThat().statusCode(200);

		JsonPath jobj= res.jsonPath();
		assertThat(jobj.getString("response.player.name").equals("Ashika"));
	
	
	}
	
	@Test(priority=8)
	public void Move_Player_() {

JSONObject ob1=new JSONObject();
		
		
		//System.out.println(Player_id);
		com.jayway.restassured.response.Response res=RestAssured.given().when().get("/rest/v2/move/{id}?player_id={id1}",Board_id,Player);
		res.then().assertThat().statusCode(200);

		JsonPath jobj=res.jsonPath();
		String turn=jobj.getString("response.player.turn");
		assertNotEquals(turn ,1);
	
	}

	@Test(priority=9)
	public void Quit_Player() {
		
		
	RestAssured.given().when().delete("/rest/v2/player/{id}", Player_id).then().assertThat().statusCode(200);
	
	}

/*	@Test
	public void _Player_move_version1() {
		JsonPath res = RestAssured.given().when().get("/rest/v1/board.json").then().contentType(ContentType.JSON)
			.extract().jsonPath();
		String str=res.getString("response.board.id[0]");
		System.out.println(str);
		JsonPath res1 = RestAssured.given().when().get("/rest/v1/board/"+str+".json").then().contentType(ContentType.JSON)
				.extract().jsonPath();
		System.out.println(res1);
		String player=res1.getString("response.board.player.id");
		//JsonPath res = RestAssured.given().when().get("/rest/v1/player.json").then().extract().jsonPath();
			//String str=res.getString("response.player.id");
			System.out.println(player);
		
	//	RestAssured.given().param("response.status", "1").when().get("/rest/v1/move/{id}"+str+".json?player_id={player_id}"+player).then()
				//.assertThat().statusCode(200);

	}
*/
	
	

}
