package com.supermap.desktop.netservices.iserver;

import javax.swing.Icon;

/**
 * 带图标的字符串，为 JTable 的单元格提供支持
 * 
 * @author highsad
 *
 */
class StringWithIcon {
	private Icon icon;
	private String text;

	public StringWithIcon(Icon icon, String text) {
		this.icon = icon;
		this.text = text;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}
