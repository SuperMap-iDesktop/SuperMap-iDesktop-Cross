package com.supermap.desktop.ui.controls;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CommonListCellRenderer implements ListCellRenderer<Object>, TableCellRenderer {
	public CommonListCellRenderer() {

	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return getJPanel(list, isSelected, value);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		return getJPanel(table, isSelected, value);
	}


	private JPanel getJPanel(JTable table, boolean isSelected, Object value) {
		if (value instanceof JPanel) {
			if (isSelected) {// 设置选中时的背景色
				((JPanel) value).setBackground(table.getSelectionBackground());
				((JPanel) value).setForeground(table.getSelectionForeground());
			} else {
				((JPanel) value).setBackground(table.getBackground());
				((JPanel) value).setForeground(table.getForeground());
			}
			return (JPanel) value;
		}
		return new JPanel();
	}

	private JPanel getJPanel(JList list, boolean isSelected, Object value) {
		if (value instanceof JPanel) {
			if (isSelected) {// 设置选中时的背景色
				((JPanel) value).setBackground(list.getSelectionBackground());
				((JPanel) value).setForeground(list.getSelectionForeground());
			} else {
				((JPanel) value).setBackground(list.getBackground());
				((JPanel) value).setForeground(list.getForeground());
			}
			return (JPanel) value;
		}
		return new JPanel();
	}

}