package org.fabri1983.refactorexample.joblogger.enhanced.exception;

public enum JobLoggerExceptionMessage {

	FILE_HANDLER_NOT_BUILT("Couldn't create FileHandler");

	private String message;
	
	private JobLoggerExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
