/*
 * Copyright (c) 2009-2010 SIS CORPORATION, All rights reserved.
 */
package com.supermap.desktop.ui.controls.mutiTable.component;

import com.supermap.desktop.Application;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李海軍
 * @since 1.0.0
 * @version 1.0.0 2012/10/23
 *          <p>
 */
public class MutiTable extends JTable implements MouseListener {
	/**
	 * ID
	 */
	private static final long serialVersionUID = 584842405181279389L;

	/**
	 * 默认的选择列
	 */
	private final int DEFAULT_CHECKHEADERCOLUMN = -1;

	/**
	 * 默认_PREFERREDWIDTH
	 */
	private final int DEFAULT_PREFERREDWIDTH = 23;

	/**
	 * 默认_MAXWIDTH
	 */
	private final int DEFAULT_MAXWIDTH = 23;

	/**
	 * 默认_MINWIDTH
	 */
	private final int DEFAULT_MINWIDTH = 23;

	/**
	 * 默认列
	 */
	private int checkHeaderColumn = DEFAULT_CHECKHEADERCOLUMN;

	/**
	 * 带CheckBox的Header的渲染
	 */
	private transient final HeaderCheckBoxRenderer checkHeader = new HeaderCheckBoxRenderer();

	/**
	 * 之前的header的渲染
	 */
	private transient TableCellRenderer oldCheckHeader = null;

