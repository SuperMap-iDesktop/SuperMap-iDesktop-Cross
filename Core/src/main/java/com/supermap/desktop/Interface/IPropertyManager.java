package com.supermap.desktop.Interface;

import com.supermap.desktop.enums.PropertyType;

/**
 * 工作空间、数据源、数据集、对象等的属性管理器
 *
 * @author highsad
 */
public interface IPropertyManager {

	/**
	 * 设置当前属性集合
	 * 
	 * @param properties
	 */
	void setProperty(IProperty[] properties);

	/**
	 * 强制刷新属性显示（用于某些数据改变，需要联动刷新的情况）
	 */
	void refreshData();

	// @formatter:off
	/**
	 * 是否处于激活状态，非激活状态下无需进行刷新。
	 * 有些时候会根据当前操作来更新属性管理器里的数据，
	 * 而如果属性管理器是可见的容器的话，当它不可见的时候是不需要刷新数据的。
	 * 处于性能上的考虑，有必要的话自行实现一下这个方法，否则返回 true 即可。
	 * 
	 * @return
	 */
	
	// @formatter:on
	boolean isUsable();

	PropertyType getPropertyType();

	int getPropertyCount();

	IProperty getPropertyByIndex(int index);

	void setSelectedProperty(int index);

	void setSelectedProperty(IProperty property);

	int getPropertyIndex(IProperty property);

	IProperty getCurrentProperty();

	void setPropertyVisible(boolean visible);
}
