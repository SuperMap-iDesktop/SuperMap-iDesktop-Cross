package com.supermap.desktop.ui.controls.TextFields;

/**
 * @author xiajt
 */
public interface ISmTextFieldLegit {
	/**
	 * 判断输入是否合法
	 *
	 * @param textFieldValue 当前值
	 * @return 是否合法
	 */
	boolean isTextFieldValueLegit(String textFieldValue);

	/**
	 * 获得一个合法值
	 *
	 * @param currentValue 当前值
	 * @param backUpValue  最后一次合法输入
	 * @return 处理后的值
	 */
	String getLegitValue(String currentValue, String backUpValue);
}
