package test.rest.recentview;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealSvcTestCase;
import test.rest.TestUtils;
import test.rest.testdata.DealDetailConstants;

/**
 * 최근 본 컨텐츠 조회
 */
public class GetRecentViewInfoTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/recentView/v2/get/info";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 public GetRecentViewInfoTest() {
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
	public void testGetRecentViewInfo_TEMPKEY_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", "nologin-uniquekey"+TestUtils.getUniqueString());// recentViewKey. member no
		requestSpec.queryParam("offset", 0);// int offset. list 의 start index 값
		requestSpec.queryParam("limit", 10);// int limit. limit 값. default : 4
		requestSpec.queryParam("platformType", "PC");// platformType. 조회시 plat
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 조회시 con
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.recentViewList"));
	}
	
	@Test
	public void testGetRecentViewInfo_MEMBERKEY_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", DealDetailConstants.QA_MEMBERNO);// recentViewKey. member no
		requestSpec.queryParam("offset", 0);// int offset. list 의 start index 값
		requestSpec.queryParam("limit", 10);// int limit. limit 값. default : 4
		requestSpec.queryParam("platformType", "PC");// platformType. 조회시 plat
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 조회시 con
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.recentViewList"));
	}
	
	@Test
	public void testGetRecentViewInfo_MoWebKey_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", "2000003183");// recentViewKey. member no
//		/RECENTVIEW%3A2000003183
		requestSpec.queryParam("offset", 0);// int offset. list 의 start index 값
		requestSpec.queryParam("limit", 10);// int limit. limit 값. default : 4
		requestSpec.queryParam("platformType", "ALL");// platformType. 조회시 plat
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 조회시 con
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertNotNull(jsonResponse.get("data.recentViewList"));
	}
	
}
