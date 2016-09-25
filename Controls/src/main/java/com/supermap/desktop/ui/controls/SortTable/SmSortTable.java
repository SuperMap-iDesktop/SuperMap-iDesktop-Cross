package com.supermap.desktop.ui.controls.SortTable;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.controls.utilities.SortUIUtilities;
import com.supermap.desktop.ui.controls.CellRenders.TableDataCellRender;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
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
		((DefaultTableCellRenderer) this.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		this.setAutoCreateRowSorter(true);
		this.getTableHeader().setReorderingAllowed(false);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int rowAtPoint = rowAtPoint(e.getPoint());
					if (rowAtPoint != -1) {
						if (!isRowSelected(rowAtPoint)) {
							setRowSelectionInterval(rowAtPoint, rowAtPoint);
						}
					}
				}
			}
		});
		this.setDefaultRenderer(Dataset.class, new TableDataCellRender());
		this.setDefaultRenderer(Datasource.class, new TableDataCellRender());
		this.setDefaultRenderer(Map.class, new TableDataCellRender());
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
		if (getSelectedRow() == -1) {
			return -1;
		}
		return convertRowIndexToModel(getSelectedRow());
	}

	public int[] getSelectedModelRows() {
		int[] selectedRows = getSelectedRows();
		for (int i = 0; i < selectedRows.length; i++) {
			try {
				selectedRows[i] = convertRowIndexToModel(selectedRows[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			int i = 0;
			try {
				i = super.convertRowIndexToView(index);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return i;
		}

		@Override
		public int getViewRowCount() {
			return super.getViewRowCount() + 1;
		}
	}
}
