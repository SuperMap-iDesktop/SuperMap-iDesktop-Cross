package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by highsad on 2017/2/23.
 */
public class SelectedDecorator extends AbstractShapedDecorator {
	private final static int BORDER_WIDTH = 2;
	private final static int ANCHOR_WIDTH = 8;

	public SelectedDecorator(GraphCanvas canvas) {
		super(canvas);
	}

//	@Override
//	public Rectangle getBounds() {
//		Rectangle rect = (Rectangle) super.getBounds().clone();
//		rect.grow(BORDER_WIDTH, BORDER_WIDTH);
//		return rect;
//	}

	@Override
	public Rectangle getBounds() {
		Point[] anchors = new Point[4];
		Rectangle rect = super.getBounds();

		if (!GraphicsUtil.isRegionValid(rect)) {
			return rect;
		}

		anchors[0] = new Point(rect.x + rect.width / 2, rect.y);
		anchors[1] = new Point(rect.x + rect.width, rect.y + rect.height / 2);
		anchors[2] = new Point(rect.x + rect.width / 2, rect.y + rect.height);
		anchors[3] = new Point(rect.x, rect.y + rect.height / 2);

		for (int i = 0; i < anchors.length; i++) {
			Point point = anchors[i];
			Ellipse2D shape = new Ellipse2D.Double(point.getX(), point.getY(), ANCHOR_WIDTH / 2, ANCHOR_WIDTH / 2);
			rect.union(shape.getBounds());
		}
		return rect;
	}


	private Point[] getAnchors() {
		Point[] anchors = new Point[4];
		Rectangle rect = getBounds();

		if (!GraphicsUtil.isRegionValid(rect)) {
			return null;
		}

		anchors[0] = new Point(rect.x + rect.width / 2, rect.y);
		anchors[1] = new Point(rect.x + rect.width, rect.y + rect.height / 2);
		anchors[2] = new Point(rect.x + rect.width / 2, rect.y + rect.height);
		anchors[3] = new Point(rect.x, rect.y + rect.height / 2);
		return anchors;
	}

	@Override
	public boolean contains(Point point) {
		return super.contains(point);
	}

	@Override
	protected void onPaint(Graphics g) {
		Point[] anchors = getAnchors();
		if (anchors == null || anchors.length == 0) {
			return;
		}

		Graphics2D graphics2D = (Graphics2D) g;
		Stroke origin = graphics2D.getStroke();
		BasicStroke stroke = new BasicStroke(1);
		graphics2D.setStroke(stroke);

		for (int i = 0; i < anchors.length; i++) {
			Ellipse2D shape = new Ellipse2D.Double(anchors[i].getX() - ANCHOR_WIDTH / 2, anchors[i].getY() - ANCHOR_WIDTH / 2, ANCHOR_WIDTH, ANCHOR_WIDTH);
			graphics2D.setColor(Color.WHITE);
			graphics2D.fill(shape);
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(shape);
		}
		graphics2D.setStroke(origin);

//		AbstractGraph graph = getShapedGraph();
//		BasicStroke stroke = new BasicStroke(2);
//		((Graphics2D) g).setStroke(stroke);
//		g.setColor(Color.BLACK);
//		((Graphics2D) g).draw(graph.getShape());
	}
}
