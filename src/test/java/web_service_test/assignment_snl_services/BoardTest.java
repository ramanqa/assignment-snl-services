package web_service_test.assignment_snl_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import static org.assertj.core.api.Assertions.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.internal.path.json.JSONAssertion;
import com.jayway.restassured.internal.path.json.mapping.JsonObjectDeserializer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import Main_methods_BoardTest.Back_Board_help;
import Readers.OptionReader;
import groovy.json.internal.JsonParserLax;
import groovyjarjarantlr.Version;
import utility.Datadecider;

public class BoardTest {

	int Board_id;
	JSONObject json_verify;
	int player_id;
	HttpURLConnection conn;
	Back_Board_help boardHelp;
	Datadecider opt;

	@BeforeTest
	public void init() throws IOException {
		boardHelp = new Back_Board_help();
		opt = new Datadecider();

	}

	@Test
	void check_board_creation() throws ClientProtocolException, IOException, ParseException {

		try {

			String temp = System.getProperty("versions");
			if (temp.equals("v1") || temp.equals("v2") || temp.equals("v3")) {

				new OptionReader().writeit("version", temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (new OptionReader().optionFileReader("version").equals("v3")) {
			boardHelp.saveToken();
		}

		conn = boardHelp.getConnection(opt.readit("baseurl") + "//rest/" + opt.readit("version") + "/board/new.json",
				"GET");
		assertThat(conn.getResponseCode()).isEqualTo(200);
		JSONObject complete = boardHelp.getJson(conn);
		JSONObject inner1 = (JSONObject) complete.get("response");
		assertThat(Integer.parseInt(inner1.get("status").toString())).isEqualTo(1);
		JSONObject inner2 = (JSONObject) inner1.get("board");
		assertThat(Integer.parseInt(inner2.get("turn").toString())).isEqualTo(1);
		Board_id = Integer.parseInt(inner2.get("id").toString());

		conn.disconnect();

	}

	@Test(dependsOnMethods = "check_board_creation")
	void player_add_check() throws IOException, ParseException {

		JSONObject jsonobj, jsonplayer;

		conn = boardHelp.addplayer_post_call(Board_id + "", "akshay");
		assertThat(conn.getResponseCode()).isEqualTo(200);
		conn = boardHelp.addplayer_post_call(Board_id + "", "nishant");
		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		assertThat(Integer.parseInt(jsonobj.get("status").toString())).isEqualTo(1);
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(Integer.parseInt(jsonplayer.get("board_id").toString())).isEqualTo(Board_id);
		assertThat(Integer.parseInt(jsonplayer.get("position").toString())).isEqualTo(0);
		assertThat(jsonplayer.get("name")).isEqualTo("nishant");

		player_id = (Integer.parseInt(jsonplayer.get("id").toString()));
		conn = boardHelp.addplayer_post_call(Board_id + "", "shabaash");

		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo(player_id + 1 + "");
		conn = boardHelp.addplayer_post_call(Board_id + "", "shadaab");

		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo(player_id + 2 + "");
		conn = boardHelp.addplayer_post_call(Board_id + "", "pankaj");
		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo((player_id + 3 + ""));

	}

	@Test(dependsOnMethods = "player_add_check")
	void check_player_details() throws IOException, ParseException {

		conn = boardHelp.getConnection(
				opt.readit("baseurl") + "//rest/" + opt.readit("version") + "/player/" + player_id + ".json", "GET");
		JSONObject complete = boardHelp.getJson(conn);
		JSONObject response = (JSONObject) complete.get("response");
		JSONObject player = (JSONObject) response.get("player");
		assertThat(Integer.parseInt(player.get("board_id").toString())).isEqualTo(Board_id);

	}

	@Test(dependsOnMethods = "player_add_check")
	void check_inavlid_player_details() throws IOException, ParseException {

		conn = boardHelp.getConnection(
				opt.readit("baseurl") + "//rest/" + opt.readit("version") + "/player/" + 0 + ".json", "GET");
		assertThat(conn.getResponseCode()).isEqualTo(404);

	}

	@Test(dependsOnMethods = "player_add_check")
	void board_details() throws IOException, ParseException {
		conn = boardHelp.getConnection(
				opt.readit("baseurl") + "//rest/" + opt.readit("version") + "/board/" + Board_id + ".json", "GET");
		JSONObject response = (JSONObject) boardHelp.getJson(conn).get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);
		JSONObject board = (JSONObject) response.get("board");
		String board_array = (String) board.get("layout");
		System.out.println(board_array);
		assertThat(Integer.parseInt(board.get("id").toString())).isEqualTo(Board_id);
		assertThat(Integer.parseInt(board.get("turn").toString())).isEqualTo(1);

	}

	@Test(dependsOnMethods = "player_add_check")
	void board_invalid_details() throws IOException, ParseException {
		conn = boardHelp.getConnection(
				opt.readit("baseurl") + "//rest/" + opt.readit("version") + "/board/" + 0 + ".json", "GET");
		JSONObject response = (JSONObject) boardHelp.getJson(conn).get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(-1);

	}

	@Test(dependsOnMethods = "check_player_details")
	void update_player() throws IOException, ParseException {

		conn = boardHelp.player_modified("nishant sharma", player_id);
		JSONObject complete = boardHelp.getJson(conn);
		JSONObject response = (JSONObject) complete.get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);
		JSONObject player = (JSONObject) response.get("player");
		assertThat(player.get("name")).isEqualTo("nishant sharma");
		assertThat(Integer.parseInt(player.get("board_id").toString())).isEqualTo(Board_id);
		assertThat(Integer.parseInt(player.get("id").toString())).isEqualTo(player_id);

	}

	@Test(dependsOnMethods = "update_player")
	void check_roll_dice() throws IOException, ParseException {

		conn = boardHelp.roll_dice(Board_id, player_id);

		String input = boardHelp.getJson(conn).toString();
		JSONParser parser = new JSONParser();
		JSONObject complete = (JSONObject) parser.parse(input);
		System.out.println(complete);
		JSONObject response = (JSONObject) complete.get("response");
		JSONObject player = (JSONObject) response.get("player");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(-1);

		player_id = player_id - 1;

		int play_id = player_id;
		it: for (int a = 1; a <= 500; a++) {

			conn = boardHelp.roll_dice(Board_id, play_id);
			complete = boardHelp.getJson(conn);
			response = (JSONObject) complete.get("response");
			System.out.println("    " + response);
			assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);
			player = (JSONObject) response.get("player");

			if (player.get("position").toString().equals("25")) {
				break it;
			}
			play_id = play_id + 1;
			if (play_id > player_id + 4) {
				play_id = player_id;
			}
		}

	}

