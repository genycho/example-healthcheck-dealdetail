package test.rest.dealdetail.halfclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
public class PostReportReviewForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/partner/report/{reviewSrl}";
	
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	final String testPartnerNo = HalfClubTestConstants.PARTNER_NO;
	
	 public PostReportReviewForPartnerTest() {
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
	public void testPostReportReviewForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewSrl", testReviewNo);
		requestSpec.queryParam("reportReason", 2);
		requestSpec.queryParam("reportContent", "TEST REPORT CONTENTS");
		
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
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.message"));
		assertNotNull(jsonResponse.get("data.status"));
		System.out.println("신고하기 수행결과 Info - "+jsonResponse.get("data.message"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostReportReviewForPartner_존재하지않는리뷰번호() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewSrl", "000001");
		requestSpec.queryParam("reportReason", 1);
		requestSpec.queryParam("reportContent", "TEST REPORT CONTENTS");
		
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
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(false, jsonResponse.get("data.result"));
		
		assertNotNull(jsonResponse.get("data.message"));
		assertNotNull(jsonResponse.get("data.status"));
		assertEquals("구매후기가 존재하지 않습니다.", jsonResponse.get("data.message"));
		assertEquals("NOT_EXIST_REVIEW", jsonResponse.get("data.status"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostReportReviewForPartner_필수입력파라미터누락() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewSrl", testReviewNo);
//		requestSpec.queryParam("reportReason", 1);
		requestSpec.queryParam("reportContent", "TEST REPORT CONTENTS");
		
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(503).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertTrue(jsonResponse.get("exceptionMessage").toString().contains("Required int parameter 'reportReason' is not present"));
	}
}
