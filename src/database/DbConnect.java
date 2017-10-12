package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * The DbConnect is used to connect to the database specified.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class DbConnect {
	public static Connection connectToDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String Db_URL = "jdbc:mysql://" + DbInit.databaseServer + ":" + DbInit.databasePort + "/"
					+ DbInit.databaseName;
			Connection connection = DriverManager.getConnection(Db_URL, DbInit.databaseUsername,
					DbInit.databasePassword);

			return connection;
		} catch (ClassNotFoundException e) {
			System.err.println("Driver not found");
		} catch (SQLException e) {
			System.err.println("Please check your database connectivity: " + e.getMessage());
			System.exit(-1);
		}
		return null;
	}
}
