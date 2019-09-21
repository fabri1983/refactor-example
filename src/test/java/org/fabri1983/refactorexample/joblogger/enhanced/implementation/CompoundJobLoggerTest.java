package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.io.IOException;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.fabri1983.refactorexample.joblogger.util.StandardConsoleRedirector;
import org.fabri1983.refactorexample.joblogger.util.XmlDomUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class CompoundJobLoggerTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void whenCreatingCompoundJobLogger_thenLoggersAreCreated() throws IOException {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a File Job Logger
		IEnhancedJobLogger fileLogger = JobLoggerFactory.newFileJobLogger(tempFile);
		
		// given: a Console Job Logger
		IEnhancedJobLogger consoleLogger = JobLoggerFactory.newConsoleJobLogger();
		
		// given: a logger compounded by two loggers 
		IEnhancedJobLogger compoundLogger = JobLoggerFactory.newCompoundJobLogger(fileLogger, consoleLogger);
		
		// then: job loggers were created
		Assert.assertNotNull(fileLogger);
		Assert.assertNotNull(consoleLogger);
		Assert.assertNotNull(compoundLogger);
	}
	
	@Test
	public void whenCreatingCompoundJobLogger_thenMessageIsLoggedInEachLogger() throws Exception {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a custom console
		StandardConsoleRedirector.saveStdErr(); // JobLogger's ConsoleHandler prints out to StdErr
		StandardConsoleRedirector.setCustomConsole();
		
		// given: a File Job Logger
		IEnhancedJobLogger fileLogger = JobLoggerFactory.newFileJobLogger(tempFile);
		
		// given: a Console Job Logger
		IEnhancedJobLogger consoleLogger = JobLoggerFactory.newConsoleJobLogger();
		
		// given: a logger compounded by two loggers 
		IEnhancedJobLogger compoundLogger = JobLoggerFactory.newCompoundJobLogger(fileLogger, consoleLogger);
		
		// when: logging a message on the compound logger
		String infoMessage = "this is an info message";
		compoundLogger.info(infoMessage);
		
		// then: retrieve logged message from custom console
		String consoleLoggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		// then: restore system console
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		// then: get logged message from file
		String fileLoggedMessage = XmlDomUtil.getMessagesFromXmlFile(tempFile).get(0);
		
		// then: console message contains expected message
		Assert.assertTrue(consoleLoggedMessage.contains(infoMessage));
		// then: file message matches expected message
		Assert.assertEquals(infoMessage, fileLoggedMessage);
	}
	
}
