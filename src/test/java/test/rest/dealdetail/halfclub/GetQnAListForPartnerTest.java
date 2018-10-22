package test.rest.dealdetail.halfclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 상품문의 목록 기간볋 조회 API-start, end date 기준
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class GetQnAListForPartnerTest extends AbstractBoardTestCase{
	public static final String apiPath = "/api/qna/partner/list/{dealSrl}";
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	
	public GetQnAListForPartnerTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
	}

	@After
	public void tearDown() throws Exception {
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
		generatedASrl = null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetQnAListForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
//		requestSpec.queryParam("page", 1);
//		requestSpec.queryParam("pagePerCount", 50);
//		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
//		requestSpec.queryParam("flagBlind", "N");
//		requestSpec.queryParam("flagNotice",  "N");
//		requestSpec.queryParam("flagView",  "N");
		requestSpec.queryParam("startDate", "2018-07-01");
		requestSpec.queryParam("endDate", "2018-12-31");
//		requestSpec.queryParam("answerYn",  "N");
		
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
		assertTrue((Integer)jsonResponse.get("data.totalCount") > 0);
		assertNotNull(jsonResponse.get("data.currentPage"));
		assertNotNull(jsonResponse.get("data.pagePerCount"));
		
		assertNotNull(jsonResponse.get("data.articles"));
		assertNotNull(jsonResponse.get("data.articles[0].aSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].bSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mNo"));
		assertNotNull(jsonResponse.get("data.articles[0].dealSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].partnerSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].title"));
		assertNotNull(jsonResponse.get("data.articles[0].content"));
		assertNotNull(jsonResponse.get("data.articles[0].userIp"));
		assertNotNull(jsonResponse.get("data.articles[0].flagView"));
		assertNotNull(jsonResponse.get("data.articles[0].flagBlind"));
		assertNotNull(jsonResponse.get("data.articles[0].flagNotice"));
		assertNotNull(jsonResponse.get("data.articles[0].readCount"));
		assertNotNull(jsonResponse.get("data.articles[0].starGrade"));
		assertNotNull(jsonResponse.get("data.articles[0].csRequest"));
		assertNotNull(jsonResponse.get("data.articles[0].needConfirm"));
		assertNotNull(jsonResponse.get("data.articles[0].answerYn"));
		assertNotNull(jsonResponse.get("data.articles[0].replyCount"));
		assertNotNull(jsonResponse.get("data.articles[0].mId"));
		assertNotNull(jsonResponse.get("data.articles[0].flagSecret"));
		assertNotNull(jsonResponse.get("data.articles[0].buyUser"));
	}
	
	/**
	 * 
	 * */
	@Ignore//향후에 고치기로 함
	public void testGetQnAListForPartner_하루건조회() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
//		requestSpec.queryParam("page", 1);
//		requestSpec.queryParam("pagePerCount", 50);
//		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
//		requestSpec.queryParam("flagBlind", "N");
//		requestSpec.queryParam("flagNotice",  "N");
//		requestSpec.queryParam("flagView",  "N");
		requestSpec.queryParam("startDate", "2018-09-03");
		requestSpec.queryParam("endDate", "2018-09-04");
//		requestSpec.queryParam("answerYn",  "N");
		
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
		assertTrue((Integer)jsonResponse.get("data.totalCount") > 0);
		assertNotNull(jsonResponse.get("data.currentPage"));
		assertNotNull(jsonResponse.get("data.pagePerCount"));
		
		assertNotNull(jsonResponse.get("data.articles"));
		assertNotNull(jsonResponse.get("data.articles[0].aSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].bSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mNo"));
		assertNotNull(jsonResponse.get("data.articles[0].dealSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].partnerSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].title"));
		assertNotNull(jsonResponse.get("data.articles[0].content"));
		assertNotNull(jsonResponse.get("data.articles[0].userIp"));
		assertNotNull(jsonResponse.get("data.articles[0].flagView"));
		assertNotNull(jsonResponse.get("data.articles[0].flagBlind"));
		assertNotNull(jsonResponse.get("data.articles[0].flagNotice"));
		assertNotNull(jsonResponse.get("data.articles[0].readCount"));
		assertNotNull(jsonResponse.get("data.articles[0].starGrade"));
		assertNotNull(jsonResponse.get("data.articles[0].csRequest"));
		assertNotNull(jsonResponse.get("data.articles[0].needConfirm"));
		assertNotNull(jsonResponse.get("data.articles[0].answerYn"));
		assertNotNull(jsonResponse.get("data.articles[0].replyCount"));
		assertNotNull(jsonResponse.get("data.articles[0].mId"));
		assertNotNull(jsonResponse.get("data.articles[0].flagSecret"));
		assertNotNull(jsonResponse.get("data.articles[0].buyUser"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetQnAListForPartner_선택입력값포함200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("page", 1);
		requestSpec.queryParam("pagePerCount", 50);
//		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
		requestSpec.queryParam("flagBlind", "N");
		requestSpec.queryParam("flagNotice",  "N");
		requestSpec.queryParam("flagView",  "Y");
		requestSpec.queryParam("startDate", "2018-07-01");
		requestSpec.queryParam("endDate", "2018-12-31");
		requestSpec.queryParam("answerYn",  "N");
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
		assertTrue((Integer)jsonResponse.get("data.totalCount") > 0);
		assertNotNull(jsonResponse.get("data.currentPage"));
		assertEquals("1", ((Integer)jsonResponse.get("data.currentPage")).toString());
		
		assertNotNull(jsonResponse.get("data.pagePerCount"));
		assertNotNull(jsonResponse.get("data.articles"));
		assertNotNull(jsonResponse.get("data.articles[0].aSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].bSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mNo"));
		assertNotNull(jsonResponse.get("data.articles[0].dealSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].partnerSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].title"));
		assertNotNull(jsonResponse.get("data.articles[0].content"));
		assertNotNull(jsonResponse.get("data.articles[0].userIp"));
		assertNotNull(jsonResponse.get("data.articles[0].flagView"));
		assertNotNull(jsonResponse.get("data.articles[0].flagBlind"));
		assertNotNull(jsonResponse.get("data.articles[0].flagNotice"));
		assertNotNull(jsonResponse.get("data.articles[0].readCount"));
		assertNotNull(jsonResponse.get("data.articles[0].starGrade"));
		assertNotNull(jsonResponse.get("data.articles[0].csRequest"));
		assertNotNull(jsonResponse.get("data.articles[0].needConfirm"));
		assertNotNull(jsonResponse.get("data.articles[0].answerYn"));
		assertNotNull(jsonResponse.get("data.articles[0].replyCount"));
		assertNotNull(jsonResponse.get("data.articles[0].mId"));
		assertNotNull(jsonResponse.get("data.articles[0].flagSecret"));
		assertNotNull(jsonResponse.get("data.articles[0].buyUser"));
	}
	
}
