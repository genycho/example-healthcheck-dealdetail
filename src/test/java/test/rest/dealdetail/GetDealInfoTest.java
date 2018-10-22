package test.rest.dealdetail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealDetailTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * 
 */
public class GetDealInfoTest extends AbstractDealDetailTestCase{
	final String apiPath = "/api/deals/{dealNo}";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 public GetDealInfoTest() {
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
	public void testGetDealInfo_200ok() throws Exception {
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
		
		assertNotNull(jsonResponse.get("data.title"));
		assertNotNull(jsonResponse.get("data.titleName"));
		assertNotNull(jsonResponse.get("data.optionCnt"));
		assertNotNull(jsonResponse.get("data.branchCount"));
		assertNotNull(jsonResponse.get("data.dealType"));
		assertNotNull(jsonResponse.get("data.dealNo"));
		assertEquals(testDealNo, String.valueOf((jsonResponse.getLong("data.dealNo"))));
		assertNotNull(jsonResponse.get("data.mainOptionNo"));
		assertNotNull(jsonResponse.get("data.dealMaxNo"));
		assertNotNull(jsonResponse.get("data.salesCategoryNo"));
		assertNotNull(jsonResponse.get("data.productType"));
		assertNotNull(jsonResponse.get("data.refundType"));
		assertNotNull(jsonResponse.get("data.statusType"));
		
		assertNotNull(jsonResponse.get("data.deliveryInfo"));
		
		assertNotNull(jsonResponse.get("data.deliveryInfo.deliveryType"));
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
			.get("/api/deals/{dealNo}").andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNull(jsonResponse.get("data"));
	}
	
}
