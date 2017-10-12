package cache;

import java.util.ArrayList;

import Android.AndroidUser;

/**
 * Synchronized ArrayList containing AndroidUsers.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 3.0
 * @since 2016-01-25
 */
public class SynchedArrayList_AndroidUser {
	private ArrayList<AndroidUser> arrayList;

	public SynchedArrayList_AndroidUser() {
		this.arrayList = new ArrayList<AndroidUser>();
	}

	public synchronized void add(AndroidUser androidUser) {
		arrayList.add(androidUser);
	}

	public synchronized boolean contains(AndroidUser androidUser) {
		return arrayList.contains(androidUser);
	}

	public synchronized void remove(AndroidUser androidUser) {
		arrayList.remove(androidUser);
	}
}
