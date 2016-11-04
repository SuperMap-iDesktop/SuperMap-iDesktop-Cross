package com.supermap.Interface;

/**
 * 通用任务管理接口
 * 
 * @author xie
 *
 */
public interface ITaskManager {
	/**
	 * 获取任务名称
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * 任务是否可用
	 *
     * @param
     * @return
	 */
	boolean isEnable();

	/**
	 * 运行任务
	 */
	void run();
}
