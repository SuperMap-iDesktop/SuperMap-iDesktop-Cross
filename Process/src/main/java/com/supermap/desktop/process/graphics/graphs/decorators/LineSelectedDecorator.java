package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by highsad on 2017/5/9.
 */
public class LineSelectedDecorator extends AbstractDecorator {
	private final static int NODE_WIDTH = 8;

	public LineSelectedDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public void decorate(IGraph graph) {
		if (graph instanceof LineGraph) {
			super.decorate(graph);
		}
	}

	@Override
	public boolean contains(Point point) {
		LineGraph lineGraph = getDecoratedLine();
		Point[] points = lineGraph.getPoints();


		return super.contains(point);
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();

		if (bounds == null) {
			return null;
		}

		LineGraph lineGraph = getDecoratedLine();
		Point[] points = lineGraph.getPoints();

		for (int i = 0; i < points.length; i++) {
			Ellipse2D shape = new Ellipse2D.Double(points[i].getX(), points[i].getY(), NODE_WIDTH / 2, NODE_WIDTH / 2);
			bounds.union(shape.getBounds());
		}
		return bounds;
	}

	@Override
	protected void onPaint(Graphics g) {

	}

	private Rectangle getPointBounds(Point point) {
		Rectangle bounds = null;

		if (point != null) {
			Ellipse2D shape = new Ellipse2D.Double(point.getX(), point.getY(), NODE_WIDTH / 2, NODE_WIDTH / 2);
			bounds = shape.getBounds();
		}
		return bounds;
	}

	private LineGraph getDecoratedLine() {
		if (getGraph() instanceof LineGraph) {
			return (LineGraph) getGraph();
		} else {
			return null;
		}
	}
}
