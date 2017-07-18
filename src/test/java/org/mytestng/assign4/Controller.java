package org.mytestng.assign4;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Controller {

	String[] names = { "nishant1", "nishant2", "nishant3", "nishant4" };
	String[] updatedNames = {"nishantAgarwal1","NishantAgarwal2","NishantAgarwal3","NishhantAgarwal4"};
	
	String boardId;
	MyConnection myConnection;
	
	
	public static void createFile(String response,String version,String name)throws Exception
	{

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(response);
		String prettyJsonString = gson.toJson(je);
		
		PrintWriter writer = new PrintWriter( version+name, "UTF-8");
        writer.println(prettyJsonString);
        writer.close();
	}
	
	
	void getBoardTest(String newBoardUrl,String version) throws Exception {
		myConnection = new MyConnection();
		System.out.println("CREATING NEW BOARD........."+version);

		String[] temp = myConnection.getResponseData(newBoardUrl,"GET",version,"").split("///");
		

		String responseCode = temp[0];
		String response = temp[1];

		JSONObject jsonFile = (JSONObject) (new JSONParser()).parse(response);

		System.out.println("...........................");
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) jsonFile.get("response");
		System.out.println(jsonObject.get("status"));
		String boardStatus = jsonObject.get("status").toString();

		JSONObject boardObject = new JSONObject();
		boardObject = (JSONObject) jsonObject.get("board");
		System.out.println(boardObject.get("id"));
		boardId = boardObject.get("id").toString();

		assertThat(boardStatus).isEqualTo("1");

		Controller.createFile(response, version,"newCreated.json");
	
	}
	

	void addPlayers(String particularBoardUrl,String addPlayerUrl , String version) throws Exception {

		myConnection = new MyConnection();

		System.out.println("ADDING NEW PLAYERS.........");
	
		
		
		for (int i = 0; i < names.length; i++) {

			myConnection.getResponseData(addPlayerUrl, "POST",version,
					"{\"board\":\"" + boardId + "\", \"player\":{\"name\": \"" + names[i] + "\"}}" );

			System.out.println("{\"board\":\"" + boardId + "\", \"player\":{\"name\": \"" + names[i] + "\"}}.........."+addPlayerUrl);
			
			String[] temp = myConnection
					.getResponseData(particularBoardUrl + boardId + ".json", "GET",version,"").split("///");
			String responseCode = temp[0];
			String response = temp[1];

			JSONObject jsonFile = (JSONObject) (new JSONParser()).parse(response);
			JSONObject jsonObject = new JSONObject();
			jsonObject = (JSONObject) jsonFile.get("response");
			System.out.println(jsonObject.get("status"));
			String playerStatus = jsonObject.get("status").toString();

			assertThat(playerStatus).isEqualTo("1");

			assertThat(Integer.parseInt(responseCode)).isEqualTo(200);
			
			Controller.createFile(response, version,"addingPlayer"+i+".json");

	

		}
	}


	void updatingPlayers(String particularBoardUrl,String addPlayerUrl , String version) throws Exception {

		myConnection = new MyConnection();

		System.out.println("Updating old PLAYERS.........");
	
		List<String> playerId = new ArrayList<String>();
		playerId = myConnection.getId(boardId,version);
		for (int i = 0; i < playerId.size(); i++) {

			myConnection.getResponseData("http://10.0.1.86/snl/rest/v1/player/"+playerId.get(i)+".json", "PUT", version,
					"{\"board\":\"" + boardId + "\", \"player\":{\"name\": \"" + updatedNames[i] + "\"}}");

			String[] temp = myConnection
					.getResponseData(particularBoardUrl + boardId + ".json", "GET",version,"").split("///");
			
			String responseCode = temp[0];
			String response = temp[1];

			JSONObject jsonFile = (JSONObject) (new JSONParser()).parse(response);
			JSONObject jsonObject = new JSONObject();
			jsonObject = (JSONObject) jsonFile.get("response");
			System.out.println(jsonObject.get("status"));
			String playerStatus = jsonObject.get("status").toString();

			assertThat(playerStatus).isEqualTo("1");

			assertThat(Integer.parseInt(responseCode)).isEqualTo(200);
		
	        Controller.createFile(response, version,"updatingPlayer"+i+".json");


		}
	}
	
	
	


	public void movePlayer(String movePlayerUrl , String version) {
		List<String> playerId = new ArrayList<String>();
		try {
			MyConnection myConnection = new MyConnection();
			playerId = myConnection.getId(boardId,version);

			for (int i = 0; i < playerId.size(); i++) {
				String[] temp = myConnection
						.getResponseData(movePlayerUrl + boardId + ".json?player_id="+playerId.get(i)   ,   "GET" ,version,"").split("///");
				String responseCode = temp[0];
				String response = temp[1];

				System.out.println("Response Code :: " + responseCode);
				System.out.println("response . .           " + response);

				assertThat(Integer.parseInt(responseCode)).isEqualTo(200);
				
		        Controller.createFile(response, version,"movingPlayer"+i+".json");


			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	
	void getList(String particularBoardUrl, String version) throws Exception {
		myConnection = new MyConnection();
		System.out.println("CHECKING FOR BOARD LIST..............");

		String[] temp = myConnection.getResponseData(particularBoardUrl + boardId + ".json", "GET",version,"")
				.split("///");
		String responseCode = temp[0];
		String response = temp[1];

		System.out.println("Response Code :: " + responseCode);
		System.out.println("response . .           " + response);

		assertThat(Integer.parseInt(responseCode)).isEqualTo(200);
		
        Controller.createFile(response, version,"gettingBoard.json");


	}

	 void deletePlayer(String deletePlayerUrl , String version) throws IOException {
	 myConnection = new MyConnection();
	 System.out.println("DELETING PLAYERS...........");

	 List<String> playerId =new ArrayList<String>();
	 try {
	
	playerId = myConnection.getId(boardId,version); 
	 for (int i = 0; i < playerId.size(); i++) {
	 try {
	
	 String id = playerId.get(i);
	
	 String deleteResponse = myConnection
	 .getResponseData(deletePlayerUrl + id + ".json",
	 "DELETE",version,"")
	 .split("///")[0];
	
	 assertThat(Integer.parseInt(deleteResponse)).isEqualTo(HttpURLConnection.HTTP_OK);
	 System.out.println("player remove with id......" + id);
	 
     Controller.createFile(deleteResponse, version,"deletingPlayer"+i+".json");

	 
	 } catch (Exception e) {
	 System.out.println(e);
	 }
	 }
	
	 } catch (Exception e) {
	 System.out.println(e);
	 }
	 }
	

	 void deleteBoard(String deleteBoardUrl ,String version) throws IOException {
	 myConnection = new MyConnection();
	 System.out.println("DELETING BOARD.....ooooooooooooooooooooooooooo         pppppppppppp.....");
	
	 System.out.println();
	
	 try {
	 String deleteResponse = myConnection
	 .getResponseData(deleteBoardUrl + boardId + ".json",
	 "DELETE",version,"")
	 .split("///")[0];
	
	 assertThat(Integer.parseInt(deleteResponse)).isEqualTo(HttpURLConnection.HTTP_OK);
	 System.out.println("delete board with board id...." + boardId);
	 
	 
     Controller.createFile(deleteResponse, version,"deletingBoard.json");

	 } catch (Exception e) {
	 }
	
	 }

}
