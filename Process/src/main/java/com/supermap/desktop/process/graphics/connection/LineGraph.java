package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.decorators.LineArrowDecorator;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/27.
 */
public class LineGraph extends AbstractGraph {
	private static String DECORATOR_KEY_LINE_ARROW = "DecoratorLineArrowKey";

	private java.util.List<Point> points;
	private LineArrowDecorator arrowDecorator;

	public LineGraph(GraphCanvas canvas) {
		super(canvas, new GeneralPath());
		this.points = new ArrayList<>();
		this.arrowDecorator = new LineArrowDecorator(getCanvas());
		this.arrowDecorator.setPriority(Integer.MAX_VALUE);
		addDecorator(DECORATOR_KEY_LINE_ARROW, this.arrowDecorator);
	}

	private GeneralPath getPath() {
		return (GeneralPath) getShape();
	}

	public int getPointCount() {
		return this.points.size();
	}

	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();

		if (bounds.width == 0 && bounds.height != 0) {
			bounds = new Rectangle(bounds.x, bounds.y, 1, bounds.height);
		} else if (bounds.width != 0 && bounds.height == 0) {
			bounds = new Rectangle(bounds.x, bounds.y, bounds.width, 1);
		}
		return bounds;
	}

	public Point getPoint(int index) {
		return this.points.get(index);
	}

	public Point[] getPoints() {
		return this.points.toArray(new Point[this.points.size()]);
	}

	public void addPoint(Point point) {
		if (!this.points.contains(point)) {
			this.points.add(point);
			refreshPath();
		}
	}

	public void setLastPoint(Point point) {
		if (this.points.size() == 0 || this.points.size() == 1) {
			addPoint(point);
		} else {
			setPoint(this.points.size() - 1, point);
		}
	}

	public void setFirstPoint(Point point) {
		if (this.points.size() == 0) {
			addPoint(point);
		} else {
			setPoint(0, point);
		}
	}

	public void setPoint(int index, Point point) {
		if (index >= 0 && index < this.points.size()) {
			this.points.remove(index);
			this.points.add(index, point);
			refreshPath();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	private void refreshPath() {
		GeneralPath path = getPath();
		path.reset();

		if (this.points.size() == 0) {
			return;
		}

		path.moveTo(this.points.get(0).getX(), this.points.get(0).getY());
		for (int i = 1, size = this.points.size(); i < size; i++) {
			path.lineTo(this.points.get(i).getX(), this.points.get(i).getY());
		}
	}

	public void reset() {
		this.points.clear();
		getPath().reset();
	}

	public Stroke getStroke() {
		return new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
	}

	@Override
	protected void applyLocation(Point point) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Point point) {
		boolean isContain = false;

		for (int i = 0; i < this.points.size() - 1; i++) {
			Point p1 = this.points.get(i);
			Point p2 = this.points.get(i + 1);
			isContain = GraphicsUtil.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, point.x, point.y, 3);
			if (isContain) {
				break;
			}
		}

		if (!isContain) {
			for (String key :
					this.decorators.keySet()) {
				isContain = this.decorators.get(key).contains(point);
				if (isContain) {
					break;
				}
			}
		}
		return isContain;
	}

	@Override
	protected void applySize(int width, int height) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onPaint(Graphics g) {
		GeneralPath path = getPath();

		if (path != null) {
			Graphics2D graphics2D = (Graphics2D) g;
			Stroke originStroke = graphics2D.getStroke();

			graphics2D.setStroke(getStroke());
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(path);
			graphics2D.setStroke(originStroke);
		}
	}
}
