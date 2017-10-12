package aggregatorManager;

import java.util.Enumeration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agent.Agent;

/**
 * Accepts a get request from the android application and responses with a json
 * containing all the active SAs
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("activesa")
public class ActiveSAAndroid {
	@GET
	@Path("/get/")
	public Response saStatus() {

		Enumeration<Agent> e = Main.activeAgents.elements();

		String agentsJSON = "[";
		while (e.hasMoreElements()) {
			Agent agent = e.nextElement();
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
