package aggregatorManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import agent.Agent;

/**
 * RegisterAgent gets post from agents, including the registration information,
 * and process the requests according to what the choices of the admin.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
@Path("register")
public class RegisterAgent {
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getClientInfoJSON(String agentInfoJSON) {
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) (new JSONParser()).parse(agentInfoJSON);
		} catch (ParseException e) {
			System.err.println("Not valid JSON");
		}

		Agent agent = new Agent(jsonObject.get("deviceName").toString(), jsonObject.get("ip").toString(),
				jsonObject.get("macAddress").toString(), jsonObject.get("osVersion").toString(),
				jsonObject.get("nmapVersion").toString(), Integer.parseInt(jsonObject.get("hash").toString()), false,
				false);

		Response r = null;

		if (Main.activeAgents.contains(agent)) {
			// agent is already running so cancel the second run
			r = Response.status(200).entity("cancel").build();
		} else if (Main.agentsToBeAccepted.contains(agent)) {
			// agent is activated from administration panel so we must now
			// inform "him" about it
			Main.agentsToBeAccepted.remove(agent); // remove from toBeActivated
													// list
			r = Response.status(200).entity("activated").build();
		} else if (Main.agentsToBeCanceled.contains(agent) == true) {
			// agent was canceled
			Main.agentsToBeCanceled.remove(agent);
			r = Response.status(200).entity("cancel").build();
		} else if (Main.agentActivationRequests.contains(agent) == false) {
			// agent is not already active, not accepted from administration
			// panel and does not already exist in agentRequests
			Main.agentActivationRequests.addElement(agent);
			r = Response.status(200).entity("wait").build();
		} else if (Main.agentActivationRequests.contains(agent) == true) {
			// agent already exists in agent requests and none of the above was
			// true
			r = Response.status(200).entity("wait").build();
		} else {
			// should never be reached
			r = Response.status(200).entity("error").build();
		}

		return r;
	}
}