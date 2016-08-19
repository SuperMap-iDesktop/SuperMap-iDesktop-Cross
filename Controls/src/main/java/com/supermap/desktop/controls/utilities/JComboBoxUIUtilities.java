package com.supermap.desktop.controls.utilities;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class JComboBoxUIUtilities {
	private JComboBoxUIUtilities() {

	}

	public static int getItemIndex(JComboBox comboBox, Object item) {
		if (item instanceof String) {
			for (int i = 0; i < comboBox.getItemCount(); i++) {
				if (item.equals(comboBox.getItemAt(i))) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < comboBox.getItemCount(); i++) {
				if (item == comboBox.getItemAt(i)) {
					return i;
				}
			}
		}
		return -1;
	}
}
