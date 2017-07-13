package com.web_snl.SNL_project;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import org.testng.annotations.Test;
import com.jayway.restassured.http.ContentType;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.restassured.response.Response;


import groovyjarjarantlr.collections.List;

import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.restassured.RestAssured.*;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import static io.restassured.RestAssured.*;
//import static io.restassured.path.json.JsonPath.from;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.testng.annotations.Test;

public class SNL {
	
	public int board_id;
	public int player_id;
	public void detail(int pid)
	{
		player_id=pid;
	}
	
	
  @Test
  public void Test_Response_code() throws MalformedURLException 
  {
	  URL url1 = new URL("http://10.0.1.86/snl/soap/v2/wsdl");
		//Response res=get(url1);
		given().when().get(url1).then().statusCode(200);
  }
  
  
  
  	@Test(priority=1)
  	public void create_board_with_player() throws UnsupportedEncodingException
  	{
  		Integer pid;
  		HttpClient httpClient = new DefaultHttpClient();
  		Response response1=get("http://10.0.1.86/snl/rest/v1/board/new.json");
  		final String  JSON_DOCUMENT	=	 response1.getBody().asString(); 
  		Integer result = parse(JSON_DOCUMENT).read("$.response.board.id");
  		System.out.println("@@@@@@@@@@@"+result);
  		try {
	        HttpPost postRequest = new HttpPost("http://10.0.1.86/snl/rest/v1/player.json");
	        postRequest.setHeader("Content-type", "application/json");
	        /*StringEntity entity = new StringEntity("{\"board\":\""+result+"\", \"player\": {\"name\": \"Shubham1\"}}");
	        postRequest.setEntity(entity);
	         pid = parse(JSON_DOCUMENT).read("$.response.board.players[0].id");
	         System.out.println("&&&&&"+pid);*/
	         //detail(pid);
	        HttpResponse response = httpClient.execute(postRequest);
	        InputStream is = response.getEntity().getContent();
	        Reader reader = new InputStreamReader(is);
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        StringBuilder builder = new StringBuilder();
	        
  		} 
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
  		 board_id=result;
	     //System.out.println("#####"+player_id);
  		 given().when().get("http://10.0.1.86/snl/rest/v1/board/"+board_id+".json").then().statusCode(200);
	  }
  	

  
   
  
  
  /*Add more player*/
   
 @Test(priority=2)
  public void player() throws IOException 
  {
	
	  @SuppressWarnings("deprecation")
	HttpClient httpClient = new DefaultHttpClient();
	    try {
	        HttpPost postRequest = new HttpPost("http://10.0.1.86/snl/rest/v1/player.json");
	        postRequest.setHeader("Content-type", "application/json");
	         StringEntity entity2 = new StringEntity("{\"board\":\""+board_id+"\", \"player\": {\"name\": \"Chaman\"}}");
	        postRequest.setEntity(entity2);
	        HttpResponse response = httpClient.execute(postRequest);
	        InputStream is = response.getEntity().getContent();
	        Reader reader = new InputStreamReader(is);
	        Response response1=get("http://10.0.1.86/snl/rest/v1/board/"+board_id+".json") ;
	        final String  JSON_DOCUMENT	=	 response1.getBody().asString();
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        StringBuilder builder = new StringBuilder();
	        Integer pid = parse(JSON_DOCUMENT).read("$.response.board.players[0].id");
	         System.out.println("&&&&&"+pid);
	         detail(pid);
	        
	        
	        } 
	    catch (Exception ex) {
	        ex.printStackTrace();
	    }
  
	    }
  
