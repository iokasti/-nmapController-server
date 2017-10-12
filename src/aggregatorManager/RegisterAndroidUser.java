package aggregatorManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import Android.AndroidUser;

/**
 * Accepts a get request from the android application, and shows the user info at the admin panel. 
 * The admin has to accept or reject the new user from the panel.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("registeruser")
public class RegisterAndroidUser {
	@GET
	@Path("/get/username/{username}/password/{password}")
	public Response registerUser(@PathParam("username") String username, @PathParam("password") String password) {

		AndroidUser androidUser = new AndroidUser(username, password);

		Response r = null;

		if (Main.androidUsersToBeAccepted.contains(androidUser)) {
			// user was accepted from the administration panel
			Main.androidUsersToBeAccepted.remove(androidUser);
			r = Response.status(200).entity("true").build();
		} else if (Main.androidUsersToBeCanceled.contains(androidUser)) {
			// user was rejected from the administration panel
			Main.androidUsersToBeCanceled.remove(androidUser);
			r = Response.status(200).entity("false").build();
		} else if (Main.androidUsersActivationRequests.contains(androidUser) == false) {
			// user is not accepted, and it's his first registration request
			Main.androidUsersActivationRequests.addElement(androidUser);
			r = Response.status(200).entity(null).build();
		} else if (Main.androidUsersActivationRequests.contains(androidUser) == true) {
			// user is not accepted, but it's not his first registration request
			r = Response.status(200).entity(null).build();
		} else {
			// should never be reached
			r = Response.status(200).entity(null).build();
		}
		
		return r;
	}
}