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

	private GraphCanvas canvas;
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	public AbstractGraph(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, this.height);
	}

	@Override
	public Point getLocation() {
		return new Point(this.x, this.y);
	}

	@Override
	public Point getCenter() {
		return new Point(this.x + this.width / 2, this.y + this.height / 2);
	}

	@Override
	public void setLocation(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	public abstract IGraph clone();
}