	/**
	 * 之前的表格的可操作状态
	 */
	private boolean oldEnable = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.columnAtPoint(e.getPoint()) == this.checkHeaderColumn) {
			if (e.getSource() == this.getTableHeader()) {
				// 点击在表头位置
				// 列的选中状态
				boolean isSelected = !checkHeader.isSelected();
				checkHeader.setSelected(isSelected);
				this.getTableHeader().repaint();
				// 设置指定列的全部cell的选中状态
				checkColumnCells(isSelected);
			} else {
				// 点击在cell的情况下
				// 设置cell的选中状态
				int row = this.rowAtPoint(e.getPoint());
				boolean isSelected = !(Boolean) (this.getModel().getValueAt(row, this.checkHeaderColumn));
				this.getModel().setValueAt(isSelected, row, this.checkHeaderColumn);
				this.setRowSelectionInterval(row, row);
				// 根据所有cell的选中状态，设置header的选中状态
				checkColumnHeader();
			}
		}

		// 设置表格的选中效果
		if (e.getSource() == this.getTableHeader()) {
			// 暂时先实现整列选中效果
			int column = this.columnAtPoint(e.getPoint());
			this.setColumnSelectionInterval(column, column);
		} else {
			// 暂时先实现整行选中效果
			int row = this.rowAtPoint(e.getPoint());
			this.setRowSelectionInterval(row, row);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// do nothing
	}

	/**
	 * 获取支持选择的列
	 * 
	 * @return 列索引
	 */
	public int getCheckHeaderColumn() {
		return checkHeaderColumn;
	}

	/**
	 * 设置支持选择的列
	 * 
	 * @param checkHeaderColumn 列索引
	 */
	public void setCheckHeaderColumn(int checkHeaderColumn) {
		// 定义表的列
		TableColumn tableColumn;

		// 删除原来的信息
		if (isCheckHeader()) {
			// 原来的选中header有效的情况下
			tableColumn = this.getColumnModel().getColumn(this.checkHeaderColumn);

			// 设置header的checkbox
			if (null != oldCheckHeader) {
				tableColumn.setHeaderRenderer(oldCheckHeader);
				// 恢复表的可操作性
				this.setEnabled(oldEnable);
			}

			// 监控header的mouse
			this.getTableHeader().removeMouseListener(this);
			// 监控cell的mouse
			this.removeMouseListener(this);
		}

		// 设置新信息
		// 设置checkheader
		this.checkHeaderColumn = checkHeaderColumn;

		if (!isCheckHeader()) {
			// checkheader无效的情况下
			this.checkHeaderColumn = DEFAULT_CHECKHEADERCOLUMN;
			return;
		}

		tableColumn = this.getColumnModel().getColumn(this.checkHeaderColumn);
		tableColumn.setPreferredWidth(DEFAULT_PREFERREDWIDTH);
		tableColumn.setMaxWidth(DEFAULT_MAXWIDTH);
		tableColumn.setMinWidth(DEFAULT_MINWIDTH);
		// 设置header的checkbox
		oldCheckHeader = tableColumn.getHeaderRenderer();
		tableColumn.setHeaderRenderer(checkHeader);
		// 监控header的mouse
		this.getTableHeader().addMouseListener(this);
		// 监控cell的mouse
		this.addMouseListener(this);
		// 删除表的可操作性
		oldEnable = this.isEnabled();

		if (oldEnable) {
			this.setEnabled(false);
		}

		// 根据所有cell的选中状态，设置head的选中状态
		checkColumnHeader();
	}

	/**
	 * 指定列の全てのセルはチェック状態を設定する。<br>
	 * 设置指定列的所有cell的选中状态
	 * 
	 * @param isCheck チェック状態 check状态
	 */
	public void checkColumnCells(boolean isCheck) {
		if (!isCheckHeader()) {
			// checkheader无效的情况
			return;
		}

		for (int ii = 0; ii < this.getRowCount(); ii++) {
			this.getModel().setValueAt(isCheck, ii, this.checkHeaderColumn);
		}
	}

	/**
	 * 根据所有cell的选中状态，设置head的选中状态
	 */
	public void checkColumnHeader() {
		if (hasCheckedRow()) {
			// 有选中的行的情况下
			if (this.checkHeader.isSelected()) {
				// header选中了的情况下
				return;
			}

			// 选中header
			this.checkHeader.setSelected(true);
			this.getTableHeader().repaint();
		} else {
			// 没有选中了的行的情况下
			if (!this.checkHeader.isSelected()) {
				return;
			}

			// 不选中header
			this.checkHeader.setSelected(false);
			this.getTableHeader().repaint();
		}
	}

	/**
	 * 获取是否有checkheader
	 * 
	 * @return 有checkheader返回true，没有返回false
	 */
	public boolean isCheckHeader() {
		return !(this.checkHeaderColumn < 0 || this.checkHeaderColumn >= this.getColumnCount());
	}

	/**
	 * 获取有没有选中了的行
	 * 
	 * @return 有选中行返回true，没有返回false
	 */
	public boolean hasCheckedRow() {
		if (!isCheckHeader()) {
			// checkheader无效的情况下
			return false;
		}

		for (int ii = 0; ii < this.getRowCount(); ii++) {
			// 取得原来的选中状态
			boolean isCheck = (Boolean) this.getModel().getValueAt(ii, this.checkHeaderColumn);

			if (isCheck) {
				// 该行选中的情况下
				return true;
			}
		}

		return false;
	}

	/**
	 * 取得所有选中了的行的索引
	 * 
	 * @return 返回所有选中了的行的索引
	 */
	public List<Integer> getAllCheckedRows() {
		// 生成选中的cell的索引
		List<Integer> rows = new ArrayList<Integer>();

		if (!isCheckHeader()) {
			// checkheader无效的情况下
			return rows;
		}

		for (int ii = 0; ii < this.getRowCount(); ii++) {
			// 取得原来的选中状态
			boolean isCheck = (Boolean) this.getModel().getValueAt(ii, this.checkHeaderColumn);

			if (isCheck) {
				// 该行选中的情况下
				rows.add(ii);
			}
		}

		return rows;
	}

	/**
	 * 取得指定列，所有选中行的值
	 * 
	 * @param col 列
	 * @return　返回指定列，所有选中行的值
	 */
	public List<Object> getAllCheckedColumn(int col) {
		// 生成选中的cell的索引
		List<Object> rows = new ArrayList<Object>();

		if (!isCheckHeader()) {
			// checkheader无效的情况下
			return rows;
		}

		for (int ii = 0; ii < this.getRowCount(); ii++) {
			// 取得原来的选中状态
			boolean isCheck = (Boolean) this.getModel().getValueAt(ii, this.checkHeaderColumn);

			if (isCheck) {
				// 该行选中的情况下
				rows.add(this.getModel().getValueAt(ii, col));
			}
		}

		return rows;
	}

	/**
	 * 更新数据的collection
	 * 
	 * @param datas　数据的collection
	 * @throws 抛出数据数不正确的异常
	 */
	public void refreshContents(Object[][] datas) {
		// 获取模型
		MutiTableModel mutiTableModel = (MutiTableModel) this.getModel();
		try {
			mutiTableModel.refreshContents(datas);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		// 根据所有cell的选中状态，设置header的选中状态
		checkColumnHeader();
		this.updateUI();
	}

	/**
	 * 追加一行数据
	 * 
	 * @param data　数据
	 * @throws Exception 抛出数据数不正确的异常
	 */
	public void addRow(Object[] data) {
		// 获取模型
		MutiTableModel mutiTableModel = (MutiTableModel) this.getModel();
		try {
			mutiTableModel.addRow(data);
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		// 根据所有cell的选中状态，设置header的选中状态
		checkColumnHeader();
		this.updateUI();

	}

	/**
	 * 删除指定行
	 * 
	 * @param row 行号
	 */
	public void removeRow(int row) {
		// 获取模型
		MutiTableModel mutiTableModel = (MutiTableModel) this.getModel();
		mutiTableModel.removeRow(row);
		// 根据所有cell的选中状态，设置header的选中状态
		checkColumnHeader();
		this.updateUI();
	}

	/**
	 * 删除指定行
	 * 
	 * @param row 行号
	 * @param count 行数
	 */
	public void removeRows(int row, int count) {
		// 获取模型
		MutiTableModel mutiTableModel = (MutiTableModel) this.getModel();
		mutiTableModel.removeRows(row, count);
		// 根据所有cell的选中状态，设置header的选中状态
		checkColumnHeader();
		this.updateUI();
	}
}
