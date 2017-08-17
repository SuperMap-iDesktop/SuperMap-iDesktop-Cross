package com.supermap.desktop.process.readyChecker;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IReadyChecker;
import com.supermap.desktop.process.core.Workflow;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class ProcessChangeSourceDataChecker<E extends Workflow> implements IReadyChecker<E> {
	@Override
	public boolean isReady(Workflow workflow) {
		ArrayList<String> changeSourceDataProcessNames = new ArrayList<>();
		Vector<IProcess> processes = workflow.getProcesses();
		for (IProcess process : processes) {
			if (process.isChangeSourceData() && !changeSourceDataProcessNames.contains(process.getTitle())) {
				changeSourceDataProcessNames.add(process.getTitle());
			}
		}
		StringBuilder stringBuffer = new StringBuilder();
		for (String changeSourceDataProcessName : changeSourceDataProcessNames) {
			stringBuffer.append(", ").append("\"").append(changeSourceDataProcessName).append("\"");
		}
		if (stringBuffer.length() > 0) {
			String processNames = stringBuffer.toString();
			processNames = processNames.substring(1);
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ProcessWillChangeSourceData"), processNames));
		}
		return true;
	}
}
