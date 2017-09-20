package com.supermap.desktop.process.ui;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.tasks.ProcessWorker;

import javax.swing.*;

/**
 * Created by highsad on 2017/9/20.
 */
public class ProcessUtil {
	private ProcessUtil() {
		// 工具类，不提供构造函数
	}

	public static void showDialogProcess(IProcess process) {
		JPanel panel = (JPanel) process.getComponent().getPanel();
		ProcessWorker worker = new ProcessWorker(process);
		DialogSingleProgressEmbedded dialog = new DialogSingleProgressEmbedded(panel, worker);
		dialog.setAutoClosed(!process.getClass().getName().equals("com.supermap.desktop.WorkflowView.meta.metaProcessImplements.gridStatisticsAnalyst.MetaProcessBasicStatistics"));
		dialog.setVisible(true);
	}
}
