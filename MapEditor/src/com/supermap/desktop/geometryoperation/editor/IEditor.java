package com.supermap.desktop.geometryoperation.editor;

import com.supermap.desktop.geometryoperation.EditEnvironment;
import com.supermap.ui.Action;

public interface IEditor {

	/**
	 * 获取需要的 MapControl Action
	 * 
	 * @return
	 */
	Action getMapControlAction();

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
