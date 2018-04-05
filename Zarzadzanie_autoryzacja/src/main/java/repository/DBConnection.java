package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private Statement statement;
	protected Connection connection;

	public Connection createConnection(Connection connection) {
		String url = "jdbc:hsqldb:hsql://localhost/workdb";
		try {
			connection = DriverManager.getConnection(url);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void executeQuery(String query, Connection connection) {
		try {
			statement = connection.createStatement();
			statement.executeQuery(query);
			statement.close();
			connection.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
