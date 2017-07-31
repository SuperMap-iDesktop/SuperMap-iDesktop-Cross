package com.supermap.desktop.WorkflowView.graphics.interaction.graph;

import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/3/3.
 */
public class DefaultGraphEventHanderFactory implements IGraphEventHandlerFactory {

	private DefaultGraphEventHandler defaultHandler = new DefaultGraphEventHandler();
	private LineGraphEventHandler lineHandler = new LineGraphEventHandler();

	@Override
	public GraphEventHandler getHander(IGraph graph) {
		return this.defaultHandler;
	}
}
