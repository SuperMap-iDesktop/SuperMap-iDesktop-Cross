package com.supermap.desktop.http;

import java.util.concurrent.CancellationException;

import com.supermap.desktop.Application;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

public class DownloadProgressCallable extends UpdateProgressCallable {

	private Boolean isSucceed;
	DownloadInfo downloadInfo;
	
	public DownloadProgressCallable(DownloadInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
	}
	
	private FileSteppedListener steppedListener = new FileSteppedListener() {

		@Override
		public void stepped(FileEvent event) {
			try {
				FileManager fileManager = (FileManager)getUpdate();
				if (event != null 
						&& event.getDownloadInfo() != null
						&& event.getDownloadInfo().equals(fileManager.getDownloadInfo())) {
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