package com.supermap.desktop.Interface;

public interface IStatusbarManager {

	/**
	 * 获取指定索引的窗口状态栏。
	 * @param index 子项索引
	 * @return
	 */
	IStatusbar get(int index);
	
	/**
	 * 获取包含指定绑定窗体类类型的窗口状态栏。
	 * @param formClassName 绑定窗体类类型
	 * @return
	 */
	IStatusbar get(String formClassName);
	
	/**
	 * 获取应用程序内所包含的窗口状态栏的总数。
	 * @return
	 */
	int getCount();
}
