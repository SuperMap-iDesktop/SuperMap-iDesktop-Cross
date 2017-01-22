package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/1/19.
 */
public class EllipseGraph extends AbstractGraph {

	public EllipseGraph(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public boolean contains(Point p) {
		return false;
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {

	}
}
