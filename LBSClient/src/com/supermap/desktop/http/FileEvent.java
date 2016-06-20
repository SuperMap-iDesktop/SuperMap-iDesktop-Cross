package com.supermap.desktop.http;

import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.GregorianCalendar;

public class FileEvent extends EventObject {

	DownloadInfo downloadInfo;
	// 取百分比的整数值
	int progress;
	// 以秒为单位
	int remainTime; 
	
	public FileEvent(Object source, DownloadInfo downloadInfo, int progress, int remainTime) {
		super(source);

		this.downloadInfo = downloadInfo;
		this.progress = progress;
		this.remainTime = remainTime;
	}

	public DownloadInfo getDownloadInfo() {
		return this.downloadInfo;
	}
	
	public int getProgress() {
		return this.progress;
	}
	
	public String getRemainTime() {
		long hour = this.remainTime / (60 * 60);
		long min = this.remainTime / (60) - hour * 60;
		long secend = this.remainTime - hour * 60 * 60 - min * 60;

	    return String.format("%02d:%02d:%02d", hour, min, secend);
	}
}
