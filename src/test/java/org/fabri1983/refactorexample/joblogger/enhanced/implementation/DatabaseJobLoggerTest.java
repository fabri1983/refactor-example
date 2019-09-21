package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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
public class DatabaseJobLoggerTest {
	
	@Before
	public void setUp() throws Exception {
		mockComponentsForThreeCalls();
	}
	
	@Test
	public void whenCreatingLogWithDatabaseOuput_thenDatabaseExecutedStatement() throws Exception {
		
		// given: a mocked connection
		Connection connection = createConnection();
		
		// given: a Database Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.newDatabaseJobLogger(connection);
		
		// when: login messages
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);
		
		// then: verify components has been called as per expectations
		PowerMock.verifyAll();
	}

	private Connection createConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("url", new Properties());
		return connection;
	}

	private void mockComponentsForThreeCalls() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		PreparedStatement mockStatement = createMock(PreparedStatement.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.prepareStatement(anyString()))
				.andReturn(mockStatement)
				.times(3);
		
		expect(mockStatement.execute())
				.andReturn(true)
				.times(3);
		
		for (int i=0; i < 3; ++i) {
			mockStatement.setString(anyInt(), anyString());
			expectLastCall();
			mockStatement.setInt(anyInt(), anyInt());
			expectLastCall();
			mockStatement.close();
			expectLastCall();
		}
		
		PowerMock.replayAll(mockConnection, mockStatement);
	}
	
}
