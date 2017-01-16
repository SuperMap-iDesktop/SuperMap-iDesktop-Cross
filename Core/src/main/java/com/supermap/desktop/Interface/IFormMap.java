package com.supermap.desktop.Interface;

import com.supermap.data.Dataset;
import com.supermap.data.TextStyle;
import com.supermap.desktop.event.ActiveLayersChangedListener;
import com.supermap.mapping.Layer;
import com.supermap.ui.MapControl;

public interface IFormMap extends IForm {

	void setVisibleScales(double[] scales);

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
	
	int getIsShowPopupMenu();

	void updataSelectNumber();

	void setSelectedGeometryProperty();

	void openMap(String mapName);

	int getSelectedCount();

	void removeLayers(Layer[] activeLayers);

	void setVisibleScalesEnabled(boolean isVisibleScalesEnabled);

	// 文本默认风格设置 2017.1.13 李逍遥 part1   共计part9
	void setDefaultTextStyle(TextStyle tempTextStyle);

	 TextStyle getDefaultTextStyle();

	void setDefaultTextRotationAngle(double tempRotationAngle);

	double getDefaultTextRotationAngle();
}
