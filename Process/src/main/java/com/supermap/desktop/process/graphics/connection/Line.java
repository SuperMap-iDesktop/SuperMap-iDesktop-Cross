package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.graphs.IGraph;

import java.awt.*;

/**
 * 与 IGraph 相互独立，IGraph 的交互逻辑不直接影响 Line，Line 的交互逻辑也不直接影响 IGraph
 * 如何处理绘制时候的线与图形交点？
 * 如何处理绑定的 PointProvider 值发生改变的刷新？
 * Created by highsad on 2017/3/22.
 */
public class Line {
	private GraphCanvas canvas;
	private IGraph origin;
	private IGraph destination;
	private Point endPoint;

	public Line(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public IGraph getOrigin() {
		return this.origin;
	}

	public void setOrigin(IGraph origin) {
		this.origin = origin;
	}

	public IGraph getDestination() {
		return this.destination;
	}

	public void setDestination(IGraph destination) {
		this.destination = destination;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getEndPoint() {
		if (this.destination != null) {
			return this.destination.getCenter();
		} else {
			return this.endPoint;
		}
	}

	public void paint(Graphics graphics) {
		if (this.origin == null) {
			return;
		}

		Graphics2D graphics2D = (Graphics2D) graphics;
		Stroke originStroke = graphics2D.getStroke();

		Point from = this.origin.getCenter();
		Point to = getEndPoint();

		BasicStroke stroke = new BasicStroke(1);
		graphics2D.setStroke(stroke);
		graphics2D.setColor(Color.BLACK);
		graphics2D.drawLine(from.x, from.y, to.x, to.y);
		graphics2D.setStroke(originStroke);
	}
}
