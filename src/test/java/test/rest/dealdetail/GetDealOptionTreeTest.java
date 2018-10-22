package test.rest.dealdetail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractDealDetailTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * 
 */
public class GetDealOptionTreeTest extends AbstractDealDetailTestCase{
	final String apiPath = "/api/deals/{dealNo}/optionTree";
	final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO3;
	 public GetDealOptionTreeTest() {
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
	public void testGetDealOptionTree_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("dealNo", testDealNo);
		
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
		assertNotNull(jsonResponse.get("data.dealNo"));
		assertEquals(Integer.valueOf(testDealNo), jsonResponse.get("data.dealNo"));
		
		assertNotNull(jsonResponse.get("data.treeDepth"));
//		int treeDepth = jsonResponse.get("data.treeDepth");
		assertNotNull(jsonResponse.get("data.optionTreeTitles"));
		
		assertNotNull(jsonResponse.get("data.treeData"));
		assertNotNull(jsonResponse.get("data.treeData.childNodes"));
		List depth1ChildList =jsonResponse.get("data.treeData.childNodes"); 

	}
}
