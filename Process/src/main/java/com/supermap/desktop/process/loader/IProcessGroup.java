package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/4.
 */
public interface IProcessGroup {
	String getID();

	String getTitle();

	void addGroup(IProcessGroup group);

	void addProcess(IProcessLoader process);

	IProcessGroup getGroup(String id);

	IProcessLoader[] getProcesses();

	IProcessLoader[] getProcesses(String groupID);
}
