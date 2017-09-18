package com.supermap.desktop.http.callable;

import com.supermap.desktop.lbs.FileInfo;

import java.util.EventObject;

public class FileEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	FileInfo downloadInfo;
	// 取百分比的整数值
	int progress;
	// 以秒为单位
	int remainTime;

	public FileEvent(Object source, FileInfo downloadInfo, int progress, int remainTime) {
		super(source);

		this.downloadInfo = downloadInfo;
		this.progress = progress;
		this.remainTime = remainTime;
	}

	public FileInfo getDownloadInfo() {
		return this.downloadInfo;
	}

	public int getProgress() {
		return this.progress;
	}

	public int getRemainTime() {
		return this.remainTime;
	}
}
