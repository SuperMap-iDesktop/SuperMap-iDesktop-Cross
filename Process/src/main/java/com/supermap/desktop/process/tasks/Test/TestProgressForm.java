package com.supermap.desktop.process.tasks.Test;

import com.supermap.desktop.process.tasks.IWorkerView;
import com.supermap.desktop.process.tasks.SingleProgress;
import com.supermap.desktop.process.tasks.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/6/24.
 */
public class TestProgressForm extends JDialog implements IWorkerView<SingleProgress> {
	private Worker<SingleProgress> worker;

	private JProgressBar progressBar = new JProgressBar();
	private JLabel message = new JLabel();
	private JLabel remainTime = new JLabel();
	private JButton buttonOK = new JButton("OK");
	private JButton buttonCancel = new JButton("Cancel");

	public TestProgressForm() {
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(this.message)
				.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.remainTime)
				.addGroup(layout.createSequentialGroup()
						.addGap(0, 5, Short.MAX_VALUE)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(this.message)
				.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.remainTime)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.buttonOK)
						.addComponent(this.buttonCancel)));

		setSize(800, 600);

		this.buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonCancel.setText("取消中...");
				buttonCancel.setEnabled(false);
				worker.cancel();
			}
		});
	}

	@Override
	public void execute(Worker<SingleProgress> worker) {
		this.worker = worker;
		worker.setView(this);
		worker.execute();
	}


	@Override
	public void update(SingleProgress chunk) {
		this.progressBar.setValue(chunk.getPercent());
		this.message.setText(chunk.getMessage());
		this.remainTime.setText(chunk.getRemainTime());
	}

	@Override
	public void done() {
		setVisible(false);
		this.buttonCancel.setText("Cancel");
		this.buttonCancel.setEnabled(true);
	}
}
