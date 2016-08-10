package com.supermap.desktop.enums;

public enum DockState {
	DOCK, FLOAT;

	private static final String DOCKSTATE_DOCK = "dock";
	private static final String DOCKSTATE_FLOAT = "float";

	public static DockState getDockState(String dockStateString) {
		DockState dockState = DockState.DOCK;

		if (!dockStateString.isEmpty() && dockStateString.equalsIgnoreCase(DOCKSTATE_FLOAT)) {
			dockState = DockState.FLOAT;
		}
		return dockState;
	}

	public static String toString(DockState dockState) {
		String result = DOCKSTATE_DOCK;

		switch (dockState) {
		case DOCK:
			break;
		case FLOAT:
			result = DOCKSTATE_FLOAT;
			break;
		default:
			break;
		}
		return result;
	}
}
