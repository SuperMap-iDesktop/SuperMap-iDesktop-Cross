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
		JLabel component = new JLabel();
		if (value != null && value instanceof StatisticsType) {
			component.setText(StatisticsTypeUtilities.getStatisticsTypeName((StatisticsType) value));
		}else if (value != null && value instanceof com.supermap.analyst.spatialanalyst.StatisticsType) {
			component.setText(StatisticsTypeUtilities.getStatisticsTypeNameForOtherType((com.supermap.analyst.spatialanalyst.StatisticsType) value));
		}
		component.setOpaque(true);
		if (isSelected) {
			component.setBackground(list.getSelectionBackground());
			component.setForeground(list.getSelectionForeground());
		} else {
			component.setBackground(list.getBackground());
			component.setForeground(list.getForeground());
		}
		return component;
	}
}
