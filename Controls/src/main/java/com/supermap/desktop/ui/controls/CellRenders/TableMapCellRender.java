package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.desktop.ui.controls.DataCell;
import com.supermap.mapping.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;

/**
 * @author XiaJT
 */
public class TableMapCellRender extends DefaultTableCellRenderer {
	private static HashMap<Map, DataCell> cache = new HashMap<>();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result;
		if (value == null) {
			result = new JLabel();
		} else if (value instanceof Map) {
			result = cache.get(value);
			if (result == null) {
				result = new DataCell(value);
				cache.put((Map) value, (DataCell) result);
			}
		} else {
			result = new JLabel(String.valueOf(value));
		}
		if (isSelected) {
			result.setBackground(table.getSelectionBackground());
		} else {
			result.setBackground(table.getBackground());
		}
		return result;
	}
}
