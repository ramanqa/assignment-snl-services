/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testsnl;

import com.jayway.restassured.path.json.JsonPath;

/**
 *
 * @author piyusharora
 */
public class hello {
    
     String response=RestAssured.given()
 				.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
 						"f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33", "client-Secret",
 						"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
 				.auth().preemptive()
 				.basic("f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33",
 						"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
 				.when().post("http://10.0.1.86/snl/oauth/token").asString();
 		JsonPath jsonPath = new JsonPath(response);
 		accessToken = jsonPath.getString("access_token");
                System.out.println(accessToken);
    
}
