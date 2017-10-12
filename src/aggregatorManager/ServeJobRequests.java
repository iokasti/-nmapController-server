package aggregatorManager;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import agent.Agent;
import database.DbOperations;
import nmapJob.Job;

/**
 * ServeJobRequests gets post from agents, including their hash and if any jobs
 * exists for them, it packages them in a json list and sends them to the SAs.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
@Path("requests")
public class ServeJobRequests {
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClientInfoJSON(String agentInfoJSON) {
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) (new JSONParser()).parse(agentInfoJSON);
		} catch (ParseException e) {
			System.err.println("Not valid JSON");
		}

		int agentHash = Integer.parseInt(jsonObject.get("hash").toString());

		try {
			boolean agentAlreadyActive = false;
			for (int i = 0; i < Main.activeAgents.getSize(); i++) {
				Agent agent = Main.activeAgents.get(i);
				if (agent.getHash() == agentHash) {
					agentAlreadyActive = true;
					agent.setLastRequestTime();
					break;
				}
			}
			if (agentAlreadyActive == false) {
				DbOperations.activateAgent(agentHash);
			}
		} catch (SQLException e) {
			System.err.println("A problem occured with the database");
		}

		Response r = null;
		if (Main.assignedJobs.get(Integer.valueOf(agentHash)) == null
				|| Main.assignedJobs.get(Integer.valueOf(agentHash)).size() == 0) {
			r = Response.status(200).entity("nojobs").build();
		} else {
			String assignedJobsJson = JSONValue.toJSONString(Main.assignedJobs.get(Integer.valueOf(agentHash)));
			r = Response.status(200).entity(assignedJobsJson).build();
			for (Job job : Main.assignedJobs.get(Integer.valueOf(agentHash))) {
				if (job.getParameters().equals("Stop") == false) {
					try {
						DbOperations.setJobStatusRunning(job.getId(), agentHash);
					} catch (SQLException e) {
						System.err.println("A problem occured with the database");
					}
					if (job.isPeriodic() && job.getTime() != -1) {
						if (Main.runningPeriodicJobs.containsKey(Integer.valueOf(agentHash)) == false) {
							Main.runningPeriodicJobs.put(Integer.valueOf(agentHash), new ArrayList<Job>());
						}
						Main.runningPeriodicJobs.get(agentHash).add(job);
					}
				}
			}
			Main.assignedJobs.remove(Integer.valueOf(agentHash));
		}
		return r;

	}
}
