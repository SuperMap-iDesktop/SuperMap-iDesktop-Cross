package com.supermap.desktop.geometryoperation;

public interface IEditor {

	/**
	 * 启用编辑功能
	 */
	void activate();

	/**
	 * 停用编辑功能
	 */
	void deactivate();

	boolean enble();

	boolean check();
}
