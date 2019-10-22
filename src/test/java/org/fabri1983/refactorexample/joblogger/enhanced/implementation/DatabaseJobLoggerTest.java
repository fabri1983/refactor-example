package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertThrows;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.EnhancedLoggerCategoryTest;
import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;
import org.fabri1983.refactorexample.joblogger.enhanced.exception.JobLoggerException;
import org.fabri1983.refactorexample.joblogger.enhanced.factory.JobLoggerFactory;
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
	
	@Test
	public void whenCreatingLogWithDatabaseOuputWithNullConnection_thenException() throws Exception {
		
		assertThrows(JobLoggerException.class, () -> {

			// given: a null connection
			Connection connection = null;
			
			// given: a Database Job Logger
			JobLoggerFactory.forDatabase(connection);
			
			// then: exception is raised
		});
	}
	
	@Test
	public void whenCreatingLogWithDatabaseOuputWithClosedConnection_thenException() throws Exception {
		
		assertThrows(JobLoggerException.class, () -> {

			// prepare mocks and expectations
			mockComponentsForClosedConnection();
			
			// given: a null connection
			Connection connection = createConnection();
			
			// given: a Database Job Logger
			JobLoggerFactory.forDatabase(connection);
			
			// then: verify components has been called as per expectations
			PowerMock.verifyAll();
			
			// then: exception is raised
		});
	}
	
	@Test
	public void whenCreatingLogWithDatabaseOuputWithErrorConnection_thenException() throws Exception {
		
		assertThrows(JobLoggerException.class, () -> {

			// prepare mocks and expectations
			mockComponentsForErrorConnection();
			
			// given: a null connection
			Connection connection = createConnection();
			
			// given: a Database Job Logger
			JobLoggerFactory.forDatabase(connection);
			
			// then: verify components has been called as per expectations
			PowerMock.verifyAll();
			
			// then: exception is raised
		});
	}
	
	@Test
	public void whenCreatingLogWithDatabaseOuputWithDummyStatement_thenExceptionIsCaughtInternally() throws Exception {
		
		// prepare mocks and expectations
		mockComponentsForThreeExceptionsOnStatementCall();
		
		// given: a mocked connection
		Connection connection = createConnection();
		
		// given: a Database Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.forDatabase(connection);
		
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
	
	@Test
	public void whenCreatingLogWithDatabaseOuput_thenDatabaseExecutedStatement() throws Exception {
		
		// prepare mocks and expectations
		mockComponentsForThreeCalls();
		
		// given: a mocked connection
		Connection connection = createConnection();
		
		// given: a Database Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.forDatabase(connection);
		
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
		Connection connection = DriverManager.getConnection("anyUrl", new Properties());
		return connection;
	}

	private void mockComponentsForClosedConnection() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.isClosed())
				.andReturn(true);
		
		PowerMock.replayAll(mockConnection);
	}
	
	private void mockComponentsForErrorConnection() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.isClosed())
				.andThrow(new SQLException("Dummy Connection Exception"));
		
		PowerMock.replayAll(mockConnection);
	}
	
	private void mockComponentsForThreeExceptionsOnStatementCall() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		PreparedStatement mockStatement = createMock(PreparedStatement.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.isClosed())
				.andReturn(false);
		
		expect(mockConnection.prepareStatement(anyString()))
				.andThrow(new SQLException("Dummy Statement Exception"))
				.times(3);
		
		PowerMock.replayAll(mockConnection, mockStatement);
	}

	private void mockComponentsForThreeCalls() throws SQLException {
		Connection mockConnection = createMock(Connection.class);
		PreparedStatement mockStatement = createMock(PreparedStatement.class);
		
		mockStatic(DriverManager.class);
		expect(DriverManager.getConnection(anyString(), anyObject()))
				.andReturn(mockConnection);
		
		expect(mockConnection.isClosed())
				.andReturn(false);
		
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
