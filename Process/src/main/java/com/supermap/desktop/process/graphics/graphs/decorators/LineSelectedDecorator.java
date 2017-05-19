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
		boolean isContain = false;
		LineGraph lineGraph = getDecoratedLine();
		Point[] points = lineGraph.getPoints();

		for (int i = 0; i < points.length; i++) {
			Ellipse2D shape = new Ellipse2D.Double(points[i].getX(), points[i].getY(), NODE_WIDTH / 2, NODE_WIDTH / 2);
			isContain = shape.contains(point.getX(), point.getY());
			if (isContain) {
				break;
			}
		}
		return isContain;
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
		LineGraph lineGraph = getDecoratedLine();

		if (lineGraph == null) {
			return;
		}

		Point[] points = lineGraph.getPoints();
		if (points == null || points.length == 0) {
			return;
		}

		Graphics2D graphics2D = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(1);

		for (int i = 0; i < points.length; i++) {
			Ellipse2D shape = new Ellipse2D.Double(points[i].getX(), points[i].getY(), NODE_WIDTH / 2, NODE_WIDTH / 2);
			graphics2D.fill(shape);
			graphics2D.draw(shape);
		}
	}

	private LineGraph getDecoratedLine() {
		if (getGraph() instanceof LineGraph) {
			return (LineGraph) getGraph();
		} else {
			return null;
		}
	}
}
