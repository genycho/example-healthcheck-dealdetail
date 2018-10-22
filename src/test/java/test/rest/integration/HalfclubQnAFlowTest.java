package test.rest.integration;

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
import test.rest.dealdetail.halfclub.GetQnAListForPartnerTest;
import test.rest.dealdetail.halfclub.PostDeleteQnAReplyTest;
import test.rest.dealdetail.halfclub.QnATestHelper;
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 1) 문의확인
 * 2) 문의답변
 * 3) 문의답변 확인(목록조회)
 * 4) 문의답변 수정
 * 5) 문의 삭제
 * 
 */
public class HalfclubQnAFlowTest extends AbstractBoardTestCase{
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
	final Long testPartnerNo = Long.valueOf(HalfClubTestConstants.PARTNER_NO);
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	
	 public HalfclubQnAFlowTest() {
		 super();
	 }
	 
	@Before
	public void setUp() throws Exception {
		if(generatedASrl == null) {
			generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
		}
	}

	@After
	public void tearDown() throws Exception {
		if(generatedASrl != null) {
			QnATestHelper.getInstance().deleteQnA(generatedASrl);
			generatedASrl = null;
		}
	}
	
	/**
	 * 1) 문의확인 </br>
	 * 2) 문의답변 </br>
	 * 2-1) 문의답변 등록 후 answerYN 값 확인
	 * 3) 문의답변 확인(목록조회) </br>
	 * 4) 문의답변 수정 </br>
	 * 5) 문의 삭제 </br>                    
	 * 6) 문의 삭제 후 AnswerYN값 확인</br>             
	 */
	@Test
	public void testPartnerQnAFlow_일반흐름() throws Exception {
		//1) 문의확인
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
//			.log().all()
		.when()
			.get("/api/qna/partner/get/{aSrl}").andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.aSrl"));
		assertNotNull(jsonResponse.get("data.buyUser"));
//		assertEquals(true, jsonResponse.getBoolean("data.buyUser"));//QA 테스트 데이터 리셋되어 주석처리함

		//2) 문의답변
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", testPartnerNo);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post("/api/qna/partner/addReply").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.replyAsrl"));
		
		Long generatedReplySrl = ((Integer)jsonResponse.get("data.replyAsrl")).longValue();
		
		//2-1) 문의답변 등록 후 answerYN 값 확인
		requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
//			.log().all()
		.when()
			.get("/api/qna/partner/get/{aSrl}").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data.answerYn"));
		assertEquals("Y", jsonResponse.get("data.answerYn"));
		
		//3) 문의답변 확인(목록조회)
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("dealSrl", testDealNo);
		requestSpec.queryParam("aSrls", generatedASrl);
		requestSpec.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
			.log().all()
		.when()
			.get("/api/qna/partner/reply/list").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertTrue((Integer)jsonResponse.get("data.size") > 0);
		
		//4) 문의답변 수정 </br>
		String modifiedContents = "문의답변 수정컨텐츠입니다";
		
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("partnerSrl", Long.valueOf(HalfClubTestConstants.PARTNER_NO));
		requestSpec.queryParam("content", modifiedContents);
		requestSpec.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post("/api/qna/partner/modifyReply").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.get("data.result"));
		
		//5) 문의 삭제
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", generatedReplySrl);
		requestSpec.queryParam("aSrl", generatedASrl);
		requestSpec.queryParam("partnerSrl", HalfClubTestConstants.PARTNER_NO);
		requestSpec.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post("/api/qna/partner/deleteReply").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.get("data.result"));
		
		//6) 문의 삭제 후 AnswerYN값 확인
		requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
