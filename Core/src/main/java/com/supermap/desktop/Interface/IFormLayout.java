package com.supermap.desktop.Interface;

import com.supermap.ui.MapLayoutControl;

public interface IFormLayout extends IForm {
	
	/**
	 * 获取布局窗口中的 MapLayoutControl 控件。
	 * @return
	 */
    MapLayoutControl getMapLayoutControl() ;
}
