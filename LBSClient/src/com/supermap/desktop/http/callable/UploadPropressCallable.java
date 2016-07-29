package com.supermap.desktop.http.callable;

import java.util.concurrent.CancellationException;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.http.upload.UploadUtils;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public class UploadPropressCallable extends UpdateProgressCallable{

	private Boolean isSucceed;
	private FileInfo fileInfo;
	
	public UploadPropressCallable(FileInfo downloadInfo) {
		this.fileInfo = downloadInfo;
	}
	
	private FileSteppedListener steppedListener = new FileSteppedListener() {

		@Override
		public void stepped(FileEvent event) {
			try {
				ITask fileManager = (ITask)getUpdate();
				if (event != null 
						&& event.getDownloadInfo() != null
						&& event.getDownloadInfo().equals(fileManager.getFileInfo())) {
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
