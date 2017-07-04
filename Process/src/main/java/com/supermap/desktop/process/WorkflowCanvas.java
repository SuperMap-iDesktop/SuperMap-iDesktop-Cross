package com.supermap.desktop.process;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.*;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.events.*;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.interaction.canvas.GraphConnectAction;
import com.supermap.desktop.process.graphics.interaction.canvas.GraphDragAction;
import com.supermap.desktop.process.graphics.interaction.canvas.PopupMenuAction;
import com.supermap.desktop.process.graphics.interaction.canvas.Selection;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/6/29.
 */
public class WorkflowCanvas extends GraphCanvas
		implements GraphBoundsChangedListener, GraphRemovingListener,
		RelationAddedListener<IProcess>, RelationRemovingListener<IProcess> {
	private Workflow workflow;
	private Map<Object, Point> locationMap;

	private Map<IProcess, ProcessGraph> processMap = new ConcurrentHashMap<>();
	private Map<OutputData, OutputGraph> outputMap = new ConcurrentHashMap<>();
	private Map<IRelation<IProcess>, ConnectionLineGraph> relationMap = new ConcurrentHashMap<>();

	private GraphConnectAction connector = new GraphConnectAction(this);

	public WorkflowCanvas(Workflow workflow) {
		this(workflow, new ConcurrentHashMap<Object, Point>());
	}


	public WorkflowCanvas(Workflow workflow, Map<Object, Point> locationMap) {
		if (workflow == null) {
			throw new NullPointerException();
		}

		this.workflow = workflow;

		if (this.workflow.getProcessCount() > 0) {
			this.locationMap = locationMap;
			loadProcesses(this.workflow.getProcesses());
			loadRelations(this.workflow.getRelations());
		}

		addGraphRemovingListener(this);

		installCanvasAction(GraphConnectAction.class, this.connector);

		getActionsManager().addMutexAction(GraphDragAction.class, GraphConnectAction.class);
		getActionsManager().addMutexAction(Selection.class, GraphConnectAction.class);

		getActionsManager().addMutexAction(GraphConnectAction.class, GraphDragAction.class);
		getActionsManager().addMutexAction(GraphConnectAction.class, Selection.class);
		getActionsManager().addMutexAction(GraphConnectAction.class, PopupMenuAction.class);
	}

	private void loadProcesses(Vector<IProcess> processes) {
		if (this.locationMap == null) {
			return;
		}

		for (int i = 0; i < processes.size(); i++) {
			initProcessGraph(processes.get(i));
		}
	}

	private ProcessGraph initProcessGraph(IProcess process) {
		if (process == null) {
			return null;
		}

		ProcessGraph processGraph = null;

		// 初始化位置信息
		if (!this.locationMap.containsKey(process)) {
			this.locationMap.put(process, new Point(0, 0));
		}

		Point location = this.locationMap.get(process);
		if (!this.processMap.containsKey(process)) {
			processGraph = new ProcessGraph(this, process);

			// 添加到 map
			this.processMap.put(process, processGraph);

			// 设置 location
			processGraph.setLocation(location);

			// 添加到画布
			addGraph(processGraph);

			processGraph.addGraphBoundsChangedListener(this);

			OutputData[] outputs = process.getOutputs().getDatas();
			for (int i = 0; i < outputs.length; i++) {
				OutputGraph outputGraph = initOutputGraph(process, outputs[i]);

				// 添加连接线
				addGraph(new ConnectionLineGraph(this, processGraph, outputGraph));
			}
		}
		return processGraph;
	}

	private OutputGraph initOutputGraph(IProcess process, OutputData outputData) {
		if (process == null || outputData == null || !this.processMap.containsKey(process)) {
			return null;
		}

		OutputGraph outputGraph = null;

		if (!this.locationMap.containsKey(outputData)) {
			this.locationMap.put(outputData, new Point(0, 0));
		}

		Point location = this.locationMap.get(outputData);
		if (!this.outputMap.containsKey(outputData)) {
			outputGraph = new OutputGraph(this, this.processMap.get(process), outputData);

			// 添加到 map
			this.outputMap.put(outputData, outputGraph);

			// 设置 location
			outputGraph.setLocation(location);

			// 添加到画布
			addGraph(outputGraph);

			outputGraph.addGraphBoundsChangedListener(this);
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

	public Workflow getWorkflow() {
		return workflow;
	}

	@Override
	public void graghBoundsChanged(GraphBoundsChangedEvent e) {
		if (!(e.getGraph() instanceof ProcessGraph) && !(e.getGraph() instanceof OutputGraph)) {
			return;
		}

		this.locationMap.put(e.getGraph(), e.getNewLocation());
	}

	@Override
	public void graphRemoving(GraphRemovingEvent e) {
		if (e.getGraph() instanceof ProcessGraph) {
			ProcessGraph processGraph = (ProcessGraph) e.getGraph();
			IProcess process = processGraph.getProcess();

			// 删除所有的输出节点
			OutputData[] outputs = process.getOutputs().getDatas();
			if (outputs != null && outputs.length > 0) {
				for (int i = 0; i < outputs.length; i++) {
					OutputGraph outputGraph = this.outputMap.get(outputs[i]);

					// 删除图上输出节点
					removeGraph(outputGraph);

					// 从 map 中移除
					this.outputMap.remove(outputs[i]);
				}
			}

			this.processMap.remove(process);
			this.workflow.removeProcess(process);

		} else if (e.getGraph() instanceof OutputGraph) {

		} else if (e.getGraph() instanceof ConnectionLineGraph) {
			ConnectionLineGraph connection = (ConnectionLineGraph) e.getGraph();

			if (connection.getFrom() instanceof ProcessGraph && connection.getTo() instanceof OutputGraph) {
				e.setCancel(true);
			} else {
				IRelation<IProcess> relation = null;

				for (IRelation<IProcess> key : this.relationMap.keySet()) {
					if (this.relationMap.get(key) == connection) {
						relation = key;
						break;
					}
				}

				this.workflow.removeRelation(relation);
			}
		}
	}

	@Override
	public void relaitonRemoving(RelationRemovingEvent<IProcess> e) {
		if (this.relationMap.containsKey(e.getRelation())) {
			ConnectionLineGraph connection = this.relationMap.get(e.getRelation());

			if (getGraphStorage().contains(connection)) {
				removeGraph(connection);
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

	private class ProcessDropTargetAdapter extends DropTargetAdapter {
		@Override
		public void drop(DropTargetDropEvent dtde) {
//			FormWorkflow.this.grabFocus();
//			Transferable transferable = dtde.getTransferable();
//			DataFlavor[] currentDataFlavors = dtde.getCurrentDataFlavors();
//			for (DataFlavor currentDataFlavor : currentDataFlavors) {
//				if (currentDataFlavor != null) {
//					try {
//						Object transferData = transferable.getTransferData(currentDataFlavor);
//						if (transferData instanceof String) {
//							MetaProcess metaProcess = WorkflowParser.getMetaProcess((String) transferData);
//							if (metaProcess == null) {
//								continue;
//							}
//							ProcessGraph graph = new ProcessGraph(getCanvas(), metaProcess);
//							Point location = dtde.getLocation();
//							location = new Point(location.x - graph.getWidth() / 2, location.y - graph.getHeight() / 2);
//							Point inverse = getCanvas().getCoordinateTransform().inverse(location);
//							graph.setLocation(inverse);
//							getCanvas().getGraphStorage().add(graph);
//							getCanvas().repaint();
//						}
//					} catch (Exception e) {
//						// ignore 当然是原谅ta啦
//						Application.getActiveApplication().getOutput().output(e);
//					}
//				}
//			}
		}
	}
}
