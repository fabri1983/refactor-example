package org.fabri1983.refactorexample.joblogger.enhanced.exception;

public class JobLoggerException extends RuntimeException {

	public JobLoggerException(JobLoggerExceptionMessage msg) {
		super(msg.getMessage());
	}
	
	public JobLoggerException(JobLoggerExceptionMessage msg, Exception ex) {
		super(msg.getMessage() + " " + ex.getMessage(), ex);
	}
	
	private static final long serialVersionUID = 1L;

}
