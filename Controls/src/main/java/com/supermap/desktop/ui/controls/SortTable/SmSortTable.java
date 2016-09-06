package com.supermap.desktop.ui.controls.SortTable;

import com.supermap.desktop.controls.utilities.SortUIUtilities;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Comparator;

/**
 * @author XiaJT
 */
public class SmSortTable extends JTable {
	private Point point;
	private boolean isSortLastRow = true;
	private static final Comparator<Object> unSortComparator = new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			return 0;
		}
	};
	public SmSortTable() {
		super();
		init();
	}

	private void init() {
		this.setAutoCreateRowSorter(true);
		this.getTableHeader().setReorderingAllowed(false);
	}

	public SmSortTable(TableModel tableModel) {
		super(tableModel);
	}

	@Override
	public void setModel(TableModel dataModel) {
		super.setModel(dataModel);
		if (!isSortLastRow) {
			this.setRowSorter(getNoLastRowSorter());
		}
		for (int i = 0; i < dataModel.getColumnCount(); i++) {
			// 改变 TableRowSorter 之后需要重写这个方法或直接继承JTable
			((TableRowSorter) this.getRowSorter()).setComparator(i, SortUIUtilities.getComparatorInstance());
		}
		this.setRowHeight(23);
	}

	public int getSelectedModelRow() {
		return convertRowIndexToModel(getSelectedRow());
	}

	public int[] getSelectedModelRows() {
		int[] selectedRows = getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedRows[i] = convertRowIndexToModel(selectedRows[i]);
		}
		return selectedRows;
	}

	@Override
	public JToolTip createToolTip() {
		if (point != null) {
			int row = this.rowAtPoint(point);
			int column = this.columnAtPoint(point);
			if (row != -1 && column != -1) {
				return ((JComponent) this.getCellRenderer(row, column).getTableCellRendererComponent(this, this.getValueAt(row, column), true, true, row, column)).createToolTip();
			}
		}
		return super.createToolTip();
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		point = event.getPoint();
		return super.getToolTipText(event);
	}

	public void setIsSortLastRow(boolean isSortLastRow) {
		this.isSortLastRow = isSortLastRow;
		setModel(this.getModel());
	}

	public void setUnSortColumn(int... columns) {
		for (int column : columns) {
			((TableRowSorter) this.getRowSorter()).setComparator(column, unSortComparator);
		}

	}

	private RowSorter<? extends TableModel> getNoLastRowSorter() {
		return new NoLastRowSorter(this.getModel());
	}


	private class NoLastRowSorter extends TableRowSorter {
		public NoLastRowSorter() {
		}

		public NoLastRowSorter(TableModel model) {
			super(model);
		}

		@Override
		public int convertRowIndexToModel(int index) {
			int maxRow = super.getViewRowCount();
			if (index >= maxRow) {
				return index;
			}
			return super.convertRowIndexToModel(index);
		}

		@Override
		public int convertRowIndexToView(int index) {
			int maxRow = super.getModelRowCount();
			if (index > maxRow) {
				return index;
			}
			return super.convertRowIndexToView(index);
		}

		@Override
		public int getViewRowCount() {
			return super.getViewRowCount() + 1;
		}
	}
}
