package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator implements IDecorator {
	private IGraph decoratedGraph;
	private GraphCanvas canvas;

	public AbstractDecorator(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public IGraph getGraph() {
		return this.decoratedGraph;
	}

	@Override
	public void decorate(IGraph graph) {
		this.decoratedGraph = graph;
	}

	@Override
	public void undecorate() {
		this.decoratedGraph = null;
	}

	@Override
	public boolean contains(Point point) {
		return this.decoratedGraph != null && this.decoratedGraph.contains(point);
	}

	@Override
	public Rectangle getBounds() {
		return this.decoratedGraph == null ? null : this.decoratedGraph.getBounds();
	}

	@Override
	public final void paint(Graphics g) {
		if (this.decoratedGraph != null) {
			onPaint(g);
		}
	}

	protected abstract void onPaint(Graphics g);
}
