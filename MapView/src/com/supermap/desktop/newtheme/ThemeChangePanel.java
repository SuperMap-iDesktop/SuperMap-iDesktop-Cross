package com.supermap.desktop.newtheme;

import com.supermap.mapping.Theme;

import javax.swing.*;

/**
 *
 */
public abstract class ThemeChangePanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 获取当前专题图
	 *
	 * @return
	 */
	public abstract Theme getCurrentTheme();

	/**
	 * 注册事件
	 */
	abstract void registActionListener();

	/**
	 * 注销事件
	 */
	public abstract void unregistActionListener();
	/**
	 * 设置是否及时刷新
	 * @param isRefreshAtOnce
	 */
	abstract void setRefreshAtOnce(boolean isRefreshAtOnce);
	
	/**
	 * 刷新专题图图层和地图
	 */
	abstract void refreshMapAndLayer();
}
