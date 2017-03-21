package com.supermap.desktop.process.graphics.interaction.graph;

import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.LineGraph;

/**
 * Created by highsad on 2017/3/3.
 */
public class DefaultGraphEventHanderFactory implements IGraphEventHandlerFactory {

	private DefaultGraphEventHandler defaultHandler = new DefaultGraphEventHandler();
	private LineGraphEventHandler lineHandler = new LineGraphEventHandler();

	@Override
	public GraphEventHandler getHander(IGraph graph) {
		if (graph instanceof LineGraph) {
			return this.lineHandler;
		}
		return this.defaultHandler;
	}
}
