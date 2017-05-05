package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.connection.LineGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by highsad on 2017/5/2.
 */
public class LineArrowDecorator extends AbstractDecorator {
	private int crossWidth = 6;

	public LineArrowDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	private LineGraph getDecoratedLine() {
		if (getGraph() instanceof LineGraph) {
			return (LineGraph) getGraph();
		} else {
			return null;
		}
	}

	@Override
	public boolean contains(Point point) {
		GeneralPath arrow = getArrowPath();

		if (arrow != null) {
			return arrow.contains(point.getX(), point.getY());
		} else {
			return true;
		}
	}

	@Override
	public Rectangle getBounds() {
		GeneralPath arrow = getArrowPath();

		if (arrow != null) {
			return arrow.getBounds();
		} else {
			return null;
		}
	}

	private GeneralPath getArrowPath() {
		GeneralPath arrowPath = null;
		LineGraph line = getDecoratedLine();
		if (line != null && line.getPointCount() > 1) {
			Point[] arrowVertexes = GraphicsUtil.computeArrow(line.getPoint(line.getPointCount() - 2), line.getPoint(line.getPointCount() - 1));

			if (arrowVertexes != null && arrowVertexes.length != 0) {
				arrowPath = new GeneralPath();
				arrowPath.moveTo(arrowVertexes[0].getX(), arrowVertexes[0].getY());
				arrowPath.lineTo(line.getPoint(line.getPointCount() - 1).getX(), line.getPoint(line.getPointCount() - 1).getY());
				arrowPath.lineTo(arrowVertexes[1].getX(), arrowVertexes[1].getY());
			}
		}
		return arrowPath;
	}

	@Override
	public void decorate(IGraph graph) {
		if (graph instanceof LineGraph) {
			super.decorate(graph);
		}
	}

	@Override
	protected void onPaint(Graphics g) {
		GeneralPath arrow = getArrowPath();
		if (arrow != null) {
			Graphics2D graphics2D = (Graphics2D) g;
			Stroke stroke = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
			graphics2D.setStroke(stroke);
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(arrow);
		}
	}
}
