package test.rest.review;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.APITestUtils;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;
import test.rest.testdata.PhotoReviewTestConstants;

/**
 * 리뷰를 추천한다.
 * http://boardapi.qa.tmon.co.kr/api-helper/node/apis
 */
public class PostRecommendReviewTest extends AbstractBoardTestCase{
	public static final String apiPath = "/api/review/recommendation/{reviewNo}";
	final String testDealNo = PhotoReviewTestConstants.PHOTOREVIEW_DEALNO;
	String reviewNo = null;
	
	 public PostRecommendReviewTest() {
		 super();
	 }
	 
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if(reviewNo!=null) {
			ReviewTestHelper.getInstance().deleteReviewRecommendation(reviewNo, DealDetailConstants.QA_MEMBERNO);
		}
		reviewNo =null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostRecommendReview_200ok() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", DealDetailConstants.QA_MEMBERNO);
		RequestSpecification requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", HalfClubTestConstants.TEST_REVIEW_NO);
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
//		assertEquals(3, jsonResponse.getInt("data"));
	}
	
	@Test
	public void testPostRecommendReview_자신이작성한리뷰추천시도() throws Exception {
		String mNo = DealDetailConstants.QA_MEMBERNO;
		reviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
		try {
			ReviewTestHelper.getInstance().deleteReviewRecommendation(reviewNo, mNo);
		}catch(Exception ignore) {
		}
		
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", mNo);
		RequestSpecification requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", reviewNo);
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
//		assertEquals(3, jsonResponse.getInt("data"));
	}
	
	@Test
	public void testPostRecommendReview_중복리뷰추천시도() throws Exception {
		String mNo = DealDetailConstants.QA_MEMBERNO;
		reviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
		try {
			ReviewTestHelper.getInstance().deleteReviewRecommendation(reviewNo, mNo);
		}catch(Exception ignore) {
		}
		
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", mNo);
		RequestSpecification requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", reviewNo);
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
		int firstCount = jsonResponse.getInt("data");
		
		builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", mNo);
		requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", reviewNo);
		requestSpec.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(503).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertNotNull(jsonResponse.get("exceptionCode"));
		assertEquals("이미 추천한 구매후기입니다.", jsonResponse.get("exceptionMessage"));
		assertEquals("com.tmoncorp.api.board.common.exception.ReviewException",jsonResponse.get("exception") );
		assertEquals("108",jsonResponse.get("exceptionCode") );
	}
	
	@Test
	public void testPostReviewRecommendation_멤버번호누락() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("reviewNo", PhotoReviewTestConstants.PHOTOREVIEW_REVIEWNO);
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(400).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exception"));
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertEquals("com.tmoncorp.core.exception.ApiException", jsonResponse.get("exception"));
		assertEquals("error.common.invalid.parameter", jsonResponse.get("exceptionMessage"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostRecommendReview_존재하지않는리뷰번호() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.pathParam("reviewNo", "9999999");
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
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertNotNull(jsonResponse.get("exceptionCode"));
		assertEquals("구매후기가 존재하지 않습니다.", jsonResponse.get("exceptionMessage"));
		assertEquals("com.tmoncorp.api.board.common.exception.ReviewException", jsonResponse.get("exception"));
		assertEquals("105", jsonResponse.get("exceptionCode"));
	}
}
