package test.rest.review;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealDetailTestCase;

/**
 * 
 */
public class PostGetReviewListTest extends AbstractDealDetailTestCase{
	final String apiPath = "/api/review/list/{dealSrl}";
	final String testDealNo = "1399733994";
	
	 public PostGetReviewListTest() {
		 super();
		 RestAssured.baseURI = "http://boardapi.qa.tmon.co.kr";
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
	public void testPostGetReviewList_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", this.testDealNo);
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		int dataCount = jsonResponse.getInt("data.size");
		assertTrue(dataCount>0);
		assertNotNull(jsonResponse.get("data[0].reviewSrl"));
		assertNotNull(jsonResponse.get("data[0].mainBuySrl"));
		assertNotNull(jsonResponse.get("data[0].mainDealSrl"));
		assertNotNull(jsonResponse.get("data[0].memberSrl"));
		assertNotNull(jsonResponse.get("data[0].contents"));
		assertNotNull(jsonResponse.get("data[0].status"));
		assertNotNull(jsonResponse.get("data[0].displayYn"));
		assertNotNull(jsonResponse.get("data[0].recommendCnt"));
		assertNotNull(jsonResponse.get("data[0].reportCnt"));
		assertNotNull(jsonResponse.get("data[0].buyOptionList"));
	}
}
