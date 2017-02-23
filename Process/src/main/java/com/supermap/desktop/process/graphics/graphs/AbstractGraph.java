package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/20.
 */
public abstract class AbstractGraph implements IGraph {

	public final static int DEFAULT_BORDER_WIDTH = 2;

	private Shape shape;
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

	public int getBorderWidth() {
		return this.borderWidth;
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = new Rectangle();
		bounds.setRect(x, y, width, height);
//		double grow = this.borderWidth > CONNECTORSIZE / 2 + 1 ? borderWidth : CONNECTORSIZE / 2 + 1;
//		bounds.grow(Double.valueOf(grow).intValue(), Double.valueOf(grow).intValue());
		return bounds;
	}

	public Point getCenter() {
		Point center = new Point();
		center.setLocation(this.x + this.width / 2, this.y + this.height / 2);
		return center;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {
		paintBounds(g);
	}

	public void paintPreview(Graphics2D g) {
		paintBounds(g);
	}

	private void paintBounds(Graphics2D g) {
//		Rectangle2D[] rects = new Rectangle2D[8];
//		rects[0] = new Rectangle2D.Double(this.x - CONNECTORSIZE / 2, this.y - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[1] = new Rectangle2D.Double(this.x + this.width / 2 - CONNECTORSIZE / 2, this.y - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[2] = new Rectangle2D.Double(this.x + this.width - CONNECTORSIZE / 2, this.y - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[3] = new Rectangle2D.Double(this.x + this.width - CONNECTORSIZE / 2, this.y + this.height / 2 - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[4] = new Rectangle2D.Double(this.x + this.width - CONNECTORSIZE / 2, this.y + this.height - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[5] = new Rectangle2D.Double(this.x + this.width / 2 - CONNECTORSIZE / 2, this.y + this.height - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[6] = new Rectangle2D.Double(this.x - CONNECTORSIZE / 2, this.y + this.height - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//		rects[7] = new Rectangle2D.Double(this.x - CONNECTORSIZE / 2, this.y + this.height / 2 - CONNECTORSIZE / 2, CONNECTORSIZE, CONNECTORSIZE);
//
//		g.setColor(Color.BLACK);
//		for (int i = 0; i < rects.length; i++) {
//			g.fill(rects[i]);
//		}
	}

	public abstract IGraph clone();
}
