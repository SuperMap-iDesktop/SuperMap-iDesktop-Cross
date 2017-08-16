package com.supermap.desktop.process.readyChecker;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IReadyChecker;
import com.supermap.desktop.process.core.Workflow;

import java.text.MessageFormat;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class WorkflowProcessReadyChecker<E extends Workflow> implements IReadyChecker<E> {
	@Override
	public boolean isReady(Workflow workflow) {
		Vector<IProcess> processes = workflow.getProcesses();
		StringBuilder stringBuilder = new StringBuilder();
		for (IProcess process : processes) {
			boolean readyState = process.checkReadyState();
			if (!readyState) {
				stringBuilder.append(", \"").append(process.getTitle()).append("\"");
			}
		}
		if (stringBuilder.length() > 0) {
			Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ProcessParameterError"), stringBuilder.toString().substring(1)));
			return false;
		}
		return true;
	}
}
