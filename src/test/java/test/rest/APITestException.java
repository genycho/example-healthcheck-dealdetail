package test.rest;


/**
 *
 */
public class APITestException extends Exception {

	private static final long serialVersionUID = 1L;

	public APITestException(String internalMessage, Throwable t) {
		super(internalMessage, t);
	}

	public APITestException(String internalMessage) {
		super(internalMessage);
	}

	public APITestException(Exception e2) {
		super(e2);
	}

}
