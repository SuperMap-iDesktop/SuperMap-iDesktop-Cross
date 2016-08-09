package com.supermap.desktop.Interface;

import com.supermap.desktop.PluginInfo;
import com.supermap.desktop.enums.DockState;

import java.awt.*;

/**
 * 定义浮动窗口属性的接口。
 */
public interface IDockbar {

	/**
	 * 获取浮动窗口的浮动/停靠状态。
	 * 
	 * @return
	 */
	DockState getDockState();

	/**
	 * 设置浮动窗口的浮动/停靠状态。
	 */
	void setDockState(DockState dockState);

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
	Component getComponent();

	/**
	 * 获取浮动窗口的自定义属性。
	 * 
	 * @return
	 */
	String getCustomProperty();

	/**
	 * 设置浮动窗口的自定义属性。
	 */
	void setCustomProperty(String customProperty);

	/**
	 * 获取浮动窗口的标题栏标题。
	 * 
	 * @return
	 */
	String getLabel();

	/**
	 * 设置浮动窗口的标题栏标题。
	 */
	void setLabel(String label);

	/**
	 * 获取浮动窗口是否激活。
	 * 
	 * @return
	 */
	boolean isActived();

	/**
	 * 获取浮动窗口所属的插件信息。
	 * 
	 * @return
	 */
	PluginInfo getPluginInfo();

	/**
	 * 获取浮动窗口的索引。
	 * 
	 * @return
	 */
	int getIndex();

	/**
	 * 设置浮动窗口的索引。
	 */
	void setIndex(int index);

	/**
	 * 获取浮动窗口所包含的控件名称。
	 * 
	 * @return
	 */
	String getComponentName();

	/**
	 * 获取浮动窗口是否自动隐藏。 iDesktop java 后续使用 WindowBar 来代替这个属性，目前这个属性并没用处。
	 * 
	 * @return
	 */
	boolean isAutoHide();

	/**
	 * 设置浮动窗口是否自动隐藏。
	 */
	void setAutoHide(boolean isAutoHide);

	/**
	 * 获取浮动窗口左上角的浮动位置。
	 * 
	 * @return
	 */
	Point getFloatingLocation();

	/**
	 * 设置浮动窗口左上角的浮动位置。
	 */
	void setFloatingLocation(Point floatingLocation);

	/**
	 * 激活浮动窗口。
	 */
	void active();
}
