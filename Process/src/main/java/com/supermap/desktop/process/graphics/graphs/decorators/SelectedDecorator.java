package com.supermap.desktop.process.graphics.graphs.decorators;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;

import java.awt.*;

/**
 * Created by highsad on 2017/2/23.
 */
public class SelectedDecorator extends AbstractShapedDecorator {
	private static final int BORDER_WIDTH = 2;

	public SelectedDecorator(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public Rectangle getBounds() {
		Rectangle rect = (Rectangle) super.getBounds().clone();
		rect.grow(BORDER_WIDTH, BORDER_WIDTH);
		return rect;
	}

	@Override
	public boolean contains(Point point) {
		return super.contains(point);
	}

	@Override
	protected void onPaint(Graphics g) {
		AbstractGraph graph = getShapedGraph();
		BasicStroke stroke = new BasicStroke(2);
		((Graphics2D) g).setStroke(stroke);
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(graph.getShape());
	}
}
