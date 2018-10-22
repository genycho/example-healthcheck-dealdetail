package test.rest.dealdetail.halfclub;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class GetReviewForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/partner/{reviewSrl}";
	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	
	 public GetReviewForPartnerTest() {
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
	public void testGetReviewForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewSrl", testReviewNo);
		
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
		
		assertNotNull(jsonResponse.get("data.reviewSrl"));
		assertNotNull(jsonResponse.get("data.mainBuySrl"));
		assertNotNull(jsonResponse.get("data.mainDealSrl"));
		assertNotNull(jsonResponse.get("data.accountSrl"));
		assertNotNull(jsonResponse.get("data.memberSrl"));
		assertNotNull(jsonResponse.get("data.contents"));
		assertNotNull(jsonResponse.get("data.imageUseYn"));
		assertNotNull(jsonResponse.get("data.status"));
		assertNotNull(jsonResponse.get("data.displayYn"));
		assertNotNull(jsonResponse.get("data.useWaitYn"));
		assertNotNull(jsonResponse.get("data.catSrl"));
		assertNotNull(jsonResponse.get("data.recommendCnt"));
		assertNotNull(jsonResponse.get("data.reportCnt"));
		assertNotNull(jsonResponse.get("data.venderReportCnt"));
		assertNotNull(jsonResponse.get("data.dealGpoint"));
		assertNotNull(jsonResponse.get("data.deliveryGpoint"));
		assertNotNull(jsonResponse.get("data.whoCreate"));
		assertNotNull(jsonResponse.get("data.grade"));
		assertNotNull(jsonResponse.get("data.gradeName"));
		assertNotNull(jsonResponse.get("data.imageList"));
		assertNotNull(jsonResponse.get("data.buyOptionList"));
		assertNotNull(jsonResponse.get("data.mobileProductReview"));
		assertNotNull(jsonResponse.get("data.editable"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetReviewForPartner_존재하지않는리뷰번호() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewSrl", "00000001");
		
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
		assertNull(jsonResponse.get("data"));
	}
}
