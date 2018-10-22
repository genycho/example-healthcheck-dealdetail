package test.rest.dealdetail.halfclub;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractRestAPITestRunner;
import test.rest.testdata.DealDetailConstants;

public class QnATestHelper extends AbstractRestAPITestRunner{
	private static final QnATestHelper INSTANCE = new QnATestHelper();
	  
    private QnATestHelper(){ }

    public static QnATestHelper getInstance(){
        return INSTANCE;
    }
    
	Long qnaASrl =null;
	Long replySrl = null;
	
	public void reset() {
		qnaASrl = null;
		replySrl = null;
	}
	
	public Long prepareQnA(String dealNo, String mNo) {
		return prepareQnA(dealNo, mNo, false);
	}
	/**
	 * 테스트 수행을 위한 사전작업</br>
	 * 1) 구매문의 등록 
	 * 2) 구매에 대한 답변 등록?  
	 */
	public Long prepareQnA(String dealNo, String mNo, boolean flagSecret) {
		Long generatedASrl = null;
		if (qnaASrl != null) {
			generatedASrl = qnaASrl;
		} else {
			RequestSpecification requestSpec = getDefaultRequestSpec();
			requestSpec.pathParam("dealSrl", dealNo);
			requestSpec.queryParam("mNo", mNo);
			requestSpec.queryParam("content", "테스트 구매문의입니다");
			if (flagSecret) {
				requestSpec.queryParam("flagSecret", "Y");// 비밀여부 (기본 : N)
			} else {
				requestSpec.queryParam("flagSecret", "N");// 비밀여부 (기본 : N)
			}
			requestSpec.queryParam("withAsrl", "TRUE");// 결과에 등록한 aSrl 포함
			// requestSpec.log().all();

			Response response = 
					RestAssured
						.given().spec(requestSpec)
						.expect()
							.statusCode(200)
//							.log().all()
						.when()
					.post("/api/qna/add/{dealSrl}").andReturn();// TODO 상수화

			JsonPath jsonResponse = new JsonPath(response.asString());
			generatedASrl = jsonResponse.get("data.asrl");
		}
		return generatedASrl;
	}
	
	public void deleteQnA(Long generatedASrl) {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("aSrl", generatedASrl);
		requestSpec.queryParam("mNo", DealDetailConstants.QA_MEMBERNO);
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
		.when()
			.post("/api/qna/delete/{aSrl}").andReturn();
		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("data"));
//		assertNotNull(jsonResponse.get("data.result"));
	}
	
	public Long prepareQnAAnswer(Long aSrl, Long partnerSrl) {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("aSrl", aSrl);
		requestSpec.queryParam("partnerSrl", partnerSrl);
		requestSpec.queryParam("content", "테스트 문의에 대한 테스트 답변입니다 ");//비밀여부 (기본 : N)
		requestSpec.queryParam("withReplyAsrl", "TRUE");//결과에 등록한 aSrl 포함
		
//		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
			//.log().all()
		.when()
			.post("/api/qna/partner/addReply").andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		Integer returnValue =jsonResponse.get("data.replyAsrl"); 
		return returnValue.longValue();
	}
	
	public void deleteQnAAnswer(Long replySrl, Long aSrl, Long partnerNo) {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("replySrl", replySrl);
		requestSpec.queryParam("aSrl", aSrl);
		requestSpec.queryParam("partnerSrl", partnerNo);
//		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
//			.log().all()
		.when()
			.post("/api/qna/partner/deleteReply").andReturn();
		
		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("data"));
//		assertNotNull(jsonResponse.get("data.result"));
//		assertEquals(true, jsonResponse.get("data.result"));
	}
}
