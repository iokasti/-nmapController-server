package database;

import static database.DbConnect.connectToDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import agent.Agent;
import aggregatorManager.Main;
import nmapJob.Job;
import nmapJob.Result;

/**
 * The DbOperations is used for all the operations on the database.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2015-12-31
 */
public class DbOperations {

	/**
	 * Inserts an agent to the database.
	 * 
	 * @param agent
	 * @throws SQLException
	 */
	public static void acceptAgent(Agent agent) throws SQLException {
		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			String checkAgentSQL = "SELECT 1 FROM softwareAgents WHERE hash=?";
			PreparedStatement ps = connection.prepareStatement(checkAgentSQL);
			ps.setInt(1, agent.getHash());
			ResultSet rs = ps.executeQuery();

			// check if agent already exists
			if (rs.next()) {
				// 1 entry returned, agent already exists update to accepted and
				// active
				String updateAgentSQL = "UPDATE softwareAgents SET active=? WHERE hash=?";
				PreparedStatement ps2 = connection.prepareStatement(updateAgentSQL);
				ps2.setBoolean(1, agent.getActive());
				ps2.setInt(2, agent.getHash());
				ps2.executeUpdate();
			} else {
				// 0 entries return, agent doesn't exist
				// prepare update
				String insertAgentSQL = "INSERT INTO softwareAgents"
						+ "(device_name, device_ip, device_mac_address, os_version, nmap_version, hash, active) VALUES"
						+ "(?,?,?,?,?,?,?)";
				PreparedStatement ps3 = connection.prepareStatement(insertAgentSQL);
				ps3.setString(1, agent.getDeviceName());
				ps3.setString(2, agent.getIp());
				ps3.setString(3, agent.getMacAddress());
				ps3.setString(4, agent.getOsVersion());
				ps3.setString(5, agent.getNmapVersion());
				ps3.setInt(6, agent.getHash());
				ps3.setBoolean(7, agent.getActive());

				// execute update
				ps3.executeUpdate();
			}

		}
	}

	/**
	 * Set agents active flag to false.
	 * 
	 * @param agent
	 * @throws SQLException
	 */
	public static void deactivateAgent(Agent agent) throws SQLException {
		// remove agent from active
		Main.activeAgents.removeElement(agent);

		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			String updateAgentSQL = "UPDATE softwareAgents SET active=? WHERE hash=?";
			PreparedStatement ps2 = connection.prepareStatement(updateAgentSQL);
			ps2.setBoolean(1, false);
			ps2.setInt(2, agent.getHash());
			ps2.executeUpdate();
		}
	}

	/**
	 * Sets agent active flag to true.
	 * 
	 * @param agentHash
	 * @throws SQLException
	 */
	public static void activateAgent(int agentHash) throws SQLException {
		// connect to database
		Connection connection = connectToDB();

		if (connection != null) {
			String updateAgentSQL = "UPDATE softwareAgents SET active=? WHERE hash=?";
			PreparedStatement ps2 = connection.prepareStatement(updateAgentSQL);
			ps2.setBoolean(1, true);
			ps2.setInt(2, agentHash);
			ps2.executeUpdate();

			String checkAgentSQL = "SELECT device_name, device_ip, device_mac_address,os_version,nmap_version,active FROM softwareAgents WHERE hash=?";
			PreparedStatement ps = connection.prepareStatement(checkAgentSQL);
			ps.setInt(1, agentHash);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Main.activeAgents.addElement(new Agent(rs.getString("device_name"), rs.getString("device_ip"),
						rs.getString("device_mac_address"), rs.getString("os_version"), rs.getString("nmap_version"),
						agentHash, true, true));
			}
		}
	}

	/**
	 * Returns all agents in the database.
	 * 
	 * @return ArrayList with all agents
	 * @throws SQLException
	 */
	public static ArrayList<Agent> getAllAgents() throws SQLException {
		ArrayList<Agent> agents = new ArrayList<Agent>();
		Connection connection = connectToDB();

		if (connection != null) {
			String getAgentsSQL = "SELECT * FROM softwareAgents";
			ResultSet rs = null;
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(getAgentsSQL);
			while (rs.next()) {
				Agent agent = new Agent(rs.getString("device_name"), rs.getString("device_ip"),
						rs.getString("device_mac_address"), rs.getString("os_version"), rs.getString("nmap_version"),
						rs.getInt("hash"), rs.getBoolean("active"), false);
				agents.add(agent);
			}

		}
		return agents;
	}

	/**
	 * Inserts a job in the database.
	 * 
	 * @param job
	 * @param agentHash
	 * @throws SQLException
	 */
	public static void insertJob(Job job, int agentHash) throws SQLException {
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

			String insertJobSQL = "INSERT INTO jobs" + "(id, parameters, periodic, time, status, softwareAgents_id_sa)"
					+ "VALUES" + "(?, ?, ?, ?, ?, ?)";
			PreparedStatement ps3 = connection.prepareStatement(insertJobSQL);
			ps3.setInt(1, job.getId());
			ps3.setString(2, job.getParameters());
			ps3.setBoolean(3, job.isPeriodic());
			ps3.setInt(4, job.getTime());
			ps3.setString(5, "pending");
			ps3.setInt(6, id_sa);

			// execute update
			ps3.executeUpdate();
		}
	}

	/**
	 * Sets job status to running.
	 * 
	 * @param jobID
	 * @param agentHash
	 * @throws SQLException
	 */
	public static void setJobStatusRunning(int jobID, int agentHash) throws SQLException {
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

			String updateJobSQL = "UPDATE jobs SET status=? WHERE id=? AND softwareAgents_id_sa=?";

			PreparedStatement ps2 = connection.prepareStatement(updateJobSQL);
			ps2.setString(1, "running");
			ps2.setInt(2, jobID);
			ps2.setInt(3, id_sa);
			ps2.executeUpdate();

		}
	}

	/**
	 * Sets job status to done.
	 * 
	 * @param jobID
	 * @param agentHash
	 * @throws SQLException
	 */
	public static void setJobStatusDone(int jobID, int agentHash) throws SQLException {
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

			String updateJobSQL = "UPDATE jobs SET status=? WHERE id=? AND softwareAgents_id_sa=?";

			PreparedStatement ps2 = connection.prepareStatement(updateJobSQL);
			ps2.setString(1, "done");
			ps2.setInt(2, jobID);
			ps2.setInt(3, id_sa);
			ps2.executeUpdate();

		}
	}

	/**
	 * Inserts a result to the database.
	 * 
	 * @param jobID
	 * @param agentHash
	 * @param resultXML
	 * @throws SQLException
	 */
	public static void insertResult(int jobID, int agentHash, String resultXML) throws SQLException {
		Connection connection = connectToDB();
		if (connection != null) {
			setJobStatusDone(jobID, agentHash);

			String checkAgentSQL = "SELECT id_sa FROM softwareAgents WHERE hash=?";
			PreparedStatement ps = connection.prepareStatement(checkAgentSQL);
			ps.setInt(1, agentHash);
			ResultSet rs = ps.executeQuery();
			int id_sa = 0;
			while (rs.next()) {
				id_sa = rs.getInt("id_sa");
			}

			String checkJobSQL = "SELECT id_jobs FROM jobs WHERE id=? AND softwareAgents_id_sa=?";
			PreparedStatement ps2 = connection.prepareStatement(checkJobSQL);
			ps2.setInt(1, jobID);
			ps2.setInt(2, id_sa);
			ResultSet rs2 = ps2.executeQuery();
			int id_jobs = 0;
			while (rs2.next()) {
				id_jobs = rs2.getInt("id_jobs");
			}

			String insertResultSQL = "INSERT INTO results" + "(xml, jobs_id_jobs)" + "VALUES" + "(?, ?)";
			PreparedStatement ps3 = connection.prepareStatement(insertResultSQL);
			ps3.setString(1, resultXML);
			ps3.setInt(2, id_jobs);

			// execute update
			ps3.executeUpdate();
		}
	}

	/**
	 * Gets all results from the database.
	 * 
	 * @return ArrayList with all the results.
	 * @throws SQLException
	 */
	public static ArrayList<Result> getAllResults() throws SQLException {
		ArrayList<Result> results = new ArrayList<Result>();
		Connection connection = connectToDB();

		if (connection != null) {
			String getResultsSQL = "SELECT * FROM results";
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
		return results;
	}

	/**
	 * Gets specific agent results from the database.
	 * 
	 * @return ArrayList with the results.
	 * @throws SQLException
	 */
	public static ArrayList<Result> getAgentResults(int agentHash) throws SQLException {
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
			while (rs2.next()) {
				int id_jobs = rs2.getInt("id_jobs");
				String getResultsSQL = "SELECT * FROM results WHERE jobs_id_jobs=?";
				PreparedStatement ps3 = connection.prepareStatement(getResultsSQL);
				ps3.setInt(1, id_jobs);
				ResultSet rs3 = ps3.executeQuery();
				while (rs3.next()) {
					Result result = new Result(rs3.getString("xml"), id_jobs, rs3.getTimestamp("date_created"));
					results.add(result);
				}
			}
		}
		return results;
	}

	/**
	 * Sets all agents active flag to false.
	 * 
	 * @throws SQLException
	 */
	public static void setAllAgentsOffline() throws SQLException {
		Connection connection = connectToDB();
		if (connection != null) {
			String updateAgentsSQL = "UPDATE softwareAgents SET active=?";

			PreparedStatement ps2 = connection.prepareStatement(updateAgentsSQL);
			ps2.setBoolean(1, false);
			ps2.executeUpdate();
		}
	}

	/**
	 * Returns an array list with all jobs.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Job> getAllJobs() throws SQLException {
		ArrayList<Job> jobs = new ArrayList<Job>();
		Connection connection = connectToDB();

		if (connection != null) {
			String getAgentsSQL = "SELECT * FROM jobs";
			ResultSet rs = null;
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(getAgentsSQL);
			while (rs.next()) {
				Job job = new Job(rs.getInt("id"), rs.getString("parameters"), rs.getBoolean("periodic"),
						rs.getInt("time"));
				jobs.add(job);
			}

		}
		return jobs;
	}
}
