package org.fabri1983.refactorexample.joblogger.enhanced.factory;

import java.io.File;
import java.sql.Connection;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.CompoundJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.ConsoleJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.DatabaseJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.implementation.FileJobLogger;

public class JobLoggerFactory {

	public static IEnhancedJobLogger newConsoleJobLogger() {
		return new ConsoleJobLogger();
	}
	
	public static IEnhancedJobLogger newFileJobLogger(File file) {
		return new FileJobLogger(file);
	}
	
	public static IEnhancedJobLogger newDatabaseJobLogger(Connection connection) {
		return new DatabaseJobLogger(connection);
	}
	
	public static IEnhancedJobLogger newCompoundJobLogger(IEnhancedJobLogger ... loggers) {
		return new CompoundJobLogger(loggers);
	}
}