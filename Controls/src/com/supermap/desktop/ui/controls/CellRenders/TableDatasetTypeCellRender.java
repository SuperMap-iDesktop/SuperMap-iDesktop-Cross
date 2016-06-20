package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.data.DatasetType;
import com.supermap.desktop.ui.controls.DataCell;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author XiaJT
 */
public class TableDatasetTypeCellRender extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component result;
		if (value == null) {
			result = new JLabel();
		} else if (!(value instanceof DatasetType)) {
			result = new JLabel(String.valueOf(value));
		} else {
			result = new DataCell(((DatasetType) value));
		}
		if (isSelected) {
			result.setBackground(table.getSelectionBackground());
		} else {
			result.setBackground(table.getBackground());
		}
		return result;
	}
}
