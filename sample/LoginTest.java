package tmon.test.rest.media;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import tmon.test.rest.TMonAPITestCase;

public class LoginTest extends TMonAPITestCase{
	
	String deviceId = null;
	String mobileNo = null;
	String password = null;
	String deviceType = null;
	
	
	public LoginTest() {
		super();
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * 
	 * */
	@Test
	public void testLogin_기본() throws Exception {
		JSONObject reqBodyJSon = new JSONObject();
		reqBodyJSon.put("id", "01051126937");
		reqBodyJSon.put("password", "Test1@3$");
		reqBodyJSon.put("api_key", "ohSNiUGos5UOq6ahiedFFkff5mZvh3rR");
		reqBodyJSon.put("secret", "PTm+Xk6U");
//		reqBodyJSon.put("deviceId", "uuid");
		
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri("https://gateway-qa.ticketmonster.co.kr/api/direct/v1");
		builder.addHeader("Accept", "application/json");
		builder.addHeader("Content-Type", "application/json;charset=UTF-8");
//		builder.addHeader("deviceId", "uuid");
//		builder.addHeader("deviceType", "ANDROID");
		builder.addHeader("version", "4.5.3");
		
		builder.addHeader("permanent", "pid");
		builder.addHeader("platform", "ad");//ad, ios
		
//		builder.addQueryParam("pid", "PTm+Xk6U");
//		builder.addQueryParam("autologin", "true");
//		builder.addQueryParam("memberInfo", "false");
		
		RequestSpecification requestSpec = builder.build();
		requestSpec.body( reqBodyJSon.toJSONString());
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post("/auth/login").andReturn();
		
		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("createdBy"));
//		
//		assertTrue(jsonResponse.getString("email").contains("ramail@eoe.com"));
//		assertEquals("CREATED",jsonResponse.get("status"));
	}
	
}
