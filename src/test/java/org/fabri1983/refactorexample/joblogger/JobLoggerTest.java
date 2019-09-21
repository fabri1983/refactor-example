package org.fabri1983.refactorexample.joblogger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.fabri1983.refactorexample.joblogger.category.AllLoggersCategoryTest;
import org.fabri1983.refactorexample.joblogger.category.OldLoggerCategoryTest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
@Category({ OldLoggerCategoryTest.class, AllLoggersCategoryTest.class })
public class JobLoggerTest {
	
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void whenCreatingLogWithNoOutput_thenException() {
		
		// set static variables
		new JobLogger(false, false, false, false, false, false, Collections.emptyMap());
		
		Exception e = Assert.assertThrows(Exception.class, () -> {
			JobLogger.LogMessage("msg", false, false, false);
		});
		
		Assert.assertEquals("Invalid configuration", e.getMessage());
	}
	
	@Test
	public void whenCreatingLogWithNoLevel_thenException() {
		
		// set at least one of the output arguments to avoid other exception message
		boolean logToConsoleParam = true;
		
		// set static variables
		new JobLogger(false, logToConsoleParam, false, false, false, false, Collections.emptyMap());
		
		Exception e = Assert.assertThrows(Exception.class, () -> {
			JobLogger.LogMessage("msg", false, false, false);
		});
		
		Assert.assertEquals("Error or Warning or Message must be specified", e.getMessage());
	}
	
	@Test
	public void whenCreatingLogAndSendNullMessage_thenException() {
		
		Assert.assertThrows(NullPointerException.class, () -> {
			JobLogger.LogMessage(null, false, false, false);
		});
	}
	
	@Test
	public void whenCreatingLogAndSendEmptyMessage_thenNothingHappens() throws Exception {
		
		String emptyMsg = "";
		JobLogger.LogMessage(emptyMsg, false, false, false);
	}
	
	@Test
	public void whenCreatingLogWithDatabaseOuputAndNullProperties_thenException() {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToDatabaseParam = true;
		boolean logMessageParam = true;
		
		// set static variables
		new JobLogger(false, false, logToDatabaseParam, logMessageParam, false, false, null);
		
		Assert.assertThrows(NullPointerException.class, () -> {
			JobLogger.LogMessage("msg", logMessageParam, false, false);
		});
	}
	
	@Test
	@Parameters(method = "dbParamsUserLoginForTest")
	public void whenCreatingLogWithDatabaseOuputAndMissingUserLoginProperties_thenException(
			String userName, String password) {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToDatabaseParam = true;
		boolean logMessageParam = true;
		
		// set db parameters
		Map<String, String> dbParams = new HashMap<>((int)(2 / 0.75) + 1);
		dbParams.put("userName", userName);
		dbParams.put("password", password);
		
		// set static variables
		new JobLogger(false, false, logToDatabaseParam, logMessageParam, false, false, dbParams);
		
		
		Assert.assertThrows(NullPointerException.class, () -> {
			JobLogger.LogMessage("msg", logMessageParam, false, false);
		});
	}
	
	@Test
	@Parameters(method = "dbParamsForTest")
	public void whenCreatingLogWithDatabaseOuputAndWrongProperties_thenException(
			String userName, String password, String dbms, String serverName, String portNumber) {
		
		// set one of the output arguments and one of the level arguments to avoid other exception message
		boolean logToDatabaseParam = true;
		boolean logMessageParam = true;
		
		// set db parameters
		Map<String, String> dbParams = new HashMap<>((int)(5 / 0.75) + 1);
		dbParams.put("userName", userName);
		dbParams.put("password", password);
		dbParams.put("dbms", dbms);
		dbParams.put("serverName", serverName);
		dbParams.put("portNumber", portNumber);
		
		// set static variables
		new JobLogger(false, false, logToDatabaseParam, logMessageParam, false, false, dbParams);
		
		
		Assert.assertThrows(SQLException.class, () -> {
			JobLogger.LogMessage("msg", logMessageParam, false, false);
		});
	}
	
	protected Object[] dbParamsUserLoginForTest() {
		return new Object[] {
				new Object[] { null, null },
				new Object[] { "user1", null },
				new Object[] { null, "pass" }
		};
	}

	protected Object[] dbParamsForTest() {
		return new Object[] {
				new Object[] { "user1", "pass", null, null, null },
				new Object[] { "user1", "pass", "mysql", null, null },
				new Object[] { "user1", "pass", "mysql", "localhost", null },
				new Object[] { "user1", "pass", null, "localhost", "3306" },
				new Object[] { "user1", "pass", null, "localhost", null },
				new Object[] { "user1", "pass", null, null, "3306" }
		};
	}

}
