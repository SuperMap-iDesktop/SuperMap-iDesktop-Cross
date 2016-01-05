package com.supermap.desktop.utilties;

public class DoubleUtilties {

	private DoubleUtilties() {
		// 默认私有构造器
	}

	public static boolean equals(double d1, double d2, double pow) {
		return (d1 - d2) < Math.pow(10, pow * (-1));
	}
}
