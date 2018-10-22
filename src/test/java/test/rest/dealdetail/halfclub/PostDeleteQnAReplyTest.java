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
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 상품문의 답변 한 건 삭제 API
 * 
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class PostDeleteQnAReplyTest extends AbstractBoardTestCase{
	public static final String apiPath = "/api/qna/partner/deleteReply";
	
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
	final Long testPartnerNo = Long.valueOf(HalfClubTestConstants.PARTNER_NO);
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	Long generatedReplySrl = null;
	
	public PostDeleteQnAReplyTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
		generatedReplySrl = QnATestHelper.getInstance().prepareQnAAnswer(generatedASrl, testPartnerNo);
	}

	@After
	public void tearDown() throws Exception {
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
		if(generatedReplySrl !=null) {
			try {
				QnATestHelper.getInstance().deleteQnAAnswer(generatedReplySrl, generatedASrl, testPartnerNo);
			}catch(Exception ignore) {
				//do nothing.
			}
		}
		generatedASrl = null;
		generatedReplySrl = null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostDeleteQnAReply_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
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
		assertEquals(true, jsonResponse.get("data.result"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostDeleteQnAReply_필수입력답변번호누락() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", generatedReplySrl);
//		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
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
		assertTrue(jsonResponse.get("exceptionMessage").toString().contains("Required long parameter 'aSrl' is not present"));
	}
	
	@Test
	public void testPostDeleteQnAReply_존재하지않은답변삭제시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("aSrl", 00000001L);
		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
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
		
		assertNotNull(jsonResponse.get("data.status"));
		assertEquals("NOT_EXIST_ARTICLE",jsonResponse.get("data.status"));
		
		assertNotNull(jsonResponse.get("data.message"));
		assertEquals("상품문의 글 정보가 없습니다.",jsonResponse.get("data.message"));

	}
	
	@Test
	public void testPostDeleteQnAReply_존재하지않는파트너가답변삭제시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", "0000001");
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
		assertEquals("파트너정보가 다릅니다.",jsonResponse.get("data.message"));
		
		assertNotNull(jsonResponse.get("data.status"));
		assertEquals("DIFFERENT_PARTNER",jsonResponse.get("data.status"));
		
	}
}
