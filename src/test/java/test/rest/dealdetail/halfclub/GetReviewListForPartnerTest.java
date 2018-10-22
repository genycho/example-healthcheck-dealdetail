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
 * 기간별 구매후기 목록 조회 API
 * 해당 딜에 달린 구매후기 목록을 조회한다.
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class GetReviewListForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/partner/list/{dealSrl}";
	final String testDealNo = HalfClubTestConstants.TEST_MAINDEAL_NO;
	
	 public GetReviewListForPartnerTest() {
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
	public void testGetReviewListForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerLength", 50);
		requestSpec.queryParam("report", "N");//Y, N, 없으면 전체
		requestSpec.queryParam("startDate", "2018-07-01");//yyyy-MM-dd
		requestSpec.queryParam("endDate", "2018-08-05");
		
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
		
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.currentPage"));
		assertNotNull(jsonResponse.get("data.pagePerCount"));
		assertNotNull(jsonResponse.get("data.articles"));
		int articlesCount = jsonResponse.get("data.articles.size");
		assertTrue("조회된 후기 갯수가 없습니다", articlesCount>0);
		
		assertNotNull(jsonResponse.get("data.articles[0].reviewSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mainBuySrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mainDealSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].accountSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].memberSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].contents"));
		assertNotNull(jsonResponse.get("data.articles[0].imageUseYn"));
		assertNotNull(jsonResponse.get("data.articles[0].status"));
		assertNotNull(jsonResponse.get("data.articles[0].displayYn"));
		assertNotNull(jsonResponse.get("data.articles[0].catSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].recommendCnt"));
		assertNotNull(jsonResponse.get("data.articles[0].reportCnt"));
		assertNotNull(jsonResponse.get("data.articles[0].venderReportCnt"));
		assertNotNull(jsonResponse.get("data.articles[0].dealGpoint"));
		assertNotNull(jsonResponse.get("data.articles[0].deliveryGpoint"));
		assertNotNull(jsonResponse.get("data.articles[0].createAt"));
		assertNotNull(jsonResponse.get("data.articles[0].whoUpdate"));
		assertNotNull(jsonResponse.get("data.articles[0].gradeName"));
		assertNotNull(jsonResponse.get("data.articles[0].buyOptionList"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetReviewListForPartner_신고된건만조회() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerLength", 50);
		requestSpec.queryParam("report", "Y");//Y, N, 없으면 전체
		requestSpec.queryParam("startDate", "2018-08-01");//yyyy-MM-dd
		requestSpec.queryParam("endDate", "2018-08-13");
		
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
		
		assertNotNull(jsonResponse.get("data.totalCount"));
		int totalCount = jsonResponse.get("data.totalCount");
		assertTrue(totalCount>0);
	}
	
	/**
	 * 
	 * 실제결과 : 조회결과가 없는 것으로 처리됨 
	 * */
	@Test
	public void testGetReviewListForPartner_존재하지않는딜번호() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", "00000001");
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerLength", 50);
		requestSpec.queryParam("report", testDealNo);//Y, N, 없으면 전체
		requestSpec.queryParam("startDate", "2018-07-01");//yyyy-MM-dd
		requestSpec.queryParam("endDate", "2018-08-05");
		
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
		
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.articles"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetReviewListForPartner_구매후기가없는조건() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerLength", 50);
		requestSpec.queryParam("report", "Y");//Y, N, 없으면 전체
		requestSpec.queryParam("startDate", "2018-08-05");//yyyy-MM-dd
		requestSpec.queryParam("endDate", "2018-08-05");
		
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
		
		assertNotNull(jsonResponse.get("data.totalCount"));
		int totalCount = jsonResponse.get("data.totalCount");
		assertEquals(0,  totalCount, 0);
	}
	
	/**
	 * 수행결과: report 값이 존재하지 않는 형태로 조회되는 것으로 보임
	 * */
	@Test
	public void testGetReviewListForPartner_맞지않는reportYN옵션값() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerLength", 50);
		requestSpec.queryParam("report", "error");//Y, N, 없으면 전체
		requestSpec.queryParam("startDate", "2018-07-01");//yyyy-MM-dd
		requestSpec.queryParam("endDate", "2018-08-13");
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
		
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.currentPage"));
		assertNotNull(jsonResponse.get("data.pagePerCount"));
		assertNotNull(jsonResponse.get("data.articles"));
		int articlesCount = jsonResponse.get("data.articles.size");
		assertTrue("조회된 후기 갯수가 없습니다", articlesCount>0);
		
		assertNotNull(jsonResponse.get("data.articles[0].reviewSrl"));
	}
}
