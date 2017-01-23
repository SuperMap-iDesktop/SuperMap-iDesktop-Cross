package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by highsad on 2017/1/20.
 */
public abstract class AbstractGraph implements IGraph {
	public final static int DEFAULT_BORDER_WIDTH = 2;

	private int borderWidth;
	private GraphCanvas canvas;
	private double x = 0d;
	private double y = 0d;
	private double width = 0d;
	private double height = 0d;

	public AbstractGraph(GraphCanvas canvas) {
		this.borderWidth = DEFAULT_BORDER_WIDTH;
		this.canvas = canvas;
	}

	@Override
	public int getBorderWidth() {
		return this.borderWidth;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = new Rectangle();
		bounds.setRect(x, y, width, height);
		bounds.grow(borderWidth, borderWidth);
		return bounds;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public double getHeight() {
		return this.height;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {

	}

	@Override
	public void paintPreview(Graphics2D g) {

	}

	public abstract IGraph clone();
}
