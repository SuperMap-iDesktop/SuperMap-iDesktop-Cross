package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * Created by highsad on 2017/1/19.
 */
public class LineGraph extends AbstractGraph {

	private GeneralPath path;

	public LineGraph(GraphCanvas canvas) {
		super(canvas, new GeneralPath());
		this.path = new GeneralPath();
	}

	public Shape getShape() {
		return this.path;
	}

	public void reset() {
		this.path.reset();
	}

	@Override
	public IGraph clone() {
		return null;
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
}
