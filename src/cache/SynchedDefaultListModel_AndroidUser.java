
package cache;

import java.util.Enumeration;

import javax.swing.DefaultListModel;

import Android.AndroidUser;

/**
 * Synchronized defaultListModel containing AndroidUsers.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class SynchedDefaultListModel_AndroidUser {

	private DefaultListModel<AndroidUser> defaultListModel;

	public SynchedDefaultListModel_AndroidUser() {
		this.defaultListModel = new DefaultListModel<AndroidUser>();
	}

	public synchronized boolean contains(AndroidUser androidUser) {
		return defaultListModel.contains(androidUser);
	}

	public synchronized void addElement(AndroidUser androidUser) {
		defaultListModel.addElement(androidUser);
	}

	public synchronized DefaultListModel<AndroidUser> getModel() {
		return defaultListModel;
	}

	public synchronized int getSize() {
		return defaultListModel.getSize();
	}

	public synchronized AndroidUser get(int i) {
		return defaultListModel.get(i);
	}

	public synchronized void remove(int i) {
		defaultListModel.remove(i);
	}

	public synchronized void removeElement(AndroidUser androidUser) {
		defaultListModel.removeElement(androidUser);
	}
	
	public synchronized Enumeration<AndroidUser> elements(){
		return defaultListModel.elements();
	}

}
