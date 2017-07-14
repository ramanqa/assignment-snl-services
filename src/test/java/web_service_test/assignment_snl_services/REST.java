package web_service_test.assignment_snl_services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;



 

public class REST
{
    public REST() throws ClientProtocolException, IOException {
       
        URL url = new URL("http://10.0.1.86/snl/oauth/token");
     
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("client_id", "29bdde51cc24c0909c400b1cc5c9c5021dc3968743ae2d04f0804bd2109d4161");
        conn.setRequestProperty("client_secret", "29edccea9b72d7d771fa2c5f966a37424f3934b814f79f264e9707a1ef27cf13");
        conn.setRequestProperty("grant_type", "password");
        conn.setRequestProperty("username", "su");
        conn.setRequestProperty("password", "root_pass");
        conn.setRequestProperty("Accept", "application/json");
    	
        
        //conn.connect();
        System.out.println(conn.getResponseMessage());
        
    
    }
    
    
    public static void main(String...S) throws ClientProtocolException, IOException
    {
    	new REST();
    }
}