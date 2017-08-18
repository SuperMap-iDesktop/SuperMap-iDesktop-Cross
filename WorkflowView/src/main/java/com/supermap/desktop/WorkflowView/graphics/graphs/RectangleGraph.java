package com.supermap.desktop.WorkflowView.graphics.graphs;

import com.supermap.desktop.WorkflowView.graphics.GraphCanvas;
import com.supermap.desktop.WorkflowView.graphics.connection.IConnectable;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class RectangleGraph extends AbstractGraph implements IConnectable {
	private double arcWidth = 0d;
	private double arcHeight = 0d;

	public RectangleGraph(GraphCanvas canvas) {
		super(canvas, new RoundRectangle2D.Double(0, 0, 120, 40, 12, 12));
	}

	public RectangleGraph(GraphCanvas canvas, double arcWidth, double arcHeight) {
		super(canvas, new RoundRectangle2D.Double(0, 0, 120, 40, arcWidth, arcHeight));
	}

	@Override
	public RoundRectangle2D getShape() {
		return (RoundRectangle2D) super.shape;
	}

	protected Color getBackColor() {
		return new Color(202, 221, 254);
	}

	public double getArcWidth() {
		return this.arcWidth;
	}

	public double getArcHeight() {
		return this.arcHeight;
	}

	public void setArcWidth(double arcWidth) {
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), arcWidth, getShape().getArcHeight());
	}

	public void setArcHeight(double arcHeight) {
		this.arcHeight = arcHeight;
		getShape().setRoundRect(getShape().getX(), getShape().getY(), getShape().getWidth(), getShape().getHeight(), getShape().getArcWidth(), arcHeight);
	}

	@Override
	public void applyLocation(Point point) {
		getShape().setFrame(point.getX(), point.getY(), getShape().getWidth(), getShape().getHeight());
	}

	@Override
	public void applySize(int width, int height) {
		getShape().setFrame(getShape().getX(), getShape().getY(), width, height);
	}

	@Override
	public boolean contains(Point p) {
		return this.shape.contains(p);
	}

	@Override
	protected void onPaint(Graphics g) {
		g.setColor(getBackColor());
		((Graphics2D) g).fill(this.shape);
	}

	@Override
	public IGraph getConnector() {
		return this;
	}
}
