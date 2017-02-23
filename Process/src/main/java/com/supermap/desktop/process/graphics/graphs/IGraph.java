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
	Rectangle getBounds();

	boolean contains(Point point);

	void draw(Graphics graphics);
}
