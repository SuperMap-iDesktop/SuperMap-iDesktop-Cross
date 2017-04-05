package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.datas.Data;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends EllipseGraph {

	private ProcessGraph processGraph;
	private Data processData;

	public OutputGraph(GraphCanvas canvas, ProcessGraph processGraph, Data processData) {
		super(canvas);
		this.processGraph = processGraph;
		this.processData = processData;
	}

	public ProcessGraph getProcessGraph() {
		return processGraph;
	}

	public Data getProcessData() {
		return processData;
	}

	public String getTitle() {
		return this.processData.getName();
	}
}
