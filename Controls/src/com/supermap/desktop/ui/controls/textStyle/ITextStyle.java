package com.supermap.desktop.ui.controls.textStyle;

import java.util.HashMap;

import javax.swing.*;

import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;

public interface ITextStyle extends IGeoTextStyle {

	/**
	 * 设置显示的textStyle
	 * 
	 * @param textStyle
	 */
	public void setTextStyle(TextStyle textStyle);

	/**
	 * 设置是否为文本属性
	 * 
	 * @param isProperty
	 */
	public void setProperty(boolean isProperty);

	/**
	 * 是否显示字高和字宽的单位
	 * 
	 * @param unityVisible
	 */
	public void setUnityVisible(boolean unityVisible);

	/**
	 * 是否显示轮廓宽度设置
	 * 
	 * @param showOutLineWidth
	 */
	public void setOutLineWidth(boolean showOutLineWidth);

	/**
	 * 初始化界面
	 */
	public void initTextBasicPanel();

	/**
	 * 返回设置结果
	 * 
	 * @return
	 */
	public HashMap<TextStyleType, Object> getResultMap();

	/**
	 * 获取和TextStyleType类容对应的容器
	 * 
	 * @return
	 */
	public HashMap<TextStyleType, JComponent> getComponentsMap();

	public void addTextStyleChangeListener(TextStyleChangeListener l);

	public void removeTextStyleChangeListener(TextStyleChangeListener l);

	void fireTextStyleChanged(TextStyleType newValue);
	/**
	 * 获取文本基本信息界面
	 * @return
	 */
	public JPanel getBasicsetPanel();
	/**
	 * 获取文本效果设置界面
	 * @return
	 */
	public JPanel getEffectPanel();
}
