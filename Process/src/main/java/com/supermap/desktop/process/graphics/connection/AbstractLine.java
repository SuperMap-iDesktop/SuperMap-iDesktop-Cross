package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * 与 IGraph 相互独立，IGraph 的交互逻辑不直接影响 AbstractLine，AbstractLine 的交互逻辑也不直接影响 IGraph
 * Created by highsad on 2017/3/22.
 */
public abstract class AbstractLine {
	private GraphCanvas canvas;
	private Point start;
	private Point end;
	private Rectangle dirty;
	private GeneralPath arrow = new GeneralPath();

	public AbstractLine(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public GraphCanvas getCanvas() {
		return this.canvas;
	}

	public Point getStartPoint() {
		return this.start;
	}

	public Point getEndPoint() {
		return this.end;
	}

	public void setStartPoint(Point start) {
		this.dirty = getBounds();
		this.start = start;
		computeArrowBounds();
		repaint();
	}

	public void setEndPoint(Point end) {
		this.dirty = getBounds();
		this.end = end;
		computeArrowBounds();
		repaint();
	}

	public Rectangle getBounds() {
		if (GraphicsUtil.isPointValid(getStartPoint()) && GraphicsUtil.isPointValid(getEndPoint())) {
			Rectangle arrowBounds = this.arrow.getBounds();
			return GraphicsUtil.isRegionValid(arrowBounds) ?
					GraphicsUtil.createRectangle(getStartPoint(), getEndPoint()).union(arrowBounds) :
					GraphicsUtil.createRectangle(getStartPoint(), getEndPoint());
		} else {
			return null;
		}
	}

	private void computeArrowBounds() {
		if (this.start != null && this.end != null) {
			Point[] arrowVertexes = computeArrow(this.start, this.end);
			if (arrowVertexes == null || arrowVertexes.length == 0) {
				return;
			}

			this.arrow.reset();
			this.arrow.moveTo(arrowVertexes[0].getX(), arrowVertexes[0].getY());
			this.arrow.lineTo(this.end.getX(), this.end.getY());
			this.arrow.lineTo(arrowVertexes[1].getX(), arrowVertexes[1].getY());
		} else {
			this.arrow.reset();
		}
	}

	public Stroke getStroke() {
		return new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
	}

	public void paint(Graphics graphics) {
		if (GraphicsUtil.isPointValid(getStartPoint()) && GraphicsUtil.isPointValid(getEndPoint())) {
			Graphics2D graphics2D = (Graphics2D) graphics;
			Stroke originStroke = graphics2D.getStroke();

			graphics2D.setStroke(getStroke());
			graphics2D.setColor(Color.GRAY);
			graphics2D.drawLine(getStartPoint().x, getStartPoint().y, getEndPoint().x, getEndPoint().y);

			Stroke stroke = new BasicStroke(2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10);
			graphics2D.setStroke(stroke);
			graphics2D.setColor(Color.GRAY);
			graphics2D.draw(this.arrow);
			graphics2D.setStroke(originStroke);
		}
	}

	public void repaint() {
		if (GraphicsUtil.isRegionValid(this.dirty)) {
			dirty.grow(2, 2);
			this.canvas.repaint(this.canvas.getCoordinateTransform().transform(dirty));
			this.dirty = null;
		}

		if (this.start != null && this.end != null) {
			Rectangle current = getBounds();

			if (GraphicsUtil.isRegionValid(current)) {
				current.grow(2, 2);
				this.canvas.repaint(this.canvas.getCoordinateTransform().transform(current));
			}
		}
	}

	public Point[] computeArrow(Point start, Point end) {
		return computeArrow(start.x, start.y, end.x, end.y);
	}

	public Point[] computeArrow(int startX, int startY, int endX, int endY) {
		double awrad = 15 * Math.PI / 180;// 30表示角度，但是在计算中要用弧度进行计算，所以要把角度转换为弧度
		double arraow_len = 16;// 箭头长度
		double[] arr1 = rotateVec(endX - startX, endY - startY, awrad, arraow_len);
		double[] arr2 = rotateVec(endX - startX, endY - startY, -awrad, arraow_len);
		double x1 = endX - arr1[0]; // (x3,y3)是第一端点
		double y1 = endY - arr1[1];
		double x2 = endX - arr2[0]; // (x4,y4)是第二端点
		double y2 = endY - arr2[1];
		Point point1 = new Point(DoubleUtilities.intValue(x1), DoubleUtilities.intValue(y1));
		Point point2 = new Point(DoubleUtilities.intValue(x2), DoubleUtilities.intValue(y2));
		return new Point[]{point1, point2};
	}

	// 计算
	public double[] rotateVec(int px, int py, double ang, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		double d = Math.sqrt(vx * vx + vy * vy);
		vx = vx / d * newLen;
		vy = vy / d * newLen;
		mathstr[0] = vx;
		mathstr[1] = vy;
		return mathstr;
	}
}
