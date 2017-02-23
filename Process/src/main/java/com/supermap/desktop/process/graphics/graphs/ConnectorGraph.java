package com.supermap.desktop.process.graphics.graphs;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public class ConnectorGraph implements IGraph {

	public AbstractGraph graph;

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public boolean contains(Point point) {
		return false;
	}

	@Override
	public void draw(Graphics graphics) {

	}
}
