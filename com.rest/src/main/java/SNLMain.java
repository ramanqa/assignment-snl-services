package com.googlerestapi.com.rest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.restassured.response.Response;

public class SNLMain {

	public static int getBoardId(Response response) throws ParseException
	{
		   String jsonData = response.asString();
		    JSONParser parser = new JSONParser();
		    JSONObject obj = (JSONObject)parser.parse(jsonData);
		    JSONObject resp=(JSONObject)obj.get("response");
		    JSONObject boards=(JSONObject)resp.get("board");
		    return(Integer.parseInt(boards.get("id").toString())); 
	}
	  public String getNewPlayerJsonString(int boardId)
	  {
		  JSONObject objj = new JSONObject();
			objj.put("board",boardId);
			JSONObject playerObj = new JSONObject();
			playerObj.put("name", "manu");
			objj.put("player",playerObj);
			return(objj.toJSONString());
	  }
	  public int getPlayerIdFromBoardListPlayers(ArrayList listPlayers,int playerId)
	  {
			int size  = listPlayers.size();
		    int hav = 0;
		 	for(int i=0;i<size;i++)
		    {
		   	int id =	(Integer) ((HashMap<String,Object>)listPlayers.get(i)).get("id");
		    if(id==playerId)
		    	hav = id;
		      break;
		    } 
		 	return hav;
	  }
	  public String getUpdatePlayerJsonObject()
	  {
		  JSONObject obj = new JSONObject();
		  JSONObject obj1 = new JSONObject();
		  obj1.put("name", "swapnil");
		  obj.put("player", obj1);
		  return(obj.toJSONString());
	  }
	
}
