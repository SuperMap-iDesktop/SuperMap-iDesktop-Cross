package com.supermap.desktop.newtheme.themeLabel;

import com.supermap.data.TextStyle;
import com.supermap.mapping.ThemeLabelItem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class TableColorCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private List<ThemeLabelItem> items;
	private boolean isLabelRange = false;
	private TextStyle[] textStyle;

	public TableColorCellRenderer(List<ThemeLabelItem> items, boolean isLabelRange) {
		this.items = items;
		this.isLabelRange = isLabelRange;
	}

	public TableColorCellRenderer(TextStyle[] textStyle) {
		this.textStyle = textStyle;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (2 == column && isLabelRange) {
			setForeground(items.get(row).getStyle().getForeColor());
		}
		if (0 == column && !isLabelRange) {
			setForeground(textStyle[row].getForeColor());
		}
		return c;
	}
}
