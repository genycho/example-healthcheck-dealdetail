package test.rest.dibs;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractRestAPITestRunner;

public class DibsTestHelper extends AbstractRestAPITestRunner{
	private static final DibsTestHelper INSTANCE = new DibsTestHelper();
	  
    private DibsTestHelper(){ }

    public static DibsTestHelper getInstance(){
        return INSTANCE;
    }
    
	Long qnaASrl =null;
	Long replySrl = null;
	
	public void reset() {
		qnaASrl = null;
		replySrl = null;
	}
	
	public void deleteDibs(String dealNo, String mNo, String type) {
//		RequestSpecification requestSpec = getLoggedInDefaultRequestSpec(Long.valueOf(mNo));
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", dealNo);
		requestSpec.queryParam("dibsType", type);
//		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200)
//				.log().all()
				.when()
				.delete(DeleteDibsContentTest.apiPath).andReturn();
	}
	
	public void deleteDealDibs(String dealNo, String mNo) {
		deleteDibs(dealNo, mNo, "DEAL");
	}
	
	public void deletePlanDibs(String testPlanNo, String qaMemberno) {
		deleteDibs(testPlanNo, qaMemberno, "PLAN");
	}
	
	public void addDealDibs(String dealNo, String mNo) {
		addDibs(dealNo, mNo, "DEAL");
	}
	
	public void addDibs(String dealNo, String mNo, String type) {
//		RequestSpecification requestSpec = getLoggedInDefaultRequestSpec(Long.valueOf(mNo));
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		
		requestSpec.pathParam("contentId", dealNo);
		requestSpec.queryParam("dibsType", type);
//		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
//			.statusCode(200)
//			.log().all()
		.when()
			.post(PostInsertDibsItemTest.apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
//		JsonPath jsonResponse = new JsonPath(response.asString());
//		assertNotNull(jsonResponse.get("data"));
//		assertNotNull(jsonResponse.get("data.result"));
	}


}
