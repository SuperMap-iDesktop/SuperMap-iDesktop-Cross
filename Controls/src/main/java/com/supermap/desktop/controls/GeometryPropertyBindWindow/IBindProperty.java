package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;

public interface IBindProperty {

	/**
	 * 刷新地图
	 */
    void refreshMap(Selection selection, Layer layer);

	/**
	 * 移除事件
	 */
    void removeEvents();

	/**
	 * 销毁类，释放资源
	 */
    void dispose();

    void addPropertySelectChangeListener(PropertySelectChangeListener l);

    void removePropertySelectChangeListener(PropertySelectChangeListener l);

    void firePropertySelectChanged(int[] rows, Dataset dataset);
}
