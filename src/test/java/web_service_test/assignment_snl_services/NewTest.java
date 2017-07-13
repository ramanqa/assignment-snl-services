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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import com.jayway.restassured.internal.path.json.JSONAssertion;
import com.jayway.restassured.internal.path.json.mapping.JsonObjectDeserializer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import groovy.json.internal.JsonParserLax;

public class NewTest {

	int Board_id;
	JSONObject json_verify;
	int player_id;
	HttpURLConnection conn;

	@Test
	public void init() {

	}

	@Test
	public void check_board_creation() throws ClientProtocolException, IOException, ParseException {

		try {

			conn = getConnection("http://10.0.1.86/snl//rest/v1/board/new.json", "GET");
			assertThat(conn.getResponseCode()).isEqualTo(200);

			JSONObject complete = getJson(conn);

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
	public void player_add_check() throws IOException, ParseException {

		JSONObject jsonobj, jsonplayer;

		conn = this.addplayer_post_call(Board_id + "", "akshay");
		assertThat(conn.getResponseCode()).isEqualTo(200);
		conn = this.addplayer_post_call(Board_id + "", "nishant");

		jsonobj = (JSONObject) getJson(conn).get("response");
		assertThat(Integer.parseInt(jsonobj.get("status").toString())).isEqualTo(1);

		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(Integer.parseInt(jsonplayer.get("board_id").toString())).isEqualTo(Board_id);
		assertThat(Integer.parseInt(jsonplayer.get("position").toString())).isEqualTo(0);
		assertThat(jsonplayer.get("name")).isEqualTo("nishant");

		player_id = (Integer.parseInt(jsonplayer.get("id").toString()));

		conn = this.addplayer_post_call(Board_id + "", "shabaash");

		jsonobj = (JSONObject) getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo(player_id + 1 + "");

		conn = this.addplayer_post_call(Board_id + "", "shadaab");
		// assertThat(getJson(conn).get("response")).isNotNull();
		jsonobj = (JSONObject) getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo(player_id + 2 + "");

		conn = this.addplayer_post_call(Board_id + "", "pankaj");
		jsonobj = (JSONObject) getJson(conn).get("response");
		jsonplayer = (JSONObject) jsonobj.get("player");
		assertThat(jsonplayer.get("id").toString()).isEqualTo((player_id + 3 + ""));
		// assertThat(getJson(conn).get("response")).isNotNull();

	}

	@Test(dependsOnMethods = "player_add_check")
	public void check_player_details() throws IOException, ParseException {

		conn = getConnection("http://10.0.1.86/snl//rest/v1/player/" + player_id + ".json", "GET");
		JSONObject complete = getJson(conn);

		JSONObject response = (JSONObject) complete.get("response");
		assertThat(response).isNotNull();

		JSONObject player = (JSONObject) response.get("player");
		assertThat(Integer.parseInt(player.get("board_id").toString())).isEqualTo(Board_id);

	}

	@Test(dependsOnMethods = "check_player_details")
	void update_player() throws IOException, ParseException {

		conn = player_modified("nishant sharma", player_id);

		JSONObject complete = getJson(conn);

		JSONObject response = (JSONObject) complete.get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);

		JSONObject player = (JSONObject) response.get("player");

		assertThat(player.get("name")).isEqualTo("nishant sharma");
		assertThat(Integer.parseInt(player.get("board_id").toString())).isEqualTo(Board_id);
		assertThat(Integer.parseInt(player.get("id").toString())).isEqualTo(player_id);

	}

	@Test(dependsOnMethods = "update_player")
	void check_roll_dice() throws IOException, ParseException {

		conn = roll_dice(Board_id, player_id);
		JSONObject complete = getJson(conn);
		JSONObject response = (JSONObject) complete.get("response");
		JSONObject player = (JSONObject) response.get("player");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(-1);

		player_id = player_id - 1;

		int play_id = player_id;
		it: for (int a = 1; a <= 500; a++) {

			conn = roll_dice(Board_id, play_id);
			complete = getJson(conn);
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

		conn = player_deleted(player_id);

		assertThat(conn.getResponseCode()).isEqualTo(200);
		JSONObject responce = (JSONObject) getJson(conn).get("response");
		String status = (String) responce.get("success");

		assertThat(status).isEqualTo("OK");

	}

	@Test(dependsOnMethods = "delete_player")
	void add_player_while_game_is_on() throws IOException, ParseException {
		conn = addplayer_post_call(Board_id + "", "akshaykumar");

		JSONObject complete = getJson(conn);
		JSONObject response = (JSONObject) complete.get("response");
		assertThat(Integer.parseInt(response.get("status").toString())).isEqualTo(1);
		JSONObject player = (JSONObject) response.get("player");

		assertThat((player.get("name").toString())).isEqualTo("akshaykumar");
		assertThat(Integer.parseInt(player.get("position").toString())).isEqualTo(0);

	}

	@Test(dependsOnMethods = "add_player_while_game_is_on")
	public void deleteboard() throws IOException {

		try {

			URL url = new URL("http://10.0.1.86/snl/rest/v1/board/" + Board_id + ".json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setDoOutput(true);
			conn.connect();

			assertThat(conn.getResponseCode()).isEqualTo(200);
			JSONObject responce = (JSONObject) getJson(conn).get("response");
			String status = (String) responce.get("success");

			assertThat(status).isEqualTo("OK");

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	HttpURLConnection getConnection(String urls, String type) throws IOException {

		URL url = new URL(urls);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(type);
		conn.setRequestProperty("Accept", "application/json");
		return conn;

	}

	HttpURLConnection addplayer_post_call(String Board, String name) {

		String message = "{\"board\":\"" + Board + "\",\"player\":{\"name\": \"" + name + "\"}}";
		try {
			// instantiate the URL object with the target URL of the resource to
			// request
			URL url = new URL("http://10.0.1.86/snl/rest/v1/player.json");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(message);

			writer.close();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	HttpURLConnection player_modified(String name, int player_id) {

		String message = "{\"player\":{\"name\": \"" + name + "\"}}";
		try {
			// instantiate the URL object with the target URL of the resource to
			// request
			URL url = new URL("http://10.0.1.86/snl/rest/v1/player/" + player_id + ".json");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(message);

			writer.close();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	HttpURLConnection player_deleted(int player_id) {

		try {
			// instantiate the URL object with the target URL of the resource to
			// request
			URL url = new URL("http://10.0.1.86/snl/rest/v1/player/" + player_id + ".json");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("DELETE");
			connection.connect();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	HttpURLConnection roll_dice(int board, int player_id) {

		try {
			// instantiate the URL object with the target URL of the resource to
			// request
			URL url = new URL("http://10.0.1.86/snl/rest/v1/move/" + board + ".json?player_id=" + player_id);

			System.out.println(url.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			// connection.connect();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	JSONObject getJson(HttpURLConnection conn) throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		JSONObject complete = new JSONObject();
		JSONParser parser = new JSONParser();
		// test3
		complete = (JSONObject) parser.parse(br.readLine());
		return complete;

	}

}
