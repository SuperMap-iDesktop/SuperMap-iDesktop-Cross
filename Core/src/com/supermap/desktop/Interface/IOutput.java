package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.InfoType;

public interface IOutput {

	/**
	 * 获取指定索引的输出信息。
	 */
	String getLineText(int index);

	/**
	 * 获取输出信息的条数。
	 */
	int getLineCount();

	/**
	 * 是否可以复制输出信息
	 * 
	 * @return 有选中内容返回true，否则返回false
	 */
	boolean canCopy();

	/**
	 * 复制选中的信息
	 */
	void copy();

	/**
	 * 是否可以清空输出窗口
	 * 
	 * @return 有输出的内容返回true，否则返回false
	 */
	boolean canClear();

	/**
	 * 清空输出窗口
	 */
	void clear();

	/**
	 * 获取或设置最大的输出信息条数。
	 */
	int getMaxLineCount();

	void setMaxLineCount(int maxCount);

	/**
	 * 获取或设置最大的输出信息条数。
	 */
	Boolean getIsWordWrapped();

	void setIsWordWrapped(Boolean isWordWrapped);

	/**
	 * 获取或设置是否在输出信息前添加信息输出的时间。
	 */
	Boolean getIsTimePrefixAdded();

	void setIsTimePrefixAdded(Boolean isTimePrefixAdded);

	/**
	 * 获取或设置信息输出时间的格式。
	 */
	String getTimePrefixFormat();

	void setTimePrefixFormat(String timePrefixFormat);

	/**
	 * 将指定的输出信息进行输出。
	 */
	void output(String message);

	/**
	 * 将指定的输出信息进行输出。
	 */
	void output(Exception exception);

	/**
	 * 将指定的输出信息进行输出。
	 */
	void output(String message, InfoType type);

	/**
	 * 清空输出信息。
	 */
	void clearOutput();

	void output(Throwable e);
}