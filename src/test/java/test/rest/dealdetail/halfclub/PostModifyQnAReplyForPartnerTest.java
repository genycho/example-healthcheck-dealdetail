package test.rest.dealdetail.halfclub;

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
 * 상품문의 답변 한 건 수정 API
 * 
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class PostModifyQnAReplyForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/qna/partner/modifyReply";
	
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
	final String testPartnerNo = HalfClubTestConstants.PARTNER_NO;
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	Long generatedReplySrl = null;
	
	public PostModifyQnAReplyForPartnerTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
		generatedReplySrl = QnATestHelper.getInstance().prepareQnAAnswer(generatedASrl, Long.valueOf(testPartnerNo));
	}

	@After
	public void tearDown() throws Exception {
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
		if(generatedReplySrl !=null) {
			QnATestHelper.getInstance().deleteQnAAnswer(generatedReplySrl, generatedASrl, Long.valueOf(testPartnerNo));
		}
		generatedReplySrl = null;
		generatedASrl = null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostModifyQnAReplyForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec.queryParam("content", "문의답변 수정테스트입니다");
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
	
	@Test
	public void testPostModifyQnAReplyForPartner_동일내용으로수정시false반환확인() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec.queryParam("content", "same_contents");
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
		
		RequestSpecification requestSpec2 = getDefaultRequestSpec();
		requestSpec2.queryParam("aSrl", generatedASrl);
		requestSpec2.queryParam("replySrl", generatedReplySrl);
		requestSpec2.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec2.queryParam("content", "same_contents");
		requestSpec2.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec2)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post(apiPath).andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(false, jsonResponse.get("data.result"));//
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostModifyQnAReplyForPartner_존재하지않는답변수정시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("replySrl", 00000001L);//115574767
//		requestSpec.queryParam("replySrl", 115574767);//
		requestSpec.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec.queryParam("content", "문의답변 수정테스트입니다");
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
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostModifyQnAReplyForPartner_문의와다른답변수정시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("replySrl", 115574767);
		requestSpec.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec.queryParam("content", "문의답변 수정테스트입니다");
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
	}
	
}