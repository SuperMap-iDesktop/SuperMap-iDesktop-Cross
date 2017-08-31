package com.supermap.desktop.process.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IWorkflow;
import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.readyChecker.ProcessChangeSourceDataChecker;
import com.supermap.desktop.process.readyChecker.WorkflowProcessReadyChecker;
import com.supermap.desktop.process.readyChecker.WorkflowRunnableChecker;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.desktop.utilities.XmlUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.event.EventListenerList;
import java.text.MessageFormat;
import java.util.ArrayList;
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
	private ArrayList<IReadyChecker<Workflow>> readyCheckers = new ArrayList<>();

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

		this.readyCheckers.add(new ProcessChangeSourceDataChecker<>());
		this.readyCheckers.add(new WorkflowProcessReadyChecker<>());
		this.readyCheckers.add(new WorkflowRunnableChecker<>());
	}

	@Override
	public String getName() {
		return this.name;
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
		Document doc = XmlUtilities.getEmptyDocument();

		// 处理 workflow
		Element workflowNode = doc.createElement("Workflow");
		workflowNode.setAttribute("Name", this.name);
		doc.appendChild(workflowNode);

		// 处理 processes
		Element processesNode = doc.createElement("Processes");
		Vector<IProcess> processes = this.processMatrix.getNodes();
		for (int i = 0; i < processes.size(); i++) {
			IProcess process = processes.get(i);
			Element processNode = doc.createElement("Process");
			processNode.setAttribute("Key", process.getKey());
			processNode.setAttribute("ClassName", process.getClass().getName());
			processNode.setAttribute("LoaderClassName", process.getLoader().getName());
			processesNode.appendChild(processesNode);
		}
		workflowNode.appendChild(processesNode);

		// 处理 relations
		Element relationsNode = doc.createElement("Relations");
		Vector<IRelation<IProcess>> relations = this.processMatrix.getRelations();
		for (int i = 0; i < relations.size(); i++) {
			IRelation relation = relations.get(i);

			// 目前只有一种数据匹配关系，先就只处理这一种关系的导入和导出
			if (relation instanceof DataMatch) {
				Element relationNode = doc.createElement("Relation");
				relationNode.setAttribute("ClassName", relation.getClass().getName());
				relationNode.setAttribute("FromKey", ((DataMatch) relation).getFrom().getKey());
				relationNode.setAttribute("ToKey", ((DataMatch) relation).getTo().getKey());
				relationNode.setAttribute("FromOutputData", ((DataMatch) relation).getFromOutputData().getName());
				relationNode.setAttribute("ToInputData", ((DataMatch) relation).getToInputData().getName());
				relationsNode.appendChild(relationNode);
			}
		}
		workflowNode.appendChild(relationsNode);

		return XmlUtilities.nodeToString(workflowNode);
	}

	public void serializeTo(Element workflowNode) {
		Document doc = workflowNode.getOwnerDocument();

		// 设置 workflow 的属性
		workflowNode.setAttribute("Name", this.name);

		// 处理 processes
		Element processesNode = doc.createElement("Processes");
		Vector<IProcess> processes = this.processMatrix.getNodes();
		for (int i = 0; i < processes.size(); i++) {
			IProcess process = processes.get(i);
			Element processNode = doc.createElement("Process");
			processNode.setAttribute("Key", process.getKey());

			// 一个工作流可能存在多个相同类型的 process，此时导出就需要有一个标记，用以在导入的时候匹配
			processNode.setAttribute("SerialID", String.valueOf(process.getSerialID()));
			processesNode.appendChild(processNode);
		}
		workflowNode.appendChild(processesNode);

		// 处理 relations
		Element relationsNode = doc.createElement("Relations");
		Vector<IRelation<IProcess>> relations = this.processMatrix.getRelations();
		for (int i = 0; i < relations.size(); i++) {
			IRelation relation = relations.get(i);

			// 目前只有一种数据匹配关系，先就只处理这一种关系的导入和导出
			if (relation instanceof DataMatch) {
				Element relationNode = doc.createElement("Relation");
				relationNode.setAttribute("ClassName", relation.getClass().getName());
				relationNode.setAttribute("FromKey", ((DataMatch) relation).getFrom().getKey());
				relationNode.setAttribute("FromSerialID", String.valueOf(((DataMatch) relation).getFrom().getSerialID()));
				relationNode.setAttribute("ToKey", ((DataMatch) relation).getTo().getKey());
				relationNode.setAttribute("ToSerialID", String.valueOf(((DataMatch) relation).getTo().getSerialID()));
				relationNode.setAttribute("FromOutputData", ((DataMatch) relation).getFromOutputData().getName());
				relationNode.setAttribute("ToInputData", ((DataMatch) relation).getToInputData().getName());
				relationsNode.appendChild(relationNode);
			}
		}
		workflowNode.appendChild(relationsNode);
	}

	public void serializeFrom(Element workflowNode) {

		// 处理 workflow 的属性
		String workflowName = workflowNode.getAttribute("Name");
		this.name = workflowName;

		// 处理 process
		Element processesNode = XmlUtilities.getChildElementNodeByName(workflowNode, "Processes");
		Element[] processNodes = XmlUtilities.getChildElementNodesByName(processesNode, "Process");
		for (int i = 0; i < processNodes.length; i++) {
			Element processNode = processNodes[i];
			String key = processNode.getAttribute("Key");
			int serialID = Integer.valueOf(processNode.getAttribute("SerialID"));

			IProcessLoader loader = ProcessManager.INSTANCE.findProcess(key);
			if (loader == null) {
				throw new IllegalArgumentException("The specified process with key \"" + key + "\" don't exist.");
			}

			IProcess process = loader.loadProcess();
			process.setSerialID(serialID);
			addProcess(process);
		}

		// 处理 Relation
		Element relationsNode = XmlUtilities.getChildElementNodeByName(workflowNode, "Relations");
		Element[] relationNodes = XmlUtilities.getChildElementNodesByName(relationsNode, "Relation");
		for (int i = 0; i < relationNodes.length; i++) {
			Element relationNode = relationNodes[i];
			String className = relationNode.getAttribute("ClassName");

			if (StringUtilities.stringEquals(className, DataMatch.class.getName())) {
				String fromKey = relationNode.getAttribute("FromKey");
				int fromSerialID = Integer.valueOf(relationNode.getAttribute("FromSerialID"));
				IProcess from = getProcess(fromKey, fromSerialID);

				String toKey = relationNode.getAttribute("ToKey");
				int toSerialID = Integer.valueOf(relationNode.getAttribute("ToSerialID"));
				IProcess to = getProcess(toKey, toSerialID);

				if (from == null || to == null) {
					continue;
				}

				String fromOutputData = relationNode.getAttribute("FromOutputData");
				String toInputData = relationNode.getAttribute("ToInputData");
				DataMatch dataMatch = new DataMatch(from, to, fromOutputData, toInputData);
				addRelation(dataMatch);
			}
		}
	}

	public IProcess getProcess(String key, int serialID) {
		IProcess ret = null;

		Vector<IProcess> processes = getProcesses();
		for (IProcess process : processes) {
			if (StringUtilities.stringEquals(process.getKey(), key) && process.getSerialID() == serialID) {
				ret = process;
				break;
			}
		}
		return ret;
	}

	@Override
	public void serializeFrom(String xmlDescription) {

	}

	public Vector<IRelation<IProcess>> getRelations() {
		return this.processMatrix.getRelations();
	}

	public IProcess getProcess(String key) {
		IProcess process = null;

		Vector<IProcess> processes = this.processMatrix.getNodes();
		for (int i = 0; i < processes.size(); i++) {
			if (StringUtilities.stringEquals(processes.get(i).getKey(), key)) {
				process = processes.get(i);
				break;
			}
		}
		return process;
	}

	public void addProcess(IProcess process) {
		process.setWorkflow(this);
		this.processMatrix.addNode(process);
	}

	public void removeProcess(IProcess process) {
		process.setWorkflow(null);
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
	public Vector<IProcess> getFreeProcesses() {
		return this.processMatrix.getFreeNodes();
	}

	// 获取所有的领头节点
	public Vector<IProcess> getLeadingProcesses() {
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

	public void addWorkflowReadyChecker(IReadyChecker<Workflow> readyChecker) {
		if (readyCheckers != null && readyChecker != null) {
			readyCheckers.add(readyChecker);
		}
	}

	public void removeWorkflowReadyChecker(IReadyChecker<IWorkflow> readyChecker) {
		if (readyCheckers != null && readyChecker != null) {
			readyCheckers.remove(readyChecker);
		}
	}

	@Override
	public boolean isReady() {
		boolean result = true;
		for (IReadyChecker<Workflow> readyChecker : readyCheckers) {
			result = result && readyChecker.isReady(this);
		}
		return result;
	}

	private void checkProcessChangeSourceData() {
		ArrayList<String> changeSourceDataProcessNames = new ArrayList<>();
		Vector<IProcess> processes = getProcesses();
		for (IProcess process : processes) {
			if (process.isChangeSourceData() && !changeSourceDataProcessNames.contains(process.getTitle())) {
				changeSourceDataProcessNames.add(process.getTitle());
			}
		}
		StringBuilder stringBuffer = new StringBuilder();
		for (String changeSourceDataProcessName : changeSourceDataProcessNames) {
			stringBuffer.append(", ").append(changeSourceDataProcessName);
		}
		String processNames = stringBuffer.toString();
		processNames = processNames.substring(1);
		Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ProcessWillChangeSourceData"), processNames));
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
