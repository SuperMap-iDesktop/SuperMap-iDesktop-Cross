package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.data.FieldType;
import com.supermap.desktop.utilities.FieldTypeUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hanyz on 2017/5/4.
 */
public class ListFieldTypeCellRender extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel result = new JLabel();
		if (value != null && value instanceof FieldType) {
			result.setText(FieldTypeUtilities.getFieldTypeName((FieldType) value));
		}
		result.setOpaque(true);
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
