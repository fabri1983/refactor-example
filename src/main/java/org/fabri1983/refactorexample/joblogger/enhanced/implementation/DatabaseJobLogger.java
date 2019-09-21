package org.fabri1983.refactorexample.joblogger.enhanced.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fabri1983.refactorexample.joblogger.enhanced.contract.IEnhancedJobLogger;

public class DatabaseJobLogger extends JobLoggerSlf4jBridge implements IEnhancedJobLogger {

	private Connection connection;
	private String sqlInserteQuery = "insert into Log_Values(?, ?)";
	
	public DatabaseJobLogger(Connection connection) {
		super(DatabaseJobLogger.class.getSimpleName());
		this.connection = connection;
	}

	@Override
	public void info(String message) {
		try (PreparedStatement prepStatement = connection.prepareStatement(sqlInserteQuery)) {
			prepStatement.setString(1, message);
			prepStatement.setInt(2, 1);
			prepStatement.execute();
		} catch (SQLException ex) {
			logger.severe(ex.getMessage());
		}
	}

	@Override
	public void warn(String message) {
		try (PreparedStatement prepStatement = connection.prepareStatement(sqlInserteQuery)) {
			prepStatement.setString(1, message);
			prepStatement.setInt(2, 3);
			prepStatement.execute();
		} catch (SQLException ex) {
			logger.severe(ex.getMessage());
		}
	}

	@Override
	public void error(String message) {
		try (PreparedStatement prepStatement = connection.prepareStatement(sqlInserteQuery)) {
			prepStatement.setString(1, message);
			prepStatement.setInt(2, 2);
			prepStatement.execute();
		} catch (SQLException ex) {
			logger.severe(ex.getMessage());
		}
	}
	
}
