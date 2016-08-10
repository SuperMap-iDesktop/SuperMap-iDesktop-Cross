package com.supermap.desktop.ui.controls;

import com.supermap.desktop.properties.CharsetProperties;

import javax.swing.*;

@SuppressWarnings("serial")
public class CharsetComboBox extends JComboBox<Object> {
	public CharsetComboBox() {
		setComboBoxModel();
	}

	private void setComboBoxModel() {
		//@formatter:off
		setModel(new DefaultComboBoxModel<Object>(new String[] { 
				CharsetProperties.getString(CharsetProperties.Default),
				CharsetProperties.getString(CharsetProperties.ANSI),
				CharsetProperties.getString(CharsetProperties.OEM),
				CharsetProperties.getString(CharsetProperties.ChineseBIG5),
				CharsetProperties.getString(CharsetProperties.GB18030),
				CharsetProperties.getString(CharsetProperties.Cyrillic),
				CharsetProperties.getString(CharsetProperties.XIA5),
				CharsetProperties.getString(CharsetProperties.XIA5German),
				CharsetProperties.getString(CharsetProperties.XIA5Norwegian),
				CharsetProperties.getString(CharsetProperties.XIA5Swedish),
				CharsetProperties.getString(CharsetProperties.MAC),
				CharsetProperties.getString(CharsetProperties.Unicode),
				CharsetProperties.getString(CharsetProperties.UTF7),
				CharsetProperties.getString(CharsetProperties.UTF8),
				CharsetProperties.getString(CharsetProperties.UTF32), 
				CharsetProperties.getString(CharsetProperties.Windows1252),
				CharsetProperties.getString(CharsetProperties.Arabic),
				CharsetProperties.getString(CharsetProperties.Baltic),
				CharsetProperties.getString(CharsetProperties.Greek),
				CharsetProperties.getString(CharsetProperties.Johab),
				CharsetProperties.getString(CharsetProperties.Hangeul), 
				CharsetProperties.getString(CharsetProperties.EastEurope),
				CharsetProperties.getString(CharsetProperties.Russian),
				CharsetProperties.getString(CharsetProperties.Symbol),
				CharsetProperties.getString(CharsetProperties.Korean),
				CharsetProperties.getString(CharsetProperties.ShiftJIS),
				CharsetProperties.getString(CharsetProperties.Thai),
				CharsetProperties.getString(CharsetProperties.Turkish), 
				CharsetProperties.getString(CharsetProperties.Hebrew),
				CharsetProperties.getString(CharsetProperties.Vietnamese),
				 }));
		//@formatter:on
	}

}
