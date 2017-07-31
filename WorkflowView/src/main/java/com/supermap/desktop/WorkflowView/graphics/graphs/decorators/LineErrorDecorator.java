package com.supermap.desktop.WorkflowView.graphics.graphs.decorators;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.GraphicsUtil;
import com.supermap.desktop.WorkflowView.graphics.connection.LineGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/5/2.
 */
public class LineErrorDecorator extends AbstractDecorator {
	private int errorWidth = 6;

	public LineErrorDecorator(GraphCanvas canvas) {
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
	public void decorate(IGraph graph) {
		if (graph instanceof LineGraph) {
			super.decorate(graph);
		}
	}

	@Override
	public boolean contains(Point point) {
		Rectangle bounds = getBounds();

		if (GraphicsUtil.isRegionValid(bounds)) {
			return bounds.contains(point);
		} else {
			return true;
		}
	}

	@Override
	public Rectangle getBounds() {
		Point errorCenter = getErrorCenter();

		if (errorCenter != null) {
			return new Rectangle(errorCenter.x - errorWidth, errorCenter.y - errorWidth, errorWidth * 2, errorWidth * 2);
		} else {
			return null;
		}
	}

	private Point getErrorCenter() {
		LineGraph line = getDecoratedLine();
		if (line != null && line.getPointCount() > 1) {
			Point lastPoint = line.getPoint(line.getPointCount() - 1);
			Point penultPoint = line.getPoint(line.getPointCount() - 2);
			int errorCenterX = Math.min(penultPoint.x, lastPoint.x) + (Math.abs(penultPoint.x - lastPoint.x)) / 2;
			int errorCenterY = Math.min(penultPoint.y, lastPoint.y) + (Math.abs(penultPoint.y - lastPoint.y)) / 2;
			return new Point(errorCenterX, errorCenterY);
		} else {
			return null;
		}
	}

	@Override
	protected void onPaint(Graphics g) {
		Point errorCenter = getErrorCenter();

		if (errorCenter != null) {
			Graphics2D graphics2D = (Graphics2D) g;
			graphics2D.setColor(Color.RED);
			Stroke originStroke = graphics2D.getStroke();
			BasicStroke stroke = new BasicStroke(2);
			graphics2D.setStroke(stroke);

			graphics2D.drawLine(errorCenter.x - errorWidth, errorCenter.y - errorWidth, errorCenter.x + errorWidth, errorCenter.y + errorWidth);
			graphics2D.drawLine(errorCenter.x - errorWidth, errorCenter.y + errorWidth, errorCenter.x + errorWidth, errorCenter.y - errorWidth);
			graphics2D.setStroke(originStroke);
		}
	}
}
