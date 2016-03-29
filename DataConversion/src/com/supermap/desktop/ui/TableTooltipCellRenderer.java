package com.supermap.desktop.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.supermap.desktop.dataconversion.DataConversionProperties;

public class TableTooltipCellRenderer extends JLabel implements TableCellRenderer {
	private static TableTooltipCellRenderer tooltipCellRenderer;

	private TableTooltipCellRenderer() {

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (null != value && !DataConversionProperties.getString("string_change").equals((String) value)
				&& !DataConversionProperties.getString("string_haschange").equals((String) value)) {
			this.setText((String) value);
			this.setToolTipText((String) value);
		}else {
			this.setText((String) value);
		}
		if (isSelected) {
			this.setOpaque(true);
			this.setBackground(new Color(36, 124, 255));
		} else {
			this.setOpaque(true);
			this.setBackground(Color.white);
		}
		return this;
	}

	public static TableTooltipCellRenderer getInstance() {
		if (null == tooltipCellRenderer) {
			tooltipCellRenderer = new TableTooltipCellRenderer();
		}
		return tooltipCellRenderer;
	}
}