package web_service_test.assignment_snl_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

public class NewTest {

	int Board_id;
	JSONObject json_verify;

	@Test
	public void init() {

	}

	@Test
	public void check_board_creation() throws ClientProtocolException, IOException, ParseException {

		try {

			URL url = new URL("http://10.0.1.86/snl//rest/v1/board/new.json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			// test1
			assertThat(conn.getResponseCode()).isEqualTo(200);

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			output = br.readLine();

			// test2
			assertThat(output).isNotBlank();
			JSONObject complete = new JSONObject();
			JSONParser parser = new JSONParser();

			// test3
			complete = (JSONObject) parser.parse(output);
			JSONObject inner1 = (JSONObject) complete.get("response");
			assertThat(Integer.parseInt(inner1.get("status").toString())).isEqualTo(1);

			JSONObject inner2 = (JSONObject) inner1.get("board");

			assertThat(Integer.parseInt(inner2.get("turn").toString())).isEqualTo(1);

			Board_id = Integer.parseInt(inner2.get("id").toString());

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Test(dependsOnMethods = "check_board_creation")
	public void player_add_check() throws IOException, ParseException {

		String message = "{\"board\":\"" + Board_id + "\",\"player\":{\"name\": \"akshay7\"}}";

		HttpURLConnection conn = this.addplayer_post_call(Board_id + "", "akshay");
		assertThat(conn.getResponseCode()).isEqualTo(200);

		conn = this.addplayer_post_call(Board_id + "", "nishant");

		System.out.println(getJson(conn));

		conn = this.addplayer_post_call(Board_id + "", "shabaash");

		System.out.println(getJson(conn));
		conn = this.addplayer_post_call(Board_id + "", "shadaab");

		System.out.println(getJson(conn));
		conn = this.addplayer_post_call(Board_id + "", "pankaj");

		System.out.println(getJson(conn));

	}

	@Test
	public void deleteboard() throws IOException {

		try {

			URL url = new URL("http://10.0.1.86/snl/rest/v1/board/" + Board_id + ".json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setDoOutput(true);
			conn.connect();

			System.out.println(conn.getResponseCode());

			conn.disconnect();

		} catch (Exception e) {
		}

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

	JSONObject getJson(HttpURLConnection conn) throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		JSONObject complete = new JSONObject();
		JSONParser parser = new JSONParser();
		// test3
		complete = (JSONObject) parser.parse(br.readLine());
		return complete;

	}

}
