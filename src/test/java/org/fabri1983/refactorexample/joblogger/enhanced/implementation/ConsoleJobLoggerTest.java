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
		
		// when: logging a message to console
		String messageToLog = "message to be logged";
		logger.info(messageToLog);
		
		// then: retrieve logged message from cusotm console
		String loggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: message from custom console matches with expected message
		boolean contains = loggedMessage.contains(messageToLog);
		Assert.assertTrue("Console logged message is not the same than expected message.", contains);
	}
	
}
