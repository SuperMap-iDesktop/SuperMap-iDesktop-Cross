package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/27.
 */
public class LineGraph extends AbstractGraph {
	private java.util.List<Point> points;

	public LineGraph(GraphCanvas canvas) {
		super(canvas, new GeneralPath());
		this.points = new ArrayList<>();
	}

	private GeneralPath getPath() {
		return (GeneralPath) getShape();
	}

	public void addPoint(Point point) {
		if (!this.points.contains(point)) {
			this.points.add(point);
			GeneralPath path = getPath();

			if (this.points.size() == 0) {
				return;
			}

			if (this.points.size() == 1) {
				path.moveTo(point.getX(), point.getY());
			} else {
				path.lineTo(point.getX(), point.getY());
			}
		}
	}

	public void reset() {
		this.points.clear();
		getPath().reset();
	}

	public Stroke getStroke() {
		return new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
	}

	@Override
	protected void applyLocation(Point point) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void applySize(int width, int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onPaint(Graphics g) {
		GeneralPath path = getPath();

		if (path != null) {
			Graphics2D graphics2D = (Graphics2D) g;
			Stroke originStroke = graphics2D.getStroke();

			graphics2D.setStroke(getStroke());
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(path);
			graphics2D.setStroke(originStroke);
		}
	}
}
