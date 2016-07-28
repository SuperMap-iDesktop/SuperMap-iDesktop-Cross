package com.supermap.desktop.http.callable;

import java.io.File;
import java.util.concurrent.CancellationException;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.http.upload.UploadUtils;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public class UploadPropressCallable extends UpdateProgressCallable{

	private Boolean isSucceed;
	private FileInfo fileInfo;
	private File file;
	
	public UploadPropressCallable(FileInfo downloadInfo,File file) {
		this.fileInfo = downloadInfo;
		this.file = file;
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
			UploadUtils.upload(fileInfo, file.getParentFile().getPath(), file.getName(), file.length(), true);
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
