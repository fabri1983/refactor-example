package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;

public class FileJobLogger extends JobLoggerSlf4jBridge implements IEnhancedJobLogger {

	public FileJobLogger(File file) {
		super(FileJobLogger.class.getSimpleName());
		setHandler(file);
	}

	private void setHandler(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileHandler fh= new FileHandler(file.getAbsolutePath(), true);
			logger.addHandler(fh);			
		} catch (SecurityException | IOException ex) {
			throw new JobLoggerException("Couldn't create FileHandler", ex);
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
