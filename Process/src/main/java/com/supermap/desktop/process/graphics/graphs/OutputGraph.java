package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends EllipseGraph {

	private ProcessGraph processGraph;
	private OutputData processData;

	public OutputGraph(GraphCanvas canvas, ProcessGraph processGraph, OutputData processData) {
		super(canvas);
		this.processGraph = processGraph;
		this.processData = processData;
	}

	public ProcessGraph getProcessGraph() {
		return processGraph;
	}

	public OutputData getProcessData() {
		return processData;
	}

	public String getTitle() {
		return this.processData.getName();
	}
}
