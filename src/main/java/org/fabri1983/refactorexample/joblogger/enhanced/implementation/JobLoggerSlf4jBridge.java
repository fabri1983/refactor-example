package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

public abstract class JobLoggerSlf4jBridge {

	// Enable SLF4J bridge: redirect JUL calls to SLF4J. 
	// This way we get bug fixes and performance improvements using a better implementation of a SLF4J. For example Logback.
	static {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.install();
	}
	
	protected Logger logger;
	
	protected JobLoggerSlf4jBridge(String name) {
		this.logger = Logger.getLogger(name);
	}
	
}
