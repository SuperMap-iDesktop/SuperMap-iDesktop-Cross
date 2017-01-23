package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {

	int getBorderWidth();

	GraphCanvas getCanvas();

	Rectangle getBounds();

	double getX();

	double getY();

	double getWidth();

	double getHeight();

	void setX(double x);

	void setY(double y);

	void setWidth(double width);

	void setHeight(double height);

	boolean contains(Point p);

	void paint(Graphics2D g, boolean isHot, boolean isSelected);

	void paintPreview(Graphics2D g);

	IGraph clone();
}
