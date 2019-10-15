package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.OutputStream;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.handler.CustomConsoleHandler;

public class ConsoleJobLogger extends JobLoggerSlf4jBridge implements IEnhancedJobLogger {

	public ConsoleJobLogger(final OutputStream out) {
		super(ConsoleJobLogger.class.getSimpleName());
		setHandlerFor(out);
	}

	private void setHandlerFor(final OutputStream out) {
		CustomConsoleHandler ch = new CustomConsoleHandler(out);
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
