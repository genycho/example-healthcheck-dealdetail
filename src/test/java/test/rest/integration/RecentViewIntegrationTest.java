package test.rest.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealDetailTestCase;
import test.rest.TestUtils;
import test.rest.testdata.DealDetailConstants;

/**
 * 
 */
public class RecentViewIntegrationTest extends AbstractDealDetailTestCase {
	
	final String pathAdd = "/api/recentView/add/contents";
	final String pathGetList = "/api/recentView/v2/get/info";
	final String pathRemove = "/api/recentView/remove/contents";
	final String pathMerge = "/api/recentView/merge";
	
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO3;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;

	public RecentViewIntegrationTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * (1)비로그인 모바일</br>
	 * (2)비로그인 PC *최근본 목록 조회</br>
	 * (3-0) 머지 - 비로그인 > 로그인
	 * (3) 로그인 모바일</br>
	 * (3-1) 기획전
	 * (3-2) 카테고리
	 * (4) 로그인 PC *최근본 목록 조회</br>
	 */
	@Test
	public void testRecentView_integration() throws Exception {
		String tempUniqueKey = "nologin" + TestUtils.getUniqueString();
//		String tempUniqueKey = "?ZX?{_?^";
		String planNo = DealDetailConstants.PLAN_NO;// 8월몬스터딜

		RestAssured.baseURI = "http://dealsvcapi.tmon.co.kr";
		/////////////////////////////////// (0) 비로그인 최초 최근본 조회 //////////////////////////////////////
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", tempUniqueKey);
		requestSpec.queryParam("contentsType", "ALL");
		requestSpec.queryParam("platformType", "MOBILE");
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/v2/get/info").andReturn();
		/* response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		int firstCount = jsonResponse.getInt("data.totalCount");
		assertEquals(0, firstCount);

		///////////////////////////////////  (1)비로그인 모바일 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", tempUniqueKey);
		requestSpec.queryParam("contentType", "DEAL");
		requestSpec.queryParam("value", testDealNo);
		requestSpec.queryParam("platformType", "MOBILE");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/add/contents").andReturn();

		/* response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
		///////////////////////////////////  (2)비로그인 PC		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", tempUniqueKey);
		requestSpec.queryParam("contentType", "DEAL");
		requestSpec.queryParam("value", DealDetailConstants.SUPERMART_NORMAL_DEALNO);
		requestSpec.queryParam("platformType", "PC");

		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/add/contents").andReturn();
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
		///////////////////////////////////  (3) 2개딜 최근본 추가 확인 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", tempUniqueKey);
		requestSpec.queryParam("platformType", "ALL");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/v2/get/info").andReturn();
		/* response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		int secondCount = jsonResponse.getInt("data.totalCount");
		assertEquals(2, secondCount);

		/////////////////////////////////// (3-0) 머지 - 비로그인 to 로그인 아이디		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("toRecentViewKey", DealDetailConstants.QA_MEMBERNO);// 
		requestSpec.queryParam("fromRecentViewKey", tempUniqueKey);//
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(pathMerge).andReturn();
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK", jsonResponse.get("data"));
		
		//(3-1) 기존 본 이력 삭제
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", testMNo);
		requestSpec.queryParam("removeRecentViewContentsList", "DEAL:"+testDealNo);
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/remove/contents").andReturn();
		jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK", jsonResponse.get("data"));
		
		///////////////////////////////////  (3) 로그인 최초 최근본 조회 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", testMNo);
		requestSpec.queryParam("contentsType", "DEAL");
		requestSpec.queryParam("platformType", "MOBILE");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/v2/get/info").andReturn();
		/* response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		int reFirstCount = jsonResponse.getInt("data.totalCount");
		// assertEquals(0, reFirstCount);

		///////////////////////////////////  (11) 로그인 기획전 모바일 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", testMNo);
		requestSpec.queryParam("contentType", "PLAN");
//		requestSpec.queryParam("contentsType", "PLAN");
		requestSpec.queryParam("value", planNo);
		requestSpec.queryParam("platformType", "MOBILE");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(pathAdd).andReturn();
		jsonResponse = new JsonPath(response.asString());

		///////////////////////////////////  (12) 로그인 PC 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", testMNo);
		requestSpec.queryParam("contentType", "DEAL");
		requestSpec.queryParam("value", DealDetailConstants.SUPERMART_NORMAL_DEALNO);
		requestSpec.queryParam("platformType", "PC");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/add/contents").andReturn();

		///////////////////////////////////  (13) 2개딜 최근본 추가 확인 		/////////////////////////////////// 
		requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", testMNo);
		requestSpec.queryParam("contentsType", "ALL");
		requestSpec.queryParam("platformType", "ALL");
		requestSpec.log().all();

		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get("/api/recentView/v2/get/info").andReturn();
		/* response printing & detail assertions */
		jsonResponse = new JsonPath(response.asString());
		int reSecondCount = jsonResponse.getInt("data.totalCount");
		assertEquals(reFirstCount + 1, reSecondCount);

	}
}
