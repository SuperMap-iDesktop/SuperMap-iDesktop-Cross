package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/1/19.
 */
public class RectangleGraph extends AbstractGraph {

	public RectangleGraph(GraphCanvas canvas) {
		super(canvas);
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
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void paint(Graphics2D g) {

	}
}
