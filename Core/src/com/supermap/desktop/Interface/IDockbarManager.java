package com.supermap.desktop.Interface;

public interface IDockbarManager {

	/**
	 * 获取指定索引的浮动窗口。
	 */
	IDockbar get(int index);

	/**
	 * 获取指定索引的浮动窗口。
	 */
	IDockbar get(Class<?> controlClass);

	/**
	 * 获取应用程序内所包含的浮动窗口的总数。
	 */
	int getCount();

	/**
	 * 检查是否包含指定的浮动窗口。
	 * 
	 * @param dockBar
	 * @return
	 */
	boolean contains(IDockbar dockBar);
}
