package com.supermap.desktop.http.callable;

import com.supermap.Interface.ILBSTask;
import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.ui.lbs.impl.FileInfo;
import com.supermap.desktop.http.upload.UploadUtils;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.utilities.ManagerXMLParser;

import java.util.concurrent.CancellationException;

public class UploadPropressCallable extends UpdateProgressCallable {

	private Boolean isSucceed;
	private FileInfo fileInfo;
	// 是否为新任务
	private boolean isNew;

	public UploadPropressCallable(FileInfo fileInfo, boolean isNew) {
		this.fileInfo = fileInfo;
		this.isNew = isNew;
	}

	private FileSteppedListener steppedListener = new FileSteppedListener() {

		@Override
		public void stepped(FileEvent event) {
			try {
				ILBSTask fileManager = (ILBSTask) getUpdate();
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
				// 是新任务，则将任务节点添加到xml文件中
				String property = "URL=" + this.fileInfo.getUrl() + ",FileName=" + this.fileInfo.getFileName() + ",FilePath=" + this.fileInfo.getFilePath()
						+ ",FileSize=" + this.fileInfo.getFileSize();
				ManagerXMLParser.addTask(TaskEnum.UPLOADTASK, property);
			}
			UploadUtils.addNewWindowListener(steppedListener);
			UploadUtils.upload(fileInfo);
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
