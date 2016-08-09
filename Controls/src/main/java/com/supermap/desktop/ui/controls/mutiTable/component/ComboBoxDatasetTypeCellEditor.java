package com.supermap.desktop.ui.controls.mutiTable.component;

import com.supermap.desktop.ui.controls.comboBox.ComboBoxDatasetType;
import com.supermap.desktop.ui.controls.comboBox.ComboBoxItem;

import javax.swing.*;

public class ComboBoxDatasetTypeCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public ComboBoxDatasetTypeCellEditor() {
		super(new ComboBoxDatasetType());
	}

	public ComboBoxDatasetTypeCellEditor(ComboBoxItem[] items) {
		super(new ComboBoxDatasetType());

		ComboBoxDatasetType comboBox = getComboBox();
		for (ComboBoxItem item : items) {
			comboBox.addItem(item);
		}
	}

	public ComboBoxDatasetType getComboBox() {
		return (ComboBoxDatasetType) super.getComponent();
	}
}
