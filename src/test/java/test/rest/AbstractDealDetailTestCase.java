package test.rest;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * 
 */
public class AbstractDealDetailTestCase extends AbstractRestAPITestRunner{
	
	 static {
		 String testMode = System.getProperty("TEST_MODE");
		 if(testMode==null) {
			 System.setProperty("TEST_MODE", "QA");
//			 Local로 세팅하고 테스트하는 방법
//			 1) 테스트 실행 시 Java arguments에 -DTEST_MODE = LOCAL 설정
//			 2) 위의 static 내 변수 주석을 해제
//			 3) 상위 EoEAPITestCase의 디폴트 값을 현재 DEV 에서 LOCAL로 변경
		 }else {
			 System.out.println("SYSTEM_VARIABLE::TEST_MODE is already set as - "  +testMode);
		 }
		}

		public AbstractDealDetailTestCase() {
			String baseURI = "";
			switch (getTestMode()) {
				case AbstractRestAPITestRunner.TESTMODE_LOCAL:
					baseURI = "??";
					RestAssured.port = 8080;
					break;
				case AbstractRestAPITestRunner.TESTMODE_DEVSERVER:
					baseURI =  "http://xxx..co.kr";
					/** 히스토리 기록. SSL 옵션 관련 검색하여 설정 시도 했던 내용들 */
					// RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames());
					// RestAssured.useRelaxedHTTPSValidation();
					// System.setProperty("javax.net.debug", "ssl,handshake");
					// System.setProperty("jsse.enableSNIExtension", "false");
					break;
				case AbstractRestAPITestRunner.TESTMODE_TESTSERVER:
					baseURI = "http://xxx..co.kr";
					 break;
//				case AbstractRestAPITestRunner.TESTMODE_PRODSERVER:
//					throw new APITestRuntimeException("Not yew supported test env value - " + getTestMode());
					// break;
				default:
					throw new APITestRuntimeException("Not yew supported test env value - " + getTestMode());
			}
			RestAssured.baseURI = baseURI;
			// RestAssured.port = targetPort;
		}

		public RequestSpecification getDefaultRequestSpec() {
			return APITestUtils.getDefaultRequestSpec();
		}
		
}
