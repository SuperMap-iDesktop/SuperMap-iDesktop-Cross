package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.desktop.ui.controls.DataCell;

import javax.swing.*;
import java.awt.*;

/**
 * @author YuanR 17.2.15
 *         构造ListCellRenderer渲染器，主要适用于DatasetCombox和datasourceComboBox
 */
public class ListDataCellRender extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component result;
		if (value == null) {
			result = new JLabel(" ");
			((JLabel) result).setOpaque(true);
		} else {
			result = new DataCell(value);
		}
		if (isSelected) {
			result.setBackground(list.getSelectionBackground());
			result.setForeground(list.getSelectionForeground());
		} else {
			result.setBackground(list.getBackground());
			result.setForeground(list.getForeground());
		}
		return result;
	}
}