package web_service_test.assignment_snl_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.jayway.restassured.internal.path.json.JSONAssertion;
import com.jayway.restassured.internal.path.json.mapping.JsonObjectDeserializer;

public class NewTest {

	@Test
	public void lotto_resource_returns_200_with_expected_id_and_winners() {

		// when().
		//
		// get("/lotto/{id}", 5).then().statusCode(200).body("lotto.lottoId",
		// equalTo(5), "lotto.winners.winnerId",
		// containsOnly(23, 54));

	}

	public static void main1() throws ClientProtocolException, IOException {

		try {

			URL url = new URL("http://10.0.1.86/snl//rest/v1/board/new.json");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
//				System.out.println(output);
			}

			System.out.println(conn.getResponseCode()==HttpURLConnection.HTTP_OK);
			
			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	
	
	public static void main(String...S) throws IOException
	{
		URL obj = new URL("http://10.0.1.86/snl/rest/v1/player.json");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
	  JSONObject  POST_PARAMS = new "{“board”:[4], “player”:{“name”: [akshay]}}";
		
	
		
		
		
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
	
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}

}
