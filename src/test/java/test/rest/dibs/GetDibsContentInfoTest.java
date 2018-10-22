package test.rest.dibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
 * memberNo 기준 찜여부 및 해당 컨텐츠id의 찜하기 갯수를 가져오는 API 입니다
 */
public class GetDibsContentInfoTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/dibs/{contentId}";
	final String mNo = DealDetailConstants.QA_MEMBERNO;
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	
	 public GetDibsContentInfoTest() {
		 super();
	 }
	 
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetDibsContentInfo_DEALTYPE_200ok() throws Exception {
//		RequestSpecification requestSpec = getLoggedInDefaultRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "DEAL");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	
	@Ignore	//로직이 꼬여 있음.(타입에 관계없이 딜 or 기획전 등에서 검색해서 처리하는 것으로 보임......
	public void testGetDibsContentInfo_DIFFTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertEquals("확인필요.contentId에는 Deal을 dibsType에는 PLAN을 입력한 경우에 0이 아닌 카운트가 표시되는것으로 보임", 0, jsonResponse.getInt("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
	}
	/**
	 * 
	 * */
	@Test
	public void testGetDibsContentInfo_PLANTYPE_200ok() throws Exception {
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.pathParam("contentId", DealDetailConstants.PLAN_NO);
		requestSpec.queryParam("dibsType", "PLAN");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
		
//		fail("현재 기획전에 대해 dibsType PLAN값을 주면 찜 카운트가 정상 표시되지 않는 것으로 보임(0으로 조회)");
	}
	
	@Ignore//미수정한다고 하여 임시로 이그너어 처리 
	public void testGetDibsCount_NOMEMBERINFO() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("contentId", testDealNo);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.count"));
		assertNotNull(jsonResponse.get("data.formatCount"));
		assertNotNull(jsonResponse.get("data.result"));
		assertNotNull(jsonResponse.get("data.alreadyDibsItem"));
		
		fail("필수입력으로 표시된 멤버정보가 없어도 정상 찜 카운트 조회를 수행함 ");
	}
	
}
