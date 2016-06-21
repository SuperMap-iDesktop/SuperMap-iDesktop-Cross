package com.supermap.desktop.Interface;

import java.util.HashMap;

import javax.swing.JList;
import javax.swing.JTable;

import com.supermap.data.Recordset;
import com.supermap.data.StatisticMode;

public interface IFormTabular extends IForm {

	/**
	 * 获取数据表
	 */
	public Recordset getRecordset();

	/**
	 * 设置数据表
	 * 
	 * @param recordset
	 * @return
	 */
	public void setRecordset(Recordset recordset);

	/**
	 * 获取数据行数
	 * 
	 * @return
	 */
	public int getRowCount();

	/**
	 * 获取选中行
	 * 
	 * @return
	 */
	public int getSelectedRow();

	/**
	 * 获取选中行
	 * 
	 * @return
	 */
	public int[] getSelectedRows();

	/**
	 * 定位到指定行
	 */
	public void goToRow(int goToRow);

	/**
	 * 添加选中项
	 */
	public void addRows(int[] addRows);

	/**
	 * 获取选中行的数目
	 */
	public int getSelectColumnCount();

	/**
	 * 获取选中的列
	 * 
	 * @return
	 */
	public int[] getSelectedColumns();

	/**
	 * 获取指定行列的数据
	 */
	public Object getValueAt(int row, int column);

	/**
	 * 按条件排序
	 */
	public boolean sortRecordset(String sortKind, int... selectedColumns);

	/**
	 * 统计信息
	 */
	public boolean doStatisticAnalust(StatisticMode statisticMode, String successMessage);

	/**
	 * 获取字段类型
	 */
	public String getSelectColumnType(int columnIndex);

	/**
	 * 获取字段名称
	 * 
	 * @param columnIndex
	 *            列号
	 * @return
	 */
	public String getSelectColumnName(int columnIndex);

	/**
	 * 获取属性表table容器
	 * 
	 * @return
	 */
	public JTable getjTableTabular();

	/**
	 * 获取属性表list容器
	 * 
	 * @return
	 */
	public JList getRowHeader();
	/**
	 * 获取属性表中和行数对应的ID
	 * @return
	 */
	public HashMap<Integer, Object> getRowIndexMap();
	/**
	 * 获取属性表中和ID对应的行数
	 * @return
	 */
	public HashMap<Object, Integer> getIdMap();
}
