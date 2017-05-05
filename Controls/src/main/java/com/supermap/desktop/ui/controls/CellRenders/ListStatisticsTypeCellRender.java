package com.supermap.desktop.ui.controls.CellRenders;

import com.supermap.analyst.spatialstatistics.StatisticsType;
import com.supermap.desktop.utilities.StatisticsTypeUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hanyz on 2017/5/4.
 */
public class ListStatisticsTypeCellRender extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel result = new JLabel();
		if (value != null && value instanceof StatisticsType) {
			result.setText(StatisticsTypeUtilities.getStatisticsTypeName((StatisticsType) value));
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
