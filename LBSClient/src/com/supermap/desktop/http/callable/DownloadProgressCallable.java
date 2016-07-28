package com.supermap.desktop.http.callable;

import java.util.concurrent.CancellationException;

import com.supermap.Interface.ITask;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.http.download.DownloadUtils;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.ManagerXMLParser;
import com.supermap.desktop.utilities.StringUtilities;

public class DownloadProgressCallable extends UpdateProgressCallable {

	private Boolean isSucceed;
	FileInfo downloadInfo;
	private boolean isNew;

	public DownloadProgressCallable(FileInfo downloadInfo,boolean isNew) {
		this.downloadInfo = downloadInfo;
		this.isNew = isNew;
	}

	private FileSteppedListener steppedListener = new FileSteppedListener() {

		@Override
		public void stepped(FileEvent event) {
			try {
				ITask fileManager = (ITask) getUpdate();
				if (event != null && event.getDownloadInfo() != null && event.getDownloadInfo().equals(fileManager.getFileInfo())) {
					updateProgress(event.getProgress(), String.valueOf(event.getRemainTime()), event.getDownloadInfo().getFileName());
				}
			} catch (CancellationException e) {
			}
		}
	};

	@Override
	public Boolean call() throws Exception {
		try {
			isSucceed = false;
			if (isNew) {
				DownloadUtils.addNewWindowListener(steppedListener);
				DownloadUtils.download(this.downloadInfo);
				String property = "URL=" + this.downloadInfo.toString();
				ManagerXMLParser.addTask(TaskEnum.DOWNLOADTASK, property);
			} else {
				DownloadUtils.addNewWindowListener(steppedListener);
				DownloadUtils.download(this.downloadInfo);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
		}
		return false;
	}

	public boolean isSucceed() {
		return isSucceed;
	}

	public void setSucceed(boolean isSucceed) {
		this.isSucceed = isSucceed;
	}

}
