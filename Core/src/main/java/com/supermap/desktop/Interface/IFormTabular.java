package com.supermap.desktop.Interface;

import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;
import com.supermap.desktop.event.TabularValueChangedListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

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

	/**
	 * 添加选中项
	 */
	void addRows(List<Integer> tempRows);

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
	/**
	 * 获取属性表中和行数对应的ID
	 * @return
	 */
	HashMap<Integer, Object> getRowIndexMap();
	/**
	 * 获取属性表中和ID对应的行数
	 * @return
	 */
	HashMap<Object, Integer> getIdMap();

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
}
