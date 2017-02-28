package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;

/**
 * Created by highsad on 2017/2/28.
 */
public class OutputGraph extends EllipseGraph {

	private ProcessData processData;

	public OutputGraph(GraphCanvas canvas, ProcessData processData) {
		super(canvas);
		this.processData = processData;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public String getTitle() {
		return this.processData.getText();
	}
}
