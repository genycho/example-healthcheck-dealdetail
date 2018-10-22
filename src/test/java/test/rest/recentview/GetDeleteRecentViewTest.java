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
 * 
 */
public class GetDeleteRecentViewTest extends AbstractDealSvcTestCase{
	public static final String apiPath = "/api/recentView/remove/contents";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	
	 public GetDeleteRecentViewTest() {
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
	public void testGetDeleteRecentView_DEALTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", "nologin"+TestUtils.getUniqueString());
		requestSpec.queryParam("removeRecentViewContentsList", "DEAL:"+testDealNo);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK", jsonResponse.get("data"));
	}
}
