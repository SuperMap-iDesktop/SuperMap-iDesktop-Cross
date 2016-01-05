package com.supermap.desktop.Interface;

import com.supermap.data.Dataset;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

public interface IFormMap extends IForm {

	/**
	 * 获取地图窗口中的 MapControl 控件。
	 * 
	 * @return
	 */
	MapControl getMapControl();

	/**
	 * 获取或设置地图窗口中所激活的图层（即选中图层）的数组。
	 * 
	 * @return
	 */
	Layer[] getActiveLayers();

	void setActiveLayers(Layer... activeLayers);

	void addActiveLayersChangedListener(ActiveLayersChangedListener listener);

	void removeActiveLayersChangedListener(ActiveLayersChangedListener listener);

	/**
	 * 删除图层时从当前选中图层中移除
	 * @param datasets
	 */
	void removeActiveLayersByDatasets(Dataset... datasets);

	void dontShowPopupMenu();

	void showPopupMenu();

	void updataSelectNumber();
}
