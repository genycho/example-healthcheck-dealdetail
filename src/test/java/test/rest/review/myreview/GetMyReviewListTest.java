package test.rest.review.myreview;

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
import test.rest.testdata.PhotoReviewTestConstants;

/**
 * 나의 리뷰 목록 조회
 */
public class GetMyReviewListTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/my/list";
	final String testDealNo = PhotoReviewTestConstants.PHOTOREVIEW_DEALNO;

	public GetMyReviewListTest() {
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
	public void testGetMyReviewList_필수입력값200ok() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", DealDetailConstants.QA_MEMBERNO);
//		builder.addHeader("X-TMon-Agent", "ios");
		RequestSpecification requestSpec = builder.build();
//		requestSpec.queryParam("useDealInfo", true);
//		requestSpec.queryParam("offset", 3);
//		requestSpec.queryParam("page", 1);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.nextEntity"));
		assertNotNull(jsonResponse.get("data.list"));
		assertNotNull(jsonResponse.getInt("data.list.size()")>0);
		assertNotNull(jsonResponse.get("data.list[0].reviewNo"));
		assertNotNull(jsonResponse.get("data.list[0].mainBuyNo"));
		assertNotNull(jsonResponse.get("data.list[0].mainDealNo"));
		assertNotNull(jsonResponse.get("data.list[0].accountNo"));
		assertNotNull(jsonResponse.get("data.list[0].memberNo"));
		assertNotNull(jsonResponse.get("data.list[0].memberGrade"));
		assertNotNull(jsonResponse.get("data.list[0].title"));
		assertNotNull(jsonResponse.get("data.list[0].contents"));
		assertNotNull(jsonResponse.get("data.list[0].hasImage"));
		assertNotNull(jsonResponse.get("data.list[0].catSrl"));
		assertNotNull(jsonResponse.get("data.list[0].recommendCnt"));
		assertNotNull(jsonResponse.get("data.list[0].reportCnt"));
		assertNotNull(jsonResponse.get("data.list[0].venderReportCnt"));
		assertNotNull(jsonResponse.get("data.list[0].dealGpoint"));
		assertNotNull(jsonResponse.get("data.list[0].deliveryGpoint"));
		assertNotNull(jsonResponse.get("data.list[0].whoCreate"));
		assertNotNull(jsonResponse.get("data.list[0].createDt"));
		assertNotNull(jsonResponse.get("data.list[0].whoUpdate"));
		assertNotNull(jsonResponse.get("data.list[0].imageInfo"));
		assertNotNull(jsonResponse.get("data.list[0].optionDealInfo"));
//		assertNotNull(jsonResponse.get("data.list[0].optionDealInfo.dealType"));
		assertNotNull(jsonResponse.get("data.list[0].optionDealInfo.optionDealInfoList"));
		assertNotNull(jsonResponse.get("data.list[0].isOwner"));
		assertNotNull(jsonResponse.get("data.list[0].isRecommended"));
		
	}
	
	@Test
	public void testGetMyReviewList_선택입력값200ok() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", DealDetailConstants.QA_MEMBERNO);
		builder.addHeader("X-TMon-Agent", "ios");
		RequestSpecification requestSpec = builder.build();
		requestSpec.queryParam("useDealInfo", true);
		requestSpec.queryParam("offset", 3);
		requestSpec.queryParam("page", 1);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.nextEntity"));
		assertNotNull(jsonResponse.get("data.list"));
		assertNotNull(jsonResponse.getInt("data.list.size()")>0);
		//useDealInfo는 true 
		assertNotNull(jsonResponse.get("data.list[0].dealInfo"));
		assertNotNull(jsonResponse.get("data.list[0].reviewNo"));
		
	}
	
	@Test
	public void testGetMyReviewList_잘못된useDealInfo값() throws Exception {
		RequestSpecBuilder builder = APITestUtils.getSpecBuilder();
		builder.addHeader("X-Tmon-Member-No", DealDetailConstants.QA_MEMBERNO);
		builder.addHeader("X-TMon-Agent", "ios");
		RequestSpecification requestSpec = builder.build();
		requestSpec.queryParam("useDealInfo", "fasdf");
		requestSpec.queryParam("offset", 3);
		requestSpec.queryParam("page", 1);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(400).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
	}
	
}
