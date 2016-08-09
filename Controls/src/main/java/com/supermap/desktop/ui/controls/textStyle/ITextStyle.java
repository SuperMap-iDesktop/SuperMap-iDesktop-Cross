package com.supermap.desktop.ui.controls.textStyle;

import com.supermap.data.TextStyle;
import com.supermap.desktop.enums.TextStyleType;

import javax.swing.*;
import java.util.HashMap;

public interface ITextStyle extends IGeoTextStyle {

	/**
	 * 设置显示的textStyle
	 * 
	 * @param textStyle
	 */
	void setTextStyle(TextStyle textStyle);

	/**
	 * 设置是否为文本属性
	 * 
	 * @param isProperty
	 */
	void setProperty(boolean isProperty);

	/**
	 * 是否显示字高和字宽的单位
	 * 
	 * @param unityVisible
	 */
	void setUnityVisible(boolean unityVisible);

	/**
	 * 是否显示轮廓宽度设置
	 * 
	 * @param showOutLineWidth
	 */
	void setOutLineWidth(boolean showOutLineWidth);

	/**
	 * 是否是文本风格界面
	 * 
	 * @param isTextStyleSet
	 */
	void setTextStyleSet(boolean isTextStyleSet);

	/**
	 * 初始化界面
	 */
	void initTextBasicPanel();

	/**
	 * 返回设置结果
	 * 
	 * @return
	 */
	HashMap<TextStyleType, Object> getResultMap();

	/**
	 * 获取和TextStyleType类容对应的容器
	 * 
	 * @return
	 */
	HashMap<TextStyleType, JComponent> getComponentsMap();

	void addTextStyleChangeListener(TextStyleChangeListener l);

	void removeTextStyleChangeListener(TextStyleChangeListener l);

	void fireTextStyleChanged(TextStyleType newValue);

	/**
	 * 获取文本基本信息界面
	 * 
	 * @return
	 */
	JPanel getBasicsetPanel();

	/**
	 * 获取文本效果设置界面
	 * 
	 * @return
	 */
	JPanel getEffectPanel();

	void initCheckBoxState();
}
