package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class RectangleGraph extends AbstractGraph {

	private double arcWidth = 0d;
	private double arcHeight = 0d;

	public RectangleGraph(GraphCanvas canvas) {
		super(canvas);
	}

	public double getArcWidth() {
		return arcWidth;
	}

	public double getArcHeight() {
		return arcHeight;
	}

	public void setArcWidth(double arcWidth) {
		this.arcWidth = arcWidth;
	}

	public void setArcHeight(double arcHeight) {
		this.arcHeight = arcHeight;
	}

	@Override
	public boolean contains(Point p) {
		RoundRectangle2D rect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		return rect.contains(p);
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {
		RoundRectangle2D rect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		g.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(getBorderWidth());
		g.setStroke(stroke);
		g.draw(rect);

		Color color = new Color(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), 100);
		g.setColor(color);
		g.fill(rect);
	}
}
