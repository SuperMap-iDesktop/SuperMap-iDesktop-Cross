package com.supermap.desktop.ui.controls.prjcoordsys;

import com.supermap.data.Enum;
import com.supermap.desktop.controls.utilities.JLabelUIUtilities;
import com.supermap.desktop.ui.controls.comboBox.JSearchComboBox;
import com.supermap.desktop.ui.controls.comboBox.SearchBoxModel;
import com.supermap.desktop.utilities.PrjCoordSysTypeUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class MyEnumCellRender implements ListCellRenderer<Object> {
	private static Dimension labelPreferredSize = new Dimension(20, 23);
	private final JSearchComboBox<? extends Enum> source;

	public MyEnumCellRender(JSearchComboBox<? extends Enum> comboBox) {
		source = comboBox;
	}


	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel jLabel = new JLabel();
		jLabel.setOpaque(true);
		jLabel.setPreferredSize(labelPreferredSize);
		jLabel.setText(" " + PrjCoordSysTypeUtilities.getDescribe(((Enum) value).name()));
		if (isSelected) {
			jLabel.setBackground(list.getSelectionBackground());
		} else {
			jLabel.setBackground(list.getBackground());
		}

		ComboBoxEditor editor = source.getEditor();
		if (editor != null && editor.getItem() != null && ((SearchBoxModel) source.getModel()).isSearch()) {
			JLabelUIUtilities.highLightText(jLabel, editor.getItem().toString());
		}
		return jLabel;
	}


}
