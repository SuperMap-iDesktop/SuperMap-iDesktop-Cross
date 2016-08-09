package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.Charset;
import com.supermap.data.Enum;
import com.supermap.desktop.utilities.CharsetUtilities;

import javax.swing.*;

public class ComboBoxCharset extends JComboBox<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComboBoxCharset() {
		for (Enum item : Charset.getEnums(Charset.class)) {
			addItem(CharsetUtilities.toString((Charset) item));
		}
		this.setSelectedItem(Charset.DEFAULT);
	}

	public void setSelectedItem(Charset anObject) {
		super.setSelectedItem(CharsetUtilities.toString(anObject));
	}

	@Override
	public Charset getSelectedItem() {
		return CharsetUtilities.valueOf(dataModel.getSelectedItem().toString());
	}

}
