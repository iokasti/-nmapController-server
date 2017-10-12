package nmapJob;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The Result class includes a result and info about it.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class Result {
	/**
	 * Result of the job.
	 */
	private String result;

	/**
	 * Job id.
	 */
	private int job_id;

	/**
	 * Date the result was created.
	 */
	private Date date;

	/**
	 * Constructor or Result.
	 * 
	 * @param result
	 *            Job results from nmap.
	 * @param job
	 *            Job information.
	 * @param timestamp
	 *            Timestamp of the result creation.
	 * 
	 */
	public Result(String result, int job_id, Timestamp timestamp) {
		this.result = result;
		this.job_id = job_id;
		date = timestamp;
	}

	@Override
	public String toString() {
		return "{\"job_id\":\"" + job_id + "\",\"date\":\"" + date + "\",\"result\":\"" + result + "\"}";
	}

	/**
	 * Getter function for result.
	 * 
	 * @return result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Setter function for result
	 * 
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Getter function for job id.
	 * 
	 * @return job_id
	 */
	public int getJob_id() {
		return job_id;
	}

	/**
	 * Setter function for the job id.
	 * 
	 * @param job_id
	 */
	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	/**
	 * Getter function for the date.
	 * 
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setter function for the date.
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
