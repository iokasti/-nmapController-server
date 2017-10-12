package agent;

import java.sql.SQLException;

import aggregatorManager.Main;
import database.DbOperations;

/**
 * The Agent class represents a software agent.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-31-12
 */
public class Agent {

	/**
	 * Terminal device name.
	 */
	private String deviceName;

	/**
	 * Interface IP.
	 */
	private String ip;

	/**
	 * Interface MacAddr.
	 */
	private String macAddress;

	/**
	 * Terminal OS Version.
	 */
	private String osVersion;

	/**
	 * nmap Version.
	 */
	private String nmapVersion;

	/**
	 * hash.
	 */
	private int hash;

	/**
	 * If set to true agent is active.
	 */
	private boolean active;

	/**
	 * Saves the seconds of last request.
	 */
	private long lastRequestTime;

	/**
	 * Constructor for the Agent class.
	 * 
	 * @param deviceName
	 * @param ip
	 * @param macAddress
	 * @param osVersion
	 * @param nmapVersion
	 * @param hash
	 * @param active
	 * @param accepted
	 */
	public Agent(String deviceName, String ip, String macAddress, String osVersion, String nmapVersion, int hash,
			boolean active, boolean setRequestTime) {
		this.deviceName = deviceName;
		this.ip = ip;
		this.macAddress = macAddress;
		this.osVersion = osVersion;
		this.nmapVersion = nmapVersion;
		this.hash = hash;
		this.active = active;
		if (setRequestTime == true) {
			lastRequestTime = System.currentTimeMillis() / 1000;
		}
	}

	/**
	 * Getter function for the device name.
	 * 
	 * @return deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Getter function for the IP of the device.
	 * 
	 * @return ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Getter function for the mac address of the device.
	 * 
	 * @return macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Getter function for the terminal os version.
	 * 
	 * @return osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * Getter function for the nmap version.
	 * 
	 * @return nmapVersion
	 */
	public String getNmapVersion() {
		return nmapVersion;
	}

	/**
	 * Getter function for the hash.
	 * 
	 * @return hash
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * Getter function for the active flag.
	 * 
	 * @return active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * Getter function for the last request time.
	 * 
	 * @return lastRequestTime
	 */
	public long getLastRequestTime() {
		return lastRequestTime;
	}

	/**
	 * Setter function for the last request time.
	 */
	public void setLastRequestTime() {
		this.lastRequestTime = System.currentTimeMillis() / 1000;
	}

	@Override
	public String toString() {
		return deviceName + ", " + ip + ", " + macAddress + ", " + osVersion + ", " + nmapVersion + ", " + hash + ", "
				+ (active ? "online" : "offline");
	}

	public String toJSON() {
		return "{\"deviceName\":\"" + deviceName + "\",\"ip\":\"" + ip + "\",\"macAddress\":\"" + macAddress
				+ "\",\"osVersion\":\"" + osVersion + "\",\"nmapVersion\":\"" + nmapVersion + "\",\"hash\":\"" + hash
				+ "\",\"status\":\"" + (active ? "online" : "offline") + "\"}";
	}

	/**
	 * If agent is waiting to be accepted, accept it.
	 */
	public void accept() {
		this.active = false;
		try {
			DbOperations.acceptAgent(this);
		} catch (SQLException e) {
			System.err.println("Something went wrong accepting agent with hash: " + hash);
		}
		Main.agentsToBeAccepted.add(this);
	}

	/**
	 * If agent is waiting to be accepted, reject it.
	 */
	public void reject() {
		this.active = false;
		Main.agentsToBeCanceled.add(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agent other = (Agent) obj;
		if (deviceName == null) {
			if (other.deviceName != null)
				return false;
		} else if (!deviceName.equals(other.deviceName))
			return false;
		if (hash != other.hash)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (nmapVersion == null) {
			if (other.nmapVersion != null)
				return false;
		} else if (!nmapVersion.equals(other.nmapVersion))
			return false;
		if (osVersion == null) {
			if (other.osVersion != null)
				return false;
		} else if (!osVersion.equals(other.osVersion))
			return false;
		return true;
	}
}
