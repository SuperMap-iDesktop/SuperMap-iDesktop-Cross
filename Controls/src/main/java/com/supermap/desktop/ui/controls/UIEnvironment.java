package com.supermap.desktop.ui.controls;

import com.supermap.data.GeoPoint;

/**
 * Control模块环境类型，该类型用于与本地库的交互
 */
public class UIEnvironment {
	private static GeoPoint geoPoint;

	static {
		// 加载一下Wrap
		geoPoint = new GeoPoint();
	}

	private UIEnvironment() {
	}

	// 获取base库的父路径
	public native static String jni_GetBasePath();

	private static String resourcesPath = null;

	public static void setResourcesPath(String path) {
		resourcesPath = path;
	}

	public static String getResourcePath() {
		return resourcesPath;
	}

	public static int symbolPointMax = 50000;
}
