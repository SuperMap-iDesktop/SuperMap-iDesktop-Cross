package com.supermap.desktop.process.graphics.events;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.util.EventObject;

/**
 * Created by highsad on 2017/3/8.
 */
public class GraphCreatingEvent extends EventObject {
	private GraphCanvas canvas;
	private IGraph graph;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param canvas The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphCreatingEvent(GraphCanvas canvas, IGraph graph) {
		super(canvas);
		this.graph = graph;
	}

	public GraphCanvas getCanvas() {
		return canvas;
	}

	public IGraph getGraph() {
		return graph;
	}
}
