package com.supermap.desktop.WorkflowView.graphics.graphs.decorators;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.graphs.AbstractGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/4/26.
 */
public abstract class AbstractShapedDecorator extends AbstractDecorator {
	private AbstractGraph shapedGraph;

	public AbstractShapedDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	public AbstractGraph getShapedGraph() {
		return this.shapedGraph;
	}

	@Override
	public void decorate(IGraph graph) {
		if (graph instanceof AbstractGraph) {
			super.decorate(graph);
			this.shapedGraph = (AbstractGraph) graph;
		}
	}

	@Override
	public void undecorate() {
		super.undecorate();
		this.shapedGraph = null;
	}
}
