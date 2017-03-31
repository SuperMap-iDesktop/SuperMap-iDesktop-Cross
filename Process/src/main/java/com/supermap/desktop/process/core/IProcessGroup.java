package com.supermap.desktop.process.core;

/**
 * @author XiaJT
 */
public interface IProcessGroup extends IProcess {


	int getChildCount();

	IProcess getProcessByIndex(int index);

	int addProcess(IProcess process);

	boolean removeProcess(IProcess process);

	IProcess getProcessByKey(String key);

	void setKey(String key);


	boolean isLegitName(String name, IProcess process);

	void setIconPath(String path);
}
