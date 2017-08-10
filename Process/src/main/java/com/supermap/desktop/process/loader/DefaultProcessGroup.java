package com.supermap.desktop.process.loader;

import com.supermap.desktop.utilities.StringUtilities;

import java.util.Vector;

/**
 * Created by highsad on 2017/8/8.
 */
public class DefaultProcessGroup implements IProcessGroup {
	private String id;
	private String title;
	private Vector<IProcessGroup> groups;
	private Vector<IProcessLoader> processes;

	public DefaultProcessGroup(String id, String title) {
		this.id = id;
		this.title = title;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public void addGroup(IProcessGroup group) {
		if (this.groups.contains(group)) {
			return;
		}

		if (getGroup(group.getID()) != null) {
			return;
		}

		this.groups.add(group);
	}

	@Override
	public void addProcess(IProcessLoader process) {
		this.processes.add(process);
	}

	@Override
	public IProcessGroup getGroup(String id) {
		IProcessGroup group = null;

		for (int i = 0; i < this.groups.size(); i++) {
			if (StringUtilities.stringEquals(this.groups.get(i).getID(), id, false)) {
				group = this.groups.get(i);
				break;
			}
		}

		return group;
	}

	@Override
	public IProcessLoader[] getProcesses() {
		return this.processes.toArray(new IProcessLoader[this.processes.size()]);
	}

	@Override
	public IProcessLoader[] getProcesses(String groupID) {
		IProcessGroup group = getGroup(groupID);

		if (group != null) {
			return group.getProcesses();
		}
		return null;
	}
}
