package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by highsad on 2017/1/19.
 */
public class EllipseGraph extends AbstractGraph {

	public EllipseGraph(GraphCanvas canvas) {
		super(canvas);
	}

	@Override
	public boolean contains(Point p) {
		Ellipse2D ellipse2D = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		return ellipse2D.contains(p);
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
		Ellipse2D rect = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		Color color = Color.decode("#C1FFC1");
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

		Ellipse2D borderRect = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
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
		Ellipse2D rect = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		Color color = GraphicsUtil.transparentColor(Color.LIGHT_GRAY, 100);
		g.setColor(color);
		g.fill(rect);

		Ellipse2D borderRect = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		g.setColor(Color.BLACK);
		Stroke stroke = new BasicStroke(getBorderWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);
		g.draw(borderRect);

	}

	@Override
	public IGraph clone() {
		EllipseGraph graph = new EllipseGraph(getCanvas());
		graph.setX(getX());
		graph.setY(getY());
		graph.setWidth(getWidth());
		graph.setHeight(getHeight());

		return graph;
	}
}
