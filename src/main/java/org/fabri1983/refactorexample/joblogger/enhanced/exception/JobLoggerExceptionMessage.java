package org.fabri1983.refactorexample.joblogger.enhanced.exception;

public enum JobLoggerExceptionMessage {

	FILE_HANDLER_NOT_BUILT("Couldn't create FileHandler."),
	CONNECTION_IS_MISSING("Connection is missing."),
	CONNECTION_IS_CLOSED("Connection is closed."),
	CONNECTION_SQL_EXCEPTION("Connection threw a SQL Exception."),
	JOB_LOGGERS_MISSING("Null or empty array of Job Loggers. Expected at least one.");

	private String message;
	
	private JobLoggerExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
