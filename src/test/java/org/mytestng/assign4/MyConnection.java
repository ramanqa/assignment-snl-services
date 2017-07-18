package org.mytestng.assign4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

public class MyConnection {
	String name = "su";
	String password = "root_pass";

	String authString = name + ":" + password;

	byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	String authStringEnc = new String(authEncBytes);

	public static final String TOKEN_REQUEST_URL = "http://10.0.1.86/snl/oauth/token";

	public static final String CLIENT_ID = "b4d051ebe21a11fecc1cfcaa0c870d8dd564bf0c089eb4a40c9419fc14aa434c";

	public static final String CLIENT_SECRET = "ef0c7cb5a10a6c692eec0b863c954fffaa64066a4e78c5b72851222fa100aa6c";

	public String getResponseData(String url, String method, String version, String data) throws Exception {
		String responseString = null;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		if (version.equals("v2"))
			con.setRequestProperty("Authorization", "Basic " + authStringEnc);
		else if (version.equals("v3")) {
			OAuthClient client = new OAuthClient(new URLConnectionClient());

			OAuthClientRequest request = OAuthClientRequest.tokenLocation(TOKEN_REQUEST_URL)
					.setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET)
					.buildQueryMessage();

			String token = client.accessToken(request, OAuthJSONAccessTokenResponse.class).getAccessToken();

			con.addRequestProperty("Authorization", "Bearer " + token);
			

		}
	
		con.setRequestMethod(method);
		
		if(method.equals("POST") || method.equals("PUT"))
		{
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(data.getBytes());
			System.out.println(method+";;;;;;;;;;;;;;;;;;"+data);
			os.flush();
			os.close();
		}
		
		
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(method + " response...." + response.toString());
			responseString = response.toString();
		} else {
			System.out.println("GET request not worked");
		}
		return responseCode + "///" + responseString;
	}

	

	public List<String> getId(String boardId, String version) {
		List<String> playerId = new ArrayList();
		MyConnection myConnection = new MyConnection();
		try {

			
			String[] temp = myConnection
					.getResponseData("http://10.0.1.86/snl/rest/v1/board/" + boardId + ".json", "GET", version,"")
					.split("///");
			String responseCode = temp[0];
			String response = temp[1];
			JSONObject jsonFile = (JSONObject) (new JSONParser()).parse(response);
			JSONObject jsonObject = new JSONObject();
			jsonObject = (JSONObject) jsonFile.get("response");
			System.out.println(jsonObject.get("status"));

			JSONObject boardObject = new JSONObject();
			boardObject = (JSONObject) jsonObject.get("board");
			JSONArray playerArray = new JSONArray();
			playerArray = (JSONArray) boardObject.get("players");
			System.out.println("......." + playerArray.toString());
		
			for (int i = 0; i < playerArray.size(); i++) {
				try {

					JSONObject player = (JSONObject) playerArray.get(i);
					String id = ((Long) player.get("id")).toString();
					playerId.add(id);

				} catch (Exception e) {
					System.out.println(e);
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		for (String s : playerId)
			System.out.println(s);

		return playerId;
	}

}
