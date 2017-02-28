package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.Interface.ICloneable;
import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/18.
 */
public interface IGraph {
	GraphCanvas getCanvas();

	Rectangle getBounds();

	Point getLocation();

	Point getCenter();

	int getWidth();

	int getHeight();

	void setLocation(Point point);

	void setSize(int width, int height);

	boolean contains(Point point);
}
