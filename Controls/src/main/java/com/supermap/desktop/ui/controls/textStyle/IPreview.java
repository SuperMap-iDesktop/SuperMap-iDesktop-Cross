package com.supermap.desktop.ui.controls.textStyle;

import com.supermap.data.TextStyle;

public interface IPreview extends IGeoTextStyle{
	/**
	 * 刷新预览面板
	 */
	public void refresh(String text, TextStyle tempTextStyle,double rotation);

}
