package org.fabri1983.refactorexample.joblogger.enhanced.factory;

import java.io.File;
import java.sql.Connection;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerExceptionMessage;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.CompoundJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.ConsoleJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.DatabaseJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.FileJobLogger;

public class JobLoggerFactory {

	public static IEnhancedJobLogger newConsoleJobLogger() {
		return new ConsoleJobLogger();
	}
	
	public static IEnhancedJobLogger newFileJobLogger(File file) {
		if (file == null) {
			throw new JobLoggerException(JobLoggerExceptionMessage.FILE_IS_NULL);
		}
		return new FileJobLogger(file);
	}
	
	public static IEnhancedJobLogger newDatabaseJobLogger(Connection connection) {
		if (connection == null) {
			throw new JobLoggerException(JobLoggerExceptionMessage.CONNECTION_IS_MISSING);
		}
		return new DatabaseJobLogger(connection);
	}
	
	public static IEnhancedJobLogger newCompoundJobLogger(IEnhancedJobLogger ... loggers) {
		if (loggers == null || loggers.length == 0) {
			throw new JobLoggerException(JobLoggerExceptionMessage.JOB_LOGGERS_MISSING);
		}
		return new CompoundJobLogger(loggers);
	}
}
