package com.supermap.desktop.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.http.upload.BatchUploadFile;
import com.supermap.desktop.http.upload.UploadUtils;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.utilities.CommonUtilities;

public class UploadTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionListener buttonNowRunListener;
	private ActionListener buttonNowRemoveListener;

	public UploadTask(FileInfo uploadInfo) {
		super(uploadInfo);
		labelTitle.setText(LBSClientProperties.getString("String_Uploading"));
		labelLogo.setIcon(CommonUtilities.getImageIcon("image_upload.png"));
		labelLogo.setToolTipText(LBSClientProperties.getString("String_Upload"));
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
		if (!UploadUtils.getBatchUploadFileWorker(this.fileInfo).isFinished()) {
			SmOptionPane optionPane = new SmOptionPane();
			if (optionPane.showConfirmDialogWithCancle(MessageFormat.format(LBSClientProperties.getString("String_UpLoadInfo"), this.fileInfo.getFileName())) == JOptionPane.YES_OPTION) {
				UploadUtils.getBatchUploadFileWorker(fileInfo).stopUpload();
				removeUploadInfoItem();

			}
			return;
		}
		if (UploadUtils.getBatchUploadFileWorker(this.fileInfo).isFinished()) {
			removeUploadInfoItem();
			return;
		}

	}

	private void removeUploadInfoItem() {
		CommonUtilities.removeItem(this);
		UploadUtils.getHashMap().remove(this.fileInfo);
		Application.getActiveApplication().getOutput()
				.output(MessageFormat.format(LBSClientProperties.getString("String_RemoveUpLoadMessionInfo"), this.fileInfo.getFileName()));
	}

	@Override
	public void setCancel(boolean isCancel) {
		try {
			if (this.isCancel != isCancel) {
				if (isCancel) {
					UploadUtils.getBatchUploadFileWorker(this.fileInfo).stopUpload();
				} else {
					BatchUploadFile uploadFile = new BatchUploadFile(fileInfo);
					UploadUtils.getHashMap().remove(this.fileInfo);
					UploadUtils.getHashMap().put(this.fileInfo, uploadFile);
					uploadFile.start();
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
							labelStatus.setText("uploading");
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
					labelStatus.setText("upload canceled.");
				} else {
					if (percent != 100) {
						labelStatus.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
					} else {
						progressBar.setVisible(false);
						labelStatus.setText(LBSClientProperties.getString("String_UploadEnd"));
						// 刷新大数据展示列表
						CommonUtilities.getActiveLBSControl().refresh();
						buttonRun.setEnabled(false);
					}
				}
			}
		});
	}

}