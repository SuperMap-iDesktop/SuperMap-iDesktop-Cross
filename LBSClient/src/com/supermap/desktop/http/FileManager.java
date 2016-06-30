//package com.supermap.desktop.http;
//
//import java.awt.Color;
//import java.awt.event.*;
//import java.io.*;
//import java.text.MessageFormat;
//import java.util.concurrent.CancellationException;
//
//import javax.swing.*;
//import javax.swing.GroupLayout.Alignment;
//
//import com.supermap.desktop.Application;
//import com.supermap.desktop.Interface.IAfterWork;
//import com.supermap.desktop.controls.ControlsProperties;
//import com.supermap.desktop.dialog.SmOptionPane;
//import com.supermap.desktop.http.download.*;
//import com.supermap.desktop.lbsclient.LBSClientProperties;
//import com.supermap.desktop.progress.Interface.IUpdateProgress;
//import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
//import com.supermap.desktop.ui.controls.button.SmButton;
//import com.supermap.desktop.utilities.*;
//
//public class FileManager extends JPanel implements IUpdateProgress {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private static final int buttonWidth = 23;
//	private static final int buttonHeight = 23;
//	private transient SwingWorker<Boolean, Object> worker = null;
//
//	private JLabel labelTitle;//
//	private JLabel labelLogo;
//	private JProgressBar progressBar = null;
//	private SmButton buttonRun;
//	private SmButton buttonRemove;
//	private JLabel labelProcess;
//	private JLabel labelStatus;
//
//	private Boolean isCancel = false;
//	private int percent;
//	private String remainTime;
//	private String message;
//	private FileInfo fileInfo;
//	public static final int DOWNLOADTYPE = 0;
//	public static final int UPLOADTYPE = 1;
//	private int type;
//	private ActionListener buttonRunListener;
//	private ActionListener buttonRemoveListener;
//
//	public FileManager(FileInfo downloadInfo, int type) {
//		this.fileInfo = downloadInfo;
//		this.type = type;
//		initializeComponents();
//		initializeResources();
//		registEvents();
//	}
//
//	private void registEvents() {
//		this.buttonRunListener = new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				buttonRunClicked();
//			}
//		};
//		this.buttonRemoveListener = new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				buttonRemoveClicked();
//			}
//		};
//		removeEvents();
//		this.buttonRun.addActionListener(buttonRunListener);
//		this.buttonRemove.addActionListener(buttonRemoveListener);
//	}
//
//	private void removeEvents() {
//		this.buttonRun.removeActionListener(buttonRunListener);
//		this.buttonRemove.removeActionListener(buttonRemoveListener);
//	}
//
//	private void initializeComponents() {
//
//		labelTitle = new JLabel("file name");
//		labelLogo = new JLabel(this.getImageIcon("image_datasource.png"));
//		this.buttonRun = new SmButton(this.getImageIcon("Image_Stop.png"));
//		this.buttonRemove = new SmButton(this.getImageIcon("Image_Delete.png"));
//		labelProcess = new JLabel("-0%");
//
//		GroupLayout groupLayout = new GroupLayout(this);
//		groupLayout.setAutoCreateContainerGaps(true);
//		groupLayout.setAutoCreateGaps(true);
//		this.setLayout(groupLayout);
//
//		// @formatter:off
//		if (type==DOWNLOADTYPE) {
//			progressBar = new JProgressBar();
//			progressBar.setStringPainted(true);
//			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//					.addComponent(labelLogo, 32, 32, 32)
//					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//							.addGroup(groupLayout.createSequentialGroup()
//									.addComponent(this.labelTitle)
//									.addGap(0, 10, Short.MAX_VALUE))
//							.addComponent(this.progressBar)
//							.addGroup(groupLayout.createSequentialGroup()
//									.addComponent(this.labelProcess)
//									.addGap(0, 10, Short.MAX_VALUE)
//									.addComponent(this.labelStatus)))
//					.addGroup(groupLayout.createSequentialGroup()
//							.addComponent(this.buttonRun, buttonWidth, buttonWidth, buttonWidth)
//							.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth)));	
//			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//					.addComponent(labelLogo, 32, 32, 32)
//					.addGroup(groupLayout.createSequentialGroup()
//							.addComponent(this.labelTitle)
//							.addComponent(this.progressBar)
//							.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//									.addComponent(this.labelProcess)
//									.addComponent(this.labelStatus)))
//					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//							.addComponent(this.buttonRun, buttonHeight, buttonHeight, buttonHeight)
//							.addComponent(this.buttonRemove, buttonHeight, buttonHeight, buttonHeight)));
//			// @formatter:on
//			return;
//		}else{
//			
//			progressBar = new JProgressBar(1, 100); // 实例化进度条
//			progressBar.setStringPainted(true); // 描绘文字
//			progressBar.setBackground(Color.white); // 设置背景色
//			labelStatus = new JLabel("");
//			if (type==UPLOADTYPE) {
//				progressBar.setString(LBSClientProperties.getString("String_Uploading")); // 设置显示文字
//			}
//		
//			// @formatter:off
//			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
//					    .addComponent(labelLogo, 32, 32, 32)
//						.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//								.addGroup(groupLayout.createSequentialGroup()
//										.addComponent(this.labelTitle)
//										.addGap(0, 10, Short.MAX_VALUE))
//								.addComponent(this.progressBar)
//								.addGroup(groupLayout.createSequentialGroup()
//										.addComponent(this.labelProcess)
//										.addGap(0, 10, Short.MAX_VALUE)
//										.addComponent(this.labelStatus)))
//						.addGroup(groupLayout.createSequentialGroup()
//								.addComponent(this.buttonRemove, buttonWidth, buttonWidth, buttonWidth)));	
//			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//						.addComponent(labelLogo, 32, 32, 32)
//						.addGroup(groupLayout.createSequentialGroup()
//								.addComponent(this.labelTitle)
//								.addComponent(this.progressBar)
//								.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//										.addComponent(this.labelProcess)
//										.addComponent(this.labelStatus)))
//						.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
//								.addComponent(this.buttonRemove, buttonHeight, buttonHeight, buttonHeight)));
//					// @formatter:on
//			return;
//		}
//	}
//
//	private void initializeResources() {
//
//	}
//
//	private ImageIcon getImageIcon(String imagePath) {
//		ImageIcon imageIcon = null;
//		try {
//			String[] pathPrams = new String[] { PathUtilities.getRootPathName(), "../LBSClient/reosurces/", imagePath };
//			imagePath = PathUtilities.combinePath(pathPrams, false);
//			File imageFile = new File(imagePath);
//			if (imageFile.exists()) {
//				imageIcon = new ImageIcon(imagePath);
//			}
//		} catch (Exception exception) {
//			Application.getActiveApplication().getOutput().output(exception);
//		}
//
//		return imageIcon;
//	}
//
//	public void doWork(final UpdateProgressCallable doWork) {
//		doWork.setUpdate(this);
//
//		this.worker = new SwingWorker<Boolean, Object>() {
//
//			@Override
//			protected Boolean doInBackground() throws Exception {
//				return doWork.call();
//			}
//
//			@Override
//			protected void done() {
//				try {
//				} catch (Exception e) {
//					Application.getActiveApplication().getOutput().output(e);
//				} finally {
//				}
//			}
//		};
//
//		this.worker.execute();
//		if (null != this) {
//			this.setVisible(true);
//		}
//	}
//
//	public void doWork(final UpdateProgressCallable doWork, final IAfterWork<Boolean> afterWork) {
//		doWork.setUpdate(this);
//
//		this.worker = new SwingWorker<Boolean, Object>() {
//
//			@Override
//			protected Boolean doInBackground() throws Exception {
//				return doWork.call();
//			}
//
//			@Override
//			protected void done() {
//			}
//		};
//
//		this.worker.execute();
//		if (null != this) {
//			this.setVisible(true);
//		}
//	}
//
//	private void buttonRunClicked() {
//		this.setCancel(!this.isCancel);
//
//		if (this.isCancel) {
//			this.buttonRun.setIcon(this.getImageIcon("Image_Run.png"));
//		} else {
//			this.buttonRun.setIcon(this.getImageIcon("Image_Stop.png"));
//		}
//	}
//
//	private void buttonRemoveClicked() {
//		try {
//			if (type == DOWNLOADTYPE && !DownloadUtils.getBatchDownloadFileWorker(fileInfo).isFinished()) {
//				SmOptionPane optionPane = new SmOptionPane();
//				if (optionPane.showConfirmDialogWithCancle(MessageFormat.format(ControlsProperties.getString("String_DownLoadInfo"),
//						this.fileInfo.getFileName())) == JOptionPane.YES_OPTION) {
//					DownloadUtils.getBatchDownloadFileWorker(fileInfo).stopDownload();
//					removeDownloadInfoItem();
//
//				}
//				return;
//			}
//			if (type == DOWNLOADTYPE && DownloadUtils.getBatchDownloadFileWorker(fileInfo).isFinished()) {
//				removeDownloadInfoItem();
//				return;
//			}
//			if (type == UPLOADTYPE) {
//				CommonUtilities.removeItem(fileInfo);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void removeDownloadInfoItem() {
//		CommonUtilities.removeItem(fileInfo);
//		Application.getActiveApplication().getOutput()
//				.output(MessageFormat.format(ControlsProperties.getString("String_RemoveDownLoadMessionInfo"), this.fileInfo.getFileName()));
//	}
//
//	@Override
//	public boolean isCancel() {
//		return this.isCancel;
//	}
//
//	@Override
//	public void setCancel(boolean isCancel) {
//		try {
//			if (this.isCancel != isCancel) {
//				if (isCancel) {
//					DownloadUtils.getBatchDownloadFileWorker(this.fileInfo).stopDownload();
//				} else {
//					BatchDownloadFile downLoadFile = new BatchDownloadFile(fileInfo);
//					DownloadUtils.getHashMap().remove(this.fileInfo);
//					DownloadUtils.getHashMap().put(this.fileInfo, downLoadFile);
//					downLoadFile.start();
//				}
//
//				this.isCancel = isCancel;
//
//				if (this.isCancel) {
//					SwingUtilities.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							labelStatus.setText("pausing");
//						}
//					});
//				} else {
//					SwingUtilities.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							labelStatus.setText("downling");
//						}
//					});
//				}
//			}
//		} catch (IOException e) {
//			Application.getActiveApplication().getOutput().output(e);
//		}
//	}
//
//	@Override
//	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
//		this.percent = percent;
//		this.remainTime = remainTime;
//		this.message = message;
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				if (type == DOWNLOADTYPE) {
//					labelTitle.setText(message);
//					progressBar.setValue(percent);
//					labelProcess.setText(""); // 10MB/100MB
//
//					if (isCancel) {
//						labelStatus.setText("download canceled.");
//					} else {
//						if (percent != 100) {
//							labelStatus.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
//						} else {
//							labelStatus.setText("download finished.");
//							buttonRun.setEnabled(false);
//						}
//					}
//				}
//				if (type == UPLOADTYPE) {
//					labelTitle.setText("");
//					progressBar.setValue(percent);
//					if (100==percent) {
//						progressBar.setString(LBSClientProperties.getString("String_UploadEnd"));
//					}
//					labelProcess.setText(""); // 10MB/100MB
//				}
//
//			}
//		});
//	}
//
//	@Override
//	public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {
//		// 默认实现，后续进行初始化操作
//	}
//
//	@Override
//	public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {
//		// 默认实现，后续进行初始化操作
//	}
//
//	public FileInfo getDownloadInfo() {
//		return fileInfo;
//	}
//
//	public void setDownloadInfo(FileInfo downloadInfo) {
//		this.fileInfo = downloadInfo;
//	}
//
//}
