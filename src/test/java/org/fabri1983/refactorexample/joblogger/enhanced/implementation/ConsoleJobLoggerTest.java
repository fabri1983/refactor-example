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
	public void whenCreatingLogWithConsoleOuput_thenConsoleContainsMessage() throws Exception {
		
		// given: a custom console
		StandardConsoleRedirector.saveStdErr(); // JobLogger's ConsoleHandler prints out to StdErr
		StandardConsoleRedirector.setCustomConsole();
		
		// given: a Console Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.newConsoleJobLogger();
		
		// when: logging messages to console
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: retrieve logged message from custom console
		String loggedMessages = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: messages from custom console matches with expected messages
		boolean containsAll = loggedMessages.contains(infoMessage) 
				&& loggedMessages.contains(warningMessage)
				&& loggedMessages.contains(errorMessage);
		Assert.assertTrue("Console logged messages are not the same than expected messages.", containsAll);
	}
	
}
