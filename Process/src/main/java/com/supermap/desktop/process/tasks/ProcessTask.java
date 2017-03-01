package com.supermap.desktop.process.tasks;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.Interface.IAfterWork;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class ProcessTask extends JPanel implements IProcessTask, IContentModel {
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

	public ProcessTask(IProcess process) {
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
	public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {
		//do nothing
	}

	@Override
	public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {
		//do nothing
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
		} else if (process.getKey().equals(MetaKeys.SPATIAL_INDEX)) {
			labelTitle.setText(ControlsProperties.getString("String_SpatialIndexProgress"));
		} else if (process.getKey().equals(MetaKeys.BUFFER)) {
			labelTitle.setText(ControlsProperties.getString("String_BufferProgress"));
		} else if (process.getKey().equals(MetaKeys.HEAT_MAP)) {
			labelTitle.setText(ControlsProperties.getString("String_HeatMap"));
		} else if (process.getKey().equals(MetaKeys.KERNEL_DENSITY)) {
			labelTitle.setText(ControlsProperties.getString("String_KernelDensity"));
		} else if (process.getKey().equals(MetaKeys.OVERLAY_ANALYST)) {
			OverlayAnalystType analystType = ((MetaProcessOverlayAnalyst) process).getAnalystType();
			switch (analystType) {
				case CLIP:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_CLIP"));
					break;
				case ERASE:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_ERASE"));
					break;
				case IDENTITY:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_IDENTITY"));
					break;
				case INTERSECT:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_INTERSECT"));
					break;
				case UNION:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_UNION"));
					break;
				case XOR:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_XOR"));
					break;
				case UPDATE:
					labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_UPDATE"));
					break;
				default:
					break;
			}
		} else if (process.getKey().equals(MetaKeys.INTERPOLATOR)) {
			InterpolationAlgorithmType type = ((MetaProcessInterpolator) process).getInterpolationAlgorithmType();
			if (type.equals(InterpolationAlgorithmType.IDW)) {
				labelTitle.setText(ControlsProperties.getString("String_Interpolator_IDW"));
			} else if (type.equals(InterpolationAlgorithmType.RBF)) {
				labelTitle.setText(ControlsProperties.getString("String_Interpolator_RBF"));
			} else if (type.equals(InterpolationAlgorithmType.KRIGING)) {
				labelTitle.setText(ControlsProperties.getString("String_Interpolator_KRIGING"));
			} else if (type.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
				labelTitle.setText(ControlsProperties.getString("String_Interpolator_SimpleKRIGING"));
			} else if (type.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
				labelTitle.setText(ControlsProperties.getString("String_Interpolator_UniversalKRIGING"));
			}
		} else {
			labelTitle.setText(process.getTitle());
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
