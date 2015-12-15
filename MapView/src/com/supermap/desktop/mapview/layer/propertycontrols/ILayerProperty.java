package com.supermap.desktop.mapview.layer.propertycontrols;

public interface ILayerProperty {

	/**
	 * 获取属性是否有发生改变
	 * 
	 * @return
	 */
	boolean isChanged();

	/**
	 * 获取属性是否自动刷新
	 * 
	 * @return
	 */
	boolean isAutoApply();

	/**
	 * 设置属性是否自动刷新
	 * 
	 * @param isAutoApply
	 */
	void setAutoApply(boolean isAutoApply);

	/**
	 * 应用属性的更改
	 * 
	 * @return
	 */
	boolean apply();

	/**
	 * 添加属性改变事件监听
	 * 
	 * @param listener
	 */
	void addLayerPropertyChangedListener(ChangedListener listener);

	/**
	 * 移除属性改变事件监听
	 * 
	 * @param listener
	 */
	void removeLayerPropertyChangedListener(ChangedListener listener);
}
