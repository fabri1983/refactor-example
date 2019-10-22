package org.fabri1983.refactorexample.joblogger.enhanced.implementation.handler;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.handler.CustomConsoleHandler;
import org.fabri1983.refactorexample.joblogger.util.StandardConsoleRedirector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class CustomConsoleHandlerTest {

	@Before
	public void before() {
		StandardConsoleRedirector.saveStdOut();
		StandardConsoleRedirector.setCustomConsole();
	}
	
	@After
	public void after() throws IOException {
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
	}
	
	@Test
	public void whenCreatingConsoleHandlerWithNullOutputStream_ThenException() {
		
		assertThrows(NullPointerException.class, () -> {
			
			// given: a ConsoleHandler with a null OutputStream
			CustomConsoleHandler cch = new CustomConsoleHandler(null);
			
			// then: exception is thrown
			cch.getLevel();
		});
	}
	
	@Test
	public void whenPublishingALogRecord_ThenItWorksOk() {
		
		// given: a ConsoleHandler for System.out
		CustomConsoleHandler cch = new CustomConsoleHandler(System.out);
		
		// when: publishing a log record
		LogRecord logRecord = new LogRecord(Level.INFO, "A message to StdOut.");
		cch.publish(logRecord);
		
		// then: retrieve logged messages from custom console
		String loggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: published message with expected message
		boolean contains = loggedMessage.contains(logRecord.getMessage());
		assertTrue("Published message is not the same than expected.", contains);
	}
	
	@Test
	public void whenPublishingALogRecordAfterClose_ThenItWorksOk() {
		
		// given: a ConsoleHandler for System.out
		CustomConsoleHandler cch = new CustomConsoleHandler(System.out);
		
		// when: publishing a log record
		LogRecord logRecord1 = new LogRecord(Level.INFO, "A message to StdOut.");
		cch.publish(logRecord1);
		
		// when: closing the handler only forces flush it
		cch.close();
		
		// when: a new log record is published expecting handler hasn't been closed just flushed
		LogRecord logRecord2 = new LogRecord(Level.INFO, 
				"A new message to StdOut after invoking close() which internally is a flush().");
		cch.publish(logRecord2);
		
		// then: retrieve logged messages from custom console
		String loggedMessages = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: messages from custom console matches with expected messages
		boolean containsAll = loggedMessages.contains(logRecord1.getMessage()) 
				&& loggedMessages.contains(logRecord2.getMessage());
		assertTrue("Published messages are not the same than expected.", containsAll);
	}
	
}
