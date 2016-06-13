package com.supermap.desktop.ui.controls.textStyle;

import java.util.HashMap;

import javax.swing.JComponent;

import com.supermap.desktop.enums.TextPartType;

public interface ITextPart extends IGeoTextStyle{
	/**
	 * 获取结果
	 * @return
	 */
	public HashMap<TextPartType,Object> getResultMap();
	
	public void setSubobjectEnabled(boolean enabled);
	
	public void setRotationEnabled(boolean enabled);
	
	/**
	 * 获取和TextStyleType内容对应的容器
	 * @return
	 */
	public HashMap<TextPartType,JComponent> getComponentsMap();
	/**
	 * 获取TextPart容器
	 * @return
	 */
	public HashMap<Integer, Object> getTextPartInfo();
	
	public void addTextPartChangeListener(TextPartChangeListener l);

	public void removeTextPartChangeListener(TextPartChangeListener l);

	 void fireTextPartChanged(TextPartType newValue);
}
