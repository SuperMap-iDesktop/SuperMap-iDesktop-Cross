package com.supermap.desktop.core;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * log输出类
 * log4j提供的RollingFileAppender不支持输出日期；
 * log4j提供的DailyRollingFileAppender不支持指定文件最大长度，
 *   而且只有在日期超过当前指定时才会在文件加上日期标签。
 * @see org.apache.log4j.RollingFileAppender
 * @see org.apache.log4j.DailyRollingFileAppender
 */
public class SmRollingFileAppender extends RollingFileAppender {

	private long nextRollover = 0;
	private static Map<String, BeginFileData> fileMaps = new HashMap<>();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 先只支持这一个格式
	private static final String DATA = "[DATE]";

	/**
	 * 与父类方法大致相同，添加时间判断
	 */
	public void rollOver() {
		File target;
		File file;

		if (qw != null) {
			long size = ((CountingQuietWriter) qw).getCount();
			LogLog.debug("rolling over count=" + size);
			nextRollover = size + maxFileSize;
		}

		LogLog.debug("maxBackupIndex=" + maxBackupIndex);
		String nowDateString = sdf.format(new Date());
		String newFileName = (fileName.contains(".")
				? fileName.substring(0, fileName.lastIndexOf("."))
				: fileName);

		boolean renameSucceeded = true;
		// 如果maxBackups <= 0，那么文件不用重命名
		if (maxBackupIndex > 0) {

			// 删除旧文件
			file = new File(newFileName + '_'
					+ getIndex(maxBackupIndex));
			if (file.exists()) {
				renameSucceeded = file.delete();
			}

			for (int i = maxBackupIndex - 1; (i >= 1 && renameSucceeded); i--) {
				file = new File(newFileName + '_'
						+ getIndex(i));

				if (file.exists()) {
					target = new File(newFileName + '_'
							+ getIndex(i + 1));

					LogLog.debug("Renaming file " + file + " to " + target);
					renameSucceeded = file.renameTo(target);
				}
			}

			if (renameSucceeded) {
				BeginFileData beginFileData = fileMaps.get(fileName);
				// 在每天一个日志目录的方式下，检测日期是否变更了，如果变更了就要把变更后的日志文件拷贝到变更后的日期目录下。

				if (!newFileName.contains(nowDateString)
						&& beginFileData.getFileName().contains(DATA)) {
					newFileName = beginFileData.getFileName().replace(
							DATA, nowDateString);

					newFileName = (newFileName.contains(".") ? newFileName
							.substring(0, newFileName.lastIndexOf("."))
							: newFileName);

				}
				target = new File(newFileName + '_'
						+ getIndex(1));
				this.closeFile();
				file = new File(fileName);
				LogLog.debug("Renaming file " + file + " to " + target);

				renameSucceeded = file.renameTo(target);
				// 如果重命名失败，重新打开文件，并在文件上追加
				if (!renameSucceeded) {
					try {
						this.setFile(fileName, true, bufferedIO, bufferSize);
					} catch (IOException e) {
						LogLog.error("setFile(" + fileName
								+ ", true) call failed.", e);
					}
				}
			}
		}

		if (renameSucceeded) {

			try {
				this.setFile(fileName, false, bufferedIO, bufferSize);
				nextRollover = 0;
			} catch (IOException e) {
				LogLog.error("setFile(" + fileName + ", false) call failed.", e);
			}
		}
	}

	/**
	 * 获得文件后缀名
	 *
	 * @param i 当前文件序号
	 * @return 文件后缀
	 */
	private String getIndex(int i) {
		return "(" + i + ")" + ".log";
	}


	protected void subAppend(LoggingEvent event) {
		super.subAppend(event);
		if (fileName != null && qw != null) {
			String nowDate = sdf.format(new Date());
			// 检测日期是否已经变更了，如果变更了就要重创建文件
			if (!fileMaps.get(fileName).getDate().equals(nowDate)) {
				rollOver();
				return;
			}
			// 检测文件大小，超过指定大小重新创建文件.
			// 因为父类的nextRollover是private字段，所以要自行判断
			long size = ((CountingQuietWriter) qw).getCount();
			if (size >= maxFileSize && size >= nextRollover) {
				rollOver();
			}
		}
	}

	@Override
	public synchronized void setFile(String fileName, boolean append,
	                                 boolean bufferedIO, int bufferSize) throws IOException {

		String nowDate = sdf.format(new Date());

		// 第一次进入需要修改为当前日期
		if (fileName.contains(DATA)) {
			String beginFileName = fileName;
			fileName = fileName.replace(DATA, nowDate);
			fileMaps.put(fileName, new BeginFileData(beginFileName, nowDate));
		}
		BeginFileData beginFileData = fileMaps.get(fileName);

		// 检测日期是否已经变更了，如果变更了就要把原始的字符串给fileName变量，把变更后的日期做为开始日期
		if (!beginFileData.getDate().equals(nowDate)) {
			beginFileData.setDate(nowDate);
			fileName = beginFileData.getFileName().replace(DATA, nowDate);
			fileMaps.put(fileName, beginFileData);
		}
		super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
	}

	/**
	 * 存放文件名和对应日期
	 */
	class BeginFileData {

		public BeginFileData(String fileName, String date) {
			super();
			this.fileName = fileName;
			this.date = date;
		}

		private String fileName;
		private String date;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}
}