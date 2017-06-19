package com.supermap.desktop.process.core;

import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;
import com.supermap.desktop.process.util.WorkFlowXmlUtilties;

import javax.swing.event.EventListenerList;

/**
 * Created by xie on 2017/3/18.
 * WorkFLow应该只存描述字符串，而不是具体的对象。不然打开多个会导致多个窗体指向同一个对象。
 */
public class Workflow implements IWorkflow {
	private String name = "workFLow";
	private String matrixXml;
	private EventListenerList listenerList = new EventListenerList();

	public Workflow(String name) {
		this.name = name;
	}

	public void setMatrix(NodeMatrix matrix) {
		this.matrixXml = WorkFlowXmlUtilties.parseToXml(matrix, name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		setMatrix(getMatrix()); // 把名字改了
	}

	public NodeMatrix getMatrix() {
		return WorkFlowXmlUtilties.stringToNodeMatrix(matrixXml);
	}

	public IProcess[] getNextProcesses(IProcess process) {
		return null;
	}

	public IProcess[] getPreProcesses(IProcess process) {
		return null;
	}

	// 获取所有的游离节点
	public IProcess[] getSingleProcesses(IProcess process) {
		return null;
	}

	// 获取所有的领头节点
	public IProcess[] getLeaderProcesses(IProcess process) {
		return null;
	}

	public IProcess[] getProcesses() {
		return null;
	}

	public int getProcessCount() {
		return 0;
	}

	public boolean isLeadProcess(IProcess process) {
		return true;
	}

	@Override
	public String getMatrixXml() {
		return matrixXml;
	}

	@Override
	public void setMatrixXml(String matrixXml) {
		this.matrixXml = matrixXml;
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
