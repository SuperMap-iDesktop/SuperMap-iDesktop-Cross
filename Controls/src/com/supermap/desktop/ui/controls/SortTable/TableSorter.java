package com.supermap.desktop.ui.controls.SortTable;

import com.supermap.desktop.controls.utilities.SortUIUtilities;

import java.util.HashMap;

/**
 * 表排序类，根据类型匹配对应的函数
 */
public class TableSorter {
	private SortableTableModel model;

	public TableSorter(SortableTableModel model) {
		this.model = model;
	}


	//n2 selection
	public void sort(int column, boolean isAscent) {
		int[] rows = model.getSortRows();
		HashMap indexes = model.getSortIndexes();

		int row1;
		int row2;
		for (int i = 0; i < rows.length - 1; i++) {
			row1 = rows[i];
			int k = row1;
			for (int j = i + 1; j < rows.length; j++) {
				row2 = rows[j];
				if (isAscent) {
					if (compare(column, k, row2) < 0) {
						k = row2;
					}
				} else {
					if (compare(column, k, row2) > 0) {
						k = row2;
					}
				}
			}
			if (k != row1) {
				int tmp = (int) indexes.get(row1);
				indexes.put(row1, indexes.get(k));
				indexes.put(k, tmp);
			}
		}
	}


	// comparaters

	private int compare(int column, int row1, int row2) {
		Object o1 = model.getValueAt(row1, column);
		Object o2 = model.getValueAt(row2, column);
		return SortUIUtilities.compareObject(o1, o2);

	}



}