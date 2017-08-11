package com.supermap.desktop.process.loader;

import com.supermap.desktop.utilities.StringUtilities;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/8/8.
 */
public class DefaultProcessGroup implements IProcessGroup {
	private final static int DEFAULT_INDEX = 9999;
	private String id;
	private String title;
	private int index;
	private Vector<IProcessGroup> groups;
	private Vector<IProcessLoader> processes;
	private Map<String, IProcessLoader> processLoaderMap;
	private IProcessGroup parent;

	public DefaultProcessGroup(String id, String title, String index, IProcessGroup parent) {
		this.id = id;
		this.title = title;

		try {
			this.index = Integer.valueOf(index);
		} catch (Exception e) {
			this.index = DEFAULT_INDEX;
		}
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
	public int getIndex() {
		return this.index;
	}

	@Override
	public int getGroupCount() {
		return this.groups.size();
	}

	@Override
	public int getProcessCount() {
		return this.processes.size();
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

		int insertIndex = -1;
		for (int i = 0; i < this.groups.size(); i++) {
			IProcessGroup g = this.groups.get(i);
			if (group.getIndex() < g.getIndex()) {
				insertIndex = g.getIndex();
				break;
			}
		}

		if (insertIndex != -1) {
			this.groups.add(insertIndex, group);
		} else {
			this.groups.add(group);
		}
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

		int insertIndex = -1;
		for (int i = 0; i < this.processes.size(); i++) {
			IProcessLoader p = this.processes.get(i);
			if (process.getProcessDescriptor().getIndex() < p.getProcessDescriptor().getIndex()) {
				insertIndex = p.getProcessDescriptor().getIndex();
				break;
			}
		}

		if (insertIndex != -1) {

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
	public IProcessGroup[] getGroups() {
		return this.groups.toArray(new IProcessGroup[this.groups.size()]);
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
	public IProcessLoader findProcess(String processKey) {
		IProcessLoader loader = null;

		if (this.processLoaderMap.containsKey(processKey)) {
			loader = this.processLoaderMap.get(processKey);
		} else {
			for (int i = 0; i < this.groups.size(); i++) {
				loader = this.groups.get(i).findProcess(processKey);
				if (loader != null) {
					break;
				}
			}
		}
		return loader;
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
