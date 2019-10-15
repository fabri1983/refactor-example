package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.fabri1983.refactorexample.joblogger.util.StandardConsoleRedirector;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class ConsoleJobLoggerTest {

	@Test
	public void whenCreatingLogWithConsoleStdErr_thenStdErrContainsMessage() throws Exception {
		
		// given: a custom console
		StandardConsoleRedirector.saveStdErr();
		StandardConsoleRedirector.setCustomConsole();
		
		// given: a Console Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.forStdErr();
		
		// when: logging messages to console
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: retrieve logged messages from custom console
		String loggedMessages = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: messages from custom console matches with expected messages
		boolean containsAll = loggedMessages.contains(infoMessage) 
				&& loggedMessages.contains(warningMessage)
				&& loggedMessages.contains(errorMessage);
		Assert.assertTrue("Console Std Err logged messages are not the same than expected.", containsAll);
	}
	
	@Test
	public void whenCreatingLogWithConsoleStdOut_thenStdOutContainsMessage() throws Exception {
		
		// given: a custom console
		StandardConsoleRedirector.saveStdOut();
		StandardConsoleRedirector.setCustomConsole();
		
		// given: a Console Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.forStdOut();
		
		// when: logging messages to console
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: retrieve logged messages from custom console
		String loggedMessages = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: messages from custom console matches with expected messages
		boolean containsAll = loggedMessages.contains(infoMessage) 
				&& loggedMessages.contains(warningMessage)
				&& loggedMessages.contains(errorMessage);
		Assert.assertTrue("Console Std Out logged messages are not the same than expected.", containsAll);
	}
	
}
