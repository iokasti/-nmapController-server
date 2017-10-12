package aggregatorManager;

import static aggregatorManager.Main.assignedJobs;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import nmapJob.Job;

/**
 * Accepts a get request from the android application, an terminates the
 * indicated SA.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("terminatesa")
public class TerminateSAAndroid {
	@GET
	@Path("/get/hash/{hash}")
	public Response terminateSA(@PathParam("hash") String hash) {

		if (assignedJobs.containsKey(Integer.valueOf(hash)) == false) {
			Main.assignedJobs.put(Integer.valueOf(hash), new ArrayList<Job>());
		}
		Main.assignedJobs.get(Integer.valueOf(hash)).add(new Job(true));

		Response r = Response.status(200).entity("true").build();

		return r;
	}

}
