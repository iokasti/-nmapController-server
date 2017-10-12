package aggregatorManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import database.DbOperations;

/**
 * AgentResults gets post from agents, including the results and processes them
 * in the database.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
@Path("results")
public class AgentResults {
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getResults(String agentsJSON) {
		try {
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse(agentsJSON);
			for (int i = 0; i < array.size(); i++) {
				JSONObject JSON = (JSONObject) array.get(i);
				int agentHash = Integer.parseInt(JSON.get("hash").toString());
				int jobID = Integer.parseInt(JSON.get("jobID").toString());
				String resultXML = StringEscapeUtils.unescapeXml((JSON.get("result").toString()));
				DbOperations.insertResult(jobID, agentHash, resultXML);
			}
		} catch (Exception e) {
			System.err.println("Problem in results.");
		}

		Response r = null;
		r = Response.status(200).entity("successful").build();

		return r;
	}
}
