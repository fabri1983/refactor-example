package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;

public class CompoundJobLogger implements IEnhancedJobLogger {

	private List<IEnhancedJobLogger> jobLoggers = Collections.emptyList();
	
	public CompoundJobLogger(IEnhancedJobLogger[] loggers) {
		this.jobLoggers = Arrays.asList(loggers);
	}

	@Override
	public void info(String message) {
		jobLoggers.forEach( l -> l.info(message) );
	}

	@Override
	public void warn(String message) {
		jobLoggers.forEach( l -> l.warn(message) );
	}

	@Override
	public void error(String message) {
		jobLoggers.forEach( l -> l.error(message) );
	}

}
