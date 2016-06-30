package com.supermap.desktop.task;

import java.awt.Color;
import java.awt.event.*;
import java.util.concurrent.CancellationException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CommonUtilities;

@SuppressWarnings("serial")
public class Task extends JPanel implements ITask {

	JProgressBar progressBar = null;
	private SmButton buttonRemove;
	private SwingWorker<Boolean, Object> worker;
	private int buttonWidth = 23;
	private int buttonHeight = 23;
	private ActionListener buttonRemoveListener;

	public Task() {
		initializeComponents();
		initializeResources();
		registEvents();
	}

	@Override
	public boolean isCancel() {
		return false;
	}

	@Override
	public void setCancel(boolean isCancel) {

	}

	@Override
	public void updateProgress(final int percent, String remainTime, String message) throws CancellationException {
		
	}

	@Override
	public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {

	}

	@Override
	public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {

	}

	@Override
	public void initializeComponents() {
		this.buttonRemove = new SmButton(CommonUtilities.getImageIcon("Image_Delete.png"));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		progressBar = new JProgressBar(1, 100); // 实例化进度条
		progressBar.setStringPainted(true); // 描绘文字
		progressBar.setBackground(Color.white); // 设置背景色
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addComponent(progressBar)
				.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER).addComponent(progressBar)
				.addComponent(this.buttonRemove, buttonHeight , buttonHeight, buttonHeight));
		// @formatter:on
		return;
	}

	@Override
	public void initializeResources() {
		
	}

	@Override
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

	protected void buttonRemoveClicked() {
		CommonUtilities.removeItem(this);
	}

	@Override
	public void removeEvents() {

	}

	@Override
	public FileInfo getDownloadInfo() {
		return null;
	}

	@Override
	public void setDownloadInfo(FileInfo downloadInfo) {

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

}
