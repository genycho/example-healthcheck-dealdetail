package test.rest.review;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.PhotoReviewTestConstants;

/**
 * 리뷰 이미지 목록 제공
 */
public class GetImageReviewListTest extends AbstractBoardTestCase{
	final String apiPath = "/api/review/image/{mainDealNo}";
	final String testDealNo = PhotoReviewTestConstants.PHOTOREVIEW_DEALNO;

	public GetImageReviewListTest() {
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
	public void testGetImageReviewList_필수입력값200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("mainDealNo", testDealNo);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertTrue(jsonResponse.getInt("data.totalCount") > 0);
		assertNotNull(jsonResponse.get("data.reviewImageList"));
		assertEquals(6, jsonResponse.getInt("data.reviewImageList.size()"));//딜폴트 6건 조회 확인 
		assertNotNull(jsonResponse.get("data.reviewImageList[0].reviewImageSrl"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].reviewSrl"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].image"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].createdAt"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetImageReviewList_선택입력포함200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("mainDealNo", testDealNo);
		requestSpec.queryParam("offset",  1);// contentsType. 조회시 con
		requestSpec.queryParam("limit", 2);// contentsType. 조회시 con
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertTrue(jsonResponse.getInt("data.totalCount") > 0);
		assertNotNull(jsonResponse.get("data.reviewImageList"));
		assertEquals(2, jsonResponse.getInt("data.reviewImageList.size()"));//
		assertNotNull(jsonResponse.get("data.reviewImageList[0].reviewImageSrl"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].reviewSrl"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].image"));
		assertNotNull(jsonResponse.get("data.reviewImageList[0].createdAt"));
	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetImageReviewList_존재하지않는딜번호() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.pathParam("mainDealNo", 0000000001);
		requestSpec.log().all();

		Response response = RestAssured.given().spec(requestSpec).expect().statusCode(200).log().all().when()
				.get(apiPath).andReturn();

		/* 3. response printing & detail assertions */
		JsonPath jsonResponse = new JsonPath(response.asString());
		assertNotNull(jsonResponse.get("data"));
		assertNotNull(jsonResponse.get("data.totalCount"));
		assertEquals(0, jsonResponse.getInt("data.totalCount"));//
		assertNotNull(jsonResponse.get("data.reviewImageList"));
		assertEquals(0, jsonResponse.getInt("data.reviewImageList.size()"));//
	}
	
}
