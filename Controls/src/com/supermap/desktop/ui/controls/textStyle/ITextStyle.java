package com.supermap.desktop.ui.controls.textStyle;

import java.util.HashMap;

import javax.swing.*;

import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;

public interface ITextStyle extends IGeoTextStyle {
	
	public void setInitInfo(TextStyle textStyle, boolean isProperty,boolean unityVisible);
	/**
	 * 返回设置结果
	 * @return
	 */
	public HashMap<TextStyleType,Object> getResultMap();
	/**
	 * 获取和TextStyleType类容对应的容器
	 * @return
	 */
	public HashMap<TextStyleType,JComponent> getComponentsMap();
	
	
	public void addTextStyleChangeListener(TextStyleChangeListener l);

	public void removeTextStyleChangeListener(TextStyleChangeListener l);

	void fireTextStyleChanged(TextStyleType newValue);
	
	public JPanel getBasicsetPanel();
	
	public JPanel getEffectPanel();
}
