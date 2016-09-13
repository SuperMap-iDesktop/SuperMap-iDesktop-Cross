package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author XiaJT
 */
public class TableDataCellRender extends DefaultTableCellRenderer {
	private TableMapCellRender tableMapCellRender;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result;
		if (value == null) {
			result = new JLabel();
		} else if (value instanceof Map) {
			return getTableMapCellRender().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		} else {
			result = new DataCell(value);
		}
		if (isSelected) {
			result.setBackground(table.getSelectionBackground());
		} else {
			result.setBackground(table.getBackground());
		}
		return result;
	}

	public TableMapCellRender getTableMapCellRender() {
		if (tableMapCellRender == null) {
			tableMapCellRender = new TableMapCellRender();
		}
		return tableMapCellRender;
	}
}
