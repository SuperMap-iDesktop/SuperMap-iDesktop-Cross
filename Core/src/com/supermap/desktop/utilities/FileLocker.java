package com.supermap.desktop.utilities;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * @author XiaJT
 */
public class FileLocker {

	private File lockFile;
	private RandomAccessFile randomAccessFile;
	private FileLock fileLock;

	public FileLocker(File lockFile) {
		this.lockFile = lockFile;
	}

	public boolean tryLock() {
		if (lockFile.exists() && randomAccessFile == null) {
			try {
				randomAccessFile = new RandomAccessFile(lockFile, "rw");
				fileLock = randomAccessFile.getChannel().tryLock();

			} catch (Exception e) {
				if (randomAccessFile != null) {
					try {
						randomAccessFile.close();
					} catch (IOException e1) {
						// ignore 尽力了
					}
				}
				return false;
			}
		}
		return fileLock != null && fileLock.isValid();
	}

	public void release() {
		try {
			fileLock.release();
			randomAccessFile.close();
		} catch (IOException e) {
			LogUtilities.outPut("FileLocker Release Failed");
			// 释放失败怎么办！
		}
	}

	public RandomAccessFile getRandomAccessFile() {
		return randomAccessFile;
	}
}
