package org.fabri1983.refactorexample.joblogger.util;

import static org.junit.Assert.assertTrue;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.SupportingCategoryTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ SupportingCategoryTest.class, AllLoggersCategoryTest.class })
public class StandardConsoleRedirectorTest {

	@Test
	public void whenRedirectStdOutToNewConsole_thenNewConsoleWritesMessages() throws Exception {
		
		// given: a custom console
		StandardConsoleRedirector.saveStdOut();
		StandardConsoleRedirector.setCustomConsole();
		
		// when: logging a message to StdOut console
		String messageToLog = "message to be logged";
		System.out.println(messageToLog);
		
		// then: retrieve logged message from cusotm console
		String loggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: message from custom console matches with expected message
		boolean contains = loggedMessage.contains(messageToLog);
		assertTrue("Console logged message is not the same than expected.", contains);
	}
	
	@Test
	public void whenRedirectStdErroNewConsole_thenNewConsoleWritesMessages() throws Exception {
		
		// given: a custom console
		StandardConsoleRedirector.saveStdErr();
		StandardConsoleRedirector.setCustomConsole();
		
		// when: logging a message to StdErr console
		String messageToLog = "message to be logged";
		System.err.println(messageToLog);
		
		// then: retrieve logged message from cusotm console
		String loggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: message from custom console matches with expected message
		boolean contains = loggedMessage.contains(messageToLog);
		assertTrue("Console logged message is not the same than expected.", contains);
	}
	
}
