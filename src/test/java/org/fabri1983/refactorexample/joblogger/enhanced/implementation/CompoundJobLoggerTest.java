package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.fabri1983.refactorexample.joblogger.util.StandardConsoleRedirector;
import org.fabri1983.refactorexample.joblogger.util.XmlDomUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class CompoundJobLoggerTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void whenCreatingCompoundJobLoggerWithNullArray_thenException() {
		
		assertThrows(JobLoggerException.class, () -> {

			// given: a Compound Job Logger with no null array
			JobLoggerFactory.compoundBy((IEnhancedJobLogger[])null);
			
			// then: exception is thrown
		});
	}
	
	@Test
	public void whenCreatingCompoundJobLoggerWithEmptyArray_thenException() {
		
		assertThrows(JobLoggerException.class, () -> {

			// given: a Compound Job Logger with empty array
			JobLoggerFactory.compoundBy(new IEnhancedJobLogger[]{});
			
			// then: exception is thrown
		});
	}
	
	@Test
	public void whenCreatingCompoundJobLogger_thenLoggersAreCreated() throws IOException {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a File Job Logger
		IEnhancedJobLogger fileLogger = JobLoggerFactory.forFile(tempFile);
		
		// given: a Console Job Logger
		IEnhancedJobLogger consoleLogger = JobLoggerFactory.forStdErr();
		
		// given: a logger compounded by two loggers 
		IEnhancedJobLogger compoundLogger = JobLoggerFactory.compoundBy(fileLogger, consoleLogger);
		
		// then: job loggers were created
		assertNotNull(fileLogger);
		assertNotNull(consoleLogger);
		assertNotNull(compoundLogger);
	}
	
	@Test
	public void whenCreatingCompoundJobLogger_thenMessageIsLoggedInEachLogger() throws Exception {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a custom console
		StandardConsoleRedirector.saveStdErr(); // JobLogger's ConsoleHandler prints out to StdErr
		StandardConsoleRedirector.setCustomConsole();
		
		// given: a File Job Logger
		IEnhancedJobLogger fileLogger = JobLoggerFactory.forFile(tempFile);
		
		// given: a Console Job Logger
		IEnhancedJobLogger consoleLogger = JobLoggerFactory.forStdErr();
		
		// given: a logger compounded by two loggers 
		IEnhancedJobLogger logger = JobLoggerFactory.compoundBy(fileLogger, consoleLogger);
		
		// when: logging messages on the compound logger
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: retrieve logged messages from custom console
		String consoleLoggedMessages = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: messages from custom console matches with expected messages
		boolean containsAll = consoleLoggedMessages.contains(infoMessage) 
				&& consoleLoggedMessages.contains(warningMessage)
				&& consoleLoggedMessages.contains(errorMessage);
		assertTrue("Console logged messages are not the same than expected messages.", containsAll);
		
		// then: logged messages exists in file
		List<String> fileLoggedMessages = XmlDomUtil.getMessagesFromLogXmlFile(tempFile);
		assertEquals(Arrays.asList(infoMessage, warningMessage, errorMessage), fileLoggedMessages);
	}
	
}
