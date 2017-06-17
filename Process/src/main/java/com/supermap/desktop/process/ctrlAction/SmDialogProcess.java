package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.tasks.ProcessTask;
import com.supermap.desktop.process.util.TaskUtil;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class SmDialogProcess extends SmDialog {
	private MetaProcess metaProcess;

	public SmDialogProcess(MetaProcess metaProcess) {
		this.metaProcess = metaProcess;
		this.setTitle(metaProcess.getTitle());
		JPanel panel = (JPanel) metaProcess.getComponent().getPanel();
		ProcessTask task = TaskUtil.getTask(metaProcess);
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new GridBagLayout());
		this.add(scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
//		this.add(new JPanel(), new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraintsHelper.BOTH));
		this.add(task, new GridBagConstraintsHelper(0, 2, 1, 1).setWeight(1, 0).setFill(GridBagConstraintsHelper.HORIZONTAL));
		int count = task.getComponentCount();
		for (int i = 0; i < count; i++) {
			if ("ProcessTask_buttonRemove".equals(task.getComponent(i).getName())) {
				task.getComponent(i).setVisible(false);
			}
		}
//		this.pack();
		this.setMinimumSize(new Dimension(400, 600));
		this.setPreferredSize(new Dimension(400, 600));
//		this.setPreferredSize(new Dimension(getWidth() + 200, getHeight()));
		this.setLocationRelativeTo(null);
	}
}
