package com.supermap.desktop.utilties;

import java.awt.*;

public class SystemPropertyUtilities {

	private static final double DEFAULT_SCREEN_RESOLUTION = 96;// 默认为96
	private SystemPropertyUtilities() {
		super();
	}

	/**
	 * 判断当前系统是否是windows系统，应用暂时只支持windows和linux系统
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		boolean isWindows = false;
		String system = System.getProperties().getProperty("os.name");
		if (system.startsWith("Windows")) {
			isWindows = true;
		}
		return isWindows;
	}

	public static double getSystemSizeRate() {
		return Toolkit.getDefaultToolkit().getScreenResolution() / DEFAULT_SCREEN_RESOLUTION;
	}
}
