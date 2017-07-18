package com.mycompany.testsnl;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.basic;
import com.jayway.restassured.response.Response;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author piyusharora
 */
public class testing_SNLWebService_v2 {

    @BeforeTest
    public void SetupURI() {
        RestAssured.baseURI = "http://10.0.1.86/snl/";
        RestAssured.authentication=basic("su","root_pass");

    }

    
    public Long Board_Id_Grab() throws ParseException {
        Long id = null;
        Response response = RestAssured.get("/rest/v2/board/new.json");
        String string_response = response.asString();
        JSONParser json_paser = new JSONParser();
        JSONObject json_response = (JSONObject) json_paser.parse(string_response);
        JSONObject json_response1 = (JSONObject) json_response.get("response");
        JSONObject json_response_board = (JSONObject) json_response1.get("board");
        id = (Long) json_response_board.get("id");
        return id;
    }

    public long[] Player_Id_Grab() throws ParseException {
        long player_Id;
        long board_Id = Board_Id_Grab();
//        System.out.println(id);
        JSONObject json_object = new JSONObject();
        JSONObject player = new JSONObject();
        json_object.put("board", +board_Id);
        player.put("name", "Ram");
        json_object.put("player", player);
        System.out.println(json_object);
        Response resp = RestAssured.given().contentType("application/json").body(json_object).when().post("http://10.0.1.86/snl/rest/v2/player.json");
        player_Id = resp.jsonPath().getLong("response.player.id");
//        System.out.println("Player ID :- " + player_Id);
        long arr[] = new long[2];
        arr[0] = board_Id;
        arr[1] = player_Id;

        return arr;

    }
//    @Test
//    public void Personal_Interset() throws ParseException {
//
//        Response resp = RestAssured.get("http://10.0.1.86/snl/rest/v1/board.json");
//        String response = resp.asString();
//        System.out.println(response);
//        JSONParser jp = new JSONParser();
//        JSONObject respon = new JSONObject();
//        respon = (JSONObject) jp.parse(response);
//        JSONObject resp1=new JSONObject();
//        resp1=(JSONObject)respon.get("response");
//        JSONArray board = (JSONArray) resp1.get("board");
//        for (int i = 0; i < 9999; i++) {
//            String idd = ((JSONObject) board.get(i)).get("id").toString();
//            RestAssured.delete("/rest/v1/board/" + idd + ".json");
//        }
//    }

    @Test(priority = 0)
    public void test_List_Of_Boards() {
        RestAssured.get("/rest/v2/board.json").then().assertThat().statusCode(200);

    }

    @Test(priority = 1)
    public void test_New_Board_Creation() {

        RestAssured.get("/rest/v2/board/new.json").then().assertThat().statusCode(200);

    }

    @Test(priority = 2)
    public void test_Board_Is_Creating() throws ParseException {
        Long id = Board_Id_Grab();
//        System.out.println("id : " + id);

        RestAssured.get("/rest/v2/board/" + id + ".json").then().assertThat().statusCode(200);

    }

    @Test(priority = 3)
    public void test_Board_Is_Deleting() throws ParseException {

        Long id = Board_Id_Grab();
//        System.out.println("id : " + id);
        RestAssured.delete("/rest/v2/board/" + id + ".json");
        Response response = RestAssured.get("/rest/v1/board/" + id + ".json");
        JSONParser parser = new JSONParser();
        String String_response = response.asString();
        JSONObject json_object = (JSONObject) parser.parse(String_response);
        JSONObject json_respose = (JSONObject) json_object.get("response");
        Long status = (Long) json_respose.get("status");
        assertThat((status) == -1);

    }

    @Test(priority = 4)
    public void test_Board_Is_Resetting() throws ParseException {
        Long id = Board_Id_Grab();
        RestAssured.put("/rest/v2/board/" + id + ".json");
        String resp = RestAssured.put("/rest/v2/board/" + id + ".json").asString();
//        System.out.println("Response : " + resp);
    }

    @Test(priority = 5)
    public void test_add_new_player() throws IOException, ParseException {
        long arr_resp[] = Player_Id_Grab();
        Long board_id_return = arr_resp[0];
        Long player_id_return = arr_resp[1];

        Response resp = RestAssured.get("/rest/v2/board/" + board_id_return + ".json");
        resp.then().assertThat().statusCode(200);
        Long player_id_response = resp.jsonPath().getLong("response.board.players[0].id");
        String player_name_response = resp.jsonPath().get("response.board.players[0].name");
        assertThat(player_id_response.equals(player_id_return)).isTrue();
        assertThat(player_name_response.equals("Ram")).isTrue();

    }

    @Test(priority = 6)
    public void test_delete_player() throws ParseException {
        long ids[] = Player_Id_Grab();
        long player_id_return = ids[1];
        long board_id_return = ids[0];
        System.out.println("Player id = "+player_id_return);
        Response resp = RestAssured.delete("/rest/v2/player/" + player_id_return + ".json");
        resp.then().extract().response();
        resp.then().assertThat().statusCode(200);
        RestAssured.get("/rest/v2/player/" + player_id_return + ".json").then().assertThat().statusCode(404);
    }

    @Test(priority = 7)
    public void test_player_moves() throws ParseException {
        long ids[] = Player_Id_Grab();
        long player_id_return = ids[1];
        long board_id_return = ids[0];
        Response resp = RestAssured.get("/rest/v2/move/" + board_id_return + ".json?player_id=" + player_id_return);
        resp.then().assertThat().statusCode(200);
    }
}
