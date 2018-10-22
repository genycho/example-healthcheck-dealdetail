package test.rest.dibs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealSvcTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * memberNo 기준 찜목록 갯수를 가져오는 API 입니다.
 */
public class GetContentDibsCountTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/dibs/{contentId}/count";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	final String testPlanNo = DealDetailConstants.PLAN_NO;
	
	 public GetContentDibsCountTest() {
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
	public void testGetContentDibsCount_DEALTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "DEAL");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	
	@Test
	public void testGetContentDibsCount_PLANTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("contentId", testPlanNo);
		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	
	@Test
	public void testGetContentDibsCount_INVALIDDIBSTYPE() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "INVALIDTYPE");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(400).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertTrue(jsonResponse.get("exceptionMessage").toString().contains("No enum constant com.tmoncorp.dealsvc.api.dibs.constant.DibsType.INVALIDTYPE"));
	}
	
}
