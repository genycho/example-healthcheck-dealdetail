package test.rest.recentview;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractRestAPITestRunner;

public class RecentViewTestHelper extends AbstractRestAPITestRunner{
	private static final RecentViewTestHelper INSTANCE = new RecentViewTestHelper();
	  
    private RecentViewTestHelper(){ }

    public static RecentViewTestHelper getInstance(){
        return INSTANCE;
    }
    
	Long qnaASrl =null;
	Long replySrl = null;
	
	public void reset() {
	}
	
	/**
	 * 테스트 수행을 위한 사전작업</br>
	 * 1) 구매문의 등록 
	 * 2) 구매에 대한 답변 등록?  
	 */
	public boolean deleteRecentView(String dealNo, String recentViewKey) {
		boolean resultFlag = false;
		try {
			RequestSpecification requestSpec = getDefaultRequestSpec();
			requestSpec.queryParam("recentViewKey", recentViewKey);
			requestSpec.queryParam("removeRecentViewContentsList", "DEAL:" + dealNo);
//			requestSpec.log().all();

			Response response = 
				RestAssured
					.given()
						.spec(requestSpec)
					.expect()
//						.statusCode(200)
//						.log().all()
					.when()
						.get(GetDeleteRecentViewTest.apiPath)
						.andReturn();

			/* 3. response printing & detail assertions */
			JsonPath jsonResponse = new JsonPath(response.asString());
			if ("OK".equalsIgnoreCase(jsonResponse.get("data").toString())) {
				resultFlag = true;
			}
		} catch (Exception ex) {
			resultFlag = false;
		}
		return resultFlag;
	}
	
}
