package test.rest.review;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 리뷰 추천을 취소한다.
 */
public class DeleteReviewRecommendTest extends AbstractBoardTestCase{
	public static final String apiPath = "/api/review/recommendation/{reviewNo}";
	String memberNo = DealDetailConstants.QA_MEMBERNO;
	String reviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	
	public DeleteReviewRecommendTest() {
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
	public void testDeleteReviewRecommend_필수입력값200ok() throws Exception {
		ReviewTestHelper.getInstance().postReviewRecommendation(reviewNo, memberNo);
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(memberNo));
		requestSpec.pathParam("reviewNo", reviewNo);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testDeleteReviewRecommend_존재하지않는리뷰번호() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.pathParam("reviewNo", "999999");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(503).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertNotNull(jsonResponse.get("exceptionCode"));
		assertEquals("구매후기가 존재하지 않습니다.", jsonResponse.get("exceptionMessage"));
		assertEquals("com.tmoncorp.api.board.common.exception.ReviewException", jsonResponse.get("exception"));
		assertEquals("105", jsonResponse.get("exceptionCode"));
	}
	
	@Test
	public void testDeleteReviewRecommend_멤버번호누락() throws Exception {
		ReviewTestHelper.getInstance().postReviewRecommendation(reviewNo, memberNo);
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewNo", "0000001");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(400).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertEquals("error.common.invalid.parameter", jsonResponse.get("exceptionMessage"));
	}
}
