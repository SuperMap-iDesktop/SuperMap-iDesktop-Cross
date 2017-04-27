package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/4/26.
 */
public interface IDecorator {
	GraphCanvas getCanvas();

	IGraph getGraph();

	void decorate(IGraph graph);

	void undecorate();

	boolean contains(Point point);

	Rectangle getBounds();

	void paint(Graphics g);
}
