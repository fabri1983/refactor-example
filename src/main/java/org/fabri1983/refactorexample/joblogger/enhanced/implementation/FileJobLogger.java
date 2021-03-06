package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.util.logging.FileHandler;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerExceptionMessage;

public class FileJobLogger extends JobLoggerSlf4jBridge implements IEnhancedJobLogger {

	public FileJobLogger(File file) {
		super(FileJobLogger.class.getSimpleName());
		setHandler(file);
	}

	private void setHandler(File file) {
		try {
			FileHandler fh = new FileHandler(file.getAbsolutePath(), true);
			logger.addHandler(fh);			
		} catch (Exception ex) {
			throw new JobLoggerException(JobLoggerExceptionMessage.FILE_HANDLER_NOT_BUILT, ex);
		}
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void warn(String message) {
		logger.warning(message);
	}

	@Override
	public void error(String message) {
		logger.severe(message);
	}
	
}
