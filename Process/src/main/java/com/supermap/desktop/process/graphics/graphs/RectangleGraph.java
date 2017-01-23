package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

	/**
	 * 为了演示出结果先暂时这样，后续使用 decorator 来重构
	 * hotDecorator selectedDecorator 等
	 *
	 * @param g
	 * @param isHot
	 * @param isSelected
	 */
	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {
		RoundRectangle2D rect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		Color color = Color.decode("#AEEEEE");
		g.setColor(color);
		g.fill(rect);

		if (isSelected) {
			Color hot = GraphicsUtil.transparentColor(Color.BLUE, 100);
			g.setColor(hot);
			g.fill(rect);
		}

		if (isHot) {
			Color hot = GraphicsUtil.transparentColor(Color.LIGHT_GRAY, 100);
			g.setColor(hot);
			g.fill(rect);
		}

		RoundRectangle2D borderRect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		g.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(getBorderWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);
		g.draw(borderRect);
	}

	/**
	 * 为了演示出结果先暂时这样，后续使用 decorator 来重构
	 *
	 * @param g
	 */
	@Override
	public void paintPreview(Graphics2D g) {
		RoundRectangle2D rect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		Color color = GraphicsUtil.transparentColor(Color.LIGHT_GRAY, 100);
		g.setColor(color);
		g.fill(rect);

		RoundRectangle2D borderRect = new RoundRectangle2D.Double(getX(), getY(), getWidth(), getHeight(), this.arcWidth, this.arcHeight);
		g.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(getBorderWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);
		g.draw(borderRect);
	}

	@Override
	public IGraph clone() {
		RectangleGraph graph = new RectangleGraph(getCanvas());
		graph.setX(getX());
		graph.setY(getY());
		graph.setWidth(getWidth());
		graph.setHeight(getHeight());
		graph.setArcWidth(getArcWidth());
		graph.setArcHeight(getArcHeight());
		return graph;
	}
}
