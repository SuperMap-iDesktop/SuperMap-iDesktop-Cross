package com.supermap.desktop.task;

import java.awt.event.*;
import java.util.concurrent.CancellationException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.*;

public class Task extends JPanel implements ITask {
	private static final long serialVersionUID = 1L;
	private static final int buttonWidth = 23;
	private static final int buttonHeight = 23;
	private transient SwingWorker<Boolean, Object> worker = null;

	JLabel labelTitle;// 标题
	protected JLabel labelLogo;// 图标
	JProgressBar progressBar = null;
	SmButton buttonRun;// 赞成
	protected SmButton buttonRemove;// 删除
	JLabel labelProcess; // 进度信息
	JLabel labelStatus; // 状态
	GroupLayout groupLayout = new GroupLayout(this);
	protected Boolean isCancel = false;
	protected FileInfo fileInfo;
	private ActionListener buttonRemoveListener;

	public Task(FileInfo downloadInfo) {
		this.fileInfo = downloadInfo;
		initializeComponents();
		registEvents();
	}

	public Task() {
		this(null);
	}

	public void registEvents() {
		this.buttonRemoveListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRemoveClicked();
			}
		};
		removeEvents();
		this.buttonRemove.addActionListener(buttonRemoveListener);
	}

	@Override
	public void removeEvents() {
		this.buttonRemove.removeActionListener(buttonRemoveListener);
	}

	public void initializeComponents() {

		labelTitle = new JLabel("file name");
		labelLogo = new JLabel(CommonUtilities.getImageIcon("image_datasource.png"));
		this.buttonRun = new SmButton(CommonUtilities.getImageIcon("Image_Stop.png"));
		this.buttonRemove = new SmButton(CommonUtilities.getImageIcon("Image_Delete.png"));
		labelProcess = new JLabel("-0%");
		labelStatus = new JLabel("Remain time:0");
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		// @formatter:off
			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
					.addComponent(labelLogo, 32, 32, 32)
					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
							.addGroup(groupLayout.createSequentialGroup()
									.addComponent(this.labelTitle)
									.addGap(0, 10, Short.MAX_VALUE))
							.addComponent(this.progressBar)
							.addGroup(groupLayout.createSequentialGroup()
									.addComponent(this.labelProcess)
									.addGap(0, 10, Short.MAX_VALUE)
									.addComponent(this.labelStatus)))
					.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.buttonRun, buttonWidth, buttonWidth, buttonWidth)
							.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth)));	
			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
					.addComponent(labelLogo, 32, 32, 32)
					.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.labelTitle)
							.addComponent(this.progressBar)
							.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
									.addComponent(this.labelProcess)
									.addComponent(this.labelStatus)))
					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
							.addComponent(this.buttonRun, buttonHeight, buttonHeight, buttonHeight)
							.addComponent(this.buttonRemove, buttonHeight, buttonHeight, buttonHeight)));
		// @formatter:on
		this.setLayout(groupLayout);
		return;
	}

	@Override
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

	private void buttonRemoveClicked() {
		CommonUtilities.removeItem(this);
	}

	@Override
	public boolean isCancel() {
		return this.isCancel;
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

	@Override
	public void setCancel(boolean isCancel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProgress(int percent, String remainTime, String message) throws CancellationException {
		// TODO Auto-generated method stub

	}

}
