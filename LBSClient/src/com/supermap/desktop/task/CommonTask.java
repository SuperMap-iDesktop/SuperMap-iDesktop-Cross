package com.supermap.desktop.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CommonUtilities;

public class CommonTask extends JPanel implements ITask {
	private static final long serialVersionUID = 1L;
	private static final int buttonWidth = 23;
	private static final int buttonHeight = 23;
	private transient SwingWorker<Boolean, Object> worker = null;

	JLabel labelTitle;//
	JLabel labelLogo;
	JProgressBar progressBar = null;
	protected SmButton buttonRemove;
	JLabel labelStatus;
	GroupLayout groupLayout = new GroupLayout(this);
	protected Boolean isCancel = false;
	private int percent;
	private String remainTime;
	private String message;
	protected FileInfo fileInfo;
	private ActionListener buttonRunListener;
	private ActionListener buttonRemoveListener;

	public CommonTask() {
		// this.fileInfo = downloadInfo;
		initializeComponents();
		initializeResources();
		registEvents();
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

	@Override
	public void removeEvents() {
		this.buttonRemove.removeActionListener(buttonRemoveListener);
	}

	@Override
	public void initializeComponents() {

		labelTitle = new JLabel("Info");
		labelLogo = new JLabel();
		this.buttonRemove = new SmButton(CommonUtilities.getImageIcon("Image_Delete.png"));
		labelStatus = new JLabel("Remain time:0");
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addComponent(labelLogo)
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.labelTitle)
								.addGap(0, 10, Short.MAX_VALUE))
						.addComponent(this.progressBar)
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(0, 10, Short.MAX_VALUE)
								.addComponent(this.labelStatus)))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth)));	
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(labelLogo)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.labelTitle)
						.addComponent(this.progressBar)
						.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
								.addComponent(this.labelStatus)))
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addComponent(this.buttonRemove, buttonHeight, buttonHeight, buttonHeight)));
		// @formatter:on
		this.setLayout(groupLayout);
		return;
	}

	@Override
	public void initializeResources() {

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
