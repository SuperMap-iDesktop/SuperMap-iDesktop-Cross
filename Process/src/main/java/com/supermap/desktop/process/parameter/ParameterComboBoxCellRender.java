package com.supermap.desktop.process.parameter;

import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ParameterComboBoxCellRender implements ListCellRenderer<ParameterDataNode> {
	@Override
	public Component getListCellRendererComponent(JList<? extends ParameterDataNode> list, ParameterDataNode value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel jLabel = new JLabel();
		jLabel.setOpaque(true);
		if (isSelected) {
			jLabel.setBackground(list.getSelectionBackground());
			jLabel.setForeground(list.getSelectionForeground());
		} else {
			jLabel.setBackground(list.getBackground());
			jLabel.setForeground(list.getForeground());
		}
		String describe = value.getDescribe();
		if (StringUtilities.isNullOrEmpty(describe)) {
			describe = String.valueOf(value.getData());
		}
		jLabel.setText(describe);
		return jLabel;

	}

}
