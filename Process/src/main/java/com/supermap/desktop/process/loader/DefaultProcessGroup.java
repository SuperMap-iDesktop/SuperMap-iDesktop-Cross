package com.supermap.desktop.process.loader;

import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/8/8.
 */
public class DefaultProcessGroup implements IProcessGroup {
	private String id;
	private String title;
	private Vector<IProcessGroup> groups;
	private Vector<IProcessLoader> processes;
	private Map<String, IProcessLoader> processLoaderMap;
	private IProcessGroup parent;

	public DefaultProcessGroup(String id, String title, IProcessGroup parent) {
		this.id = id;
		this.title = title;
		this.parent = parent;
		if (this.parent != null) {
			this.parent.addGroup(this);
		}
		this.groups = new Vector<>();
		this.processes = new Vector<>();
		this.processLoaderMap = new ConcurrentHashMap<>();
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

		if (group.getParent() != this) {
			return;
		}

		this.groups.add(group);
	}

	@Override
	public void addProcess(IProcessLoader process) {
		if (process == null) {
			return;
		}

		String processKey = process.getProcessDescriptor().getKey();
		if (StringUtilities.isNullOrEmpty(processKey)) {
			return;
		}

		if (!isProcessKeyValid(processKey)) {
			return;
		}

		this.processes.add(process);
		this.processLoaderMap.put(processKey, process);
	}

	private boolean isProcessKeyValid(String processKey) {
		return !isContainProcess(processKey) && !isParentContainProcess(processKey) && !isChildContainProcess(processKey);
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
	public IProcessGroup getParent() {
		return this.parent;
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

	@Override
	public boolean isParentContainProcess(String processKey) {
		if (StringUtilities.isNullOrEmpty(processKey)) {
			throw new NullPointerException();
		}

		if (this.parent != null) {
			return this.parent.isContainProcess(processKey) || this.parent.isParentContainProcess(processKey);
		} else {
			return false;
		}
	}

	@Override
	public boolean isChildContainProcess(String processKey) {
		if (StringUtilities.isNullOrEmpty(processKey)) {
			throw new NullPointerException();
		}

		boolean ret = false;
		for (int i = 0; i < this.groups.size(); i++) {
			IProcessGroup group = this.groups.get(i);

			ret = group.isContainProcess(processKey) || group.isChildContainProcess(processKey);
			if (ret) {
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean isContainProcess(String processKey) {
		if (StringUtilities.isNullOrEmpty(processKey)) {
			throw new NullPointerException();
		}
		return this.processLoaderMap.containsKey(processKey);
	}
}
