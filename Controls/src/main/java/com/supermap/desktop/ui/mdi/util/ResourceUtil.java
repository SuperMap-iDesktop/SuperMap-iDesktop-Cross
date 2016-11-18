package com.supermap.desktop.ui.mdi.util;

import javax.swing.ImageIcon;

public class ResourceUtil {

	public static ImageIcon getIcon(String resourcePath) {
		return new ImageIcon(ResourceUtil.class.getResource(resourcePath));
	}
}
