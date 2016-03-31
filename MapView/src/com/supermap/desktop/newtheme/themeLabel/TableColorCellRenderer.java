package com.supermap.desktop.newtheme.themeLabel;

import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.supermap.mapping.ThemeLabelItem;

public class TableColorCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private List<ThemeLabelItem> items;

	public TableColorCellRenderer(List<ThemeLabelItem> items) {
		this.items = items;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (2 == column) {
			setForeground(items.get(row).getStyle().getForeColor());
		}
		return c;
	}
}
