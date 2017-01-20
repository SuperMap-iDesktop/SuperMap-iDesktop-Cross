package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/1/20.
 */
public class AbstractGraph implements IGraph {

	private GraphCanvas canvas;

	public AbstractGraph(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public GraphCanvas getCanvas() {
		return null;
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public Point getCenter() {
		return null;
	}

	@Override
	public Point setCenter() {
		return null;
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void paint(Graphics2D g) {

	}

	@Override
	public IGraph clone() {
		return null;
	}
}
