package com.supermap.desktop.Interface;

import com.supermap.data.Workspace;
import com.supermap.realspace.Layer3D;
import com.supermap.ui.SceneControl;

public interface IFormScene extends IForm {
	/**
	 * 获取场景窗口中的 SceneControl 控件。
	 * 
	 * @return
	 */
	SceneControl getSceneControl();

	/**
	 * 设置工作空间。
	 * 
	 * @param workspace
	 */
	void setWorkspace(Workspace workspace);

	/**
	 * 获取或设置场景窗口中所显示的三维图层的数组。
	 * 
	 * @return
	 */
	Layer3D[] getActiveLayer3Ds();

	void setActiveLayer3Ds(Layer3D[] activeLayer3Ds);
}
