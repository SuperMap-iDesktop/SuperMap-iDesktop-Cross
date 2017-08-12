package com.supermap.desktop.process.loader;

/**
 * Created by highsad on 2017/8/4.
 */
public interface IProcessGroup {
	String getID();

	String getTitle();

	/**
	 * 获取 Index，不与集合中的位置一致
	 *
	 * @return
	 */
	int getIndex();

	int getGroupCount();

	/**
	 * 获取当前 Group 下的 Process 数量，不包含子 Group 的 Process。
	 *
	 * @return
	 */
	int getProcessCount();

	void addGroup(IProcessGroup group);

	void addProcess(IProcessLoader process);

	IProcessGroup getGroup(String id);

	IProcessGroup getParent();

	IProcessGroup[] getGroups();

	IProcessLoader[] getProcesses();

	IProcessLoader[] getProcesses(String groupID);

	/**
	 * 从自己和自己的子 Group 中查找指定 processKey 的 ProcessLoader
	 *
	 * @param processKey
	 * @return
	 */
	IProcessLoader findProcess(String processKey);

	/**
	 * 父Group 中是否包含指定 Key 的 ProcessLoader
	 *
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
