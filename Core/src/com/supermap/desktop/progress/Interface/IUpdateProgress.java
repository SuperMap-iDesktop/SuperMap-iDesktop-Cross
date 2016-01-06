package com.supermap.desktop.progress.Interface;

import java.util.concurrent.CancellationException;

/**
 * 用来更新进度信息接口
 * 
 * @author highsad
 *
 */
public interface IUpdateProgress {

	/**
	 * 获取操作是否已取消
	 * 
	 * @return
	 */
	boolean isCancel();

	/**
	 * 设置是否取消操作
	 * 
	 * @param isCancel
	 */
	void setCancel(boolean isCancel);

	/**
	 * 更新进度信息
	 * 
	 * @param percent
	 *            进度
	 * @param remainTime
	 *            剩余时间
	 * @param message
	 *            进度信息
	 */
	void updateProgress(int percent, String remainTime, String message) throws CancellationException;

	/**
	 * 更新进度信息
	 * 
	 * @param percent
	 *            进度
	 * @param totalPercent
	 *            总进度
	 * @param remainTime
	 *            剩余时间
	 * @param message
	 *            进度信息
	 */
	void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException;
	
	/**
	 * 
	 * @param percent
	 * 				进度
	 * @param recentTask
	 * 				当前任务信息
	 * @param totalPercent
	 * 				剩余时间
	 * @param message
	 * 				进度信息
	 * @throws CancellationException
	 */
	void updateProgress(int percent,String recentTask,int totalPercent,String message) throws CancellationException;
}
