package com.supermap.desktop.progress.Interface;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * 提供进度更新信息的 Callable
 * 
 * @author highsad
 *
 */
public abstract class UpdateProgressCallable implements Callable<Boolean> {

	protected IUpdateProgress update;

	public IUpdateProgress getUpdate() {
		return update;
	}

	public void setUpdate(IUpdateProgress update) {
		this.update = update;
	}

	/**
	 * 单进度
	 * 
	 * @param percent
	 * @param remainTime
	 * @param message
	 * @throws CancellationException
	 */
	public void updateProgress(int percent, String remainTime, String message) throws CancellationException {
		this.update.updateProgress(percent, remainTime, message);
	}

	/**
	 * 总进度
	 * 
	 * @param percent
	 * @param totalPercent
	 * @param remainTime
	 * @param message
	 * @throws CancellationException
	 */
	public void updateProgressTotal(int percent, int totalPercent, String remainTime, String message) throws CancellationException {
		this.update.updateProgress(percent, totalPercent, remainTime, message);
	}

	/**
	 * 总进度
	 * 
	 * @param percent
	 * @param recentTask
	 * @param totalPercent
	 * @param message
	 * @throws CancellationException
	 */
	public void updateProgressTotal(int percent, String recentTask, int totalPercent, String message) throws CancellationException {
		this.update.updateProgress(percent, recentTask, totalPercent, message);
	}
}
