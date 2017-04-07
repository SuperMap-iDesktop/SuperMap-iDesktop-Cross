package com.supermap.desktop.process.graphics.storage;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.GraphRelationLine;
import com.supermap.desktop.process.graphics.graphs.IGraph;

/**
 * Created by highsad on 2017/4/5.
 */
public interface IGraphConnection {
	GraphCanvas getCanvas();

	GraphRelationLine[] getLines();

	void connect(IGraph start, IGraph end);

	void connect(IGraph start, IGraph end, String message);

	void removeConnection(IGraph graph);

	IGraph[] getPreGraphs(IGraph end);

	IGraph[] getNextGraphs(IGraph start);

	boolean isConnectedAsStart(IGraph start);

	boolean isConnectedAsEnd(IGraph end);

	boolean isConnected(IGraph graph1, IGraph graph2);
}
