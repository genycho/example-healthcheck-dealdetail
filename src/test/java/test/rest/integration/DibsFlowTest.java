package test.rest.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.rest.AbstractDealSvcTestCase;
import test.rest.testdata.DealDetailConstants;

/**
 * 
 */
public class DibsFlowTest extends AbstractDealSvcTestCase{
	
	final String pathAdd = "/api/recentView/add/contents";
	final String pathGetList = "/api/recentView/v2/get/info";
	final String pathRemove = "/api/recentView/remove/contents";
	final String pathMerge = "/api/recentView/merge";
	
	public DibsFlowTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 */
	@Test
	public void testDibsTypeDealFlowTest() throws Exception {
		final String testDealNo = DealDetailConstants.STOREDEAL_NORMAL_DEALNO;
		final String mNo = DealDetailConstants.QA_MEMBERNO;

		//1) 해당 멤버의 찜하기 목록 확인
		
		
		//2) 찜하려는 딜의 찜당한 카운트 확인
		
		
		//3) 찜하기
		
		//3-1) 해당 멤버의 찜하기 목록(개수 추가) 확인
		
		//3-1) 찜당한 딜의 카운트 확인
		
		//4) 찜 삭제
		
		//4-1) 해당 멤버의 찜하기 목록(개수 추가) 확인
		
		//4-2) 찜당한 딜의 카운트 확인 
		
	}
}
