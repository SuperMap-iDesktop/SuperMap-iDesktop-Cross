package com.supermap.desktop.ui.controls;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CommonListCellRenderer implements ListCellRenderer<Object>, TableCellRenderer {
	public CommonListCellRenderer() {

	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return getJPanel(isSelected, value);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		return getJPanel(isSelected, value);
	}


	private JPanel getJPanel(boolean isSelected, Object value) {
		if (value instanceof JPanel) {
			if (isSelected) {// 设置选中时的背景色
				((JPanel) value).setBackground(new Color(51, 153, 255));
				((JPanel) value).setForeground(Color.white);
			} else {
				((JPanel) value).setBackground(Color.white);
				((JPanel) value).setForeground(Color.black);
			}
			return (JPanel) value;
		}
		return new JPanel();
	}

}