package com.supermap.desktop.process.graphics.handler.graph;

import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/3/3.
 */
public interface IGraphEventHandlerFactory {
	GraphEventHandler getHander(IGraph graph);
}
