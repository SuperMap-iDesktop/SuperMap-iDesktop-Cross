package com.supermap.desktop.utilties;

import java.io.File;

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
					fileSize += files[i].length();
				}
			} else {
				fileSize = file.length();
			}
		}
		return fileSize;
	}

	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf(".");

		// 该字符串中存在 . 并且不在末尾
		if (lastDotIndex != -1 && lastDotIndex != fileName.length() - 1) {
			fileName = fileName.substring(0, lastDotIndex - 1);
		}
		return fileName;
	}
}
