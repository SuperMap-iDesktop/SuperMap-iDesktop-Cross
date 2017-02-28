package com.supermap.desktop.process.graphics.painter;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/25.
 */
public class DefaultGraphPainterFactory implements IGraphPainterFactory {

	private GraphCanvas canvas;
	private IGraphPainter graphPainter = new DefaultGraphPainter();

	public DefaultGraphPainterFactory(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public IGraphPainter getPainter(IGraph graph, Graphics graphics) {
		this.graphPainter.setCanvas(this.canvas);
		this.graphPainter.setGraph(graph);
		this.graphPainter.setGraphics(graphics);
		return this.graphPainter;
	}

	@Override
	public IGraphPainter getPainter(IGraph graph, Graphics graphics, IStyleFactory styleFactory) {
		this.graphPainter.setGraph(graph);
		this.graphPainter.setGraphics(graphics);
		this.graphPainter.setStyleFactory(styleFactory);
		return this.graphPainter;
	}
}
