package org.fabri1983.refactorexample.joblogger;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.OldLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.util.StandardConsoleRedirector;
import org.fabri1983.refactorexample.joblogger.util.XmlDomUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({ "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "com.sun.org.apache.xalan.*",
	"javax.management.*", "ch.qos.logback.*", "org.slf4j.*", "javax.activation.*" })
@PrepareForTest({ DriverManager.class, JobLogger.class })
@Category({ OldLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class JobLoggerWithMocksTest {
	
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void whenCreatingLogWithFileOuput_thenFileContainsMessage() throws Exception {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToFileParam = true;
		boolean logMessageParam = true;
		
		// set db params to avoid unexpected exceptions
		Map<String, String> dbParams = setDbParams(false, logMessageParam);
		
		// create static mocks
		createMocksAndSetExpectations(false);
		
		// set static variables
		new JobLogger(logToFileParam, false, false, logMessageParam, false, false, dbParams);
		
		// create the log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		String messageToLog = "message to be logged";
		JobLogger.LogMessage(messageToLog, logMessageParam, false, false);
		
		// verify components has been called as per expectations
		PowerMock.verifyAll();
		
		assertTrue(tempFile.exists());
		String loggedMessage = XmlDomUtil.getMessagesFromLogXmlFile(tempFile).get(0);
		assertEquals(messageToLog, loggedMessage);
	}

	@Test
	public void whenCreatingLogWithConsoleOuput_thenConsoleContainsMessage() throws Exception {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToConsoleParam = true;
		boolean logMessageParam = true;
		
		// set db params to avoid unexpected exceptions
		Map<String, String> dbParams = setDbParams(false, logMessageParam);
		
		// create static mocks
		createMocksAndSetExpectations(false);
		
		// set static variables
		new JobLogger(false, logToConsoleParam, false, logMessageParam, false, false, dbParams);
		
		StandardConsoleRedirector.saveStdErr(); // JobLogger's ConsoleHandler prints out to StdErr
		StandardConsoleRedirector.setCustomConsole();
		
		String messageToLog = "message to be logged";
		JobLogger.LogMessage(messageToLog, logMessageParam, false, false);
		
		// verify components has been called as per expectations
		PowerMock.verifyAll();
		
		String loggedMessage = StandardConsoleRedirector.getCurrentConsoleContent();
		
		StandardConsoleRedirector.restoreStd();
		StandardConsoleRedirector.closeCustomConsole();
		
		boolean contains = loggedMessage.contains(messageToLog);
		assertTrue("Console logged message is not the same than expected message.", contains);
	}

	@Test
	public void whenCreatingLogWithDatabaseOuput_thenDatabaseExecutedStatement() throws Exception {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToDatabaseParam = true;
		boolean logMessageParam = true;
		
		// set db params to avoid unexpected exceptions
		Map<String, String> dbParams = setDbParams(logToDatabaseParam, logMessageParam);
		
		// create static mocks
		createMocksAndSetExpectations(logToDatabaseParam);
		
		// set static variables
		new JobLogger(false, false, logToDatabaseParam, logMessageParam, false, false, dbParams);
		
		String messageToLog = "message to be logged";
		JobLogger.LogMessage(messageToLog, logMessageParam, false, false);
		
		// verify components has been called as per expectations
		PowerMock.verifyAll();
	}
	
	private Map<String, String> setDbParams(boolean logToDatabase, boolean message) throws Exception {
		// set db properties 
		String dbms = "mysql";
		String host = "localhost";
		String port = "3306";
		String userName = "user1"; 
		String password = "pass";
		
		// set db parameters so we avoid any exception before writing to a file
		Map<String, String> dbParams = new HashMap<>((int)(6 / 0.75) + 1);
		dbParams.put("userName", userName);
		dbParams.put("password", password);
		dbParams.put("dbms", dbms);
		dbParams.put("serverName", host);
		dbParams.put("portNumber", port);
		// set the temporary folder
		dbParams.put("logFileFolder", temporaryFolder.getRoot().toPath().toString());
		
		return dbParams;
	}

	private void createMocksAndSetExpectations(boolean logToDatabase) throws Exception {
		
		Connection mockConnection = createMock(Connection.class);
		Statement mockStatement = createMock(Statement.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.createStatement())
				.andReturn(mockStatement);
		
		if (logToDatabase) {
			expect(mockStatement.executeUpdate(anyString()))
					.andReturn(0);
		}
		
		PowerMock.replayAll(mockConnection, mockStatement);
	}
	
}
