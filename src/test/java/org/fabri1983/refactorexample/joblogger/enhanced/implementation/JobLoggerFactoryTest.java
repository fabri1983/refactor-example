package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.junit.Assert;
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
@PrepareForTest({ DriverManager.class })
@Category({ EnhancedLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class JobLoggerFactoryTest {

	@Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void whenFactoryMethodIsInvoked_thenJobLoggersAreNotNull() throws Exception {
		
		// prepare mocks and expectations
		mockComponentsForOpenConnection();
		
		// given: a log file in temporary folder
		File tempFile = temporaryFolder.newFile("logFile.txt");
		
		// given: a mocked connection
		Connection connection = createConnection();
		
		// given: all possible job loggers
		IEnhancedJobLogger fileLogger = JobLoggerFactory.newFileJobLogger(tempFile);
		IEnhancedJobLogger consoleLogger = JobLoggerFactory.newConsoleJobLogger();
		IEnhancedJobLogger dbLogger = JobLoggerFactory.newDatabaseJobLogger(connection);
		IEnhancedJobLogger compoundLogger = JobLoggerFactory.newCompoundJobLogger(fileLogger, consoleLogger, dbLogger);
		
		// then: job loggers are not null
		Assert.assertNotNull(fileLogger);
		Assert.assertNotNull(consoleLogger);
		Assert.assertNotNull(dbLogger);
		Assert.assertNotNull(compoundLogger);
		
		// then: verify components has been called as per expectations
		PowerMock.verifyAll();
	}
	
	private Connection createConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("anyUrl", new Properties());
		return connection;
	}
	
	private void mockComponentsForOpenConnection() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.isClosed())
				.andReturn(false);
		
		PowerMock.replayAll(mockConnection);
	}
}
