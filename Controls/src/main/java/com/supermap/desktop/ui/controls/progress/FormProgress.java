package com.supermap.desktop.ui.controls.progress;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class FormProgress extends SmDialog implements IUpdateProgress {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFUALT_PROGRESSBAR_HEIGHT = 30;

	private transient SwingWorker<Boolean, Object> worker = null;
	private String message = "";
	private String remainTime = "";
	private int percent = 0;
	private boolean isCancel = false;

	private JProgressBar progressBar = null;
	private JLabel labelMessage = null;
	private JLabel labelRemaintime = null;
	private SmButton buttonCancel = null;

	public FormProgress() {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setSize(450, 160);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		labelMessage = new JLabel("...");
		labelRemaintime = new JLabel("...");
		buttonCancel = new SmButton(CommonProperties.getString(CommonProperties.Cancel));
		this.getRootPane().setDefaultButton(this.buttonCancel);
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(groupLayout);

		// @formatter:off
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.labelMessage, 420, 420, 420)
				.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, 420)
				.addComponent(this.labelRemaintime, 420, 420, 420)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap(10, Short.MAX_VALUE)
						.addComponent(this.buttonCancel)));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.labelMessage)
				.addComponent(this.progressBar, DEFUALT_PROGRESSBAR_HEIGHT, DEFUALT_PROGRESSBAR_HEIGHT, DEFUALT_PROGRESSBAR_HEIGHT)
				.addComponent(this.labelRemaintime)
				.addComponent(this.buttonCancel, DEFUALT_PROGRESSBAR_HEIGHT, DEFUALT_PROGRESSBAR_HEIGHT, DEFUALT_PROGRESSBAR_HEIGHT));
		// @formatter:on

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});

		addWindowListener(new WindowAdapter() {
			/**
			 * Invoked when a window is in the process of being closed. The close operation can be overridden at this point.
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				cancel();
			}
		});

		setLocationRelativeTo(null);
	}

	public FormProgress(String title) {
		this();
		setTitle(title);
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
					if (null != this.get()) {

						Boolean result = this.get();
						if (result) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									setVisible(false);
								}
							});
						}
					}
				} catch (InterruptedException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (ExecutionException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(e);
				} finally {
					isCancel = false;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
							buttonCancel.setEnabled(true);
							setVisible(false);
						}
					});
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
				try {
					if (null != this.get()) {

						Boolean result = this.get();
						if (result) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									setVisible(false);
								}
							});
						}
						afterWork.afterWork(result);
					}
				} catch (InterruptedException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (ExecutionException e) {
					Application.getActiveApplication().getOutput().output(e);
				} catch (Exception e) {
					Application.getActiveApplication().getOutput().output(e);
				} finally {
					isCancel = false;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
							buttonCancel.setEnabled(true);
							setVisible(false);
						}
					});
				}
			}
		};

		this.worker.execute();
		if (null != this) {
			this.setVisible(true);
		}
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelMessage.setText(message);
			}
		});
	}

	public String getRemainTime() {
		return this.remainTime;
	}

	public void setRemainTime(final String remainTime) {
		this.remainTime = remainTime;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelRemaintime.setText(remainTime);
			}
		});
	}

	public int getPercent() {
		return this.percent;
	}

	public void setPercent(final int percent) {
		this.percent = percent;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
			}
		});
	}

	@Override
	public boolean isCancel() {
		return this.isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;

		if (this.isCancel) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					buttonCancel.setText(CommonProperties.getString(CommonProperties.BeingCanceled));
					buttonCancel.setEnabled(false);
				}
			});
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					buttonCancel.setText(CommonProperties.getString(CommonProperties.Cancel));
					buttonCancel.setEnabled(true);
				}
			});
		}
	}

	private void cancel() {
		setCancel(true);
	}

	@Override
	public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
		if (this.isCancel) {
			throw new CancellationException();
		}

		this.percent = percent;
		this.remainTime = remainTime;
		this.message = message;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(percent);
				labelRemaintime.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
				labelMessage.setText(message);
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

}
