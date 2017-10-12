package aggregatorManager;

import java.sql.SQLException;

import agent.Agent;
import database.DbOperations;

/**
 * Checks for timeout of software agents
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class CheckForTimeOut implements Runnable {

	int timeoutTime;
	int checkForTimeOutSleepTime;

	public CheckForTimeOut() {
		this.timeoutTime = Properties.jobRequestInterval * 3;
		this.checkForTimeOutSleepTime = Properties.jobRequestInterval;
	}

	public void run() {
		while (Thread.currentThread().isInterrupted() == false) {
			for (int i = 0; i < Main.activeAgents.getSize(); i++) {
				Agent agent = Main.activeAgents.get(i);
				if (((System.currentTimeMillis() / 1000) - agent.getLastRequestTime()) > timeoutTime) {
					try {
						DbOperations.deactivateAgent(agent);
					} catch (SQLException e) {
						System.err.println("A problem occured with the database");
					}
				}
			}
			try {
				Thread.sleep(checkForTimeOutSleepTime * 1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
