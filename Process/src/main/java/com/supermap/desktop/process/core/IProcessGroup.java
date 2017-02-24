package com.supermap.desktop.process.core;

/**
 * @author XiaJT
 */
public interface IProcessGroup extends IProcess {


	int getChildCount();

	IProcess getProcessByIndex(int index);

	IProcess getProcessByKey(String key);

	void setName(String name);

	boolean isLegitName(String name);
}
