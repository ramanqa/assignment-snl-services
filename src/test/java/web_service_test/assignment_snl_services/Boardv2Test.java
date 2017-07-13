package web_service_test.assignment_snl_services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Boardv2Test {

	int Board_id;
	JSONObject json_verify;
	int player_id;
	HttpURLConnection conn;
	Back_Board_help boardHelp;
	static String user_pass;
	

	@BeforeTest
	public void init() {
		boardHelp = new Back_Board_help();
	    user_pass = "?username=su&password=root_pass";
	}

	@Test
	void check_board_creation() throws ClientProtocolException, IOException, ParseException {

		try {

			conn = boardHelp.getConnection("http://10.0.1.86/snl//rest/v2/board/new.json"+user_pass, "GET");
			//assertThat(conn.getResponseCode()).isEqualTo(200);

			JSONObject complete = boardHelp.getJson(conn);

			JSONObject inner1 = (JSONObject) complete.get("response");
			assertThat(Integer.parseInt(inner1.get("status").toString())).isEqualTo(1);

			JSONObject inner2 = (JSONObject) inner1.get("board");

			assertThat(Integer.parseInt(inner2.get("turn").toString())).isEqualTo(1);

			Board_id = Integer.parseInt(inner2.get("id").toString());

			conn.disconnect();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

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
		// assertThat(getJson(conn).get("response")).isNotNull();
		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo(player_id + 2 + "");

		conn = boardHelp.addplayer_post_call(Board_id + "", "pankaj");
		jsonobj = (JSONObject) boardHelp.getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo((player_id + 3 + ""));
		// assertThat(getJson(conn).get("response")).isNotNull();

	}

	@Test(dependsOnMethods = "player_add_check")
	void check_player_details() throws IOException, ParseException {

		conn = boardHelp.getConnection("http://10.0.1.86/snl//rest/v2/player/" + player_id + ".json"+user_pass, "GET");
		JSONObject complete = boardHelp.getJson(conn);

		JSONObject response = (JSONObject) complete.get("response");
		assertThat(response).isNotNull();

		JSONObject player = (JSONObject) response.get("player");
		assertThat(Integer.parseInt(player.get("board_id").toString())).isEqualTo(Board_id);

	}

	@Test(dependsOnMethods = "player_add_check")
	void check_inavlid_player_details() throws IOException, ParseException {

		conn = boardHelp.getConnection("http://10.0.1.86/snl//rest/v2/player/" + 0 + ".json"+user_pass, "GET");
		assertThat(conn.getResponseCode()).isEqualTo(404);

	}

	@Test(dependsOnMethods = "player_add_check")
	void board_details() throws IOException, ParseException {
		conn = boardHelp.getConnection("http://10.0.1.86/snl//rest/v2/board/" + Board_id + ".json"+user_pass, "GET");
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
		conn = boardHelp.getConnection("http://10.0.1.86/snl//rest/v2/board/" + 0 + ".json"+user_pass, "GET");
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
		JSONObject complete = boardHelp.getJson(conn);
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

	@Test(dependsOnMethods = "check_roll_dice")
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
	void deleteboard() throws IOException {

		try {

			URL url = new URL("http://10.0.1.86/snl/rest/v2/board/" + Board_id + ".json"+user_pass);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setDoOutput(true);
			conn.connect();

			assertThat(conn.getResponseCode()).isEqualTo(200);
			JSONObject responce = (JSONObject) boardHelp.getJson(conn).get("response");
			String status = (String) responce.get("success");

			assertThat(status).isEqualTo("OK");

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

