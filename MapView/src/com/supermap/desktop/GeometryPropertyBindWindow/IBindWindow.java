package com.supermap.desktop.GeometryPropertyBindWindow;

import com.supermap.data.Dataset;
import com.supermap.desktop.Interface.IFormTabular;
import com.supermap.mapping.Selection;

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
	public boolean isSelectionHasChanged();

	/**
	 * 设置当前活动的数据集
	 * 
	 * @param dataset
	 */
	public void setActiveDataset(Dataset dataset);

	/**
	 * 获取当前活动的数据集
	 * 
	 * @param dataset
	 */
	public Dataset getActiveDataset();

	/**
	 * 刷新属性表
	 */
	public void refreshFormTabular(int[] addRows);

	/**
	 * 移除事件
	 */
	public void removeEvents();

	/**
	 * 销毁类，释放资源
	 */
	public void dispose();

	public void addMapSelectionChangeListener(MapSelectionChangeListener l);

	public void removeMapSelectionChangeListener(MapSelectionChangeListener l);

	public void fireSelectionChanged(Selection selection);

	/**
	 * 获取属性表
	 */
	public IFormTabular getTabular();
}
