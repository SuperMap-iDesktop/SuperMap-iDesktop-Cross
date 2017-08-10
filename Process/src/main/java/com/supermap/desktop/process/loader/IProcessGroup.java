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

	IProcessGroup getParent();

	IProcessLoader[] getProcesses();

	IProcessLoader[] getProcesses(String groupID);

	/**
	 * 父Group 中是否包含指定 Key 的 ProcessLoader
	 * @param processKey
	 * @return
	 */
	boolean isParentContainProcess(String processKey);

	/**
	 * 子Group 中是否包含指定 key 的 ProcessLoader
	 *
	 * @param processKey
	 * @return
	 */
	boolean isChildContainProcess(String processKey);

	/**
	 * 是否包含指定 key 的 ProcessLoader，不包含子Group 和 parent
	 *
	 * @param processKey
	 * @return
	 */
	boolean isContainProcess(String processKey);
}
