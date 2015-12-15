package com.supermap.desktop.ui.controls;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

public class CommonListCellRenderer implements ListCellRenderer<Object>,TableCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return getJPanel(isSelected, value);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return getJPanel(isSelected, value);
	}
	
	private JPanel getJPanel(boolean isSelected,Object value){
		if (value instanceof JPanel) {
			if (isSelected) {// 设置选中时的背景色
				((JPanel) value).setBackground(Color.LIGHT_GRAY);
			} else {
				((JPanel) value).setBackground(Color.white);
			}
			return (JPanel) value;
		}
		return new JPanel();
	}

}