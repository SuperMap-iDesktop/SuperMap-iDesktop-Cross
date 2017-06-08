package com.supermap.desktop.controls.GeometryPropertyBindWindow;

import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Selection;
import com.supermap.ui.MapControl;

/**
 * 关联窗口接口
 * 
 * @author xie
 *
 */
public interface IBindWindow {

	/**
	 * 选择集是否变化
	 * 
	 * @return
	 */
	 boolean isSelectionHasChanged();

	/**
	 * 设置当前活动的图层
	 * 
	 * @param mapcontrol
	 */
	 void setMapControl(MapControl mapcontrol);

	/**
	 * 获取当前活动的图层
	 *
     * @param
     */
	 MapControl getMapControl();

	/**
	 * 刷新属性表
	 */
	 void refreshFormTabular(int[] addRows);

	/**
	 * 移除事件
	 */
	 void removeEvents();

	/**
	 * 销毁类，释放资源
	 */
	 void dispose();

	 void addMapSelectionChangeListener(MapSelectionChangeListener l);

	 void removeMapSelectionChangeListener(MapSelectionChangeListener l);

     void fireSelectionChanged(Selection selection, Layer layer);

	/**
	 * 获取属性表
	 */
	 IFormTabular getTabular();
}
