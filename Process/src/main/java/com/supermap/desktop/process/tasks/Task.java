package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.tasks.callable.UpdateProgressCallable;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class Task extends JPanel implements ITask, IContentModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFUALT_PROGRESSBAR_HEIGHT = 30;

	private transient SwingWorker<Boolean, Object> worker;
	private String message = "";
	private String remainTime = "";
	private int percent = 0;
	private volatile boolean isCancel;

	private JProgressBar progressBar;
	private JLabel labelTitle;
	private JLabel labelMessage;
	private JLabel labelRemaintime;
	//    private JButton buttonCancel = null;
	private IProcess process;
	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cancel();
		}
	};

	public Task(IProcess process) {
		this.process = process;
		init();
	}

	private void init() {
		initComponents();
		initLayout();
		registEvents();
		initResouces();
		this.labelRemaintime.setVisible(false);
	}

	public void doWork(final UpdateProgressCallable doWork) {
		try {
			doWork.setUpdate(this);
			doWork.call();
		} catch (Exception e) {
			e.printStackTrace();
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
					System.out.println(e);
				} catch (ExecutionException e) {
					System.out.println(e);
				} catch (Exception e) {
					System.out.println(e);
				} finally {
					isCancel = false;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
//                            buttonCancel.setText("取消");
//                            buttonCancel.setEnabled(true);
							setVisible(false);
							removeEvents();
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
	public IProcess getProcess() {
		return process;
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

//                    buttonCancel.setText("正在取消...");
//                    buttonCancel.setEnabled(false);
				}
			});
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
//                    buttonCancel.setText("取消");
//                    buttonCancel.setEnabled(true);
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
				labelRemaintime.setText(MessageFormat.format("剩余时间:", remainTime));
				labelMessage.setText(message);
			}
		});
	}

	@Override
	public void initComponents() {
		labelTitle = new JLabel();
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		labelMessage = new JLabel("...");
		labelRemaintime = new JLabel("...");
//        buttonCancel = new JButton("取消");
//        this.getRootPane().setDefaultButton(this.buttonCancel);
	}

	@Override
	public void initLayout() {
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.labelTitle)
								.addGap(0, 10, Short.MAX_VALUE))
						.addComponent(this.progressBar)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(this.labelMessage)
								.addGap(0, 10, Short.MAX_VALUE)
								.addComponent(this.labelRemaintime))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.CENTER)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(this.labelTitle)
						.addComponent(this.progressBar)
						.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
								.addComponent(this.labelMessage)
								.addComponent(this.labelRemaintime))));
		this.setLayout(groupLayout);
	}

	@Override
	public void initResouces() {
		if (process.getKey().equals(MetaKeys.IMPORT)) {
			labelTitle.setText(ControlsProperties.getString("String_ImportProgress"));
		} else if (process.getKey().equals(MetaKeys.PROJECTION)) {
			labelTitle.setText(ControlsProperties.getString("String_ProjectionProgress"));
		} else if (process.getKey().equals(MetaKeys.SPATIALINDEX)) {
			labelTitle.setText(ControlsProperties.getString("String_SpatialIndexProgress"));
		} else if (process.getKey().equals(MetaKeys.BUFFER)) {
			labelTitle.setText(ControlsProperties.getString("String_BufferProgress"));
		}
	}

	@Override
	public void registEvents() {
		removeEvents();
//        this.buttonCancel.addActionListener(this.cancelListener);
	}

	@Override
	public void removeEvents() {
//        this.buttonCancel.removeActionListener(this.cancelListener);
	}
}
