package com.supermap.desktop.Interface;

import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.event.TabularValueChangedListener;

import javax.swing.*;

public interface IFormTabular extends IForm {

	/**
	 * 获取数据表
	 */
	Recordset getRecordset();

	/**
	 * 设置数据表
	 * 
	 * @param recordset
	 * @return
	 */
	void setRecordset(Recordset recordset);

	DatasetVector getDataset();

	/**
	 * 获取数据行数
	 * 
	 * @return
	 */
	int getRowCount();

	/**
	 * 获取选中行
	 * 
	 * @return
	 */
	int getSelectedRow();

	/**
	 * 获取选中行
	 * 
	 * @return
	 */
	int[] getSelectedRows();

	/**
	 * 定位到指定行
	 */
	void goToRow(int goToRow);

	void sureRowVisible(int row);

	/**
	 * 添加选中项
	 */
	void addSelectionRows(int... tempRows);

	/**
	 * 获取选中行的数目
	 */
	int getSelectColumnCount();

	/**
	 * 获取选中的列
	 * 
	 * @return
	 */
	int[] getSelectedColumns();

	/**
	 * 获取指定行列的数据
	 */
	Object getValueAt(int row, int column);

	/**
	 * 按条件排序
	 */
	boolean sortRecordset(String sortKind, int... selectedColumns);

	/**
	 * 统计信息
	 */
	boolean doStatisticAnalust(StatisticMode statisticMode, String successMessage);

	/**
	 * 获取字段类型
	 */
	String getSelectColumnType(int columnIndex);

	/**
	 * 获取字段名称
	 * 
	 * @param columnIndex
	 *            列号
	 * @return
	 */
	String getSelectColumnName(int columnIndex);

	/**
	 * 获取属性表table容器
	 * 
	 * @return
	 */
	JTable getjTableTabular();

	/**
	 * 获取属性表list容器
	 * 
	 * @return
	 */
	JList getRowHeader();

	void addValueChangedListener(TabularValueChangedListener tabularValueChangedListener);

	void removeValueChangedListener(TabularValueChangedListener tabularValueChangedListener);

	/**
	 * 将recordset定位到指定行并返回smId
	 *
	 * @param row
	 * @return
	 */
	int getSmId(int row);

	ITabularEditHistoryManager getEditHistoryManager();

	boolean canRedo();

	boolean canUndo();

	void redo();

	void undo();

	void setSelectedCellBySmIDs(int[] smIds, String fieldName);

	boolean getHiddenSystemField();

	void setHiddenSystemField(boolean hiddenSystemField);

	int getModelColumn(int columnIndex);


	void addRow(Geometry geometry);

	void deleteRows(int[] viewRows);

	void refresh();
}
