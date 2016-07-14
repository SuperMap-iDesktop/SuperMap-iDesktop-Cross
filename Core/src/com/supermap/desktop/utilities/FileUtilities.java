package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtilities {

	private FileUtilities() {
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
		if (file == null) {
			return null;
		}
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf(".");

		// 该字符串中存在 . 并且不在末尾
		if (lastDotIndex != -1 && lastDotIndex != fileName.length() - 1) {
			fileName = fileName.substring(0, lastDotIndex);
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
				}
				result = file.delete();

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

	public static boolean writeToFile(String filePath, String value) {
		return writeToFile(new File(filePath), value);
	}

	public static boolean writeToFile(File file, String value) {
		try {
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();
				file.createNewFile();
			}
			if (file.exists()) {
				FileOutputStream fOutputStream = new FileOutputStream(file.getPath());
				OutputStreamWriter OutputStreamWriter = new OutputStreamWriter(fOutputStream, "UTF-8");
				OutputStreamWriter.write(value);
				OutputStreamWriter.flush();
				OutputStreamWriter.close();
				fOutputStream.close();
			}
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		}
		return true;
	}

	public static boolean isLegalFolderName(String folderName) {
		String folder = System.getProperty("java.io.tmpdir");
		if (!folder.endsWith(File.separator)) {
			folder += File.separator;
		}
		String filePath = folder + folderName + File.separator;
		if (new File(filePath).exists() || new File(filePath).mkdirs()) {
			new File(filePath).delete();
			return true;
		}
		return false;
	}
}
