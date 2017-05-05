package com.supermap.desktop.utilities;

import javax.swing.*;
import java.awt.*;

/**
 * table公共类
 *
 * @author XiaJt
 */
public class TableUtilities {

	private TableUtilities() {

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

	public static void stopEditing(JTable table) {
		if (table != null && table.getCellEditor() != null) {
			table.getCellEditor().stopCellEditing();
		}
	}

	public static void setTableSelectedRows(JTable table, int... selectedRows) {
		if (selectedRows.length == 0) {
			table.clearSelection();
		} else {
			table.clearSelection();
			for (int selectedRow : selectedRows) {
				table.addRowSelectionInterval(selectedRow, selectedRow);
			}
		}
	}

	public static void scrollToLastSelectedRow(final JTable table) {
		if (table == null) {
			return;
		}
		int[] selectedRows = table.getSelectedRows();
		if (selectedRows.length > 0) {
			scrollToVisible(table, selectedRows[selectedRows.length - 1], 0);
		}
	}

	public static void scrollToVisible(final JTable table, int rowIndex, int vColIndex) {
		if (table == null || !(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();

		try {
			final Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
			Point pt = viewport.getViewPosition();
			rect.setLocation(rect.x - pt.x, rect.y - pt.y);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					table.scrollRectToVisible(rect);
				}
			});
		} catch (Exception e) {
		}
	}
}
