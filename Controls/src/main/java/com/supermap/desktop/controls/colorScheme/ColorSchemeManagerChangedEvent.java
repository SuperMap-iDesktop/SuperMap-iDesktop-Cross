package com.supermap.desktop.controls.colorScheme;

import com.supermap.desktop.dialog.ColorSchemeDialogs.ColorSchemeTreeNode;

/**
 * @author XiaJT
 */
public class ColorSchemeManagerChangedEvent {
	private ColorSchemeTreeNode colorSchemeTreeNode;

	public ColorSchemeManagerChangedEvent(ColorSchemeTreeNode colorSchemeTreeNode) {
		this.colorSchemeTreeNode = colorSchemeTreeNode;
	}

	public ColorSchemeTreeNode getColorSchemeList() {
		return colorSchemeTreeNode;
	}
}
