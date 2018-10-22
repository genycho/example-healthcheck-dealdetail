package test.rest.dealdetail.halfclub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
 * 상품문의 답변 한 건 등록 API
 * 
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class PostAddQnAReplyForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/qna/partner/addReply";
	
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
	final Long testPartnerNo = Long.valueOf(HalfClubTestConstants.PARTNER_NO);
	
	final String testContents = "\n첫줄엔터\n특수문자한자*(%!@+#_!%{@%?>^?!$_!+한字 \n<br/><b>굵게</b><script>alert('hello');</script><html><table>";
	
	Long generatedASrl = null;
	Long generatedReplySrl = null;
	
	public PostAddQnAReplyForPartnerTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
	}

	@After
	public void tearDown() throws Exception {
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
		if(generatedReplySrl !=null) {
			QnATestHelper.getInstance().deleteQnAAnswer(generatedReplySrl, generatedASrl, testPartnerNo);
		}
		generatedReplySrl = null;
		generatedASrl = null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostAddQnAReplyForPartnerTest_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
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
		assertNotNull(jsonResponse.get("data.replyAsrl"));
		
		generatedReplySrl = Long.valueOf(jsonResponse.get("data.replyAsrl").toString());
	}
	
	@Test
	public void testPostAddQnAReplyForPartnerTest_ReplyAsrl제외확인() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "FALSE");//결과에 등록한 aSrl 포함
		
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
		assertNull(jsonResponse.get("data.replyAsrl"));
	}
	
	@Test
	public void testPostAddQnAReplyForPartnerTest_특수문자처리확인() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", testContents);
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
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
		assertNotNull(jsonResponse.get("data.replyAsrl"));
		
		generatedReplySrl = Long.valueOf(jsonResponse.get("data.replyAsrl").toString());
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostAddQnAReplyForPartnerTest_존재하지않는문의에대한답변시도() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", 00000001L);
		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
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
		assertEquals("NOT_EXIST_ARTICLE", jsonResponse.get("data.status"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostAddQnAReplyForPartnerTest_필수입력파트너번호누락() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
//		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
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
		assertTrue(jsonResponse.get("exceptionMessage").toString().contains("Required long parameter 'partnerSrl' is not present"));
	}
}