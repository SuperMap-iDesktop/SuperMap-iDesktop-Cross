/*
 * Copyright (c) 2009-2010 SIS CORPORATION, All rights reserved.
 */
package com.supermap.desktop.ui.controls.mutiTable.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;

/**
 * @author 李海軍
 * @since 1.0.0
 * @version 1.0.0 2012/10/23 初版
 *          <p>
 */
public class MutiTableModel extends AbstractTableModel {
	/**
	 * ID
	 */
	private static final long serialVersionUID = -1264704523326656387L;

	/**
	 * 数据长度不正确
	 */
	private static final String ERROR_DATA_LENGTH = ControlsProperties.getString("String_IncorrectDataLength");

	/**
	 * 支持选择的列
	 */
	protected int checkColumn = -1;

	/**
	 * 列标题
	 */
	protected List<String> columnNames;

	/**
	 * 数据内容列表
	 */
	protected transient List<Object> contents;
	
	/**
	 * 表格行后台数据列表
	 */
	protected transient ArrayList<Object> rowTagContents;

	/**
	 * 构造函数。
	 */
	public MutiTableModel() {
		// 列头序列
		this.columnNames = new Vector<String>();
		// 数据序列
		this.contents = new Vector<Object>();
		this.rowTagContents = new ArrayList<Object>();
	}

	/**
	 * 构造函数。
	 * 
	 * @param columnNames 指定列头
	 */
	public MutiTableModel(String[] columnNames) {
		this();

		if (null == columnNames) {
			return;
		}

		for (String columnName : columnNames) {
			// 初始化列头
			this.columnNames.add(columnName);
		}
	}

	/**
	 * 构造函数。
	 * 
	 * @param datas 数据
	 * @param columnNames 列头
	 * @throws Exception 抛出数据数不正确的异常
	 */
	public MutiTableModel(Object[][] datas, String[] columnNames) {
		this(columnNames);
		// 刷新数据
		try {
			refreshContents(datas);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	/**
	 * 根据指定的数据刷新列表内容。<br>
	 * 
	 * @param datas　指定的数据
	 */
	public void refreshContents(Object[][] datas) throws Exception {
		// 先情况原来的内容
		this.contents.clear();
		this.rowTagContents.clear();

		if (null == datas) {
			// 判断数据为空，不合法
			return;
		}

		for (Object[] data : datas) {
			// 将数据逐行添加到列表中
			addRow(data, null);
		}
	}

	/**
	 * 添加指定数据的一行。<br>
	 * 
	 * @param data　数据
	 * @throws Exception 抛出数据数不正确的异常
	 */
	public void addRow(Object[] data, Object tag) {
		if (null == data) {
			return;
		}

		if (this.columnNames.size() != data.length) {
			// 抛出数据数不正确的异常
			try {
				throw new Exception(ERROR_DATA_LENGTH);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		// 初始化内容存储
		Vector<Object> content = new Vector<Object>(this.columnNames.size());

		for (int i = 0; i < this.columnNames.size(); i++) {
			// 添加数据
			content.add(data[i]);
		}

		// 追加内容
		contents.add(content);
		rowTagContents.add(tag);
	}
	
	/**
	 * 添加指定数据的一行。<br>
	 * 
	 * @param data　数据
	 * @throws Exception 抛出数据数不正确的异常
	 */
	public void addRow(Object[] data) {
		if (null == data) {
			return;
		}

		if (this.columnNames.size() != data.length) {
			// 抛出数据数不正确的异常
			try {
				throw new Exception(ERROR_DATA_LENGTH);
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}

		// 初始化内容存储
		Vector<Object> content = new Vector<Object>(this.columnNames.size());

		for (int i = 0; i < this.columnNames.size(); i++) {
			// 添加数据
			content.add(data[i]);
		}

		// 追加内容
		contents.add(content);
	}

	/**
	 * 删除指定的行。<br>
	 * 
	 * @param row 行序号
	 */
	public void removeRow(int row) {
		contents.remove(row);
		rowTagContents.remove(row);
	}

	/**
	 * 删除指定的行。<br>
	 * 
	 * @param row 行序号
	 * @param count 行数
	 */
	public void removeRows(int row, int count) {
		for (int ii = 0; ii < count; ii++) {
			if (contents.size() > row) {
				contents.remove(row);
				rowTagContents.remove(row);
			}
		}
	}

	/**
	 * 删除多行数据
	 * 
	 * @param rows
	 */
	public void removeRows(int[] rows) {
		// 初始化内容存储
		Vector<Object> removeVector = new Vector<Object>(rows.length);
		ArrayList<Object> removeRowTag = new ArrayList<Object>(rows.length);
		for (int i = 0; i < rows.length; i++) {
			removeVector.add(contents.get(rows[i]));
			removeRowTag.add(this.rowTagContents.get(rows[i]));
		}
		contents.removeAll(removeVector);
		removeRowTag.removeAll(removeRowTag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == this.checkColumn) {
			return true;
		}

		return super.isCellEditable(row, col);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setValueAt(Object value, int row, int col) {
		((Vector) contents.get(row)).set(col, value);
		this.fireTableCellUpdated(row, col);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowTagAt(Object value, int row) {
		if (this.rowTagContents.size() <= row) {
			rowTagContents.add(value);
		} else {
			this.rowTagContents.set(row, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		Object value = getValueAt(0, column);

		if (value != null) {
			return value.getClass();
		}

		return super.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.contents.size();
	}

	@SuppressWarnings("unchecked")
	public Vector<Object> getRowValue(int row) {
		return (Vector<Object>) contents.get(row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getValueAt(int row, int col) {
		return ((Vector) contents.get(row)).get(col);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@SuppressWarnings("rawtypes")
	public Object getRowTagAt(int row) {
		return this.rowTagContents.get(row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	/**
	 * 获取支持选中的列。<br>
	 * 
	 * @return 支持选中的列的索引
	 */
	public int getCheckColumn() {
		return checkColumn;
	}

	/**
	 * 设置支持选中的列。<br>
	 * 
	 * @param checkColumn 支持选中的列的索引
	 */
	public void setCheckColumn(int checkColumn) {
		this.checkColumn = checkColumn;
	}

	/**
	 * 得到TableModel中的数据
	 * 
	 * @return
	 */
	public List<Object> getContents() {
		return contents;
	}

	public void setContents(List<Object> contents) {
		this.contents = contents;
	}

}
