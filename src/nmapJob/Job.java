package nmapJob;

/**
 * The Job class represents an nmap-job for the Aggregator Manager.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-6
 */
public class Job {
	/**
	 * ID of the job.
	 */
	private int id;

	/**
	 * Parameters of the job (nmap parameters).
	 */
	private String parameters;

	/**
	 * If the job is periodic then it repeats itself after waiting some time.
	 */
	private boolean periodic;

	/**
	 * If the job is periodic time represents how much the job has to wait
	 * before running again.
	 */
	private int time;

	public Job(int id, String parameters, boolean periodic, int time) {
		this.id = id;
		this.parameters = parameters;
		this.periodic = periodic;
		this.time = time;
	}
	
	/**
	 * Constructor of a Job.
	 * 
	 * @param id
	 *            id of the job
	 * @param parameters
	 *            parameters for nmap
	 * @param periodic
	 *            if set to True, job repetas
	 * @param time
	 *            job repeats after specified time
	 */
	public Job(String job) {
		String[] jobInfo = job.split(",");
		jobInfo[0] = jobInfo[0].trim();
		jobInfo[1] = jobInfo[1].trim();
		jobInfo[2] = jobInfo[2].trim();
		jobInfo[3] = jobInfo[3].trim();
		id = Integer.parseInt(jobInfo[0]);
		parameters = jobInfo[1];
		periodic = Boolean.parseBoolean(jobInfo[2]);
		time = Integer.parseInt(jobInfo[3]);
	}

	/**
	 * Constructor of a job used to terminate an SA.
	 * 
	 * @param terminate
	 */
	public Job(boolean terminate) {
		if (terminate) {
			this.id = -1;
			this.parameters = "exit(0)";
			this.periodic = true;
			this.time = -1;
		}
	}

	/**
	 * Change paramateres of a periodic job so it can be terminated.
	 */
	public void setDelete() {
		this.parameters = "Stop";
		this.periodic = true;
		this.time = -1;
	}

	/**
	 * Getter function for id.
	 * 
	 * @return Job:id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter function for parameters.
	 * 
	 * @return Job:parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Getter function for periodic.
	 * 
	 * @return Job:periodic
	 */
	public boolean isPeriodic() {
		return periodic;
	}

	/**
	 * Getter function for time.
	 * 
	 * @return Job:time
	 */
	public int getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",\"parameters\":\"" + parameters + "\",\"periodic\":\"" + periodic
				+ "\",\"time\":\"" + time + "\"}";
	}

//	@Override
//	public boolean equals(Object obj) {
//	if (this == obj)
//		return true;
//	if (obj == null)
//		return false;
//	if (getClass() != obj.getClass())
//		return false;
//	Job other = (Job) obj;
//	if (id != other.id)
//		return false;
//	if (parameters == null) {
//		if (other.parameters != null)
//			return false;
//	} else if (!parameters.equals(other.parameters))
//		return false;
//	if (periodic != other.periodic)
//		return false;
//	if (time != other.time)
//		return false;
//	return true;
//}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
