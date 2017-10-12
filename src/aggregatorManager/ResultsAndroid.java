package aggregatorManager;

import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Accepts a get request from the android application, and returns {num}
 * results.
 * 
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("results")
public class ResultsAndroid {
	@GET
	@Path("/get/num/{num}")
	public Response getResults(@PathParam("num") String num) {
		int numberOfResults = Integer.parseInt(num);
		String resultsHTML = "";
		try {
			resultsHTML = StringEscapeUtils.escapeJson(database.DbAndroid.getTopResults(numberOfResults));
		} catch (SQLException e) {
			System.err.println("An error occured while generating results");
		} catch (IOException e) {
			System.err.println("An error occured while generating results");
		}
		String JSONresultHTML = "{\"result\":\"" + resultsHTML + "\"}";
		Response r = Response.status(200).entity(JSONresultHTML).build();
		return r;
	}

}
