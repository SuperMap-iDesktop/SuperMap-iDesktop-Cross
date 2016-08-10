package com.supermap.desktop.mapview.layer.propertymodel;

public class ComplexPropertyUtilties {

	private ComplexPropertyUtilties() {
		// 工具类，不提供构造方法
	}

	public static <T> T union(T pre, T now) {
		return (pre != null && pre.equals(now)) ? pre : null;
	}
}
