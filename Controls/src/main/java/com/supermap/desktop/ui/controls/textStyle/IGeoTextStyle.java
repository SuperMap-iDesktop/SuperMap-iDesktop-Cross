package com.supermap.desktop.ui.controls.textStyle;

import javax.swing.*;

public interface IGeoTextStyle {
	/**
	 * 获取预览面板
	 * 
	 * @return
	 */
	public JPanel getPanel();

	/**
	 * 设置控件可用性
	 */
	public void enabled(boolean enabled);

	/**
	 * 删除事件
	 */
	public void removeEvents();

}
