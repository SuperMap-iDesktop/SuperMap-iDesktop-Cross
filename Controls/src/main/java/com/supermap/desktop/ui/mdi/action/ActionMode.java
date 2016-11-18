package com.supermap.desktop.ui.mdi.action;

/**
 * Created by highsad on 2016/9/19.
 */
public enum ActionMode {

	/**
	 * 为 MdiPage 自定义功能，将出现在 MdiPage 的 Tab 标签上
	 */
	PAGE(1),

	/**
	 * 为 MdiGroup 自定义功能，将出现在 MdiGroup 的右上角功能区
	 */
	GROUP(2),

	/**
	 * 为 MdiPage 或者 MdiGroup 通用自定义功能，将可以出现在 MdiPage 的 Tab 或者 MdiGroup 的右上角功能区
	 */
	PAGE_GROUP(4);

	private int value;

	private ActionMode(int value) {
		this.value = value;
	}
}
