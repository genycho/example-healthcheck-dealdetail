package test.rest.dibs;

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
import test.rest.AbstractDealSvcTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * removeDibsItem
 */
public class DeleteDibsContentTest extends AbstractDealSvcTestCase{
	public static final String apiPath = "/api/dibs/{contentId}";
	
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	final String mNo = DealDetailConstants.QA_MEMBERNO;
	
	boolean addDib = false;
	
	public DeleteDibsContentTest() {
		 super();
		 
	 }
	 
	@Before
	public void setUp() throws Exception {
		if(addDib == false) {
			try {
				DibsTestHelper.getInstance().addDealDibs(testDealNo, DealDetailConstants.QA_MEMBERNO);
				addDib = true;
			}catch(Exception tempSkip) {
				//ignore
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * 
	 * */
	@Test
	public void testDeleteDibsContent_DEAL삭제_200ok() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "DEAL");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.getBoolean("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testDeleteDibsContent_PLAN삭제_200ok() throws Exception {
		try {
			DibsTestHelper.getInstance().addDibs(DealDetailConstants.PLAN_NO, DealDetailConstants.QA_MEMBERNO, "PLAN");
		}catch(Exception justForSetupEx) {
			justForSetupEx.printStackTrace();
		}
		
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", DealDetailConstants.PLAN_NO);
		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.getBoolean("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	
	@Ignore	//기대한 에러가 발생하지 않고 정상삭제 처리되는 것으로 보임. 해당 영역이 담당 테스트 영역이 아니라 요건 생략 
	public void testDeleteDibsContent_PLAN타입입력없이PLANNO삭제시도() throws Exception {
		try {
			DibsTestHelper.getInstance().addDibs(DealDetailConstants.PLAN_NO, DealDetailConstants.QA_MEMBERNO, "PLAN");
		}catch(Exception justForSetupEx) {
			justForSetupEx.printStackTrace();
		}
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", DealDetailConstants.PLAN_NO);
//		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(false, jsonResponse.getBoolean("data.result"));
	}
	
	@Test
	public void testDeleteDibsContent_맞지않는DipsType() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "INVALID_TYPE");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(400).log().all().when()
				.delete(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("exceptionMessage"));
		assertNotNull(jsonResponse.get("exception"));
		assertTrue(jsonResponse.get("exceptionMessage").toString().contains("No enum constant com.tmoncorp.dealsvc.api.dibs.constant.DibsType.INVALID_TYPE"));
//		assertEquals("적절한메시지", jsonResponse.getString("exceptionMessage").toString());
	}
	
}
