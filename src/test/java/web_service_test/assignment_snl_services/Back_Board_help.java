package web_service_test.assignment_snl_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Back_Board_help {

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
