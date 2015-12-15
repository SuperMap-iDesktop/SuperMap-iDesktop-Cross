package com.supermap.desktop.Interface;

/**
 * 工作空间、数据源、数据集、对象等的属性管理器
 * 
 * @author highsad
 *
 */
public interface IPropertyManager {

	/**
	 * 设置当前属性集合
	 * 
	 * @param properties
	 */
	void setProperty(IProperty[] properties);

	void refreshData();
}
