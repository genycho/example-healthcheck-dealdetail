package test.rest;


/**
 *
 */
public class APITestRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public APITestRuntimeException(String internalMessage, Throwable t) {
		super(internalMessage, t);
	}

	public APITestRuntimeException(String internalMessage) {
		super(internalMessage);
	}

	public APITestRuntimeException(Exception e2) {
		super(e2);
	}

}
