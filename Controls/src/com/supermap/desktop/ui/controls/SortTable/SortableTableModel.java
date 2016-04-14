package com.supermap.desktop.ui.controls.SortTable;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

public class SortableTableModel extends DefaultTableModel {
	/**
	 * 假排序的索引，如果有哪一行不参与排序。需要手动屏蔽掉
	 */
	protected HashMap indexes;
	private TableSorter sorter;

	public SortableTableModel() {
	}

	public Object getValueAt(int row, int col) {
		return super.getValueAt(getIndexRow(row)[0], col);
	}

	public void setValueAt(Object value, int row, int col) {
		int rowIndex = row;
		int row1 = getIndexRow(row)[0];
		if (row1 != -1) {
			rowIndex = row1;
		}
		super.setValueAt(value, rowIndex, col);
	}

	/**
	 * 显示的行数->实际的行数
	 *
	 * @param rows 显示行数
	 * @return 实际行数
	 */
	protected int[] getIndexRow(int... rows) {
		if (indexes != null) {
			for (int i = 0; i < rows.length; i++) {
				int row = rows[i];
				if (indexes.get(row) != null) {
					rows[i] = (int) indexes.get(row);
				}
			}
		}
		return rows;
	}


	public void sortByColumn(int column, boolean isAscent) {
		if (sorter == null) {
			sorter = new TableSorter(this);
		}
		sorter.sort(column, isAscent);
		fireTableDataChanged();
	}

	/**
	 * 获得索引
	 *
	 * @return 索引数组
	 */
	public HashMap getSortIndexes() {
		int n = getRowCount();
		if (indexes != null) {
			return indexes;
		}
		indexes = new HashMap();
		for (int i = 0; i < n; i++) {
			indexes.put(i, i);
		}
		return indexes;
	}

	/**
	 * 获得需要排序的行，如果有不需要排序的行请在此处屏蔽
	 *
	 * @return 参与排序的行
	 */
	public int[] getSortRows() {
		int[] result = new int[this.getRowCount()];

		for (int i = 0; i < this.getRowCount(); i++) {
			result[i] = i;
		}
		return result;
	}

	/**
	 * 删除行的时候从indexes删除
	 *
	 * @param selectedRows 选中需要删除的行
	 */
	protected void removeRows(int... selectedRows) {
		if (indexes == null) {
			return;
		}
		HashMap currentIndex = new HashMap();

		int[] realRows = new int[selectedRows.length];
		for (int i = 0; i < selectedRows.length; i++) {
			int selectedRow = selectedRows[i];
			realRows[i] = (int) indexes.get(selectedRow);
			indexes.remove(selectedRow);
		}

		for (Object o : indexes.entrySet()) {
			Map.Entry entry = (Map.Entry) o;
			currentIndex.put(getCurrentRow((Integer) entry.getKey(), selectedRows), getCurrentRow((Integer) entry.getValue(), realRows));
		}
		this.indexes.clear();
		this.indexes = currentIndex;
	}

	private int getCurrentRow(int value, int[] selectedRows) {
		int count = 0;
		for (int selectedRow : selectedRows) {
			if (value >= selectedRow) {
				count++;
			}
		}
		return value - count;
	}

	public void addIndexRow(int i) {
		if (indexes != null) {
			indexes.put(i, i);
		}
	}
}
