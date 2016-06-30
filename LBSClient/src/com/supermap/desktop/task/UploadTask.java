package com.supermap.desktop.task;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.http.download.*;
import com.supermap.desktop.lbsclient.LBSClientProperties;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.*;

public class UploadTask extends JPanel implements ITask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int buttonWidth = 23;
	private static final int buttonHeight = 23;
	private transient SwingWorker<Boolean, Object> worker = null;

	// private JLabel labelTitle;
	// private JLabel labelLogo;
	private JProgressBar progressBar = null;
//	private SmButton buttonRun;
	private SmButton buttonRemove;
	// private JLabel labelProcess;
	// private JLabel labelStatus;

	private Boolean isCancel = false;
	private int percent;
	private String remainTime;
	private String message;
	private FileInfo fileInfo;
	private ActionListener buttonRunListener;
	private ActionListener buttonRemoveListener;

	public UploadTask(FileInfo downloadInfo) {
		this.fileInfo = downloadInfo;
		initializeComponents();
		initializeResources();
		registEvents();
	}

	public void registEvents() {
		this.buttonRemoveListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRemoveClicked();
			}
		};
		removeEvents();
//		this.buttonRun.addActionListener(buttonRunListener);
		this.buttonRemove.addActionListener(buttonRemoveListener);
	}

	public void removeEvents() {
//		this.buttonRun.removeActionListener(buttonRunListener);
		this.buttonRemove.removeActionListener(buttonRemoveListener);
	}

	public void initializeComponents() {

		// labelTitle = new JLabel("file name");
		// labelLogo = new JLabel(this.getImageIcon("image_datasource.png"));
		// this.buttonRun = new SmButton(this.getImageIcon("Image_Stop.png"));
		this.buttonRemove = new SmButton(CommonUtilities.getImageIcon("Image_Delete.png"));
		// labelProcess = new JLabel("-0%");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		progressBar = new JProgressBar(1, 100); // 实例化进度条
		progressBar.setStringPainted(true); // 描绘文字
		progressBar.setString(LBSClientProperties.getString("String_Uploading")); // 设置显示文字
		progressBar.setBackground(Color.white); // 设置背景色
		// labelStatus = new JLabel("");
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
					.addComponent(progressBar)
					.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth));	
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(progressBar)
					.addComponent(this.buttonRemove, buttonHeight, buttonHeight, buttonHeight));
		// @formatter:on
		return;
	}

	public void initializeResources() {

	}


	public void doWork(final UpdateProgressCallable doWork) {
		doWork.setUpdate(this);

		this.worker = new SwingWorker<Boolean, Object>() {

			@Override
			protected Boolean doInBackground() throws Exception {
				return doWork.call();
			}

			@Override
			protected void done() {
				try {
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(e);
				} finally {
				}
			}
		};

		this.worker.execute();
		if (null != this) {
			this.setVisible(true);
		}
	}

	public void doWork(final UpdateProgressCallable doWork, final IAfterWork<Boolean> afterWork) {
		doWork.setUpdate(this);

		this.worker = new SwingWorker<Boolean, Object>() {

			@Override
			protected Boolean doInBackground() throws Exception {
				return doWork.call();
			}

			@Override
			protected void done() {
			}
		};

		this.worker.execute();
		if (null != this) {
			this.setVisible(true);
		}
	}

//	private void buttonRunClicked() {
//		this.setCancel(!this.isCancel);
//
//		if (this.isCancel) {
//			this.buttonRun.setIcon(this.getImageIcon("Image_Run.png"));
//		} else {
//			this.buttonRun.setIcon(this.getImageIcon("Image_Stop.png"));
//		}
//	}

	private void buttonRemoveClicked() {
		CommonUtilities.removeItem(this);
	}

	private void removeDownloadInfoItem() {
		CommonUtilities.removeItem(this);
		Application.getActiveApplication().getOutput()
				.output(MessageFormat.format(ControlsProperties.getString("String_RemoveDownLoadMessionInfo"), this.fileInfo.getFileName()));
	}

	@Override
	public boolean isCancel() {
		return this.isCancel;
	}

	@Override
	public void setCancel(boolean isCancel) {

	}

	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		this.percent = percent;
		this.remainTime = remainTime;
		this.message = message;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				if (100 == percent) {
					progressBar.setString(LBSClientProperties.getString("String_UploadEnd"));
				}

			}
		});
	}

	@Override
	public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {
		// 默认实现，后续进行初始化操作
	}

	@Override
	public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {
		// 默认实现，后续进行初始化操作
	}

	public FileInfo getDownloadInfo() {
		return fileInfo;
	}

	public void setDownloadInfo(FileInfo downloadInfo) {
		this.fileInfo = downloadInfo;
	}
}