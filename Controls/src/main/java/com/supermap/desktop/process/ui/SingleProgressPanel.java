package com.supermap.desktop.process.ui;

import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.process.tasks.IWorkerView;
import com.supermap.desktop.process.tasks.ProcessWorker;
import com.supermap.desktop.process.tasks.SingleProgress;
import com.supermap.desktop.ui.controls.progress.RoundProgressBar;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by highsad on 2017/6/22.
 */
public class SingleProgressPanel extends JPanel implements IWorkerView<SingleProgress> {
	private static final int WIDTH = 23;
	private static final Color DEFAULT_BACKGROUNDCOLOR = new Color(215, 215, 215);
	private static final Color DEFAULT_FOREGROUNDCOLOR = new Color(39, 162, 223);
	private static final Color CACEL_FOREGROUNDCOLOR = new Color(190, 190, 190);

	private ProcessWorker worker;
	private RoundProgressBar progressBar;
	private JLabel labelTitle;
	private JLabel labelMessage;
	private JLabel labelRemaintime;
	private ButtonExecutor buttonRun;

	private Runnable run = new Runnable() {
		@Override
		public void run() {
			if (worker != null) {
				worker.execute();
			}
		}
	};

	private Runnable cancel = new Runnable() {
		@Override
		public void run() {
			cancel();
		}
	};

	public SingleProgressPanel(String title) {
		if (StringUtilities.isNullOrEmpty(title)) {
			throw new NullPointerException("worker can not be null.");
		}

		initializeComponents();
		initializeLayout();
		this.labelTitle.setText(title);
	}

	public void setWorker(ProcessWorker worker) {
		if (worker == null) {
			throw new NullPointerException("worker can not be null.");
		}

		this.worker = worker;
		this.worker.setView(this);
	}

	private void initializeComponents() {
		labelTitle = new JLabel();
		progressBar = new RoundProgressBar();
		progressBar.setBackgroundColor(DEFAULT_BACKGROUNDCOLOR);
		progressBar.setForegroundColor(DEFAULT_FOREGROUNDCOLOR);
		progressBar.setDigitalColor(labelTitle.getBackground());
		progressBar.setDrawString(true);
		labelMessage = new JLabel("...");
		labelRemaintime = new JLabel("...");
		this.buttonRun = new ButtonExecutor(this.run, this.cancel);
		ComponentUIUtilities.setName(labelTitle, "ProcessTask_labelTitle");
		ComponentUIUtilities.setName(progressBar, "ProcessTask_progressBar");
		ComponentUIUtilities.setName(labelMessage, "ProcessTask_labelMessage");
		ComponentUIUtilities.setName(labelRemaintime, "ProcessTask_labelRemaintime");
		ComponentUIUtilities.setName(buttonRun, "ProcessTask_buttonRun");
	}

	public void initializeLayout() {
		Dimension dimension = new Dimension(18, 18);
		this.buttonRun.setPreferredSize(dimension);
		this.buttonRun.setMinimumSize(dimension);
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(this.labelTitle)
				.addGroup(layout.createSequentialGroup()
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(buttonRun, WIDTH, WIDTH, WIDTH)
				)
				.addGroup(layout.createSequentialGroup()
						.addComponent(labelMessage)
						.addComponent(labelRemaintime)
				)
		);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(this.labelTitle, WIDTH, WIDTH, WIDTH)
				.addGroup(layout.createParallelGroup()
						.addComponent(progressBar, WIDTH, WIDTH, WIDTH)
						.addComponent(buttonRun, WIDTH, WIDTH, WIDTH)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(labelMessage, WIDTH, WIDTH, WIDTH)
						.addComponent(labelRemaintime, WIDTH, WIDTH, WIDTH)
				)
		);
		this.setLayout(layout);
	}

	public void setTitleVisible(boolean isVisible) {
		this.labelTitle.setVisible(isVisible);
	}

	public void cancel() {
		this.worker.cancel();
	}

	public void reset() {
		this.progressBar.setProgress(0);
		this.labelMessage.setText("");
		this.labelRemaintime.setText("");
		this.buttonRun.setProcedure(ButtonExecutor.READY);
	}

	@Override
	public void update(SingleProgress chunk) {
		this.progressBar.setProgress(chunk.getPercent());
		this.labelMessage.setText(chunk.getMessage());
		this.labelRemaintime.setText(chunk.getRemainTime());
	}

	@Override
	public void done() {
		this.buttonRun.setProcedure(ButtonExecutor.READY);
	}
}
