package com.supermap.desktop.utilties;

public class SystemPropertyUtilties {
	private SystemPropertyUtilties() {
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
}
