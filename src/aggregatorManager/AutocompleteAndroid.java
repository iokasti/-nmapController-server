package aggregatorManager;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import database.DbOperations;
import nmapJob.Job;

/**
 * Accepts a get request from the android application and responses with a json
 * containing all the jobs, so they can be used for the autocomplete.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("autocomplete")
public class AutocompleteAndroid {
	@GET
	@Path("/get/")
	public Response saStatus() {

		ArrayList<Job> jobs = null;
		try {
			jobs = DbOperations.getAllJobs();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String jobsJSON = "[";
		for (Job job : jobs) {
			jobsJSON += job.toString();
			jobsJSON += ",";
		}
		if (jobsJSON.length() > 1) {
			jobsJSON = jobsJSON.substring(0, jobsJSON.length() - 1);
		}
		jobsJSON += "]";

		Response r = Response.status(200).entity(jobsJSON).build();
		return r;
	}

}
