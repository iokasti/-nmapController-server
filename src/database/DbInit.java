package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The DbInit saves all properties read from dbProperties.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public abstract class DbInit {

	/**
	 * Address of database server.
	 */
	static String databaseServer;

	/**
	 * Port of database server address.
	 */
	static String databasePort;

	/**
	 * Name of db to connect.
	 */
	static String databaseName;

	/**
	 * Username for the db.
	 */
	static String databaseUsername;

	/**
	 * Password for the db.
	 */
	static String databasePassword;

	public static void initialize() throws IOException {
		String dbPropertyFile = "dbProperties";
		BufferedReader property = new BufferedReader(new FileReader(dbPropertyFile));
		String line;
		while ((line = property.readLine()) != null) {
			String[] propertyInfo = line.split(" ");
			if (propertyInfo[0].equals("databaseServer")) {
				databaseServer = propertyInfo[1];
			} else if (propertyInfo[0].equals("databasePort")) {
				databasePort = propertyInfo[1];
			} else if (propertyInfo[0].equals("databaseName")) {
				databaseName = propertyInfo[1];
			} else if (propertyInfo[0].equals("databaseUsername")) {
				databaseUsername = propertyInfo[1];
			} else if (propertyInfo[0].equals("databasePassword")) {
				databasePassword = propertyInfo[1];
			}
		}
		property.close();
	}
}
