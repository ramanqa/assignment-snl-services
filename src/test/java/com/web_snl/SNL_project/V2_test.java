package com.web_snl.SNL_project;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.jayway.restassured.http.ContentType;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseOptions;

import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.restassured.RestAssured.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.testng.annotations.Test;
public class V2_test {
 	
	public int board_id;
	public int player_id;
	public void detail(int pid)
	{
		player_id=pid;
	}
	
	public void authenticate_post(String u) throws IOException
	{
		  URL url = new URL(u);
		  String authStr = "su"+":"+"root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
					 authStr.getBytes(Charset.forName("ISO-8859-1")));
		  String authEncoded = new String(encodedAuth);
		  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		  connection.setRequestProperty("Authorization", "Basic "+authEncoded);
	}
	
    @Test
    public void Http_Auth_test() throws ClientProtocolException, IOException
     {
	   HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/new.json");
	   String auth = "su" + ":" + "root_pass";
	   byte[] encodedAuth = Base64.getEncoder().encode(
	   auth.getBytes(Charset.forName("ISO-8859-1")));
	   String authHeader = "Basic " + new String(encodedAuth);
	   request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	   HttpClient client = HttpClientBuilder.create().build();
	   HttpResponse response = client.execute(request);
	   int statusCode = response.getStatusLine().getStatusCode();
	   Assert.assertEquals(statusCode, 200);
     }
  
    @Test
    public void Http_Auth_test1() throws ClientProtocolException, IOException
    {
	  	HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/new.json");
	  	String auth = "su" + ":" + "root_pass";
	  	byte[] encodedAuth = Base64.getEncoder().encode(
	    auth.getBytes(Charset.forName("ISO-8859-1")));
	  	String authHeader = "Basic " + new String(encodedAuth);
	  	request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	  	HttpClient client = HttpClientBuilder.create().build();
	  	HttpResponse response = client.execute(request);
	  	HttpEntity entity = response.getEntity();
	  	//System.out.println("Entity:"+entity);
          String response1 = EntityUtils.toString(entity);
          Integer bid = parse(response1).read("$.response.board.id");
	      System.out.println("&&&&&"+bid);
	      board_id=bid;
	   int statusCode = response.getStatusLine().getStatusCode();
	   Assert.assertEquals(statusCode, 200);
  }
  

		
  		@Test
	     public void add_player_Test() throws IOException, AuthenticationException
			  {
  			CloseableHttpClient client = HttpClients.createDefault();
  		    HttpPost httpPost = new HttpPost("http://10.0.1.86/snl/rest/v2/player.json");
            httpPost.setEntity(new StringEntity("{\"board\":\""+board_id+"\", \"player\": {\"name\": \"Chaman\"}}"));
  		    UsernamePasswordCredentials creds
  		      = new UsernamePasswordCredentials("su", "root_pass");
  		    httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
  		    CloseableHttpResponse response = client.execute(httpPost);
  		    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
  		    
  		  /*   Get player id*/  
  		    HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/"+board_id+".json");
  		    String auth = "su" + ":" + "root_pass";
  		    byte[] encodedAuth = Base64.getEncoder().encode(
  		    auth.getBytes(Charset.forName("ISO-8859-1")));
  		    String authHeader = "Basic " + new String(encodedAuth);
  		    request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
  		    HttpClient client1 = HttpClientBuilder.create().build();
  		    HttpResponse response1 = client1.execute(request);
  		    HttpEntity entity = response1.getEntity();
  		    String response2 = EntityUtils.toString(entity);
  	        Integer pid = parse(response2).read("$.response.board.players[0].id");
  		    System.out.println("&&&&&"+pid);
  		    player_id=pid;
  			 }
  
  
  	   //  @Test(priority=7)
  		public void Delete_player() throws IOException
  		{
  	      HttpDelete delete = new HttpDelete(("http://10.0.1.86/snl/rest/v1/player/"+player_id+".json"));
  	      delete.setHeader("Accept", "application/json");
  	      delete.setHeader("Content-type", "application/json");
  	      String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  delete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(delete);
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
  	  	}
  	     
  	     
  	     
  	   //@Test(priority=8)
 		public void Delete_board() throws IOException
 		{
 	      HttpDelete delete = new HttpDelete(("http://10.0.1.86/snl/rest/v1/board/"+board_id+".json"));
 	      delete.setHeader("Accept", "application/json");
 	      delete.setHeader("Content-type", "application/json");
 	      String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  delete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(delete);
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
 	  	}
  	    	 
  	   	 
  	    @Test(priority=6)
	  public void move() throws IOException, AuthenticationException 
	  {			  
		  
		  HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/move/"+board_id+".json?player_id="+player_id+"");
		  String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		    auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		   
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(request);
		  
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
	 }
  	    
  	    @Test
  	  public void update_player() throws IOException, AuthenticationException 
  	  {
  	    	CloseableHttpClient client = HttpClients.createDefault();
  	    	HttpPut httpPost = new HttpPut("http://10.0.1.86/snl/rest/v2/player/"+player_id+".json");
  	    	//{\“player\”:{\“name”\:\"sharma\"}}
            httpPost.setEntity(new StringEntity("{ \"player\": {\"name\": \"sharma\"}}"));
  		    UsernamePasswordCredentials creds
  		      = new UsernamePasswordCredentials("su", "root_pass");
  		    httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
  		    CloseableHttpResponse response = client.execute(httpPost);
		    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
  		    
  		    
  	  
  	  }
  	    
		
}
package com.web_snl.SNL_project;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import org.testng.annotations.Test;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.jayway.restassured.http.ContentType;
import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseOptions;

import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.restassured.RestAssured.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.testng.annotations.Test;
public class V2_test {
 	
