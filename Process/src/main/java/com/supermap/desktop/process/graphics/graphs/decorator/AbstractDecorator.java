package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator extends AbstractGraph {

	private AbstractGraph graph;

	public AbstractDecorator(GraphCanvas canvas, Shape shape) {
		super(canvas, shape);
	}

	public AbstractGraph getGraph() {
		return graph;
	}

	public void decorate(AbstractGraph graph) {
		this.graph = graph;
	}

	public void undecorate() {
		this.graph = null;
	}

	public boolean isDecorating() {
		return this.graph != null;
	}

	@Override
	public IGraph clone() {
		throw new UnsupportedOperationException();
	}
}
