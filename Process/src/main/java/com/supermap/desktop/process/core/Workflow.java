package com.supermap.desktop.process.core;

import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.event.EventListenerList;
import java.util.Vector;

/**
 * Created by xie on 2017/3/18.
 * WorkFLow应该只存描述字符串，而不是具体的对象。不然打开多个会导致多个窗体指向同一个对象。
 */
public class Workflow implements IWorkflow {
	private String name = "workflow";
	private boolean isEditable = true;
	private NodeMatrix<IProcess> processMatrix;
	private EventListenerList listenerList = new EventListenerList();
	private MatrixEventHandler handler = new MatrixEventHandler();

	public Workflow(String name) {
		this.name = name;
		this.processMatrix = new NodeMatrix<>();
		registerEvents();
	}

	public void setMatrix(NodeMatrix<IProcess> matrix) {
		this.processMatrix = matrix == null ? new NodeMatrix<IProcess>() : matrix;
		registerEvents();
	}

	private void registerEvents() {
		this.processMatrix.addMatrixNodeAddingListener(this.handler);
		this.processMatrix.addMatrixNodeAddedListener(this.handler);
		this.processMatrix.addMatrixNodeRemovingListener(this.handler);
		this.processMatrix.addMatrixNodeRemovedListener(this.handler);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean editable) {
		isEditable = editable;
	}

	@Override
	public String serializeTo() {
		Document doc = XmlUtilities.stringToDocument("");

		// 处理 workflow
		Element workflowNode = doc.createElement("Workflow");
		doc.appendChild(workflowNode);

		// 处理 processes
		Element processesNode = doc.createElement("processes");
		Vector<IProcess> processes = this.processMatrix.getNodes();
		for (int i = 0; i < processes.size(); i++) {
			IProcess process = processes.get(i);
			Element processNode = doc.createElement("process");
			processNode.setAttribute("key", process.getKey());
			processNode.setAttribute("className", process.getClass().getName());
			processesNode.appendChild(processesNode);
		}
		workflowNode.appendChild(processesNode);

		// 处理 relations
		Element relationsNode = doc.createElement("relations");
		Vector<IRelation<IProcess>> relations = this.processMatrix.getRelations();
		for (int i = 0; i < relations.size(); i++) {
			IRelation relation = relations.get(i);

			// 目前只有一种数据匹配关系，先就只处理这一种关系的导入和导出
			if (relation instanceof DataMatch) {
				Element relationNode = doc.createElement("relation");
				relationNode.setAttribute("className", relation.getClass().getName());
				relationNode.setAttribute("fromKey", ((DataMatch) relation).getFrom().getKey());
				relationNode.setAttribute("toKey", ((DataMatch) relation).getTo().getKey());
				relationNode.setAttribute("fromOutputData", ((DataMatch) relation).getFromOutputData().getName());
				relationNode.setAttribute("toInputData", ((DataMatch) relation).getToInputData().getName());
				relationsNode.appendChild(relationNode);
			}
		}
		workflowNode.appendChild(relationsNode);

		return XmlUtilities.nodeToString(workflowNode);
	}

	public void serializeTo(Element workflowNode) {
		Document doc = workflowNode.getOwnerDocument();

		// 处理 processes
		Element processesNode = doc.createElement("processes");
		Vector<IProcess> processes = this.processMatrix.getNodes();
		for (int i = 0; i < processes.size(); i++) {
			IProcess process = processes.get(i);
			Element processNode = doc.createElement("process");
			processNode.setAttribute("key", process.getKey());
			processNode.setAttribute("className", process.getClass().getName());
			processesNode.appendChild(processesNode);
		}
		workflowNode.appendChild(processesNode);

		// 处理 relations
		Element relationsNode = doc.createElement("relations");
		Vector<IRelation<IProcess>> relations = this.processMatrix.getRelations();
		for (int i = 0; i < relations.size(); i++) {
			IRelation relation = relations.get(i);

			// 目前只有一种数据匹配关系，先就只处理这一种关系的导入和导出
			if (relation instanceof DataMatch) {
				Element relationNode = doc.createElement("relation");
				relationNode.setAttribute("className", relation.getClass().getName());
				relationNode.setAttribute("fromKey", ((DataMatch) relation).getFrom().getKey());
				relationNode.setAttribute("toKey", ((DataMatch) relation).getTo().getKey());
				relationNode.setAttribute("fromOutputData", ((DataMatch) relation).getFromOutputData().getName());
				relationNode.setAttribute("toInputData", ((DataMatch) relation).getToInputData().getName());
				relationsNode.appendChild(relationNode);
			}
		}
		workflowNode.appendChild(relationsNode);
	}

