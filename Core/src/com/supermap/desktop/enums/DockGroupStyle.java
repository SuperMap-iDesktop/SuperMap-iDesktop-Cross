package com.supermap.desktop.enums;

public enum DockGroupStyle {
	TABWiINDOW, HORIZONTAL, VERTICAL;

	private static final String DOCKGROUPSTYLE_TABWINDOW = "tab";
	private static final String DOCKGROUPSTYLE_HORIZONTAL = "horizontal";
	private static final String DOCKGROUPSTYLE_VERTICAL = "vertical";

	public static DockGroupStyle getDockGroupStyle(String styleString) {
		DockGroupStyle dockGroupStyle = DockGroupStyle.TABWiINDOW;

		try {
			if (!styleString.isEmpty()) {
				if (styleString.equalsIgnoreCase(DOCKGROUPSTYLE_HORIZONTAL)) {
					dockGroupStyle = DockGroupStyle.HORIZONTAL;
				} else if (styleString.equalsIgnoreCase(DOCKGROUPSTYLE_VERTICAL)) {
					dockGroupStyle = DockGroupStyle.VERTICAL;
				} else {
					dockGroupStyle = DockGroupStyle.TABWiINDOW;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dockGroupStyle;
	}

	public static String toString(DockGroupStyle dockGroupStyle) {
		String result = DOCKGROUPSTYLE_TABWINDOW;

		switch (dockGroupStyle) {
		case TABWiINDOW:
			break;
		case HORIZONTAL:
			result = DOCKGROUPSTYLE_HORIZONTAL;
			break;
		case VERTICAL:
			result = DOCKGROUPSTYLE_VERTICAL;
			break;
		default:
			break;
		}
		return result;
	}
}
