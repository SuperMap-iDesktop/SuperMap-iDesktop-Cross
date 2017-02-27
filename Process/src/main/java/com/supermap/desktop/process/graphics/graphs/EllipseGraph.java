package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class EllipseGraph extends AbstractGraph {

	public EllipseGraph(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public boolean contains(Point p) {
		Ellipse2D ellipse2D = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		return ellipse2D.contains(p);
	}

	@Override
	public IGraph clone() {
		EllipseGraph graph = new EllipseGraph(getCanvas());
		graph.setWidth(getWidth());
		graph.setHeight(getHeight());

		return graph;
	}
}
