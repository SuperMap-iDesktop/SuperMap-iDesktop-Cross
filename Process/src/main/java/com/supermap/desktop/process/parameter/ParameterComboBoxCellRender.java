package com.supermap.desktop.process.parameter;

import com.supermap.desktop.process.parameter.interfaces.IConGetter;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class ParameterComboBoxCellRender implements ListCellRenderer<ParameterDataNode> {

	private IConGetter iConGetter;

	public ParameterComboBoxCellRender() {

	}

	public ParameterComboBoxCellRender(IConGetter iConGetter) {
		this.iConGetter = iConGetter;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends ParameterDataNode> list, ParameterDataNode value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel jLabel = new JLabel();
		jLabel.setOpaque(true);
		if (iConGetter != null) {
			jLabel.setIcon(iConGetter.getICon(value));
		}

		if (isSelected) {
			jLabel.setBackground(list.getSelectionBackground());
			jLabel.setForeground(list.getSelectionForeground());
		} else {
			jLabel.setBackground(list.getBackground());
			jLabel.setForeground(list.getForeground());
		}

		if (value != null) {

			String describe = value.getDescribe();
			if (StringUtilities.isNullOrEmpty(describe)) {
				describe = String.valueOf(value.getData());
			}
			jLabel.setText(describe);
		}
		return jLabel;
	}

}
