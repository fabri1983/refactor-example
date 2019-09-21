package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

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
import org.junit.Assert;
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
	public void whenCreatingLogWithDatabaseOuputAndDummyConnection_thenNoExceptionIsThrown() throws Exception {
		
		// given: a dummy connection
		Connection connection = createDummyConnection();
		
		// given: a Database Job Logger
		IEnhancedJobLogger logger = JobLoggerFactory.newDatabaseJobLogger(connection);
		
		// when: login messages
		String infoMessage = "info message";
		logger.info(infoMessage);
		String warningMessage = "warning message";
		logger.warn(warningMessage);
		String errorMessage = "error message";
		logger.error(errorMessage);

		// then: no exception is uncaught since they are handle internally
		Assert.assertTrue(true);
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

	private Connection createDummyConnection() {
		return new Connection() {
	
			@Override
			public <T> T unwrap(Class<T> iface) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public Statement createStatement() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public CallableStatement prepareCall(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public String nativeSQL(String sql) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void setAutoCommit(boolean autoCommit) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public boolean getAutoCommit() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public void commit() throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void rollback() throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void close() throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public boolean isClosed() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public DatabaseMetaData getMetaData() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void setReadOnly(boolean readOnly) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public boolean isReadOnly() throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public void setCatalog(String catalog) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public String getCatalog() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void setTransactionIsolation(int level) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public int getTransactionIsolation() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}
	
			@Override
			public SQLWarning getWarnings() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void clearWarnings() throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Map<String, Class<?>> getTypeMap() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void setHoldability(int holdability) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public int getHoldability() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}
	
			@Override
			public Savepoint setSavepoint() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Savepoint setSavepoint(String name) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void rollback(Savepoint savepoint) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void releaseSavepoint(Savepoint savepoint) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
					throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
					int resultSetHoldability) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
					int resultSetHoldability) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Clob createClob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Blob createBlob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public NClob createNClob() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public SQLXML createSQLXML() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public boolean isValid(int timeout) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public void setClientInfo(String name, String value) throws SQLClientInfoException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void setClientInfo(Properties properties) throws SQLClientInfoException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public String getClientInfo(String name) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Properties getClientInfo() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void setSchema(String schema) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public String getSchema() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
	
			@Override
			public void abort(Executor executor) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public int getNetworkTimeout() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
	}
	
}
