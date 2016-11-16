package com.supermap.desktop.Interface;

import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockState;

import java.awt.*;

/**
 * 定义浮动窗口属性的接口。
 */
public interface IDockbar {

	/**
	 * 获取浮动窗口是否可见。
	 *
	 * @return
	 */
	boolean isVisible();

	/**
	 * 设置浮动窗口是否可见。
	 */
	void setVisible(boolean isVisible);

	/**
	 * 获取浮动窗口所包含的控件。
	 *
	 * @return
	 */
	Component getInnerComponent();

	/**
	 * 获取浮动窗口的标题栏标题。
	 *
	 * @return
	 */
	String getTitle();

	/**
	 * 设置浮动窗口的标题栏标题。
	 */
	void setTitle(String title);

	/**
	 * 获取浮动窗口是否激活。
	 *
	 * @return
	 */
	boolean isActive();

	/**
	 * 获取浮动窗口所属的插件信息。
	 *
	 * @return
	 */
	PluginInfo getPluginInfo();

	/**
	 * 获取浮动窗口所包含的控件名称。
	 *
	 * @return
	 */
	String getComponentName();

	/**
	 * 激活浮动窗口。
	 */
	void active();
}