	public int board_id;
	public int player_id;
	public void detail(int pid)
	{
		player_id=pid;
	}
	
	public void authenticate_post(String u) throws IOException
	{
		  URL url = new URL(u);
		  String authStr = "su"+":"+"root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
					 authStr.getBytes(Charset.forName("ISO-8859-1")));
		  String authEncoded = new String(encodedAuth);
		  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		  connection.setRequestProperty("Authorization", "Basic "+authEncoded);
	}
	
    @Test
    public void Http_Auth_test() throws ClientProtocolException, IOException
     {
	   HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/new.json");
	   String auth = "su" + ":" + "root_pass";
	   byte[] encodedAuth = Base64.getEncoder().encode(
	   auth.getBytes(Charset.forName("ISO-8859-1")));
	   String authHeader = "Basic " + new String(encodedAuth);
	   request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	   HttpClient client = HttpClientBuilder.create().build();
	   HttpResponse response = client.execute(request);
	   int statusCode = response.getStatusLine().getStatusCode();
	   Assert.assertEquals(statusCode, 200);
     }
  
    @Test
    public void Http_Auth_test1() throws ClientProtocolException, IOException
    {
	  	HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/new.json");
	  	String auth = "su" + ":" + "root_pass";
	  	byte[] encodedAuth = Base64.getEncoder().encode(
	    auth.getBytes(Charset.forName("ISO-8859-1")));
	  	String authHeader = "Basic " + new String(encodedAuth);
	  	request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	  	HttpClient client = HttpClientBuilder.create().build();
	  	HttpResponse response = client.execute(request);
	  	HttpEntity entity = response.getEntity();
	  	//System.out.println("Entity:"+entity);
          String response1 = EntityUtils.toString(entity);
          Integer bid = parse(response1).read("$.response.board.id");
	      System.out.println("&&&&&"+bid);
	      board_id=bid;
	   int statusCode = response.getStatusLine().getStatusCode();
	   Assert.assertEquals(statusCode, 200);
  }
  

		
  		@Test
	     public void add_player_Test() throws IOException, AuthenticationException
			  {
  			CloseableHttpClient client = HttpClients.createDefault();
  		    HttpPost httpPost = new HttpPost("http://10.0.1.86/snl/rest/v2/player.json");
            httpPost.setEntity(new StringEntity("{\"board\":\""+board_id+"\", \"player\": {\"name\": \"Chaman\"}}"));
  		    UsernamePasswordCredentials creds
  		      = new UsernamePasswordCredentials("su", "root_pass");
  		    httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
  		    CloseableHttpResponse response = client.execute(httpPost);
  		    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
  		    
  		  /*   Get player id*/  
  		    HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/board/"+board_id+".json");
  		    String auth = "su" + ":" + "root_pass";
  		    byte[] encodedAuth = Base64.getEncoder().encode(
  		    auth.getBytes(Charset.forName("ISO-8859-1")));
  		    String authHeader = "Basic " + new String(encodedAuth);
  		    request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
  		    HttpClient client1 = HttpClientBuilder.create().build();
  		    HttpResponse response1 = client1.execute(request);
  		    HttpEntity entity = response1.getEntity();
  		    String response2 = EntityUtils.toString(entity);
  	        Integer pid = parse(response2).read("$.response.board.players[0].id");
  		    System.out.println("&&&&&"+pid);
  		    player_id=pid;
  			 }
  
  
  	   //  @Test(priority=7)
  		public void Delete_player() throws IOException
  		{
  	      HttpDelete delete = new HttpDelete(("http://10.0.1.86/snl/rest/v1/player/"+player_id+".json"));
  	      delete.setHeader("Accept", "application/json");
  	      delete.setHeader("Content-type", "application/json");
  	      String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  delete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(delete);
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
  	  	}
  	     
  	     
  	     
  	   //@Test(priority=8)
 		public void Delete_board() throws IOException
 		{
 	      HttpDelete delete = new HttpDelete(("http://10.0.1.86/snl/rest/v1/board/"+board_id+".json"));
 	      delete.setHeader("Accept", "application/json");
 	      delete.setHeader("Content-type", "application/json");
 	      String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		  auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  delete.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(delete);
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
 	  	}
  	    	 
  	   	 
  	    @Test(priority=6)
	  public void move() throws IOException, AuthenticationException 
	  {			  
		  
		  HttpGet request = new HttpGet("http://10.0.1.86/snl/rest/v2/move/"+board_id+".json?player_id="+player_id+"");
		  String auth = "su" + ":" + "root_pass";
		  byte[] encodedAuth = Base64.getEncoder().encode(
		    auth.getBytes(Charset.forName("ISO-8859-1")));
		  String authHeader = "Basic " + new String(encodedAuth);
		  request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		   
		  HttpClient client = HttpClientBuilder.create().build();
		  HttpResponse response = client.execute(request);
		  
		  Assert.assertEquals(200, response.getStatusLine().getStatusCode());
	 }
  	    
  	    @Test
  	  public void update_player() throws IOException, AuthenticationException 
  	  {
  	    	CloseableHttpClient client = HttpClients.createDefault();
  	    	HttpPut httpPost = new HttpPut("http://10.0.1.86/snl/rest/v2/player/"+player_id+".json");
  	    	//{\“player\”:{\“name”\:\"sharma\"}}
            httpPost.setEntity(new StringEntity("{ \"player\": {\"name\": \"sharma\"}}"));
  		    UsernamePasswordCredentials creds
  		      = new UsernamePasswordCredentials("su", "root_pass");
  		    httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
  		    CloseableHttpResponse response = client.execute(httpPost);
		    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
  		    
  		    
  	  
  	  }
  	    
		
}
