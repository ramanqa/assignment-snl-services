package Main_methods_BoardTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import utility.Datadecider;

public class Back_Board_help {

	static Datadecider opt;
	private static String username;
	private static String password;

	public Back_Board_help() throws IOException {

		opt = new Datadecider();
		username = opt.readit("username_v2");
		password = opt.readit("password_v2");

	}

	public HttpURLConnection getConnection(String urls, String type) throws IOException {

		if (opt.readit("version").equals("v3")) {
			urls = urls + "?access_token=" + opt.readit("access_token");
		}
		
		System.err.println(urls);
		

		URL url = new URL(urls);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(type);
		conn.setRequestProperty("Accept", "application/json");
		if (opt.readit("version").equals("v2")) {
			String encoded = Base64.getEncoder()
					.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
			// Java 8
			conn.setRequestProperty("Authorization", "Basic " + encoded);
		}

		return conn;

	}

	public HttpURLConnection addplayer_post_call(String Board, String name) {

		String message = "{\"board\":\"" + Board + "\",\"player\":{\"name\": \"" + name + "\"}}";

		try {
			// instantiate the URL object with the target URL of the resource to
			// request

			String urls = opt.readit("baseurl") + "/rest/" + opt.readit("version") + "/player.json";

			if (opt.readit("version").equals("v3")) {
				urls = urls + "?access_token=" + opt.readit("access_token");
			}
			URL url = new URL(urls);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (opt.readit("version").equals("v2")) {
				String encoded = Base64.getEncoder()
						.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
				// Java 8
				connection.setRequestProperty("Authorization", "Basic " + encoded);
			}

			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(message);

			writer.close();

			return connection;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public HttpURLConnection player_modified(String name, int player_id) {

		String message = "{\"player\":{\"name\": \"" + name + "\"}}";
		try {
			// instantiate the URL object with the target URL of the resource to
			// request

			String urls = opt.readit("baseurl") + "/rest/" + opt.readit("version") + "/player/" + player_id + ".json";

			if (opt.readit("version").equals("v3")) {
				urls = urls + "?access_token=" + opt.readit("access_token");
			}
			URL url = new URL(urls);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (opt.readit("version").equals("v2")) {
				String encoded = Base64.getEncoder()
						.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
				// Java 8
				connection.setRequestProperty("Authorization", "Basic " + encoded);
			}
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

	public HttpURLConnection player_deleted(int player_id) {

		try {
			// instantiate the URL object with the target URL of the resource to
			// request

			String urls = opt.readit("baseurl") + "/rest/" + opt.readit("version") + "/player/" + player_id + ".json";

			if (opt.readit("version").equals("v3")) {
				urls = urls + "?access_token=" + opt.readit("access_token");
			}
			URL url = new URL(urls);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (opt.readit("version").equals("v2")) {
				String encoded = Base64.getEncoder()
						.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
				// Java 8
				connection.setRequestProperty("Authorization", "Basic " + encoded);
			}
			connection.setDoOutput(true);
			connection.setRequestMethod("DELETE");
			connection.connect();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	public HttpURLConnection roll_dice(int board, int player_id) {

		try {
			// instantiate the URL object with the target URL of the resource to
			// request

			String urls = opt.readit("baseurl") + "/rest/" + opt.readit("version") + "/move/" + board
					+ ".json?player_id=" + player_id;

			if (opt.readit("version").equals("v3")) {
				urls = urls + "?access_token=" + opt.readit("access_token");
			}

			URL url = new URL(urls);

			System.out.println(url.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (opt.readit("version").equals("v2")) {
				String encoded = Base64.getEncoder()
						.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
				// Java 8
				connection.setRequestProperty("Authorization", "Basic " + encoded);
			}
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			// connection.connect();

			return connection;
		} catch (IOException e) {
			// ...
		}
		return null;

	}

	public HttpURLConnection deleteboard(int Board_id) throws IOException {

		String urls = opt.readit("baseurl") + "/rest/" + opt.readit("version") + "/board/" + Board_id + ".json";

		if (opt.readit("version").equals("v3")) {
			urls = urls + "?access_token=" + opt.readit("access_token");
		}

		URL url = new URL(urls);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setDoOutput(true);
		if (opt.readit("version").equals("v2")) {
			String encoded = Base64.getEncoder()
					.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
			// Java 8
			conn.setRequestProperty("Authorization", "Basic " + encoded);
		}
		conn.connect();

		return conn;

	}

	public JSONObject getJson(HttpURLConnection conn) throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		JSONObject complete = new JSONObject();
		JSONParser parser = new JSONParser();
		// test3
		complete = (JSONObject) parser.parse(br.readLine());
		return complete;

	}
	
	public static void saveToken()
	{
		  try {
			  
			  
			  opt = new Datadecider();

	            System.out.println(opt.readit("tokenaccessurl"));
	            OAuthClient client = new OAuthClient(new URLConnectionClient());
	            System.out.println(opt.readit("clientid"));
	            URL url = new URL(opt.readit("tokenaccessurl"));
	            OAuthClientRequest request = OAuthClientRequest.tokenLocation(opt.readit("tokenaccessurl"))
	            		.setGrantType(GrantType.CLIENT_CREDENTIALS)
	                    .setClientId(opt.readit("clientid"))
	                    .setClientSecret(opt.readit("clientsecret"))
	                    // .setScope() here if you want to set the token scope
	                    .buildQueryMessage();

	            String token =
	                    client.accessToken(request, OAuthJSONAccessTokenResponse.class).getAccessToken();
	            

                System.out.println(token);
                
	            opt.writeit("access_token",token);
	            
	            
	            
	            
	}
		  catch(Exception ex)
		  {
			  ex.printStackTrace();
		  }
		  
	}

}