		@Test(priority=7)
		public void Delete_player() throws IOException
		{
			URL url = new URL("http://10.0.1.86/snl/rest/player/"+player_id+".json");
			HttpURLConnection httpURLConnection = null;
			 httpURLConnection = (HttpURLConnection) url.openConnection();
			   /* httpURLConnection.setRequestProperty("Content-Type",
			                "application/x-www-form-urlencoded");*/
			    httpURLConnection.setRequestMethod("DELETE");
			    //System.out.println("@@@@@##@@@@"+httpURLConnection.getResponseCode());
			    Assert.assertEquals(404, httpURLConnection.getResponseCode());
			
			} 
		
	 
		  @Test(priority=6)
		  public void move() throws IOException 
		  {
			System.out.println("!!!!!!! "+board_id+ " !!!!!!!"+player_id);
			  URL url=new URL("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"");
			  Response res=get(url);
			  given().when().get(url).then().statusCode(200);
			  final String  JSON_DOCUMENT	=	 res.getBody().asString(); 
		 		Integer result = parse(JSON_DOCUMENT).read("$.response.player.position");
		 		
			  }
		
		  
		  /*Test for blank move i.e player position remains constant after a move */
		  
		  @Test(priority=5)
	      public void blank_move() throws IOException
	
	      {
			 
	    	Response response=get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"");
	    	final String  JSON_DOCUMENT	=	 response.getBody().asString(); 
	 		String ver=null;
	 		Integer result = parse(JSON_DOCUMENT).read("$.response.player.position");
	 		System.out.println("#######"+result);
	 		given().when().get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"").then().statusCode(200);
	 		Integer result2= parse(JSON_DOCUMENT).read("$.response.player.position");
	 		System.out.println("@@@@@@@@@@"+result);
	 		Integer Roll =parse(JSON_DOCUMENT).read("$.response.roll");
	 		System.out.println("@@@@@@@@@@"+Roll);
	 		/*check blank move*/
	 		if(result==0||result==2||(result>=7&&result<=14)||result==3||result==16||result==17||result==19||(result>=21&&result<=23)||result==25)
	 		{
	 			Assert.assertEquals(result,result2);
	 		}
	    }
	 		
		  
		  /*Test cases for ladder move*/ 
	 		@Test(priority=3)
	 		public void ladder_move() throws IOException
	 		
		      {
		    	  Response response=get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"");
		    	  final String  JSON_DOCUMENT	=	 response.getBody().asString(); 
		 		  String ver=null;
		 		  Integer pos1 = parse(JSON_DOCUMENT).read("$.response.player.position");
		 		  
		 		 given().when().get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"").then().statusCode(200);
		 		 
		 		Integer pos2= parse(JSON_DOCUMENT).read("$.response.player.position");
		 		Integer Roll =parse(JSON_DOCUMENT).read("$.response.roll");
		 		if(pos1==0&&Roll==1)
		 			Assert.assertEquals(17, "+pos2+");
		 		if(pos1==0&&Roll==4)
		 			Assert.assertEquals(16, "+pos2+");
		 		if(pos1==0&&Roll==5)
		 			Assert.assertEquals(7, "+pos2+");
		 		System.out.println("@@@@@@@@@@"+Roll);
		 		
	 		
	      }
	 		
	 		
	 		
	 		
	 		
	 		/*Test cases for Snake move*/ 
	 		 		
	 	    @Test(priority=4)
	 		public void Snake_move() throws IOException
	 		
		      {
		    	  Response response=get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"");
		    	  final String  JSON_DOCUMENT	=	 response.getBody().asString(); 
		 		  String ver=null;
		 		  Integer pos1 = parse(JSON_DOCUMENT).read("$.response.player.position");
		 		  
		 		 given().when().get("http://10.0.1.86/snl/rest/v1/move/"+board_id+".json?player_id="+player_id+"").then().statusCode(200);
		 		 
		 		Integer pos2= parse(JSON_DOCUMENT).read("$.response.player.position");
		 		Integer Roll =parse(JSON_DOCUMENT).read("$.response.roll");
		 		if(pos1==0&&Roll==6)
		 			Assert.assertEquals(3, "+pos2+");
		 	
		 		
		 		System.out.println("@@@@@@@@@@"+Roll);
		  		
	       }
		
	 	   
}

