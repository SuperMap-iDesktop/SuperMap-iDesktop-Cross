package com.supermap.desktop.utilities;

import javax.swing.*;
import java.net.URL;

/**
 * Created by highsad on 2016/8/5.
 * 资源文件并不在包里被 Bundle 导出，因此如果其他 Bundle 要获取该 Bundle 的资源文件，就需要以如下方式实现
 * 1. 在该 Bundle 里实现获取指定资源的方法，并导出
 * 2. 使用 Bundle Service
 */
public class CoreResources {

	private static final String RESOURCE_ROOT = ""; // 资源文件相对 CLASSPATH 的路径

	private CoreResources() {
		// 工具类，不提供构造方法
	}

	/*
	* 获取指定 resourceName 的资源文件相对指定 CLASSPATH 的 URL
	* */
	public static URL getResourceURL(Class classImp, String resourceName) {
		if (classImp == null) {
			return null;
		}

		return classImp.getResource(RESOURCE_ROOT + resourceName);
	}

	/*
	* 获取 Core Bundle 下的资源文件的 URL
	* */
	public static URL getResourceURL(String resourceName) {
		return getResourceURL(CoreResources.class, resourceName);
	}

	/*
	* 获取 Core Bundle 下指定 iconName 的 Icon
	* */
	public static Icon getIcon(String iconName) {
		return getIcon(CoreResources.class, iconName);
	}

	/*
	* 获取相对指定 CLASSPATH 的 iconName 的 Icon
	* */
	public static Icon getIcon(Class classImp, String iconName) {
		URL url = getResourceURL(classImp, iconName);

		if (url == null) {
			return null;
		}
		return new ImageIcon(url);
	}
}
