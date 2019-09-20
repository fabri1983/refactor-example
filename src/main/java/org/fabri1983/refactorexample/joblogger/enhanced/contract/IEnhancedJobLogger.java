package org.fabri1983.refactorexample.joblogger.enhanced.contract;

public interface IEnhancedJobLogger {

	void info( String message);
	
	void warn(String message);
	
	void error(String message);
	
}
