package test.rest.dealdetail.tvon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.testdata.DealDetailConstants;

/**
 * 해당 딜이 라이브에 포함된 딜인지 여부를 조회한다
 */
public class GetLiveDealCheckTest extends AbstractMediaTestCase{
	final String apiPath = "/api/live/deals/{dealNo}/check";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 
	public GetLiveDealCheckTest() {
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
	@Ignore
	public void testGetLiveDealCheck_라이브딜조회_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealNo", testDealNo);
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
	.get(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals(true, String.valueOf(jsonResponse.getBoolean("data")));
	}
	
	@Test
	public void testGetLiveDealCheck_라이브딜아님_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealNo", testDealNo);
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.get(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals(false, jsonResponse.getBoolean("data"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetDealInfo_존재하지않는딜번호() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealNo", "0000000001");
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.get(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals(false, jsonResponse.getBoolean("data"));
	}
	
}
