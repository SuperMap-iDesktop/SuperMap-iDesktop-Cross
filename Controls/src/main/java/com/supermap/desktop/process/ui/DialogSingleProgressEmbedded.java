package com.supermap.desktop.process.ui;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.tasks.IWorkerView;
import com.supermap.desktop.process.tasks.SingleProgress;
import com.supermap.desktop.process.tasks.Worker;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.progress.RoundProgressBar;
import org.apache.commons.lang.NullArgumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * 内嵌功能面板的进度条窗口，进度条在窗口底部进行显示
 * Created by highsad on 2017/9/20.
 */
public class DialogSingleProgressEmbedded extends SmDialog implements IWorkerView<SingleProgress> {
	private RoundProgressBar progressBar;
	private JLabel labelMessage;
	private JLabel labelRemainTime;
	private ButtonExecutor buttonRun;
	private JComponent component;
	private volatile boolean isCancelledWhenClosing = false; // 是否因关闭窗口而导致操作取消
	private volatile boolean isAutoClosed = true; // 执行成功是否自动关闭窗口

	private Worker<SingleProgress> worker;

	public DialogSingleProgressEmbedded(JComponent component, Worker<SingleProgress> worker) {
		if (component == null) {
			throw new NullArgumentException("component");
		}

		if (worker == null) {
			throw new NullArgumentException("worker");
		}

		this.component = component;
		this.worker = worker;
		this.worker.setView(this);
		initializeComponents();
		setLocationRelativeTo(null);
	}

	private void initializeComponents() {
		setTitle(this.worker.getTitle());
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.progressBar = new RoundProgressBar();
		this.labelMessage = new JLabel();
		this.labelRemainTime = new JLabel();
		this.buttonRun = new ButtonExecutor(new RunHandler(), new CancelHandler());

		JScrollPane scrollPane = new JScrollPane(this.component);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		JPanel panelProgress = new JPanel();
		initializePanelProgress(panelProgress);

		int minWidth = 400;
		int minHeight = this.component.getPreferredSize().height > 500 ? 500 : this.component.getPreferredSize().height;

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(scrollPane, minHeight, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(panelProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
		this.setMinimumSize(new Dimension(minWidth, minHeight + 120));
		this.setPreferredSize(new Dimension(minWidth, minHeight + 120));
	}

	private void initializePanelProgress(JPanel panelProgress) {
		GroupLayout layout = new GroupLayout(panelProgress);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panelProgress.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(this.labelMessage)
								.addComponent(this.labelRemainTime)))
				.addComponent(this.buttonRun, 23, 23, 23));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(this.progressBar, 23, 23, 23)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.labelMessage)
								.addComponent(this.labelRemainTime)))
				.addComponent(this.buttonRun, 23, 23, 23));
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (worker.isRunning()) {
			isCancelledWhenClosing = true;
			labelMessage.setText(ControlsProperties.getString("String_Canceling"));
			labelRemainTime.setText("");
			buttonRun.setProcedure(ButtonExecutor.CANCELLED);
			worker.cancel();
		}
	}

	public boolean isAutoClosed() {
		return isAutoClosed;
	}

	public void setAutoClosed(boolean autoClosed) {
		isAutoClosed = autoClosed;
	}

	@Override
	public void update(SingleProgress chunk) {
		// 进入这个方法就表示已经开始运行，更新按钮状态
		this.buttonRun.setProcedure(ButtonExecutor.RUNNING);

		if (chunk.isIndeterminate()) {
			this.progressBar.updateProgressIndeterminate();
			this.labelMessage.setText(chunk.getMessage());
			this.labelRemainTime.setText("");
			progressBar.setDrawString(false);
		} else {
			this.progressBar.stopUpdateProgressIndeterminate();
			this.progressBar.setProgress(chunk.getPercent());
			this.labelMessage.setText(chunk.getMessage());
			this.labelRemainTime.setText(chunk.getRemainTime());
			progressBar.setDrawString(true);
		}
	}

	@Override
	public void done() {
		try {
			this.labelRemainTime.setText("");
			this.buttonRun.setProcedure(ButtonExecutor.READY);

			boolean result = this.worker.get();
			if (result) {
				if (isAutoClosed) {

					// 执行成功，关闭窗口，并重置
					setVisible(false);
					reset();
				} else {
					this.buttonRun.setProcedure(ButtonExecutor.READY);
					this.isCancelledWhenClosing = false;
					setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				}
			} else {
				if (this.worker.isCancelled() && isCancelledWhenClosing) {
					// 或者因关闭窗口导致取消而执行失败则关闭窗口，并重置
					setVisible(false);
					reset();
				} else if (this.worker.isCancelled()) {
					this.labelMessage.setText(ProcessProperties.getString("String_Cancelled"));
				}

				this.progressBar.setProgress(0);
				setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
		}
	}

	private void reset() {
		this.progressBar.setProgress(0);
		this.labelMessage.setText("");
		this.labelRemainTime.setText("");
		this.buttonRun.setProcedure(ButtonExecutor.READY);
		this.isCancelledWhenClosing = false;
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	private class RunHandler implements Runnable {

		@Override
		public void run() {

			// 开始执行则禁用按钮关闭的功能
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			worker.execute();
		}
	}

	private class CancelHandler implements Runnable {

		@Override
		public void run() {
			labelMessage.setText(ControlsProperties.getString("String_Canceling"));
			labelRemainTime.setText("");
			worker.cancel();
		}
	}
}
