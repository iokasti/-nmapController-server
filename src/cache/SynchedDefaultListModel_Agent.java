
package cache;

import java.util.Enumeration;

import javax.swing.DefaultListModel;

import agent.Agent;

/**
 * Synchronized defaultListModel containing agents.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class SynchedDefaultListModel_Agent {

	private DefaultListModel<Agent> defaultListModel;

	public SynchedDefaultListModel_Agent() {
		this.defaultListModel = new DefaultListModel<Agent>();
	}

	public synchronized boolean contains(Agent agent) {
		return defaultListModel.contains(agent);
	}

	public synchronized void addElement(Agent agent) {
		defaultListModel.addElement(agent);
	}

	public synchronized DefaultListModel<Agent> getModel() {
		return defaultListModel;
	}

	public synchronized int getSize() {
		return defaultListModel.getSize();
	}

	public synchronized Agent get(int i) {
		return defaultListModel.get(i);
	}

	public synchronized void remove(int i) {
		defaultListModel.remove(i);
	}

	public synchronized void removeElement(Agent agent) {
		defaultListModel.removeElement(agent);
	}
	
	public synchronized Enumeration<Agent> elements(){
		return defaultListModel.elements();
	}

}
