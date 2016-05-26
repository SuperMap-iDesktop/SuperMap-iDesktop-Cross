package com.supermap.desktop.controls.property;

import com.supermap.desktop.Interface.IProperty;

import javax.swing.*;

public abstract class AbstractPropertyControl extends JPanel implements IProperty {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String propertyName = "";

	/**
	 * Create the panel.
	 */
	protected AbstractPropertyControl(String propertyName) {
		this.propertyName = propertyName;
	}

	public final String getPropertyName() {
		return this.propertyName;
	}

	// @formatter:off
	/**
	 * 刷新显示数据
	 * 由于属性面板只在打开的时候设置一下显示数据，因此如果在打开状态更改了某些内容 导致显示数据改变的，就需要进行刷新。
	 */
	// @formatter:on
	public abstract void refreshData();

	public void dispose() {

	}

	public void hidden() {
	}



}
