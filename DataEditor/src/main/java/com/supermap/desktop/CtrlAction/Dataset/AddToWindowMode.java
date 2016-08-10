package com.supermap.desktop.CtrlAction.Dataset;

import com.supermap.desktop.properties.CoreProperties;

public enum AddToWindowMode {
	NONEWINDOW,       // 不添加到地图窗口
    NEWWINDOW,        // 添加到新地图窗口
    CURRENTWINDOW;    // 添加到当前地图窗口       

	private static final String WINDOWMODE_NONE = CoreProperties.getString("String_UnAddToMap");
	private static final String WINDOWMODE_NEW = CoreProperties.getString("String_AddToNewMap");
	private static final String WINDOWMODE_CURRENT = CoreProperties.getString("String_AddToCurrentMap");	

	public static AddToWindowMode getWindowMode(String windowModeString) {
		AddToWindowMode windowMode = AddToWindowMode.NONEWINDOW;

		if (!windowModeString.isEmpty()) {
			if (windowModeString.equalsIgnoreCase(WINDOWMODE_NONE)) {
				windowMode = AddToWindowMode.NONEWINDOW;
			} else if (windowModeString.equalsIgnoreCase(WINDOWMODE_NEW)) {
				windowMode = AddToWindowMode.NEWWINDOW;
			} else if (windowModeString.equalsIgnoreCase(WINDOWMODE_CURRENT)) {
				windowMode = AddToWindowMode.CURRENTWINDOW;
			} 
		}
		return windowMode;
	}

	public static String toString(AddToWindowMode windowMode) {
		String result = WINDOWMODE_NONE;

		switch (windowMode) {
		case NONEWINDOW:
			break;
		case NEWWINDOW:
			result = WINDOWMODE_NEW;
			break;
		case CURRENTWINDOW:
			result = WINDOWMODE_CURRENT;
			break;
		default:
			break;
		}
		return result;
	}
}
