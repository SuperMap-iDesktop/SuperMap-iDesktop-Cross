package com.supermap.desktop.utilties;

import javax.swing.*;

/**
 * table公共类
 *
 * @author XiaJt
 */
public class TableUtilties {

	private TableUtilties() {

	}

	public static void invertSelection(JTable table) {
		for (int rowCount = table.getRowCount() - 1; rowCount >= 0; rowCount--) {
			if (table.isRowSelected(rowCount)) {
				table.removeRowSelectionInterval(rowCount, rowCount);
			} else {
				table.addRowSelectionInterval(rowCount, rowCount);
			}
		}
	}
}
