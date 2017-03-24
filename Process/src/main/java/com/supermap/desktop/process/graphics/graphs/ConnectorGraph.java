package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.events.GraphBoundsChangedListener;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public class ConnectorGraph implements IGraph {

	public AbstractGraph graph;

	@Override
	public GraphCanvas getCanvas() {
		return null;
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public Point getLocation() {
		return null;
	}

	@Override
	public Point getCenter() {
		return null;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setLocation(Point point) {

	}

	@Override
	public void setSize(int width, int height) {

	}

	@Override
	public boolean contains(Point point) {
		return false;
	}

	@Override
	public void addGraphBoundsChangedListener(GraphBoundsChangedListener listener) {

	}

	@Override
	public void removeGraghBoundsChangedListener(GraphBoundsChangedListener listener) {

	}

}
