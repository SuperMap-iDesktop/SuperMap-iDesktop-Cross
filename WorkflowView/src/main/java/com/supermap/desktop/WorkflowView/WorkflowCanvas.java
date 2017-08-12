package com.supermap.desktop.WorkflowView;

import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovingEvent;
import com.supermap.desktop.WorkflowView.graphics.events.GraphRemovingListener;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.OutputGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.ProcessGraph;
import com.supermap.desktop.WorkflowView.graphics.interaction.canvas.GraphConnectAction;
import com.supermap.desktop.WorkflowView.graphics.interaction.canvas.GraphDragAction;
import com.supermap.desktop.WorkflowView.graphics.interaction.canvas.PopupMenuAction;
import com.supermap.desktop.WorkflowView.graphics.interaction.canvas.Selection;
import com.supermap.desktop.process.ProcessManager;
import com.supermap.desktop.process.core.DataMatch;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.events.RelationAddedEvent;
import com.supermap.desktop.process.events.RelationAddedListener;
import com.supermap.desktop.process.events.RelationRemovingEvent;
import com.supermap.desktop.process.events.RelationRemovingListener;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.StringUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/6/29.
 */
public class WorkflowCanvas extends GraphCanvas
		implements GraphRemovingListener,
		RelationAddedListener<IProcess>, RelationRemovingListener<IProcess> {
	private Workflow workflow;

	private Map<IProcess, ProcessGraph> processMap = new ConcurrentHashMap<>();
	private Map<OutputData, OutputGraph> outputMap = new ConcurrentHashMap<>();
	private Map<IRelation<IProcess>, ConnectionLineGraph> relationMap = new ConcurrentHashMap<>();
	private Map<OutputData, ConnectionLineGraph> outputLinesMap = new ConcurrentHashMap<>();

	private GraphConnectAction connector = new GraphConnectAction(this);

	public WorkflowCanvas(Workflow workflow) {
		loadWorkflow(workflow);
		addGraphRemovingListener(this);
		this.workflow.addRelationAddedListener(this);
		this.workflow.addRelationRemovingListener(this);
		new DropTarget(this, new ProcessDropTargetHandler());

		installCanvasAction(GraphConnectAction.class, this.connector);

		getActionsManager().addMutexAction(GraphDragAction.class, GraphConnectAction.class);
		getActionsManager().addMutexAction(Selection.class, GraphConnectAction.class);

		getActionsManager().addMutexAction(GraphConnectAction.class, GraphDragAction.class);
		getActionsManager().addMutexAction(GraphConnectAction.class, Selection.class);
		getActionsManager().addMutexAction(GraphConnectAction.class, PopupMenuAction.class);
	}

	public GraphConnectAction getConnector() {
		return this.connector;
	}

	private void loadWorkflow(Workflow workflow) {
		if (workflow == null) {
			throw new NullPointerException();
		}

		this.workflow = workflow;

		if (this.workflow.getProcessCount() > 0) {
			loadProcesses(this.workflow.getProcesses());
			loadRelations(this.workflow.getRelations());
		}

	}

	private void loadProcesses(Vector<IProcess> processes) {
		for (int i = 0; i < processes.size(); i++) {
			initProcessGraph(processes.get(i));
		}
	}

	private ProcessGraph initProcessGraph(IProcess process) {
		if (process == null) {
			return null;
		}

		return addProcess(process, new Point(0, 0));
	}

	private ProcessGraph addProcess(IProcess process, Point location) {
		ProcessGraph processGraph = null;

		if (!this.processMap.containsKey(process)) {
			processGraph = new ProcessGraph(this, process);

			// 添加到 map
			this.processMap.put(process, processGraph);

			// 设置 location
			processGraph.setLocation(location);

			// 添加到画布
			addGraph(processGraph);

//			processGraph.addGraphBoundsChangedListener(this);

			OutputData[] outputs = process.getOutputs().getDatas();

			int vgap = 20;
			int length = outputs.length;
			OutputGraph[] dataGraphs = new OutputGraph[length];
			int totalHeight = vgap * (length - 1);

			for (int i = 0; i < length; i++) {
				dataGraphs[i] = new OutputGraph(this, processGraph, outputs[i]);
				totalHeight += dataGraphs[i].getHeight();
			}

			int locationX = processGraph.getLocation().x + processGraph.getWidth() * 3 / 2;
			int locationY = processGraph.getLocation().y + (processGraph.getHeight() - totalHeight) / 2;

			for (int i = 0; i < outputs.length; i++) {
				Point point = new Point(locationX, locationY);
				OutputGraph outputGraph = addOutputGraph(outputs[i], point);
				locationY += dataGraphs[i].getHeight() + vgap;

				// 添加 process 和 output 之间的连接线
				ConnectionLineGraph lineGraph = new ConnectionLineGraph(this, processGraph, outputGraph);
				this.outputLinesMap.put(outputs[i], lineGraph);
				addGraph(lineGraph);
			}
		}
		return processGraph;
	}

	private OutputGraph initOutputGraph(OutputData outputData) {
		if (outputData == null || outputData.getProcess() == null || !this.processMap.containsKey(outputData.getProcess())) {
			return null;
		}

		return addOutputGraph(outputData, new Point(0, 0));
	}

	private OutputGraph addOutputGraph(OutputData outputData, Point location) {
		if (outputData == null || outputData.getProcess() == null || !this.processMap.containsKey(outputData.getProcess())) {
			return null;
		}

		OutputGraph outputGraph = null;

		if (!this.outputMap.containsKey(outputData)) {
			outputGraph = new OutputGraph(this, this.processMap.get(outputData.getProcess()), outputData);

			// 添加到 map
			this.outputMap.put(outputData, outputGraph);

			// 设置 location
			outputGraph.setLocation(location);

			// 添加到画布
			addGraph(outputGraph);

//			outputGraph.addGraphBoundsChangedListener(this);
		}
		return outputGraph;
	}

	private void loadRelations(Vector<IRelation<IProcess>> relations) {
		if (relations == null) {
			return;
		}

		for (int i = 0; i < relations.size(); i++) {
			IRelation<IProcess> relation = relations.get(i);

			if (relation instanceof DataMatch) {
				DataMatch dataMatch = (DataMatch) relation;
				IGraph fromGraph = this.outputMap.get(dataMatch.getFromOutputData());
				IGraph toGraph = this.processMap.get(dataMatch.getTo());

				ConnectionLineGraph connectionLineGraph = new ConnectionLineGraph(this, fromGraph, toGraph);

				// 添加到 map
				this.relationMap.put(relation, connectionLineGraph);

				// 添加到画布
				addGraph(connectionLineGraph);
			}
		}
	}

	public void loadUIConfig(WorkflowUIConfig config) {
		if (config == null) {
			return;
		}

		for (IProcess process :
				this.processMap.keySet()) {
			ProcessLocationConfig processLocConf = config.getProcessConfig(process.getKey(), process.getSerialID());
			IGraph processGraph = this.processMap.get(process);

			if (processLocConf.getLocation() == null) {
				continue;
			}
			processGraph.setLocation(processLocConf.getLocation());

			OutputData[] outputs = process.getOutputs().getDatas();
			for (int i = 0; i < outputs.length; i++) {
				OutputData output = outputs[i];
				IGraph outputGraph = this.outputMap.get(output);

				if (processLocConf.getOutputLocation(output.getName()) != null) {
					outputGraph.setLocation(processLocConf.getOutputLocation(output.getName()));
				}
			}
		}
	}

	public void serializeTo(Element locationsNode) {
		Document doc = locationsNode.getOwnerDocument();

		// 处理 process
		for (IProcess process :
				this.processMap.keySet()) {
			Element processLocNode = doc.createElement("process");
			processLocNode.setAttribute("Key", process.getKey());
			processLocNode.setAttribute("SerialID", String.valueOf(process.getSerialID()));
			processLocNode.setAttribute("LocationX", String.valueOf(this.processMap.get(process).getLocation().x));
			processLocNode.setAttribute("LocationY", String.valueOf(this.processMap.get(process).getLocation().y));
			locationsNode.appendChild(processLocNode);

			// 处理 Output
			OutputData[] outputs = process.getOutputs().getDatas();
			for (int i = 0; i < outputs.length; i++) {
				if (this.outputMap.containsKey(outputs[i])) {
					Element outputLocNode = doc.createElement("Output");
					outputLocNode.setAttribute("Key", outputs[i].getName());
					outputLocNode.setAttribute("LocationX", String.valueOf(this.outputMap.get(outputs[i]).getLocation().x));
					outputLocNode.setAttribute("LocationY", String.valueOf(this.outputMap.get(outputs[i]).getLocation().y));
					processLocNode.appendChild(outputLocNode);
				}
			}
		}
	}

	public Workflow getWorkflow() {
		return workflow;
	}

//	@Override
//	public void graghBoundsChanged(GraphBoundsChangedEvent e) {
//		if (!(e.getGraph() instanceof ProcessGraph) && !(e.getGraph() instanceof OutputGraph)) {
//			return;
//		}
//
//		this.locationMap.put(e.getGraph(), e.getNewLocation());
//	}

	@Override
	public void graphRemoving(GraphRemovingEvent e) {
		if (e.getGraph() instanceof ProcessGraph) {
			ProcessGraph processGraph = (ProcessGraph) e.getGraph();
			IProcess process = processGraph.getProcess();

			// 移除 process
			this.processMap.remove(process);
			this.workflow.removeProcess(process);

			// 删除所有的输出节点
			OutputData[] outputs = process.getOutputs().getDatas();
			if (outputs != null && outputs.length > 0) {
				for (int i = 0; i < outputs.length; i++) {

					// 删除图上输出节点
					OutputGraph outputGraph = this.outputMap.get(outputs[i]);
					removeGraph(outputGraph);

					// 删除 process 和 output 之间的连线，并从 map 中移除
					ConnectionLineGraph lineGraph = this.outputLinesMap.get(outputs[i]);
					removeGraph(lineGraph);
					this.outputLinesMap.remove(outputs[i]);
				}
			}

		} else if (e.getGraph() instanceof OutputGraph) {
			ProcessGraph processGraph = ((OutputGraph) e.getGraph()).getProcessGraph();
			IProcess process = processGraph.getProcess();

			if (this.workflow.contains(process)) {
				e.setCancel(true);
			} else {
				if (this.outputLinesMap.containsKey(e.getGraph())) {
					this.outputLinesMap.remove(((OutputGraph) e.getGraph()).getProcessData());
				}
			}
		} else if (e.getGraph() instanceof ConnectionLineGraph) {
			ConnectionLineGraph connection = (ConnectionLineGraph) e.getGraph();

			if (connection.getFrom() instanceof ProcessGraph
					&& connection.getTo() instanceof OutputGraph
					&& getGraphStorage().contains(connection.getFrom())
					&& getGraphStorage().contains(connection.getTo())) {
				e.setCancel(true);
			} else {
				IRelation<IProcess> relation = null;

				for (IRelation<IProcess> key : this.relationMap.keySet()) {
					if (this.relationMap.get(key) == connection) {
						relation = key;
						break;
					}
				}

				if (relation != null && this.workflow.containsRelation(relation)) {
					this.workflow.removeRelationRemovingListener(this);
					this.workflow.removeRelation(relation);
					this.workflow.addRelationRemovingListener(this);
				}

				if (relation != null && this.relationMap.containsKey(relation)) {
					this.relationMap.remove(relation);
				}
			}
		}
	}

	@Override
	public void relaitonRemoving(RelationRemovingEvent<IProcess> e) {
		if (this.relationMap.containsKey(e.getRelation())) {
			ConnectionLineGraph connection = this.relationMap.get(e.getRelation());

			if (getGraphStorage().contains(connection)) {
				removeGraphRemovingListener(this);
				removeGraph(connection);
				addGraphRemovingListener(this);
			}

			if (this.relationMap.containsKey(e.getRelation())) {
				this.relationMap.remove(e.getRelation());
			}
		}
	}

	@Override
	public void relationAdded(RelationAddedEvent<IProcess> e) {
		if (e.getRelation() instanceof DataMatch) {
			DataMatch dataMatch = (DataMatch) e.getRelation();
			IGraph fromGraph = this.outputMap.get(dataMatch.getFromOutputData());
			IGraph toGraph = this.processMap.get(dataMatch.getTo());

			ConnectionLineGraph connectionLineGraph = new ConnectionLineGraph(this, fromGraph, toGraph);

			// 添加到 map
			this.relationMap.put(dataMatch, connectionLineGraph);

			// 添加到画布
			addGraph(connectionLineGraph);
		}
	}

	private class ProcessDropTargetHandler extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent dtde) {
			WorkflowCanvas.this.grabFocus();
			Transferable transferable = dtde.getTransferable();
			DataFlavor[] currentDataFlavors = dtde.getCurrentDataFlavors();
			for (DataFlavor currentDataFlavor : currentDataFlavors) {
				if (currentDataFlavor != null) {
					try {
						Object transferData = transferable.getTransferData(currentDataFlavor);
						if (!(transferData instanceof String)) {
							return;
						}

						String processKey = (String) transferData;
						if (!StringUtilities.isNullOrEmpty(processKey)) {
							IProcessLoader loader = ProcessManager.INSTANCE.findProcess(processKey);
							IProcess metaProcess = loader.loadProcess();
							if (metaProcess == null) {
								continue;
							}

							ProcessGraph graph = new ProcessGraph(WorkflowCanvas.this, metaProcess);
							Point screenLocation = new Point(dtde.getLocation().x - graph.getWidth() / 2, dtde.getLocation().y - graph.getHeight() / 2);
							Point canvasLocation = WorkflowCanvas.this.getCoordinateTransform().inverse(screenLocation);

							// 添加到 Workflow
							WorkflowCanvas.this.getWorkflow().addProcess(metaProcess);
							addProcess(metaProcess, canvasLocation);
							WorkflowCanvas.this.repaint();
						}
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(e);
					}
				}
			}
		}
	}
}
