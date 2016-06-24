package com.supermap.desktop.http;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.Interface.IDockbar;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.button.SmButton;
import com.supermap.desktop.utilities.PathUtilities;

public class FileManager extends JPanel implements IUpdateProgress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int buttonWidth = 23;
	private static final int buttonHeight = 23;
	private transient SwingWorker<Boolean, Object> worker = null;

	private JLabel labelTitle;
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
	private DownloadInfo downloadInfo;
	private  final String FILE_MANAGER_CONTROL_CLASS = "com.supermap.desktop.http.FileManagerContainer";
	
	public FileManager(DownloadInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
		
		initializeComponents();
		initializeResources();

		this.buttonRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRunClicked();
			}
		});
		
		this.buttonRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonRemoveClicked();
			}
		});
	}

	private void initializeComponents() {
		
		labelTitle = new JLabel("file name");
		labelLogo = new JLabel(this.getImageIcon("image_datasource.png"));
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		this.buttonRun = new SmButton(this.getImageIcon("Image_Stop.png"));
		this.buttonRemove = new SmButton(this.getImageIcon("Image_Delete.png"));
		labelProcess = new JLabel("-0%");
		labelStatus = new JLabel("Remain time : 0");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.setLayout(groupLayout);

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
	}

	private void initializeResources() {
//		this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
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
//					if (null != this.get()) {
//
//						Boolean result = this.get();
//						if (result) {
//							SwingUtilities.invokeLater(new Runnable() {
//								@Override
//								public void run() {
//									setVisible(false);
//								}
//							});
//						}
//					}
				} /*catch (InterruptedException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (ExecutionException e) {
					Application.getActiveApplication().getOutput().output(e);
				} */catch (Exception e) {
					Application.getActiveApplication().getOutput().output(e);
				} finally {
//					isCancel = false;
//					SwingUtilities.invokeLater(new Runnable() {
//						@Override
//						public void run() {
//							labelStatus.setText("downling");
//							setVisible(false);
//						}
//					});
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
			if (!DownloadUtils.getBatchDownloadFileWorker(downloadInfo).isFinished()) {
				SmOptionPane optionPane = new SmOptionPane();
				if (optionPane.showConfirmDialogWithCancle(MessageFormat.format(ControlsProperties.getString("String_DownLoadInfo"), this.downloadInfo.getFileName()))==JOptionPane.YES_OPTION) {
					DownloadUtils.getBatchDownloadFileWorker(downloadInfo).stopDownload();
					try {
						IDockbar dockbarPropertyContainer = Application.getActiveApplication().getMainFrame().getDockbarManager()
								.get(Class.forName(FILE_MANAGER_CONTROL_CLASS));
						if (null!=dockbarPropertyContainer) {
							FileManagerContainer fileManagerContainer = (FileManagerContainer) dockbarPropertyContainer.getComponent();
							fileManagerContainer.removeItem(downloadInfo);
							Application.getActiveApplication().getOutput().output(MessageFormat.format(ControlsProperties.getString("String_RemoveDownLoadMessionInfo"), this.downloadInfo.getFileName()));
						}
					} catch (ClassNotFoundException e) {
						Application.getActiveApplication().getOutput().output(e);
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					DownloadUtils.getBatchDownloadFileWorker(this.downloadInfo).stopDownload();
				} else {
					BatchDownloadFile downLoadFile = new BatchDownloadFile(downloadInfo);
					DownloadUtils.getHashMap().remove(this.downloadInfo);
					DownloadUtils.getHashMap().put(this.downloadInfo, downLoadFile);
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
//		if (this.isCancel) {
//			throw new CancellationException();
//		}

		this.percent = percent;
		this.remainTime = remainTime;
		this.message = message;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTitle.setText(message);	
				progressBar.setValue(percent);	
				labelProcess.setText(""); // 10MB/100MB
				
//				BatchDownloadFile batchDownloadFile = DownloadUtils.getBatchDownloadFileWorker(downloadInfo);
//				try {
//					labelProcess.setText(batchDownloadFile.getDownloadInformation());
//				} catch (IOException e) {
//					Application.getActiveApplication().getOutput().output(e);
//				}
				
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

	public DownloadInfo getDownloadInfo() {
		return downloadInfo;
	}

	public void setDownloadInfo(DownloadInfo downloadInfo) {
		this.downloadInfo = downloadInfo;
	}

}