	@Test(dependsOnMethods = "update_player")
	void delete_player() throws IOException, ParseException {

		conn = boardHelp.player_deleted(player_id);
		assertThat(conn.getResponseCode()).isEqualTo(200);
		JSONObject responce = (JSONObject) boardHelp.getJson(conn).get("response");
		String status = (String) responce.get("success");
		assertThat(status).isEqualTo("OK");

	}

	@Test(dependsOnMethods = "delete_player")
	void add_player_while_game_is_on() throws IOException, ParseException {
		conn = boardHelp.addplayer_post_call(Board_id + "", "akshaykumar");
		JSONObject complete = boardHelp.getJson(conn);
		JSONObject response = (JSONObject) complete.get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);
		JSONObject player = (JSONObject) response.get("player");
		assertThat((player.get("name").toString())).isEqualTo("akshaykumar");
		assertThat(Integer.parseInt(player.get("position").toString())).isEqualTo(0);

	}

	@Test(dependsOnMethods = "add_player_while_game_is_on")
	void deleteboard() throws IOException, ParseException {

		conn = boardHelp.deleteboard(Board_id);
		assertThat(conn.getResponseCode()).isEqualTo(200);
		JSONObject responce = (JSONObject) boardHelp.getJson(conn).get("response");
		String status = (String) responce.get("success");
		assertThat(status).isEqualTo("OK");
		conn.disconnect();

	}

}
