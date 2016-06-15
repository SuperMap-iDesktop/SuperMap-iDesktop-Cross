package com.supermap.desktop.GeometryPropertyBindWindow;

import com.supermap.mapping.Selection;

public interface IBindProperty {
	
	/**
	 * 刷新地图
	 */
	public void refreshMap(Selection selection);
	
	/**
	 * 移除事件
	 */
	public void removeEvents();
	
	/**
	 * 销毁类，释放资源
	 */
	public void dispose();
	
	public void addPropertySelectChangeListener(PropertySelectChangeListener l);
	
	public void removePropertySelectChangeListener(PropertySelectChangeListener l);
	
	public void firePropertySelectChanged(int[] rows);
}
