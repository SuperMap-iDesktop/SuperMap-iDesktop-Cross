package com.supermap.desktop.process;

import com.supermap.desktop.process.core.DataMatch;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.IRelation;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.events.*;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.ConnectionLineGraph;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.OutputGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;

import javax.xml.crypto.Data;
import java.awt.*;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/6/29.
 */
public class WorkflowCanvas extends GraphCanvas
		implements WorkflowChangeListener, RelationAddedListener<IProcess>,
		RelationRemovingListener<IProcess>, GraphBoundsChangedListener {
	private Workflow workflow;
	private Map<Object, Point> locationMap;

	private Map<IProcess, ProcessGraph> processMap = new ConcurrentHashMap<>();
	private Map<OutputData, OutputGraph> outputMap = new ConcurrentHashMap<>();
	private Map<IRelation<IProcess>, ConnectionLineGraph> relationMap = new ConcurrentHashMap<>();

	public WorkflowCanvas(Workflow workflow) {
		this(workflow, new ConcurrentHashMap<Object, Point>());
	}


	public WorkflowCanvas(Workflow workflow, Map<Object, Point> locationMap) {
		if (workflow == null) {
			throw new NullPointerException();
		}

		this.workflow = workflow;

		if (this.workflow.getProcessCount() > 0) {
			loadProcesses(this.workflow.getProcesses());
			loadRelations(this.workflow.getRelations());
		}

		this.workflow.addWorkflowChangeListener(this);
		this.workflow.addRelationAddedListener(this);
		this.workflow.addRelationRemovingListener(this);
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

	@Override
	public void workflowChange(WorkflowChangeEvent e) {
		if (e.getType() == WorkflowChangeEvent.ADDED) {

		} else if (e.getType() == WorkflowChangeEvent.REMOVING) {

		} else if (e.getType() == WorkflowChangeEvent.REMOVED) {

		}
	}

	@Override
	public void relationAdded(RelationAddedEvent<IProcess> e) {
		if (e.getRelation() instanceof DataMatch) {

		}
	}

	@Override
	public void relaitonRemoving(RelationRemovingEvent<IProcess> e) {
		if (e.getRelation() instanceof DataMatch) {

		}
	}

	@Override
	public void graghBoundsChanged(GraphBoundsChangedEvent e) {
		if (!(e.getGraph() instanceof ProcessGraph) && !(e.getGraph() instanceof OutputGraph)) {
			return;
		}

		this.locationMap.put(e.getGraph(), e.getNewLocation());
	}
}
