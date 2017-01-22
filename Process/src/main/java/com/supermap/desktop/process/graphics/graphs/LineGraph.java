package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/1/19.
 */
public class LineGraph extends AbstractGraph{

	public LineGraph(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public void setX(double x) {

	}

	@Override
	public void setY(double y) {

	}

	@Override
	public void setWidth(double width) {

	}

	@Override
	public void setHeight(double height) {

	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {

	}
}
