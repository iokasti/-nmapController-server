package aggregatorManager;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agent.Agent;
import database.DbOperations;

/**
 * Accepts a get request from the android application, and returns a json
 * containing all the agents and their info.
 * 
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("sastatus")
public class SAStatusAndroid {
	@GET
	@Path("/get/")
	public Response saStatus() {

		ArrayList<Agent> agents = null;
		try {
			agents = DbOperations.getAllAgents();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String agentsJSON = "[";
		for (Agent agent : agents) {
			agentsJSON += agent.toJSON();
			agentsJSON += ",";
		}
		if (agentsJSON.length() > 1) {
			agentsJSON = agentsJSON.substring(0, agentsJSON.length() - 1);
		}
		agentsJSON += "]";

		Response r = Response.status(200).entity(agentsJSON).build();

		return r;
	}

}