	@Override
	public void serializeFrom(String xmlDescription) {

	}

	public Vector<IRelation<IProcess>> getRelations() {
		return this.processMatrix.getRelations();
	}

	public void addProcess(IProcess process) {
		this.processMatrix.addNode(process);
	}

	public void removeProcess(IProcess process) {
		this.processMatrix.removeNode(process);
	}

	public boolean contains(IProcess process) {
		return this.processMatrix.contains(process);
	}

	public void addRelation(IRelation<IProcess> relation) {
		this.processMatrix.addRelation(relation);
	}

	public void removeRelation(IProcess from, IProcess to) {
		this.processMatrix.removeRelation(from, to);
	}

	public void removeRelation(IRelation<IProcess> relation) {
		this.processMatrix.removeRelation(relation);
	}

	public boolean containsRelation(IRelation<IProcess> relation) {
		return this.processMatrix.containsRelation(relation);
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
		return this.processMatrix.getCount();
	}

	public boolean isLeadingProcess(IProcess process) {
		return this.processMatrix.isLeadingNode(process);
	}

	public void reset() {
		setEditable(true);
		Vector<IProcess> processes = this.processMatrix.getNodes();

		if (processes != null && processes.size() > 0) {
			for (int i = 0; i < processes.size(); i++) {
				processes.get(i).reset();
			}
		}
	}

	public void addWorkflowChangeListener(WorkflowChangeListener listener) {
		this.listenerList.add(WorkflowChangeListener.class, listener);
	}

	public void removeWorkflowChangeListener(WorkflowChangeListener listener) {
		this.listenerList.remove(WorkflowChangeListener.class, listener);
	}

	public void addRelationAddedListener(RelationAddedListener<IProcess> listener) {
		this.processMatrix.addRelationAddedListener(listener);
	}

	public void removeRelationAddedListener(RelationAddedListener<IProcess> listener) {
		this.processMatrix.removeRelationAddedListener(listener);
	}

	public void addRelationRemovingListener(RelationRemovingListener<IProcess> listener) {
		this.processMatrix.addRelationRemovingListener(listener);
	}

	public void removeRelationRemovingListener(RelationRemovingListener<IProcess> listener) {
		this.processMatrix.addRelationRemovingListener(listener);
	}

	public void addRelationRemovedListener(RelationRemovedListener<IProcess> listener) {
		this.processMatrix.addRelationRemovedListener(listener);
	}

	public void removeRelationRemovedListener(RelationRemovedListener<IProcess> listener) {
		this.processMatrix.removeRelationRemovedListener(listener);
	}

	protected void fireWorkflowChange(WorkflowChangeEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == WorkflowChangeListener.class) {
				((WorkflowChangeListener) listeners[i + 1]).workflowChange(e);
			}
		}
	}

	private class MatrixEventHandler implements MatrixNodeAddingListener<IProcess>, MatrixNodeAddedListener<IProcess>, MatrixNodeRemovingListener<IProcess>, MatrixNodeRemovedListener<IProcess> {

		@Override
		public void matrixNodeAdded(MatrixNodeAddedEvent<IProcess> e) {
			fireWorkflowChange(new WorkflowChangeEvent(Workflow.this, WorkflowChangeEvent.ADDED, e.getNode()));
		}

		@Override
		public void matrixNodeAdding(MatrixNodeAddingEvent<IProcess> e) {
			fireWorkflowChange(new WorkflowChangeEvent(Workflow.this, WorkflowChangeEvent.ADDING, e.getNode()));
		}

		@Override
		public void matrixNodeRemoved(MatrixNodeRemovedEvent<IProcess> e) {
			fireWorkflowChange(new WorkflowChangeEvent(Workflow.this, WorkflowChangeEvent.REMOVED, e.getNode()));
		}

		@Override
		public void matrixNodeRemoving(MatrixNodeRemovingEvent<IProcess> e) {
			fireWorkflowChange(new WorkflowChangeEvent(Workflow.this, WorkflowChangeEvent.REMOVING, e.getNode()));
		}
	}
}
