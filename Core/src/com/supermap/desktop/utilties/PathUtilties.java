package com.supermap.desktop.utilties;

import com.supermap.desktop.Application;

public class PathUtilties {
	private PathUtilties() {
		// 工具类不提供构造函数
	}

	/**
	 * 获取指定相对路径的绝对路径
	 * 
	 * @return
	 */
	public static String getRootPathName() {
		String rootPath = Application.getActiveApplication().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		rootPath = getParentPath(rootPath);
		rootPath += "Bin/";
		return rootPath;
	}

	/**
	 * 获取指定相对路径的绝对路径
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getFullPathName(String pathName, boolean isFolder) {

		String result = getRootPathName();
		try {
			String[] pathPrams = new String[] { result, pathName };
			result = combinePath(pathPrams, isFolder);
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 获取指定路径的上级路径
	 * 
	 * @param pathName
	 * @return
	 */
	public static String getParentPath(String pathName) {
		String pathNameTemp = pathName;
		String result = "";
		try {
			if (pathNameTemp != "") {
				pathNameTemp = pathNameTemp.replace("\\", "/");
				String[] splits = pathNameTemp.split("/");

				for (int i = 0; i < splits.length - 1; i++) {
					result += splits[i];
					result += "/";
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}

	/**
	 * 合并多个路径字符串
	 * 
	 * @param paths 路径数组
	 * @param isFolder 是否是文件夹路径
	 * @return 合并后的路径
	 */
	public static String combinePath(String[] paths, boolean isFolder) {
		String result = "";
		try {
			if (paths.length > 0) {
				result = paths[0];
			}

			if (result.endsWith("/") || result.endsWith("\\")) {
				// do nothing
			} else {
				result += "/";
			}

			for (int i = 1; i < paths.length; i++) {
				if (paths[i] != null && paths[i] != "") {
					if (paths[i].startsWith("/") || paths[i].startsWith("\\")) {
						paths[i] = paths[i].substring(1, paths[i].length());
					}

					else if (paths[i].startsWith("../") || paths[i].startsWith("..\\")) {
						result = getParentPath(result);
						paths[i] = paths[i].substring(3, paths[i].length());
					}
					result += paths[i];

					if (result.endsWith("/") || result.endsWith("\\")) {
						// do nothing
					} else {
						result += "/";
					}
				}
			}

			if (!isFolder && (result.endsWith("/") || result.endsWith("\\"))) {
				result = result.substring(0, result.length() - 1);
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
		return result;
	}
}
