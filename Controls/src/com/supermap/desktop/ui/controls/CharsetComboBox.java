package com.supermap.desktop.ui.controls;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.supermap.desktop.properties.CharsetProperties;

@SuppressWarnings("serial")
public class CharsetComboBox extends JComboBox<Object> {
	public CharsetComboBox() {
		setComboBoxModel();
	}

	private void setComboBoxModel() {
		setModel(new DefaultComboBoxModel<Object>(new String[] { CharsetProperties.getString(CharsetProperties.ANSI),
				CharsetProperties.getString(CharsetProperties.Arabic), CharsetProperties.getString(CharsetProperties.Baltic),
				CharsetProperties.getString(CharsetProperties.ChineseBIG5), CharsetProperties.getString(CharsetProperties.Cyrillic),
				CharsetProperties.getString(CharsetProperties.Default), CharsetProperties.getString(CharsetProperties.EastEurope),
				CharsetProperties.getString(CharsetProperties.GB18030), CharsetProperties.getString(CharsetProperties.Greek),
				CharsetProperties.getString(CharsetProperties.Hangeul), CharsetProperties.getString(CharsetProperties.Hebrew),
				CharsetProperties.getString(CharsetProperties.Johab), CharsetProperties.getString(CharsetProperties.Korean),
				CharsetProperties.getString(CharsetProperties.MAC), CharsetProperties.getString(CharsetProperties.OEM),
				CharsetProperties.getString(CharsetProperties.Russian), CharsetProperties.getString(CharsetProperties.ShiftJIS),
				CharsetProperties.getString(CharsetProperties.Symbol), CharsetProperties.getString(CharsetProperties.Thai),
				CharsetProperties.getString(CharsetProperties.Turkish), CharsetProperties.getString(CharsetProperties.Unicode),
				CharsetProperties.getString(CharsetProperties.UTF32), CharsetProperties.getString(CharsetProperties.UTF7),
				CharsetProperties.getString(CharsetProperties.UTF8), CharsetProperties.getString(CharsetProperties.Vietnamese),
				CharsetProperties.getString(CharsetProperties.Windows1252), CharsetProperties.getString(CharsetProperties.XIA5),
				CharsetProperties.getString(CharsetProperties.XIA5German), CharsetProperties.getString(CharsetProperties.XIA5Norwegian),
				CharsetProperties.getString(CharsetProperties.XIA5Swedish) }));
	}

}
