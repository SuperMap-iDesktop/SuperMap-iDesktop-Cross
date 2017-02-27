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
		super(canvas, new Ellipse2D.Double());
	}

	@Override
	public Ellipse2D getShape() {
		return (Ellipse2D) super.getShape();
	}

	@Override
	public void setLocation(Point point) {

	}

	@Override
	public void setSize(int width, int height) {

	}

	@Override
	public boolean contains(Point p) {
//		Ellipse2D ellipse2D = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		return getShape().contains(p);
	}

	@Override
	public IGraph clone() {
		EllipseGraph graph = new EllipseGraph(getCanvas());
//		graph.setWidth(getWidth());
//		graph.setHeight(getHeight());

		return graph;
	}
}
