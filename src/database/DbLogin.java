package database;

import static database.DbConnect.connectToDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The DbLogin is used to login a user.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class DbLogin {

	/**
	 * Checks user credentials.
	 * 
	 * @param username
	 * @param password
	 * @return true if credentials were correct, false otherwise
	 * @throws SQLException
	 */
	public static boolean login(String username, String password) throws SQLException {
		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			// execute query to check credentials
			// this query will return 1 if credentials were right
			String loginSQL = "SELECT 1 FROM admin WHERE username=? AND password=? AND active=1";
			PreparedStatement ps = connection.prepareStatement(loginSQL);
			ps.setString(1, username);
			ps.setString(2, sha256(password));
			ResultSet rs = ps.executeQuery();

			// check if credentials were correct
			if (rs.next()) {
				// 1 entry returned, credentials were correct
				return true;
			} else {
				// 0 entries return, credentials were wrong
				return false;
			}
		}
		return false;
	}

	/**
	 * Passes a password from the sha256 hash function
	 * 
	 * @param data
	 * @return sha256 encrypted password.
	 */
	private static String sha256(String data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Something went wrong hashing the data.");
		}
		md.update(data.getBytes());
		return bytesToHex(md.digest());
	}

	/**
	 * Used by sha256 function.
	 * 
	 * @param bytes
	 * @return sha256 encrypted password.
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
		for (byte byt : bytes)
			result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}
}
