package cache;

import java.util.ArrayList;

import agent.Agent;

/**
 * Synchronized ArrayList containing agents.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class SynchedArrayList_Agent {
	private ArrayList<Agent> arrayList;

	public SynchedArrayList_Agent() {
		this.arrayList = new ArrayList<Agent>();
	}

	public synchronized void add(Agent agent) {
		arrayList.add(agent);
	}

	public synchronized boolean contains(Agent agent) {
		return arrayList.contains(agent);
	}

	public synchronized void remove(Agent agent) {
		arrayList.remove(agent);
	}
}
