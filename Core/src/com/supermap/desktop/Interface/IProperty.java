package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.PropertyType;

/**
 * 表示数据源、数据集、对象等的某一类属性
 * 
 * @author highsad
 *
 */
public interface IProperty {

	/**
	 * 获取属性类型
	 * 
	 * @return
	 */
	PropertyType getPropertyType();

	/**
	 * 获取属性名
	 * 
	 * @return
	 */
	String getPropertyName();
}
