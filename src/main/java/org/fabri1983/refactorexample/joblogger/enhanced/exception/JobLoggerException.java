package org.fabri1983.refactorexample.joblogger.enhanced.exception;

public class JobLoggerException extends RuntimeException {

	public JobLoggerException(String msg) {
		super(msg);
	}
	
	public JobLoggerException(String msg, Exception ex) {
		super(msg, ex);
	}
	
	public JobLoggerException(Exception ex) {
		super(ex);
	}

	private static final long serialVersionUID = 1L;

}
