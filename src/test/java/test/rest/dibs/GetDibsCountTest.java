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
public class GetDibsCountTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/dibs/count";
//	final String apiPath = "/api/direct/v2/dealsvcapi/dibs/count";
	final String mNo = DealDetailConstants.QA_MEMBERNO;
	
	 public GetDibsCountTest() {
		 super();
//		 RestAssured.baseURI = "https://gateway-qa.ticketmonster.co.kr";
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
	public void testGetDibsCount_MEMBER_200ok() throws Exception {
		RequestSpecification requestSpec = getGatewayRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		int dibsCount = (jsonResponse.getInt("data"));//0이상 
		assertTrue(dibsCount >=0);
	}
	
	@Test
	public void testGetDibsCount_NOMEMBERINFO() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		int dibsCount = (jsonResponse.getInt("data"));//0이상 
		assertTrue(dibsCount >=0);
	}
	
}
