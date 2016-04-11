package com.supermap.desktop.geometryoperation.editor;

import com.supermap.desktop.geometryoperation.EditEnvironment;

public interface IEditor {

	/**
	 * 启用编辑功能
	 */
	void activate(EditEnvironment environment);

	/**
	 * 停用编辑功能
	 */
	void deactivate(EditEnvironment environment);

	boolean enble(EditEnvironment environment);

	boolean check(EditEnvironment environment);
}
