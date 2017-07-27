package com.supermap.desktop.http.callable;

import com.supermap.Interface.ILBSTask;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.DownloadUtils;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.lbs.FileInfo;
import com.supermap.desktop.utilities.ManagerXMLParser;

import java.util.concurrent.CancellationException;

public class DownloadProgressCallable extends UpdateProgressCallable {

	private Boolean isSucceed;
	FileInfo downloadInfo;
	// 是否为新任务
	private boolean isNew;

	public DownloadProgressCallable(FileInfo downloadInfo, boolean isNew) {
		this.downloadInfo = downloadInfo;
		this.isNew = isNew;
	}

	private FileSteppedListener steppedListener = new FileSteppedListener() {

		@Override
		public void stepped(FileEvent event) {
			try {
				ILBSTask fileManager = (ILBSTask) getUpdate();
				if (event != null && event.getDownloadInfo() != null && event.getDownloadInfo().equals(fileManager.getFileInfo())) {
					updateProgress(event.getProgress(), String.valueOf(event.getRemainTime()), event.getDownloadInfo().getRealName());
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
				// 是新任务，则将任务节点添加到xml文件中
				String property = "URL=" + this.downloadInfo.getUrl() + ",FileName=" + downloadInfo.getFileName() + ",RealName=" + downloadInfo.getRealName()
						+ ",FilePath=" + downloadInfo.getFilePath() + ",FileSize=" + downloadInfo.getFileSize();
				ManagerXMLParser.addTask(TaskEnum.DOWNLOADTASK, property);
			}
			DownloadUtils.addNewWindowListener(steppedListener);
			DownloadUtils.download(this.downloadInfo);
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
