package test.rest.recentview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealSvcTestCase;
import test.rest.TestUtils;
import test.rest.testdata.DealDetailConstants;

/**
 * 최근 본 상품의 merge API
 */
public class GetMergeRecentViewTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/recentView/merge";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 public GetMergeRecentViewTest() {
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
	public void testGetMergeRecentView_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("toRecentViewKey", DealDetailConstants.QA_MEMBERNO);// 
		requestSpec.queryParam("fromRecentViewKey", "RECENTVIEWKEY-"+TestUtils.getUniqueString());//
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK", jsonResponse.get("data"));
	}
	
}
