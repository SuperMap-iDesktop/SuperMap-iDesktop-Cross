package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends EllipseGraph {

	private ProcessGraph processGraph;
	private ProcessData processData;

	public OutputGraph(GraphCanvas canvas, ProcessGraph processGraph, ProcessData processData) {
		super(canvas);
		this.processGraph = processGraph;
		this.processData = processData;
	}

	public ProcessGraph getProcessGraph() {
		return processGraph;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public String getTitle() {
		return this.processData.getText();
	}
}
