package com.supermap.desktop.process.graphics.interaction;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.util.EventObject;

/**
 * Created by highsad on 2017/3/8.
 */
public class GraphCreatedEvent extends EventObject {
	private IGraph graph;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GraphCreatedEvent(Object source) {
		super(source);
	}

	/**
	 * 获取创建的
	 * @return
	 */
	public IGraph getGraph() {
		return this.graph;
	}
}
