package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.util.logging.ConsoleHandler;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;

public class ConsoleJobLogger extends JobLoggerSlf4jBridge implements IEnhancedJobLogger {

	public ConsoleJobLogger() {
		super(ConsoleJobLogger.class.getSimpleName());
		setHandler();
	}

	private void setHandler() {
		ConsoleHandler ch = new ConsoleHandler();
		logger.addHandler(ch);
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
