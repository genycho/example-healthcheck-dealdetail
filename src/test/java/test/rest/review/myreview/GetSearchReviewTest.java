package test.rest.review.myreview;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import test.rest.testdata.PhotoReviewTestConstants;

/**
 * 	리뷰번호 단건 리뷰
 */
public class GetSearchReviewTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/search/{reviewNo}";
	final String testDealNo = PhotoReviewTestConstants.PHOTOREVIEW_DEALNO;

	public GetSearchReviewTest() {
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
	public void testGetSearchReview_필수입력값200ok() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
//		builder.addHeader("X-Tmon-Member-No", mNo );
//		builder.addHeader("X-TMon-Agent", "android");
		RequestSpecification requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", PhotoReviewTestConstants.PHOTOREVIEW_REVIEWNO);
		
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.reviewNo"));
		assertNotNull(jsonResponse.get("data.mainBuyNo"));
		assertNotNull(jsonResponse.get("data.mainDealNo"));
		assertNotNull(jsonResponse.get("data.accountNo"));
		assertNotNull(jsonResponse.get("data.memberNo"));
		assertNotNull(jsonResponse.get("data.memberGrade"));
		assertNotNull(jsonResponse.get("data.contents"));
		assertNotNull(jsonResponse.get("data.catSrl"));
		assertNotNull(jsonResponse.get("data.recommendCnt"));
		assertNotNull(jsonResponse.get("data.reportCnt"));
		assertNotNull(jsonResponse.get("data.venderReportCnt"));
		assertNotNull(jsonResponse.get("data.dealGpoint"));
		assertNotNull(jsonResponse.get("data.deliveryGpoint"));
		assertNotNull(jsonResponse.get("data.whoCreate"));
		assertNotNull(jsonResponse.get("data.createDt"));
		assertNotNull(jsonResponse.get("data.whoUpdate"));
		assertNotNull(jsonResponse.get("data.imageInfo"));
		assertNotNull(jsonResponse.get("data.optionDealInfo"));
	}
	
	@Test
	public void testGetSearchReview_존재하지않는리뷰번호() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
//		builder.addHeader("X-Tmon-Member-No", mNo );
//		builder.addHeader("X-TMon-Agent", "android");
		RequestSpecification requestSpec = builder.build();
		requestSpec.pathParam("reviewNo", "9999999");
		
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNull(jsonResponse.get("data"));
	}
	
}
