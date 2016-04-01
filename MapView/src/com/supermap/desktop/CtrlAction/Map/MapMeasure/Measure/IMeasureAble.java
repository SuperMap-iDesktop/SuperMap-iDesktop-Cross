package com.supermap.desktop.CtrlAction.Map.MapMeasure.Measure;

import com.supermap.desktop.enums.MeasureType;
import com.supermap.ui.MapControl;

/**
 * Created by Administrator on 2016/2/26.
 */
public interface IMeasureAble {
	/**
	 * 开始量算
	 */
	void startMeasure();

	/**
	 * 结束量算
	 */
	void stopMeasure();

	/**
	 * 设置mapControl
	 *
	 * @param mapControl 需要量算的地图控件
	 */
	void setMapControl(MapControl mapControl);

	boolean isMeasureAble();

	MeasureType getMeasureType();

	MapControl getMapControl();
}
