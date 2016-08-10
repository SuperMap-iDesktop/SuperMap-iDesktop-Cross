package com.supermap.desktop.Interface;

public interface IBaseItem {

	/**
	 * 获取或设置控件对象是否可用。
	 */
	boolean isEnabled();

	void setEnabled(boolean enabled);

	/**
	 * 获取或设置控件是否可见。
	 */
	boolean isVisible();

	void setVisible(boolean visible);

	/**
	 * 获取或设置控件是否选中。
	 */
	boolean isChecked();

	void setChecked(boolean checked);

	/**
	 * 获取或设置控件的索引值，控件的索引值用来对处于同一层次内的 Ribbon 控件进行位置的排列。
	 */
	int getIndex();

	void setIndex(int index);

	/**
	 * 获取控件的唯一标识名称。
	 */
	String getID();

	/**
	 * 获取或设置触发控件事件时所要运行的内容。
	 */
	ICtrlAction getCtrlAction();

	void setCtrlAction(ICtrlAction ctrlAction);
}
