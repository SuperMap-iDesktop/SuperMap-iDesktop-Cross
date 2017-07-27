package com.supermap.desktop.WorkflowView.graphics.events;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.util.EventObject;

/**
 * Created by highsad on 2017/3/8.
 */
public class GraphCreatedEvent extends EventObject {
	private GraphCanvas canvas;
	private IGraph graph;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param canvas The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphCreatedEvent(GraphCanvas canvas, IGraph graph) {
		super(canvas);
		this.graph = graph;
	}

	public GraphCanvas getCanvas() {
		return canvas;
	}

	/**
	 * 获取创建的
	 *
	 * @return
	 */
	public IGraph getGraph() {
		return this.graph;
	}
}
