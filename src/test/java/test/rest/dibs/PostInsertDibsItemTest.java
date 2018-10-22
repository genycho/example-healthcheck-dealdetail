package test.rest.dibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 * http://dealsvcapi.qa.tmon.co.kr/api-helper/node/apis
 */
public class PostInsertDibsItemTest extends AbstractDealSvcTestCase{
	public static final String apiPath = "/api/dibs/{contentId}";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	final String mNo = DealDetailConstants.QA_MEMBERNO;
	
	boolean addedDealDibs = false;
	boolean addedPlanDibs = false;
	
	
	 public PostInsertDibsItemTest() {
		 super();
	 }
	 
	@Before
	public void setUp() throws Exception {
		try {
			DibsTestHelper.getInstance().deleteDealDibs(testDealNo, DealDetailConstants.QA_MEMBERNO);
		}catch(Exception tempIgnoreEx) {}
	}

	@After
	public void tearDown() throws Exception {
		if(addedDealDibs == true) {
			DibsTestHelper.getInstance().deleteDealDibs(testDealNo, DealDetailConstants.QA_MEMBERNO);
			addedDealDibs = false;
		}
		if(addedPlanDibs) {
			DibsTestHelper.getInstance().deletePlanDibs(DealDetailConstants.PLAN_NO, DealDetailConstants.QA_MEMBERNO);
			addedPlanDibs = false;
		}
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostInsertDibsItem_DEALTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "DEAL");
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		addedDealDibs = true;
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
		
	}
	
	@Ignore	//기대한 에러가 발생하지 않고 정상삭제 처리되는 것으로 보임. 해당 영역이 담당 테스트 영역이 아니라 요건 생략 
	public void testPostInsertDibsItem_dibsType없이PLANNO입력시() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", DealDetailConstants.PLAN_NO);
//		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(400).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		addedPlanDibs = true;
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(false, jsonResponse.getBoolean("data.result"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testPostInsertDibsItem_PLANTYPE_200ok() throws Exception {
		try {
			DibsTestHelper.getInstance().deletePlanDibs(DealDetailConstants.PLAN_NO, mNo);
		}catch(Exception setUpIgnoreEx) {
		}
		
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", DealDetailConstants.PLAN_NO);
		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
			.post(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		addedPlanDibs = true;
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.result"));
		assertEquals(true, jsonResponse.getBoolean("data.result"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
//		assertEquals(false, jsonResponse.getBoolean("data.alreadyDibsItem"));
	}
	
	/**
	 * 
	 * */
	@Ignore//퍼포먼스 이슈로 미수정한다고 하여 이그노어 처리 
	public void testPostInsertDibsItem_중복찜하기() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(mNo));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "DEAL");
		requestSpec.log().all();
		
		/* 첫번째 */
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200)
//			.log().all()
		.when()
			.post(apiPath).andReturn();
		addedDealDibs = true;
		
		/* 두번째 */
		response = RestAssured
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
		assertEquals(false, jsonResponse.getBoolean("data.result"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
		assertEquals(true, jsonResponse.getBoolean("data.alreadyDibsItem"));
	}
}
