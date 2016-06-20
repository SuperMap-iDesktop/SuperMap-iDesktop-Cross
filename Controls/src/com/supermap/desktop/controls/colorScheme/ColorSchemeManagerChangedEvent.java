package com.supermap.desktop.controls.colorScheme;

import java.util.List;

/**
 * @author XiaJT
 */
public class ColorSchemeManagerChangedEvent {
	private List colorSchemeList;

	public ColorSchemeManagerChangedEvent(List colorSchemeList) {
		this.colorSchemeList = colorSchemeList;
	}

	public List getColorSchemeList() {
		return colorSchemeList;
	}
}
