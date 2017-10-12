package database;

import static database.DbConnect.connectToDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.text.Document;

import org.apache.commons.lang3.StringUtils;

import Android.AndroidUser;
import nmapJob.Result;

/**
 * The DbAndroid is used for all the operations on the database by the android
 * application.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
public class DbAndroid {
	
	/**
	 * Inserts a new user to the Android users table.
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static boolean registerUser(AndroidUser user) throws SQLException {
		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			String checkUsernameSQL = "SELECT 1 FROM androidUsers WHERE username=?";
			PreparedStatement ps = connection.prepareStatement(checkUsernameSQL);
			ps.setString(1, user.getUsername());
			ResultSet rs = ps.executeQuery();

			// check if use already exists
			if (rs.next()) {
				// 1 entry returned, agent already exists
				return false;
			} else {
				// 0 entries return, user doesn't exist
				String insertUserSQL = "INSERT INTO androidUsers" + "(username, password, active) VALUES" + "(?,?,?)";
				PreparedStatement ps3 = connection.prepareStatement(insertUserSQL);
				ps3.setString(1, user.getUsername());
				ps3.setString(2, sha256(user.getPassword()));
				ps3.setBoolean(3, true);

				// execute update
				ps3.executeUpdate();

				return true;
			}

		}
		return false;
	}

	/**
	 * Returns true or false, depending on the user credentials.
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static boolean loginUser(AndroidUser user) throws SQLException {
		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			String autenticateUserSQL = "SELECT 1 FROM androidUsers WHERE username=? AND password=? AND active=1";
			PreparedStatement ps = connection.prepareStatement(autenticateUserSQL);
			ps.setString(1, user.getUsername());
			ps.setString(2, sha256(user.getPassword()));
			ResultSet rs = ps.executeQuery();

			// check if use already exists
			if (rs.next()) {
				// 1 entry returned, agent already exists
				return true;
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

	/**
	 * Returns the latest {numberOfResults} results.
	 * 
	 * @param numberOfResults
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String getTopResults(int numberOfResults) throws SQLException, IOException {
		ArrayList<Result> results = new ArrayList<Result>();
		Connection connection = connectToDB();

		if (connection != null) {
			String getResultsSQL = "SELECT * FROM results ORDER BY results.date_created DESC LIMIT " + numberOfResults;
			ResultSet rs = null;
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(getResultsSQL);
			while (rs.next()) {
				String getJobId = "SELECT id FROM jobs WHERE id_jobs=?";
				PreparedStatement ps2 = connection.prepareStatement(getJobId);
				ps2.setInt(1, rs.getInt("jobs_id_jobs"));
				ResultSet rs2 = ps2.executeQuery();
				int jobId = 0;
				while (rs2.next()) {
					jobId = rs2.getInt("id");
				}

				Result result = new Result(rs.getString("xml"), jobId, rs.getTimestamp("date_created"));
				results.add(result);
			}
		}

		// convert all results to html
		String htmlBody = "";
		for (Result result : results) {
			htmlBody += (getHTML(result.getResult())) + "\n";
		}
		String htmlResult = createResultsHTML(htmlBody);
		return htmlResult;
	}

	/**
	 * Returns the latest {numberOfResults} results for the specified SA.
	 * 
	 * @param numberOfResults
	 * @param agentHash
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String getTopResults(int numberOfResults, int agentHash) throws SQLException, IOException {
		ArrayList<Result> results = new ArrayList<Result>();
		Connection connection = connectToDB();
		if (connection != null) {
			String checkAgentSQL = "SELECT id_sa FROM softwareAgents WHERE hash=?";
			PreparedStatement ps = connection.prepareStatement(checkAgentSQL);
			ps.setInt(1, agentHash);
			ResultSet rs = ps.executeQuery();
			int id_sa = 0;
			while (rs.next()) {
				id_sa = rs.getInt("id_sa");
			}

			String checkJobSQL = "SELECT id_jobs FROM jobs WHERE softwareAgents_id_sa=?";
			PreparedStatement ps2 = connection.prepareStatement(checkJobSQL);
			ps2.setInt(1, id_sa);
			ResultSet rs2 = ps2.executeQuery();
			int c = 0;
			while (rs2.next()) {
				int id_jobs = rs2.getInt("id_jobs");
				String getResultsSQL = "SELECT * FROM results WHERE jobs_id_jobs=? ORDER BY results.date_created DESC LIMIT "
						+ numberOfResults;
				PreparedStatement ps3 = connection.prepareStatement(getResultsSQL);
				ps3.setInt(1, id_jobs);
				ResultSet rs3 = ps3.executeQuery();
				while (rs3.next()) {
					c++;
					Result result = new Result(rs3.getString("xml"), id_jobs, rs3.getTimestamp("date_created"));
					results.add(result);
					if (c == numberOfResults) {
						break;
					}
				}
				if (c == numberOfResults) {
					break;
				}
			}
		}

		// convert all results to html
		String htmlBody = "";
		for (Result result : results) {
			htmlBody += (getHTML(result.getResult())) + "\n";
		}
		String htmlResult = createResultsHTML(htmlBody);
		return htmlResult;
	}

	/**
	 * Returns an xml string to html format. Uses exec with xsltproc. input
	 * string is written to a temp.xml file and output is written to a temp.html
	 * file. Output is read in a string. Then we keep only the text between the
	 * html body tags and return it after both temp files are deleted.
	 * 
	 */
	private static String getHTML(String xml) throws IOException {
		PrintWriter writer = new PrintWriter("temp.xml", "UTF-8");
		writer.println(xml);
		writer.close();

		String command = "xsltproc temp.xml -o temp.html";
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while ((reader.readLine()) != null) {
		}

		String temp_result = "";
		temp_result = readFile("temp.html");

		String result = StringUtils.substringBetween(temp_result, "<body>", "</body>");

		File fxml = new File("temp.xml");
		File fhtml = new File("temp.html");
		fxml.delete();
		fhtml.delete();
		return result;
	}

	/**
	 * Reads a file in a string.
	 * 
	 * @param filename
	 * @return String containing all the file
	 */
	private static String readFile(String filename) {
		File f = new File(filename);
		try {
			byte[] bytes = Files.readAllBytes(f.toPath());
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return "";
	}

	/**
	 * Creates a valid html String.
	 * 
	 * @param body
	 * @param output
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private static String createResultsHTML(String body) throws IOException {
		StringWriter writer = new StringWriter();
		writer.write("<html>");
		writer.append("<head>");
		writer.append("<title>Nmap Scan Reports</title>");
		writer.append("</head>");
		writer.append("<body>");
		writer.append(body);
		writer.append("</body>");
		writer.append("</html>");
		writer.close();
		return writer.toString();
	}
}