//			.log().all()
		.when()
			.get("/api/qna/partner/get/{aSrl}").andReturn();
		
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data.answerYn"));
		assertEquals("N", jsonResponse.get("data.answerYn"));
		
	}
	
	/**
	 * 1) 문의확인
	 * 2) 문의답변
	 * 3) 타파트너사가 문의 삭제 시도
	 */
	@Test
	public void testPartnerQnAFlow_다른파트너의답글삭제시도() throws Exception {
		//1) 문의확인
				RequestSpecification requestSpec = getDefaultRequestSpec();
				requestSpec.pathParam("aSrl", generatedASrl);
				
				Response response = RestAssured
				.given()
					.spec(requestSpec)
				.expect()
					.statusCode(200)
//					.log().all()
				.when()
					.get("/api/qna/partner/get/{aSrl}").andReturn();
				
				JsonPath jsonResponse = new JsonPath(response.asString());
				assertNotNull(jsonResponse.get("data"));
				assertNotNull(jsonResponse.get("data.aSrl"));
				assertNotNull(jsonResponse.get("data.buyUser"));
//				assertEquals(true, jsonResponse.getBoolean("data.buyUser"));

				//2) 문의답변
				requestSpec = getDefaultRequestSpec();
				requestSpec.queryParam("aSrl", generatedASrl);
				requestSpec.queryParam("partnerSrl", testPartnerNo);
				requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
				requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
				
				response = RestAssured
				.given()
					.spec(requestSpec)
				.expect()
					.statusCode(200).log().all()
				.when()
					.post("/api/qna/partner/addReply").andReturn();
				
				jsonResponse = new JsonPath(response.asString());
				assertNotNull(jsonResponse.get("data"));
				assertNotNull(jsonResponse.get("data.result"));
				assertEquals(true, jsonResponse.get("data.result"));
				assertNotNull(jsonResponse.get("data.replyAsrl"));
				
				Long generatedReplySrl = ((Integer)jsonResponse.get("data.replyAsrl")).longValue();
				
				//3) 타파트너사가 문의 삭제 시도
				requestSpec = getDefaultRequestSpec();
				requestSpec.queryParam("replySrl", generatedReplySrl);
				requestSpec.queryParam("aSrl", generatedASrl);
				requestSpec.queryParam("partnerSrl", "0000001");
				requestSpec.log().all();
				
				response = RestAssured
				.given()
					.spec(requestSpec)
				.expect()
					.statusCode(200).log().all()
				.when()
					.post(PostDeleteQnAReplyTest.apiPath).andReturn();
				
				/* 3. response printing & detail assertions */
				jsonResponse = new JsonPath(response.asString());
				assertNotNull(jsonResponse.get("data"));
				assertNotNull(jsonResponse.get("data.result"));
				assertEquals(false, jsonResponse.get("data.result"));
				
				assertNotNull(jsonResponse.get("data.message"));
				assertEquals("파트너정보가 다릅니다.",jsonResponse.get("data.message"));
				
				assertNotNull(jsonResponse.get("data.status"));
				assertEquals("DIFFERENT_PARTNER",jsonResponse.get("data.status"));
	}
	
	/**
	 * 1) 비밀 문의글 등록
	 * 2) 문의확인(단건)하여 내용 표시 확인
	 * 3) 목록 조회 비밀글 내용 표시되지 않는 이슈 확인
	 * 3-리셋) 삭제 
	 */
	@Test
	public void testPartnerQnAFlow_비밀글에대해파트너내용조회가능확인() throws Exception {
		//1) 비밀 문의글 등록
		Long generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo, true);
		
		//2) 문의확인
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
			.log().all()
		.when()
			.get("/api/qna/partner/get/{aSrl}").andReturn();
		
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.aSrl"));
		assertNotNull(jsonResponse.get("data.flagSecret"));
		assertEquals("Y", jsonResponse.getString("data.flagSecret"));
		assertNotNull(jsonResponse.get("data.buyUser"));
//		assertEquals(true, jsonResponse.getBoolean("data.buyUser"));
		assertNotNull(jsonResponse.get("data.content"));
		assertEquals("테스트 구매문의입니다", jsonResponse.getString("data.content"));
		
		//3) 목록 조회에서 표시되지 않는 이슈 확인
		requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealSrl", testDealNo);
		requestSpec.queryParam("startDate", "2018-07-01");
		requestSpec.queryParam("endDate", "2018-12-31");
		
		requestSpec.log().all();
		
		response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
			.log().all()
		.when()
		.get(GetQnAListForPartnerTest.apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertTrue(jsonResponse.getInt("data.totalCount")>0);
		
		assertNotNull(jsonResponse.get("data.articles"));
		assertNotNull(jsonResponse.get("data.articles[0].aSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].bSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].mNo"));
		assertNotNull(jsonResponse.get("data.articles[0].dealSrl"));
		assertNotNull(jsonResponse.get("data.articles[0].content"));
		assertNotNull(jsonResponse.get("data.articles[0].flagView"));
		assertNotNull(jsonResponse.get("data.articles[0].flagSecret"));
		assertEquals("Y", jsonResponse.get("data.articles[0].flagSecret"));
		assertEquals("테스트 구매문의입니다", jsonResponse.get("data.articles[0].content"));
		
		//3-리셋) 삭제 
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
	}
}

