package com.supermap.desktop.process;

import javax.swing.*;
import java.net.URL;

/**
 * Created by highsad on 2017/3/31.
 */
public class ProcessResources {

	private static final String RESOURCE_ROOT = ""; // 资源文件相对 CLASSPATH 的路径

	private ProcessResources() {
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
		return getResourceURL(ProcessResources.class, resourceName);
	}

	/*
	* 获取 Core Bundle 下指定 iconName 的 Icon
	* */
	public static Icon getIcon(String iconName) {
		return getIcon(ProcessResources.class, iconName);
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
