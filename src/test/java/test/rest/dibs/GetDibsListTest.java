package test.rest.dibs;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealSvcTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * 찜목록의 상세데이터까지 가져오는 API 입니다.
 */
public class GetDibsListTest extends AbstractDealSvcTestCase{
	final String apiPath = "/api/dibs/detail";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
	 public GetDibsListTest() {
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
	public void testGetDibsList_MEMBER_200ok() throws Exception {
//		RequestSpecification requestSpec = getLoggedInDefaultRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		RequestSpecification requestSpec = getHeaderDirectMNoRequestSpec(Long.valueOf(DealDetailConstants.QA_MEMBERNO));
		requestSpec.queryParam("offset", 0);// int offset. list 의 start index 값
		requestSpec.queryParam("limit", 10);// int limit. limit 값. default : 4
		requestSpec.queryParam("platformType", "ALL");// platformType. 조회시 plat
		requestSpec.queryParam("contentType", "ALL");// contentsType. 조회시 con
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
	}
	
}
