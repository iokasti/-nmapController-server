package aggregatorManager;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import nmapJob.Job;

/**
 * Accepts a get request from the android application, and returns all the
 * periodic jobs that are running at the moment.
 * 
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("getpjobs")
public class RunningJobsAndroid {
	@GET
	@Path("/get/hash/{hash}")
	public Response runningJobs(@PathParam("hash") String hash) {
		Response r;
		ArrayList<Job> jobs = null;
		jobs = Main.runningPeriodicJobs.get(Integer.parseInt(hash));
		if (jobs != null && jobs.size() != 0) {
			String jobsJSON = "[";
			for (Job job : jobs) {
				jobsJSON += job.toString();
				jobsJSON += ",";
			}
			if (jobsJSON.length() > 1) {
				jobsJSON = jobsJSON.substring(0, jobsJSON.length() - 1);
			}
			jobsJSON += "]";
			r = Response.status(200).entity(jobsJSON).build();

		} else {
			r = Response.status(200).entity(null).build();
		}
		return r;
	}
}
