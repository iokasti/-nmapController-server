package aggregatorManager;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import database.DbOperations;
import nmapJob.Job;

/**
 * Accepts a get request from the android application containing a json with
 * jobs. It then assigns those jobs to the specified SA.
 * 
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("insertjobs")
public class InsertJobAndroid {
	@GET
	@Path("/get/jobs/{jobs}")
	public Response registerUser(@PathParam("jobs") String jobsJSON) {
		try {
			jobsJSON = java.net.URLDecoder.decode(jobsJSON, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Could not decode uri");
		}
		JSONParser parser = new JSONParser();
		JSONArray array;

		Response r = null;

		try {
			array = (JSONArray) parser.parse(jobsJSON);
			for (int i = 0; i < array.size(); i++) {
				JSONObject JSON = (JSONObject) array.get(i);
				int id = Integer.parseInt(JSON.get("id").toString());
				String parameters = JSON.get("parameters").toString();
				Boolean periodic = Boolean.parseBoolean(JSON.get("periodic").toString());
				int time = Integer.parseInt(JSON.get("time").toString());
				int hash = Integer.parseInt(JSON.get("hash").toString());
				Job job = new Job(id, parameters, periodic, time);

				if (Main.assignedJobs.containsKey(Integer.valueOf(hash)) == false) {
					Main.assignedJobs.put(Integer.valueOf(hash), new ArrayList<Job>());
				}
				DbOperations.insertJob(job, hash);
				Main.assignedJobs.get(Integer.valueOf(hash)).add(job);

				if (parameters.equals("Stop") && periodic == true && time == -1) {
					// it is a job deletion
					Main.runningPeriodicJobs.get(hash).remove(job);
				}

				r = Response.status(200).entity("true").build();
			}
		} catch (ParseException e) {
			System.err.println("Problem in android job insertion.");
			r = Response.status(200).entity("false").build();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return r;
	}
}
