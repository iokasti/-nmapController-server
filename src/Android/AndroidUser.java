package Android;

import java.sql.SQLException;

import aggregatorManager.Main;
import database.DbAndroid;

/**
 * Class representation of an Android user.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
public class AndroidUser {
	private String username;
	private String password;

	public AndroidUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "AndroidUser [username=" + username + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AndroidUser other = (AndroidUser) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/**
	 * If user is waiting to be accepted, accept it.
	 */
	public void accept() {
		try {
			if (DbAndroid.registerUser(this) == true) {
				Main.androidUsersToBeAccepted.add(this);
			} else {
				Main.androidUsersToBeCanceled.add(this);
			}
		} catch (SQLException e) {
			System.err.println("Something went wrong while registering and android user.");
		}
	}

	/**
	 * If user is waiting to be accepted, reject it.
	 */
	public void reject() {
		Main.androidUsersToBeCanceled.add(this);
	}

}
