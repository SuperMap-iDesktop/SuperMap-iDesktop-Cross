package com.supermap.desktop.process.ui;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.tasks.ProcessWorker;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @author XiaJT
 */
public class SmDialogProcess extends SmDialog {
	private IProcess metaProcess;

	public SmDialogProcess(IProcess metaProcess) {
		this.metaProcess = metaProcess;
		this.metaProcess.addStatusChangeListener(new StatusChangeListener() {
			@Override
			public void statusChange(StatusChangeEvent e) {
				if (e.getStatus() == RunningStatus.COMPLETED) {
					setVisible(false);
				}
			}
		});
		this.setTitle(metaProcess.getTitle());
		JPanel panel = (JPanel) metaProcess.getComponent().getPanel();
//		ProcessTask task = TaskUtil.getTask(metaProcess);
		SingleProgressPanel singleProgressPanel = new SingleProgressPanel(metaProcess.getTitle());
		singleProgressPanel.setTitleVisible(false);
		ProcessWorker worker = new ProcessWorker(metaProcess);
		singleProgressPanel.setWorker(worker);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new GridBagLayout());
		this.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		this.add(singleProgressPanel, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraintsHelper.HORIZONTAL));
		this.setMinimumSize(new Dimension(400, 600));
		this.setPreferredSize(new Dimension(400, 600));
		this.setLocationRelativeTo(null);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		metaProcess.dispose();
	}
}
