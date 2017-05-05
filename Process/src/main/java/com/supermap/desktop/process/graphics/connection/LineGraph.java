package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/4/27.
 */
public class LineGraph extends AbstractGraph {
	private java.util.List<Point> points;

	public LineGraph(GraphCanvas canvas) {
		super(canvas, new GeneralPath());
		this.points = new ArrayList<>();
	}

	private GeneralPath getPath() {
		return (GeneralPath) getShape();
	}

	public int getPointCount() {
		return this.points.size();
	}

	public Point getPoint(int index) {
		return this.points.get(index);
	}

	public void addPoint(Point point) {
		if (!this.points.contains(point)) {
			this.points.add(point);
			refreshPath();
		}
	}

	public void setLastPoint(Point point) {
		if (this.points.size() > 0) {
			setPoint(this.points.size() - 1, point);
		} else {
			setPoint(0, point);
		}
	}

	public void setFirstPoint(Point point) {
		setPoint(0, point);
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

	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}

		System.out.println(list);
		list.remove(3);
		System.out.println(list);
		list.add(3, 100);
		System.out.println(list);
	}
}
