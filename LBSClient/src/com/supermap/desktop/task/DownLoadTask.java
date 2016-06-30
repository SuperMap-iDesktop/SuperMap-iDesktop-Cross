package com.supermap.desktop.task;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import com.supermap.Interface.ITask;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.http.download.BatchDownloadFile;
import com.supermap.desktop.http.download.DownloadUtils;
import com.supermap.desktop.http.download.FileInfo;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.PathUtilities;

public class DownLoadTask extends JPanel implements ITask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int buttonWidth = 23;
	private static final int buttonHeight = 23;
	private transient SwingWorker<Boolean, Object> worker = null;

	private JLabel labelTitle;//
	private JLabel labelLogo;
	private JProgressBar progressBar = null;
	private SmButton buttonRun;
	private SmButton buttonRemove;
	private JLabel labelProcess;
	private JLabel labelStatus;

	private Boolean isCancel = false;
	private int percent;
	private String remainTime;
	private String message;
	private FileInfo fileInfo;
	private ActionListener buttonRunListener;
	private ActionListener buttonRemoveListener;

	public DownLoadTask(FileInfo downloadInfo) {
		this.fileInfo = downloadInfo;
		initializeComponents();
		initializeResources();
		registEvents();
	}

	@Override
	public void registEvents() {
		this.buttonRunListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRunClicked();
			}
		};
		this.buttonRemoveListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRemoveClicked();
			}
		};
		removeEvents();
		this.buttonRun.addActionListener(buttonRunListener);
		this.buttonRemove.addActionListener(buttonRemoveListener);
	}

	@Override
	public void removeEvents() {
		this.buttonRun.removeActionListener(buttonRunListener);
		this.buttonRemove.removeActionListener(buttonRemoveListener);
	}

	@Override
	public void initializeComponents() {

		labelTitle = new JLabel("file name");
		labelLogo = new JLabel(this.getImageIcon("image_datasource.png"));
		this.buttonRun = new SmButton(this.getImageIcon("Image_Stop.png"));
		this.buttonRemove = new SmButton(this.getImageIcon("Image_Delete.png"));
		labelProcess = new JLabel("-0%");
		labelStatus = new JLabel("Remain time:0");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

		// @formatter:off
			progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
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
		return;
	}

	@Override
	public void initializeResources() {

	}

	private ImageIcon getImageIcon(String imagePath) {
		ImageIcon imageIcon = null;
		try {
			String[] pathPrams = new String[] { PathUtilities.getRootPathName(), "../LBSClient/reosurces/", imagePath };
			imagePath = PathUtilities.combinePath(pathPrams, false);
			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imagePath);
			}
		} catch (Exception exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}

		return imageIcon;
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

	private void buttonRunClicked() {
		this.setCancel(!this.isCancel);

		if (this.isCancel) {
			this.buttonRun.setIcon(this.getImageIcon("Image_Run.png"));
		} else {
			this.buttonRun.setIcon(this.getImageIcon("Image_Stop.png"));
		}
	}

	private void buttonRemoveClicked() {
		try {
			if (!DownloadUtils.getBatchDownloadFileWorker(fileInfo).isFinished()) {
				SmOptionPane optionPane = new SmOptionPane();
				if (optionPane.showConfirmDialogWithCancle(MessageFormat.format(ControlsProperties.getString("String_DownLoadInfo"),
						this.fileInfo.getFileName())) == JOptionPane.YES_OPTION) {
					DownloadUtils.getBatchDownloadFileWorker(fileInfo).stopDownload();
					removeDownloadInfoItem();

				}
				return;
			}
			if (DownloadUtils.getBatchDownloadFileWorker(fileInfo).isFinished()) {
				removeDownloadInfoItem();
				return;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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
		this.percent = percent;
		this.remainTime = remainTime;
		this.message = message;
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
						labelStatus.setText("download finished.");
						buttonRun.setEnabled(false);
					}
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
