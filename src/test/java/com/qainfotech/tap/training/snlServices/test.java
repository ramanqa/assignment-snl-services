package com.qainfotech.tap.training.snlServices;

	
	import org.testng.annotations.Test;

	import com.jayway.restassured.RestAssured;
	import com.jayway.restassured.path.json.JsonPath;
	import com.jayway.restassured.response.Response;

	public class test {

		String accessToken;

		@Test
		public void before() {

			RestAssured.baseURI = "http://10.0.1.86/snl";
			String response = RestAssured.given()
					.parameters("username", "su", "password", "root_pass", "grant_type", "client_credentials", "client_id",
							"f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33", "client-Secret",
							"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
					.auth().preemptive()
					.basic("f70d06f64a402b44cb8dabfbecf43e17872a85c0a1108d3622141e906d826c33",
							"934d7ffdb0212d466a5fe4cee90c8a826b71e4d55a79bd6d38fb483b440e4c7c")
					.when().post("http://10.0.1.86/snl/oauth/token").asString();
			
			JsonPath jsonPath = new JsonPath(response);
			//System.out.println("");
			String accessToken = jsonPath.getString("access_token");
			System.out.println(accessToken);

		}
}
