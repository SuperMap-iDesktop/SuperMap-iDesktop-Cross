package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

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

	public static String getTempFolder() {
		String folder = System.getProperty("java.io.tmpdir");
		if (!folder.endsWith(File.separator)) {
			folder += File.separator;
		}
		return folder;
	}

	public static boolean isLegalFolderName(String folderName) {
		if (folderName.indexOf('/') != -1 || folderName.indexOf('\\') != -1) {
			return false;
		}
		boolean isAllPoint = true;
		for (char c : folderName.toCharArray()) {
			if (c != '.') {
				isAllPoint = false;
				break;
			}
		}
		if (isAllPoint) {
			return false;
		}
		String folder = getTempFolder();
		String filePath = folder + folderName + File.separator;
		if (new File(filePath).exists() || new File(filePath).mkdirs()) {
			new File(filePath).delete();
			return true;
		}
		return false;
	}


	public static String getAppDataPath() {
		String OS = System.getProperty("os.name").toUpperCase();
		String result = null;
		if (OS.contains("WIN")) {
			result = System.getenv("APPDATA");
		} else if (OS.contains("MAC")) {
			result = System.getProperty("user.home") + "/Library/Application Support";
		} else if (OS.contains("NUX")) {
			result = "/var/tmp/";
		}
		if (result == null) {
			result = System.getProperty("user.dir");
		}

		if (StringUtilities.isNullOrEmpty(result)) {
			return null;
		}

		if (!result.endsWith(File.separator)) {
			result += File.separator;
		}

		result += "Supermap" + File.separator + "Cross" + File.separator;
		return result;
	}

	/**
	 * 复制单个文件
	 *
	 * @param srcFileName  待复制的文件名
	 * @param destFileName 目标文件名
	 * @param overlay      如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String destFileName,
	                               boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}

		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return false;
				}
			}
		}

		// 复制文件
		try (FileInputStream inStream = new FileInputStream(srcFile);
		     FileOutputStream outStream = new FileOutputStream(destFile);
		     FileChannel in = inStream.getChannel();
		     FileChannel out = outStream.getChannel()) {
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
			return false;
		}
		return true;
	}

	/**
	 * 复制整个目录的内容
	 *
	 * @param srcDirName  待复制目录的目录名
	 * @param destDirName 目标目录名
	 * @param overlay     如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String destDirName,
	                                    boolean overlay) {
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			return false;
		} else if (!srcDir.isDirectory()) {
			return false;
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists()) {
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay) {
				new File(destDirName).delete();
			} else {
				return false;
			}
		} else {
			// 创建目的目录
			if (!destDir.mkdirs()) {
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = copyFile(files[i].getAbsolutePath(),
						destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = copyDirectory(files[i].getAbsolutePath(),
						destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		return flag;
	}

}
