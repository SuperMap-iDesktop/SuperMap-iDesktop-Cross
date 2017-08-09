package com.supermap.desktop.process.loader;

import com.supermap.desktop.process.core.IProcess;

/**
 * Created by highsad on 2017/8/4.
 */
public interface IProcessGroup {
	String getID();

	String getTitle();

	void addGroup(IProcessGroup group);

	void addProcess(IProcess process);

	IProcessGroup getGroup(String id);

	IProcess[] getProcesses();

	IProcess[] getProcesses(String groupID);
}
