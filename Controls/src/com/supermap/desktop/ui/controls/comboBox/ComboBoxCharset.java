package com.supermap.desktop.ui.controls.comboBox;

import javax.swing.JComboBox;

import com.supermap.data.Enum;
import com.supermap.data.Charset;
import com.supermap.desktop.utilties.CharsetUtilties;

public class ComboBoxCharset extends JComboBox<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComboBoxCharset() {
		for (Enum item : Charset.getEnums(Charset.class)) {
			addItem(CharsetUtilties.getCharsetName((Charset) item));
		}
		this.setSelectedItem(Charset.DEFAULT);
	}

	public void setSelectedItem(Charset anObject) {
		super.setSelectedItem(CharsetUtilties.getCharsetName(anObject));
	}

	@Override
	public Charset getSelectedItem() {
		return CharsetUtilties.valueOf(dataModel.getSelectedItem().toString());
	}

}
