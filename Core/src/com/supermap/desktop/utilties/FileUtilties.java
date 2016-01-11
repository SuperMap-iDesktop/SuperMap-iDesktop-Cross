package com.supermap.desktop.utilties;

import java.io.File;

import com.supermap.desktop.Application;
import com.supermap.desktop.core.FileSize;
import com.supermap.desktop.core.FileSizeType;

public class FileUtilties {

	private FileUtilties() {
		// 工具类，不提供构造方法
	}

	/**
	 * 获取指定文件的文件大小
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file) {
		long fileSize = 0;

		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();

				for (int i = 0; i < files.length; i++) {
					fileSize += getFileSize(files[i]);
				}
			} else {
				fileSize = file.length();
			}
		}
		return fileSize;
	}

	/**
	 * 获取不带扩展名的文件名
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf(".");

		// 该字符串中存在 . 并且不在末尾
		if (lastDotIndex != -1 && lastDotIndex != fileName.length() - 1) {
			fileName = fileName.substring(0, lastDotIndex - 1);
		}
		return fileName;
	}

	/**
	 * 删除指定路径的文件或者文件夹
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		boolean result = true;

		try {
			if (file.exists()) {
				if (file.isDirectory()) {
					File[] childFiles = file.listFiles();

					for (int i = 0; i < childFiles.length; i++) {
						result = delete(childFiles[i]);

						if (!result) {
							break;
						}
					}
				} else {
					result = file.delete();
				}
			} else {
				result = false;
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return result;
	}

	/**
	 * 删除指定文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean delete(String filePath) {
		return delete(new File(filePath));
	}

	/**
	 * 判断指定文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}
}
