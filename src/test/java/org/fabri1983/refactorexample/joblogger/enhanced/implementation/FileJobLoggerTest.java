package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.fabri1983.refactorexample.joblogger.util.XmlDomUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class FileJobLoggerTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test(expected = JobLoggerException.class)
	public void whenCreatingLogWithNullFile_thenException() throws Exception {
		
		// given: a null file reference
		File file = null;
		
		// when: creating a File Job Logger
		JobLoggerFactory.newFileJobLogger(file);
		
		// then: exception is expected
	}
	
	@Test(expected = JobLoggerException.class)
	public void whenCreatingLogWithMissingFile_thenException() throws Exception {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: deleting the file
		temporaryFolder.delete();
		
		// when: creating a File Job Logger
		JobLoggerFactory.newFileJobLogger(tempFile);
		
		// then: exception is expected
	}
	
	@Test
	public void whenCreatingLogWithFileOuput_thenFileContainsMessage() throws Exception {
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a File Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.newFileJobLogger(tempFile);
		
		// when: logging messages
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: logged messages exists in file
		List<String> loggedMessages = XmlDomUtil.getMessagesFromXmlFile(tempFile);
		Assert.assertEquals(Arrays.asList(infoMessage, warningMessage, errorMessage), loggedMessages);
	}
	
}
