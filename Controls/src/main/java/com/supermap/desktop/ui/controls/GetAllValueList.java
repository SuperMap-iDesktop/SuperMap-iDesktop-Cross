package com.supermap.desktop.ui.controls;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class GetAllValueList extends JList {
	GetAllValueModel model = new GetAllValueModel();

	public GetAllValueList() {
		super();
		this.setModel(model);
		this.setCellRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = new JLabel();
				jLabel.setText(String.valueOf(value));
				jLabel.setOpaque(true);
				if (isSelected) {
					jLabel.setBackground(list.getSelectionBackground());
					jLabel.setForeground(list.getSelectionForeground());
				} else {
					jLabel.setBackground(list.getBackground());
					jLabel.setForeground(list.getForeground());
				}
				return jLabel;
			}
		});
	}

	public int getValueIndex(String value) {
		int defaultValue = this.getSelectedIndex();
		for (int i = 0; i < this.getModel().getSize(); i++) {
			if (this.getModel().getElementAt(i).toString().startsWith(value)) {
				return i;
			}
		}
		return defaultValue;
	}

	public void removeAllElements() {
		model.removeAll();
	}

	public void resetValue(Object[] allValue) {
		if (allValue != null && allValue.length > 0) {
			model.setDatas(allValue);
			this.setSelectedIndex(0);
		}
	}
}