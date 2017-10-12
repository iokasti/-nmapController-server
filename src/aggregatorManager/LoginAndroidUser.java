package aggregatorManager;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import Android.AndroidUser;
import database.DbAndroid;

/**
 * Accepts a get request from the android application, checks user credential in
 * the database and if they are correct responses true for successful login.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
@Path("loginuser")
public class LoginAndroidUser {
	@GET
	@Path("/get/username/{username}/password/{password}")
	public Response loginUser(@PathParam("username") String username, @PathParam("password") String password) {

		AndroidUser androidUser = new AndroidUser(username, password);

		Response r = null;

		try {
			if (DbAndroid.loginUser(androidUser) == true) {
				r = Response.status(200).entity("true").build();
			} else {
				r = Response.status(200).entity("false").build();
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong while logging in.");
		}

		return r;
	}
}