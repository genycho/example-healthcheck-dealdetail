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
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 상품문의 단건 조회 API
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class GetQnAForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/qna/partner/get/{aSrl}";
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	
	 public GetQnAForPartnerTest() {
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
	public void testGetQnAForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
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
		
		assertNotNull(jsonResponse.get("data.aSrl"));
		assertNotNull(jsonResponse.get("data.bSrl"));
		assertNotNull(jsonResponse.get("data.mNo"));
		assertNotNull(jsonResponse.get("data.dealSrl"));
		assertNotNull(jsonResponse.get("data.partnerSrl"));
		assertNotNull(jsonResponse.get("data.title"));
		assertNotNull(jsonResponse.get("data.content"));
		assertNotNull(jsonResponse.get("data.userIp"));
		assertNotNull(jsonResponse.get("data.flagView"));
		assertNotNull(jsonResponse.get("data.flagBlind"));
		assertNotNull(jsonResponse.get("data.flagNotice"));
		assertNotNull(jsonResponse.get("data.readCount"));
		assertNotNull(jsonResponse.get("data.starGrade"));
		assertNotNull(jsonResponse.get("data.csRequest"));
		assertNotNull(jsonResponse.get("data.tmpBNo"));
		assertNotNull(jsonResponse.get("data.needConfirm"));
		assertNotNull(jsonResponse.get("data.answerYn"));
		assertNotNull(jsonResponse.get("data.replyCount"));
		assertNotNull(jsonResponse.get("data.mId"));
		assertNotNull(jsonResponse.get("data.flagSecret"));
		assertNotNull(jsonResponse.get("data.buyUser"));
//		assertEquals(true, jsonResponse.getBoolean("data.buyUser"));//QA 테스트 데이터 변경(구매내역 삭제)되어 주석처리
	}
	
	/**
	 * 200ok에 반환값 없음 
	 * */
	@Test
	public void testGetQnAForPartner_존재하지않는문의조회시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", 000000001L);
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
