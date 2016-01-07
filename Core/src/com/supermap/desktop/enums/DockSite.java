package com.supermap.desktop.enums;

public enum DockSite {
	LEFT, TOP, RIGHT, BOTTOM;

	private static final String DOCKSITE_LEFT = "left";
	private static final String DOCKSITE_TOP = "top";
	private static final String DOCKSITE_RIGHT = "right";
	private static final String DOCKSITE_BOTTOM = "bottom";

	public static DockSite getDockSite(String dockSiteString) {
		DockSite dockSite = DockSite.LEFT;

		if (!dockSiteString.isEmpty()) {
			if (dockSiteString.equalsIgnoreCase(DOCKSITE_LEFT)) {
				dockSite = DockSite.LEFT;
			} else if (dockSiteString.equalsIgnoreCase(DOCKSITE_TOP)) {
				dockSite = DockSite.TOP;
			} else if (dockSiteString.equalsIgnoreCase(DOCKSITE_RIGHT)) {
				dockSite = DockSite.RIGHT;
			} else if (dockSiteString.equalsIgnoreCase(DOCKSITE_BOTTOM)) {
				dockSite = DockSite.BOTTOM;
			}
		}
		return dockSite;
	}

	public static String toString(DockSite dockSite) {
		String result = DOCKSITE_LEFT;

		switch (dockSite) {
		case LEFT:
			break;
		case TOP:
			result = DOCKSITE_TOP;
			break;
		case RIGHT:
			result = DOCKSITE_RIGHT;
			break;
		case BOTTOM:
			result = DOCKSITE_BOTTOM;
			break;
		default:
			break;
		}
		return result;
	}
}
