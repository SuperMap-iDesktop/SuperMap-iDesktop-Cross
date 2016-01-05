package com.supermap.desktop.ui.controls.mutiTable.component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import com.supermap.desktop.ui.controls.comboBox.ComboBoxItem;
import com.supermap.desktop.ui.controls.comboBox.UIComboBox;

public class ComboBoxCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public ComboBoxCellEditor() {
		super(new UIComboBox());
	}

	public ComboBoxCellEditor(ComboBoxItem[] items) {
		super(new UIComboBox());

		UIComboBox comboBox = getComboBox();
		for (ComboBoxItem item : items) {
			comboBox.addItem(item);
		}
	}

	public UIComboBox getComboBox() {
		return (UIComboBox) super.getComponent();
	}

	public boolean selectedByValue() {
		boolean result = false;
		for (int index = 0; index < this.getComboBox().getItemCount(); index++) {
			if (this.getCellEditorValue().equals(this.getComboBox().getItemAt(index))) {
				this.getComboBox().setSelectedIndex(index);
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean selectedByValue(UIComboBox component, Object value) {
		boolean result = false;
		for (int index = 0; index < component.getItemCount(); index++) {
			if (value.equals(component.getItemAt(index))) {
				component.setSelectedIndex(index);
				result = true;
				break;
			}
		}
		return result;
	}
}
