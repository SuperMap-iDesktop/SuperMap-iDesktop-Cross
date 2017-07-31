package com.supermap.desktop.WorkflowView.graphics.graphs.decorators;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.GraphicsUtil;
import com.supermap.desktop.WorkflowView.graphics.connection.LineGraph;
import com.supermap.desktop.WorkflowView.graphics.graphs.IGraph;
import com.supermap.desktop.utilities.StringUtilities;

import java.awt.*;

/**
 * Created by highsad on 2017/5/25.
 */
public class LineMessageDecorator extends AbstractDecorator {
	private String message;

	public LineMessageDecorator(GraphCanvas canvas, String message) {
		super(canvas);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	private Point getBasePoint() {
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

//	@Override
//	public Rectangle getBounds() {
//		return null;
//	}

	@Override
	protected void onPaint(Graphics g) {
		if (!StringUtilities.isNullOrEmpty(this.message)) {
			Graphics2D graphics2D = (Graphics2D) g;
			Point basePoint = getBasePoint();

			graphics2D.setColor(Color.BLACK);
			graphics2D.setFont(new Font(getCanvas().getFont().getName(), 0, 12));
			graphics2D.drawString(this.message, basePoint.x, basePoint.y);
		}
	}
}
