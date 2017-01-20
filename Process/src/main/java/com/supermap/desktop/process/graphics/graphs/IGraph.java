package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;

/**
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {

	GraphCanvas getCanvas();

	double getWidth();

	double getHeight();

	Point getCenter();

	Point setCenter();

	boolean contains(Point p);

	void paint(Graphics2D g);

	IGraph clone();
}
