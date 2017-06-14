package com.supermap.desktop.utilities;

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

	/**
	 * 判断是否是linux系统，暂时认为不是windows就是linux
	 * 避免增加平台时修改“!SystemPropertyUtilities.isWindows()”这样的调用
	 * @return
	 */
	public static boolean isLinux() {
		return !isWindows();
	}

	public static double getSystemSizeRate() {
		return Toolkit.getDefaultToolkit().getScreenResolution() / DEFAULT_SCREEN_RESOLUTION;
	}

	public static boolean isSupportPlatform(String platform) {
		if (StringUtilities.isNullOrEmpty(platform) || platform.equalsIgnoreCase("all")) {
			return true;
		}
		if (isWindows() == platform.equalsIgnoreCase("windows")) {
			return true;
		}
		return false;
	}
}
