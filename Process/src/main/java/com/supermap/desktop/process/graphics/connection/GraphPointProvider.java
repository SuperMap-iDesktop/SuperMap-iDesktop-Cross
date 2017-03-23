package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/3/22.
 */
public class GraphPointProvider implements IPointProvider {
	private IGraph graph;

	public IGraph getGraph() {
		return this.graph;
	}

	@Override
	public Point getPoint() {
		return null;
	}
}
