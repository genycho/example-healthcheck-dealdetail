package test.rest.dealdetail.tvon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 * 해당 딜의 라이브 정보를 조회한다
 */
public class GetLiveDealInfoTest extends AbstractMediaTestCase{
	final String apiPath = "/api/live/deals/{dealNo}";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 
	public GetLiveDealInfoTest() {
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
	public void testGetLiveDealInfo_라이브딜조회_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealNo", 1461049730);
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
		assertTrue( jsonResponse.getInt("data.size()")>0);
		
		assertNotNull(jsonResponse.get("data[0].mediaNo"));
		assertNotNull(jsonResponse.get("data[0].mediaType"));
		assertNotNull(jsonResponse.get("data[0].captionInfo"));
		assertNotNull(jsonResponse.get("data[0].statisticsInfo"));
		assertNotNull(jsonResponse.get("data[0].optionInfo"));
		assertNotNull(jsonResponse.get("data[0].optionInfo.SHARE"));
		assertNotNull(jsonResponse.get("data[0].optionInfo.CHAT"));
		assertNotNull(jsonResponse.get("data[0].optionInfo.POPUP"));
		assertNotNull(jsonResponse.get("data[0].optionInfo.PUSH"));
		
		assertNotNull(jsonResponse.get("data[0].deals"));
		
		assertNotNull(jsonResponse.get("data[0].auditInfo"));
		assertNotNull(jsonResponse.get("data[0].ownerInfo"));
		assertNotNull(jsonResponse.get("data[0].resourceInfo"));
		assertNotNull(jsonResponse.get("data[0].useYn"));
		assertNotNull(jsonResponse.get("data[0].liveStatus"));
		assertNotNull(jsonResponse.get("data[0].liveTimeInfo"));
		
		assertNotNull(jsonResponse.get("data[0].livePlanInfo"));
		assertNotNull(jsonResponse.get("data[0].useablePlan"));
	}
	
	@Test
	public void testGetLiveDealInfo_라이브딜아님_200ok() throws Exception {
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
		assertEquals(0, jsonResponse.getInt("data.size()"));
	}
	
}
