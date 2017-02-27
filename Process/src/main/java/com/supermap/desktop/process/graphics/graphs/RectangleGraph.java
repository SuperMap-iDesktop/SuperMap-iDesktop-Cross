package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class RectangleGraph extends AbstractGraph {

	private double arcWidth = 0d;
	private double arcHeight = 0d;

	public RectangleGraph(GraphCanvas canvas) {
		super(canvas, new RoundRectangle2D.Double());
	}

	@Override
	public RoundRectangle2D getShape() {
		return (RoundRectangle2D) super.shape;
	}

	public double getArcWidth() {
		return this.arcWidth;
	}

	public double getArcHeight() {
		return this.arcHeight;
	}

	public void setArcWidth(double arcWidth) {
		this.arcWidth = arcWidth;
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), arcWidth, getShape().getArcHeight());
	}

	public void setArcHeight(double arcHeight) {
		this.arcHeight = arcHeight;
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), getShape().getArcWidth(), arcHeight);
	}

	@Override
	public void setLocation(Point point) {
		getShape().setFrame(point.getX(), point.getY(), getShape().getWidth(), getShape().getHeight());
	}

	@Override
	public void setSize(int width, int height) {
		getShape().setFrame(getShape().getX(), getShape().getY(), width, height);
	}

	@Override
	public boolean contains(Point p) {
		return this.shape.contains(p);
	}

	@Override
	public IGraph clone() {
		RectangleGraph graph = new RectangleGraph(getCanvas());
		graph.setLocation(getLocation());
		graph.setSize(getWidth(), getHeight());
		graph.setArcWidth(getArcWidth());
		graph.setArcHeight(getArcHeight());
		return graph;
	}
}
