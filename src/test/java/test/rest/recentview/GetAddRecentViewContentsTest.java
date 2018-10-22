package test.rest.recentview;

import static org.junit.Assert.assertEquals;
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
 * 
 */
public class GetAddRecentViewContentsTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/recentView/add/contents";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO3;
	
	String recentViewKey = null;
	
	 public GetAddRecentViewContentsTest() {
		 super();
	 }
	 
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if(recentViewKey!=null ) {
			RecentViewTestHelper.getInstance().deleteRecentView(testDealNo, recentViewKey);
		}
		recentViewKey = null;
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetAddRecentViewContents_NOLOGIN_PC_200ok() throws Exception {
		this.recentViewKey = "2dfbaf4f-219e"+TestUtils.getUniqueString();
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", recentViewKey);// recentViewKey. member no
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", testDealNo);// value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "PC");// PC, MOBILE
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
	}
	
	@Test
	public void testGetAddRecentViewContents_NOLOGIN_MOBILE200ok() throws Exception {
		this.recentViewKey = "2dfbaf4f-219e"+TestUtils.getUniqueString();
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", recentViewKey);// recentViewKey. member no
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", testDealNo);// value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "MOBILE");// PC, MOBILE
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
	}
	
	@Test
	public void testGetAddRecentViewContents_DEALTYPE_로그인200ok() throws Exception {
		this.recentViewKey = DealDetailConstants.QA_MEMBERNO;
		
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", recentViewKey);// recentViewKey. member no
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", testDealNo);// value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "PC");// PC, MOBILE
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
	}
	
	@Test
	public void testGetAddRecentViewContents_기존건재추가시도() throws Exception {
		this.recentViewKey = DealDetailConstants.QA_MEMBERNO;
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", recentViewKey);// recentViewKey. member no
		requestSpec.queryParam("contentType", "DEAL");// contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", testDealNo);// value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "PC");// PC, MOBILE
		requestSpec.log().all();

		//1회
		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();
		//2회
		response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("FALSE" ,jsonResponse.get("data"));//FIXME	정상 추가가 TRUE가 아닌 OK인데 반해 중복 추가시 FALSE가 반환됨  
	}
	
	@Test
	public void testGetAddRecentViewContents_PLANTYPE_200ok() throws Exception {
		this.recentViewKey = "2dfbaf4f-219e"+TestUtils.getUniqueString();
		
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", "recentViewKey", recentViewKey);//recentViewKey. member no
		requestSpec.queryParam("contentType", "PLAN");//contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", "PLAN_Q8fHWMSncd");//value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "PC");//PC, MOBILE
		
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
	.get(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("OK" ,jsonResponse.get("data"));
	}
	
	@Test
	public void testGetAddRecentViewContents_존재하지않는딜번호() throws Exception {
		this.recentViewKey = "nologin-uniquekey"+TestUtils.getUniqueString();
		
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("recentViewKey", recentViewKey);//recentViewKey. member no
		requestSpec.queryParam("contentType", "DEAL");//contentsType. 컨텐츠의 분
		requestSpec.queryParam("value", "0000000001");//value. 컨텐츠의 값. 딜번호
		requestSpec.queryParam("platformType", "MOBILE");//PC, MOBILE
		
		requestSpec.log().all();
		
		Response response = RestAssured
		.given()
			.spec(requestSpec)
		.expect()
			.statusCode(200).log().all()
		.when()
	.get(apiPath).andReturn();
		
		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertEquals("FALSE", jsonResponse.get("data"));
	}
}
