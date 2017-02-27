package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import sun.swing.SwingUtilities2;

import java.awt.*;

/**
 * Created by highsad on 2017/1/24.
 */
public class ProcessGraph extends RectangleGraph {

	private IProcess process;

	public ProcessGraph(GraphCanvas canvas) {
		super(canvas);
	}

	public ProcessGraph(GraphCanvas canvas, IProcess process) {
		super(canvas);
		this.process = process;
	}

	@Override
	public IGraph clone() {
		ProcessGraph graph = new ProcessGraph(getCanvas(), this.process);
		graph.setSize(getWidth(), getHeight());
		graph.setArcWidth(getArcWidth());
		graph.setArcHeight(getArcHeight());
		return graph;
	}
}
