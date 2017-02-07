package com.supermap.desktop.process.graphics.graphs;

import com.supermap.desktop.process.graphics.GraphCanvas;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * Created by highsad on 2017/1/19.
 */
public class LineGraph extends AbstractGraph {

	private IGraph start;
	private IGraph end;
	private Point startPoint = new Point(0, 0);
	private Point endPoint = new Point(0, 0);

	public LineGraph(GraphCanvas canvas) {
		super(canvas);
	}

	public Line2D getShape() {
		return new Line2D.Double(this.startPoint, this.endPoint);
	}

	public void setStart(IGraph start) {
		this.start = start;
		this.start.getLines().add(this);
	}

	public void setEnd(IGraph end) {
		this.end = end;
		this.end.getLines().add(this);
	}

	public void setEndPoint(Point endPoint) {
		if (this.end != null) {
			this.end.getLines().remove(this);
		}
		this.end = null;
		this.endPoint = endPoint;
	}

	@Override
	public void paint(Graphics2D g, boolean isHot, boolean isSelected) {
//		if (this.start != null) {
//			Point startPoint = new Point();
//			Point endPoint = new Point();
//
//			if (this.end != null) {
//				if (this.end.getX() + this.end.getWidth() < this.start.getX()) {
//					startPoint.setLocation(this.start.getConnector().getLeftConnector());
//					endPoint.setLocation(this.end.getConnector().getRightConnector());
//				} else if (this.start.getX() + this.start.getWidth() < this.end.getX()) {
//					startPoint.setLocation(this.start.getConnector().getRightConnector());
//					endPoint.setLocation(this.end.getConnector().getLeftConnector());
//				} else if (this.end.getY() + this.end.getHeight() < this.start.getY()) {
//					startPoint.setLocation(this.start.getConnector().getTopConnector());
//					endPoint.setLocation(this.end.getConnector().getBottomConnector());
//				} else if (this.start.getY() + this.start.getHeight() < this.end.getY()) {
//					startPoint.setLocation(this.start.getConnector().getBottomConnector());
//					endPoint.setLocation(this.end.getConnector().getTopConnector());
//				} else {
//					startPoint = null;
//					endPoint = null;
//				}
//			} else if (this.endPoint != null) {
//				endPoint.setLocation(this.endPoint);
//				if (this.start.getX() + this.start.getWidth() < endPoint.getX()) {
//					startPoint.setLocation(this.start.getConnector().getRightConnector());
//				} else if (endPoint.getX() < this.start.getX()) {
//					startPoint.setLocation(this.start.getConnector().getLeftConnector());
//				} else if (endPoint.getY() < this.start.getY()) {
//					startPoint.setLocation(this.start.getConnector().getTopConnector());
//				} else if (this.start.getY() + this.start.getHeight() < endPoint.getY()) {
//					startPoint.setLocation(this.start.getConnector().getBottomConnector());
//				} else {
//					startPoint = null;
//					endPoint = null;
//				}
//			}
//
//			if (startPoint != null && endPoint != null) {
//				g.setColor(Color.BLACK);
//				Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//				g.setStroke(stroke);
//				g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
//			}
//		}
	}

	public void paint(Graphics2D g) {
		if (this.start != null) {
			this.startPoint = new Point();
			this.endPoint = new Point();

			if (this.end != null) {
				if (this.end.getX() + this.end.getWidth() < this.start.getX()) {
					startPoint.setLocation(this.start.getConnector().getLeftConnector());
					endPoint.setLocation(this.end.getConnector().getRightConnector());
				} else if (this.start.getX() + this.start.getWidth() < this.end.getX()) {
					startPoint.setLocation(this.start.getConnector().getRightConnector());
					endPoint.setLocation(this.end.getConnector().getLeftConnector());
				} else if (this.end.getY() + this.end.getHeight() < this.start.getY()) {
					startPoint.setLocation(this.start.getConnector().getTopConnector());
					endPoint.setLocation(this.end.getConnector().getBottomConnector());
				} else if (this.start.getY() + this.start.getHeight() < this.end.getY()) {
					startPoint.setLocation(this.start.getConnector().getBottomConnector());
					endPoint.setLocation(this.end.getConnector().getTopConnector());
				} else {
					startPoint.setLocation(0, 0);
					endPoint.setLocation(0, 0);
				}
			} else if (this.endPoint != null) {
				endPoint.setLocation(this.endPoint);
				if (this.start.getX() + this.start.getWidth() < endPoint.getX()) {
					startPoint.setLocation(this.start.getConnector().getRightConnector());
				} else if (endPoint.getX() < this.start.getX()) {
					startPoint.setLocation(this.start.getConnector().getLeftConnector());
				} else if (endPoint.getY() < this.start.getY()) {
					startPoint.setLocation(this.start.getConnector().getTopConnector());
				} else if (this.start.getY() + this.start.getHeight() < endPoint.getY()) {
					startPoint.setLocation(this.start.getConnector().getBottomConnector());
				} else {
					startPoint.setLocation(0, 0);
					endPoint.setLocation(0, 0);
				}
			}

			if (startPoint.getX() != 0 && startPoint.getY() != 0 && endPoint.getX() != 0 && endPoint.getY() != 0) {
				g.setColor(Color.BLACK);
				Stroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g.setStroke(stroke);
				g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

//				double[] awrTop = rotateVec(this.endPoint.x - this.startPoint.x, this.endPoint.y - this.startPoint.y, 30, true, 5);
//				double[] awrBottom = rotateVec(this.endPoint.x - this.startPoint.x, this.endPoint.y - this.startPoint.y, -30, true, 5);
//				int xTop = Double.valueOf(awrTop[0]).intValue();
//				int yTop = Double.valueOf(awrTop[1]).intValue();
//				int xBottom = Double.valueOf(awrBottom[0]).intValue();
//				int yBottom = Double.valueOf(awrBottom[1]).intValue();
//
//				GeneralPath path = new GeneralPath();
//				path.moveTo(this.endPoint.getX(), this.endPoint.getY());
//				path.lineTo(xTop, yTop);
//				path.lineTo(xBottom, yBottom);
//				path.closePath();
//				g.fill(path);
			}
		}
	}

	// 计算
	public static double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

	@Override
	public IGraph clone() {
		return null;
	}
}
