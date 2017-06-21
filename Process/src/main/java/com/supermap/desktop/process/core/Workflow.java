package com.supermap.desktop.process.core;

import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;

import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by xie on 2017/3/18.
 * WorkFLow应该只存描述字符串，而不是具体的对象。不然打开多个会导致多个窗体指向同一个对象。
 */
public class Workflow implements IWorkflow {
	private String name = "workflow";

	private NodeMatrix<IProcess> processMatrix = new NodeMatrix<>();
	private List<IConnection> connections = new ArrayList<>();
	private EventListenerList listenerList = new EventListenerList();

	public Workflow(String name) {
		this.name = name;
		this.processMatrix = new NodeMatrix<>();
	}

	public void setMatrix(NodeMatrix matrix) {

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toXML() {
		return null;
	}

	@Override
	public void fromXML(String xmlDescription) {

	}

	public Vector<IProcess> getToProcesses(IProcess process) {
		return this.processMatrix.getToNodes(process);
	}

	public Vector<IProcess> getFromProcesses(IProcess process) {
		return this.processMatrix.getFromNodes(process);
	}

	// 获取所有的游离节点
	public Vector<IProcess> getFreeProcesses(IProcess process) {
		return this.processMatrix.getFreeNodes();
	}

	// 获取所有的领头节点
	public Vector<IProcess> getLeadingProcesses(IProcess process) {
		return this.processMatrix.getLeadingNodes();
	}

	public Vector<IProcess> getProcesses() {
		return this.processMatrix.getNodes();
	}

	public int getProcessCount() {
		return 0;
	}

	public boolean isLeadProcess(IProcess process) {
		return true;
	}

	public void addWorkflowChangeListener(WorkflowChangeListener listener) {
		this.listenerList.add(WorkflowChangeListener.class, listener);
	}

	public void removeWorkflowChangeListener(WorkflowChangeListener listener) {
		this.listenerList.remove(WorkflowChangeListener.class, listener);
	}

	protected void fireWorkflowChange(WorkflowChangeEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == WorkflowChangeListener.class) {
				((WorkflowChangeListener) listeners[i + 1]).workflowChange(e);
			}
		}
	}
}
