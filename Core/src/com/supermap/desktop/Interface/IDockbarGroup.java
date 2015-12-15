package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.DockGroupStyle;
import com.supermap.desktop.enums.DockSite;
import com.supermap.desktop.enums.DockState;

/**
 * 定义浮动窗口分组所具有的属性的接口。
 * 
 * @author wuxb
 *
 */
public interface IDockbarGroup {

	/**
	 * 获取浮动窗口分组的索引。
	 * 
	 * @return
	 */
	int getIndex();

	/**
	 * 设置浮动窗口分组的索引。
	 */
	void setIndex();

	/**
	 * 获取浮动窗口的 ID。
	 * 
	 * @return
	 */
	String getID();

	/**
	 * 获取浮动窗口分组的停靠位置。
	 * 
	 * @return
	 */
	DockSite getDockSite();

	/**
	 * 设置浮动窗口分组的停靠位置。
	 */
	void setDockSite();

	/**
	 * 获取浮动窗口分组是否可见。
	 * 
	 * @return
	 */
	boolean isVisible();

	/**
	 * 设置浮动窗口分组是否可见。
	 */
	void setVisible();

	/**
	 * 获取浮动窗口分组的停靠状态。
	 * 
	 * @return
	 */
	DockState getDockState();

	/**
	 * 设置浮动窗口分组的停靠状态。
	 */
	void setDockState();

	/**
	 * 获取浮动窗口分组的布局方式。
	 * 
	 * @return
	 */
	DockGroupStyle getDockGroupStyle();

	/**
	 * 设置浮动窗口分组的布局方式。
	 */
	void setDockGroupStyle();
}
