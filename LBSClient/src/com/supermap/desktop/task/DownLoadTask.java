package com.supermap.desktop.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.supermap.Interface.TaskEnum;
import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.http.download.BatchDownloadFile;
import com.supermap.desktop.http.download.DownloadUtils;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.ManagerXMLParser;

public class DownLoadTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionListener buttonNowRemoveListener;
	private ActionListener buttonNowRunListener;

	public DownLoadTask(FileInfo downloadInfo) {
		super(downloadInfo);

	}

	public void registEvents() {
		this.buttonNowRunListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRunClicked();
			}
		};
		this.buttonNowRemoveListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					buttonRemoveClicked();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		removeEvents();
		this.buttonRun.addActionListener(buttonNowRunListener);
		this.buttonRemove.addActionListener(buttonNowRemoveListener);
	}

	private void buttonRunClicked() {
		this.setCancel(!this.isCancel);

		if (this.isCancel) {
			this.buttonRun.setIcon(CommonUtilities.getImageIcon("Image_Run.png"));
		} else {
			this.buttonRun.setIcon(CommonUtilities.getImageIcon("Image_Stop.png"));
		}
	}

	private void buttonRemoveClicked() throws IOException {
		setCancel(true);
		if (!DownloadUtils.getBatchDownloadFileWorker(this.fileInfo).isFinished()) {
			SmOptionPane optionPane = new SmOptionPane();
			if (optionPane.showConfirmDialogWithCancle(MessageFormat.format(LBSClientProperties.getString("String_DownLoadInfo"), this.fileInfo.getFileName())) == JOptionPane.YES_OPTION) {
				DownloadUtils.getBatchDownloadFileWorker(this.fileInfo).stopDownload();
				removeDownloadInfoItem();
				ManagerXMLParser.removeTask(TaskEnum.DOWNLOADTASK, this.fileInfo.getUrl());
			}
			return;
		}
		if (DownloadUtils.getBatchDownloadFileWorker(this.fileInfo).isFinished()) {
			removeDownloadInfoItem();
			ManagerXMLParser.removeTask(TaskEnum.DOWNLOADTASK, this.fileInfo.getUrl());
			return;
		}

	}

	private void removeDownloadInfoItem() {
		CommonUtilities.removeItem(this);
		DownloadUtils.getHashMap().remove(this.fileInfo);
		Application.getActiveApplication().getOutput()
				.output(MessageFormat.format(LBSClientProperties.getString("String_RemoveDownLoadMessionInfo"), this.fileInfo.getFileName()));
	}

	@Override
	public void setCancel(boolean isCancel) {
		try {
			if (this.isCancel != isCancel) {
				if (isCancel) {
					DownloadUtils.getBatchDownloadFileWorker(this.fileInfo).stopDownload();
				} else {
					BatchDownloadFile downLoadFile = new BatchDownloadFile(fileInfo);
					DownloadUtils.getHashMap().remove(this.fileInfo);
					DownloadUtils.getHashMap().put(this.fileInfo, downLoadFile);
					downLoadFile.start();
				}

				this.isCancel = isCancel;

				if (this.isCancel) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							labelStatus.setText("pausing");
						}
					});
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							labelStatus.setText("downling");
						}
					});
				}
			}
		} catch (IOException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}

	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTitle.setText(message);
				progressBar.setValue(percent);
				labelProcess.setText(""); // 10MB/100MB

				if (isCancel) {
					labelStatus.setText("download canceled.");
				} else {
					if (percent != 100) {
						labelStatus.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
					} else {
						progressBar.setVisible(false);
						labelStatus.setText(LBSClientProperties.getString("String_FileDownLoadFinished"));
						// 刷新大数据展示列表
						if (null != CommonUtilities.getActiveLBSControl()) {
							CommonUtilities.getActiveLBSControl().refresh();
						}
						buttonRun.setEnabled(false);
						ManagerXMLParser.removeTask(TaskEnum.DOWNLOADTASK, fileInfo.getUrl());
					}
				}

			}
		});
	}

	public TaskEnum getTaskType() {
		return TaskEnum.DOWNLOADTASK;
	}
}
