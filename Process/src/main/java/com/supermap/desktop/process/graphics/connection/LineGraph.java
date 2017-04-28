package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by highsad on 2017/4/27.
 */
public class LineGraph extends AbstractGraph {
	private GeneralPath path;
	private Point start;
	private Point end;
	private java.util.List<Point> points;

	public LineGraph(GraphCanvas canvas, Shape shape) {
		super(canvas, shape);
	}

	public Stroke getStroke() {
		return new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
	}

	@Override
	protected void applyLocation(Point point) {

	}

	@Override
	protected void applySize(int width, int height) {

	}

	@Override
	protected void onPaint(Graphics g) {
		if (GraphicsUtil.isPointValid(this.start) && GraphicsUtil.isPointValid(this.end)) {
			Graphics2D graphics2D = (Graphics2D) g;
			Stroke originStroke = graphics2D.getStroke();

			graphics2D.setStroke(getStroke());
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(this.path);
//			graphics2D.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
		}
	}
}
