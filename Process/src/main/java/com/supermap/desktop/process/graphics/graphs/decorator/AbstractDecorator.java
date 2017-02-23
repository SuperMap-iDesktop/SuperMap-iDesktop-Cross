package com.supermap.desktop.process.graphics.graphs.decorator;

import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/2/23.
 */
public abstract class AbstractDecorator implements IGraph {

	private AbstractGraph graph;

	public AbstractGraph getGraph() {
		return graph;
	}

	public void decorate(AbstractGraph graph) {
		this.graph = graph;
	}
}
