package test.rest.dealdetail.halfclub;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test.rest.AbstractBoardTestCase;
import test.rest.testdata.DealDetailConstants;
import test.rest.testdata.HalfClubTestConstants;

/**
 * 상품문의 답변 목록 조회 API
 * 참고 API헬퍼 : http://boardapi.qa.tmon.co.kr/api-helper/node/apis?____param=eJxTNXdSNTJKzs/PzkwFMlSNHVWBIuYuqkbOQG5GamJKahGmeEFiUWIuNuGSjLDEoszEpBxUw8xdAGT3Gng=#package=com.tmoncorp.api.board.review.controller&class=ReviewController&method=getReviewPeriodList&apiId=f8e9beb9d165f11f75e2f434b8eaf147
 */
public class GetQnAReplyListForPartnerTest extends AbstractBoardTestCase{
	final String apiPath = "/api/qna/partner/reply/list";
	final String testDealNo = HalfClubTestConstants.TEST_DEAL_NO;
	final String testMNo = DealDetailConstants.QA_MEMBERNO;
//	final String testReviewNo = HalfClubTestConstants.TEST_REVIEW_NO;
	Long generatedASrl = null;
	Long generatedReplySrl = null;
	
	public GetQnAReplyListForPartnerTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		generatedASrl = QnATestHelper.getInstance().prepareQnA(testDealNo, testMNo);
		generatedReplySrl = QnATestHelper.getInstance().prepareQnAAnswer(generatedASrl, Long.parseLong(HalfClubTestConstants.PARTNER_NO));
	}

	@After
	public void tearDown() throws Exception {
		QnATestHelper.getInstance().deleteQnA(generatedASrl);
		generatedASrl = null;

	}
	
	/**
	 * 
	 * */
	@Test
	public void testGetQnAListForPartner_200ok() throws Exception {
		RequestSpecification requestSpec = getDefaultRequestSpec();
		requestSpec.queryParam("dealSrl", testDealNo);
		requestSpec.queryParam("aSrls", generatedASrl);
		
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
		
		assertNotNull(jsonResponse.get("data.reviewSrl"));
		assertNotNull(jsonResponse.get("data.mainBuySrl"));
		assertNotNull(jsonResponse.get("data.mainDealSrl"));
		assertNotNull(jsonResponse.get("data.accountSrl"));
		assertNotNull(jsonResponse.get("data.memberSrl"));
		assertNotNull(jsonResponse.get("data.contents"));
		assertNotNull(jsonResponse.get("data.imageUseYn"));
		assertNotNull(jsonResponse.get("data.status"));
		assertNotNull(jsonResponse.get("data.displayYn"));
		assertNotNull(jsonResponse.get("data.useWaitYn"));
		assertNotNull(jsonResponse.get("data.catSrl"));
		assertNotNull(jsonResponse.get("data.recommendCnt"));
		assertNotNull(jsonResponse.get("data.reportCnt"));
		assertNotNull(jsonResponse.get("data.venderReportCnt"));
		assertNotNull(jsonResponse.get("data.dealGpoint"));
		assertNotNull(jsonResponse.get("data.deliveryGpoint"));
		assertNotNull(jsonResponse.get("data.whoCreate"));
		assertNotNull(jsonResponse.get("data.grade"));
		assertNotNull(jsonResponse.get("data.gradeName"));
		assertNotNull(jsonResponse.get("data.imageList"));
		assertNotNull(jsonResponse.get("data.buyOptionList"));
		assertNotNull(jsonResponse.get("data.mobileProductReview"));
		assertNotNull(jsonResponse.get("data.editable"));
	}
	
}
