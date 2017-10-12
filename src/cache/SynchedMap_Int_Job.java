package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nmapJob.Job;

/**
 * Synchronized map with Integer(agent hash) as a key and ArrayList(jobs) as
 * a value.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class SynchedMap_Int_Job {
	private Map<Integer, ArrayList<Job>> map;

	public SynchedMap_Int_Job() {
		map = new HashMap<Integer, ArrayList<Job>>();
	}

	public synchronized ArrayList<Job> get(Integer key) {
		return map.get(key);
	}

	public synchronized boolean containsKey(Integer key) {
		return map.containsKey(key);
	}

	public synchronized void put(Integer key, ArrayList<Job> value) {
		map.put(key, value);
	}

	public synchronized void remove(Integer key) {
		map.remove(key);
	}
}
