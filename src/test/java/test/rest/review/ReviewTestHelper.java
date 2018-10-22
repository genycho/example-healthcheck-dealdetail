package test.rest.review;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractRestAPITestRunner;

public class ReviewTestHelper extends AbstractRestAPITestRunner{
	private static final ReviewTestHelper INSTANCE = new ReviewTestHelper();
	  
    private ReviewTestHelper(){ }

    public static ReviewTestHelper getInstance(){
        return INSTANCE;
    }
    
	Long qnaASrl =null;
	Long replySrl = null;
	
	public void reset() {
	}
	
	public boolean postReviewRecommendation(String reviewNo, String memberNo) {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(memberNo));
		requestSpec.pathParam("reviewNo", reviewNo);
//		requestSpec.log().all();
		
		Response response = 
			RestAssured
				.given()
					.spec(requestSpec)
				.expect()
//					.statusCode(200)
//					.log().all()
				.when()
					.post(PostRecommendReviewTest.apiPath)
					.andReturn();

		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("data"));
		return true;
	}
	
	/**
	 * 테스트 수행을 위한 사전작업</br>
	 * 1) 구매문의 등록 
	 * 2) 구매에 대한 답변 등록?  
	 */
	public boolean deleteReviewRecommendation(String reviewNo, String memberNo) {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(memberNo));
		requestSpec.pathParam("reviewNo", reviewNo);
//		requestSpec.log().all();

		Response response = 
			RestAssured
				.given()
					.spec(requestSpec)
				.expect()
//					.statusCode(200)
//					.log().all()
				.when()
					.delete(DeleteReviewRecommendTest.apiPath).andReturn();

		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("data"));
		return true;
	}
	
}
