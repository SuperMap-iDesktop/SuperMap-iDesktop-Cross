package com.supermap.desktop.Interface;

/**
 * 定义应用程序主窗口和子窗口的状态栏所具有的功能的接口。每个主窗口和子窗口只有一个状态栏。
 * @author wuxb
 *
 */
public interface IStatusbar {

	/**
	 * 获取状态栏中指定索引的控件。
	 * 
	 * @param index
	 *            控件索引
	 * @return 状态栏中指定索引值的控件
	 */
	IBaseItem get(int index);
	
	/**
	 * 根据状态栏中指定类型的控件。
	 * @param controlType 控件类型
	 * @return 状态栏中指定类型的控件
	 */
	IBaseItem get(Class<?> controlType);
	
	/**
	 * 获取绑定窗体类型的名字。
	 * @return
	 */
	String getFormClassName();
	
	/**
	 * 获取状态栏中所包含的控件的总个数。
	 * @return
	 */
	int getCount();
}
