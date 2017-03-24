package com.supermap.desktop.process.graphics.connection;

import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;

import java.awt.*;

/**
 * 与 IGraph 相互独立，IGraph 的交互逻辑不直接影响 AbstractLine，AbstractLine 的交互逻辑也不直接影响 IGraph
 * 如何处理绘制时候的线与图形交点？
 * 如何处理绑定的 PointProvider 值发生改变的刷新？
 * Created by highsad on 2017/3/22.
 */
public abstract class AbstractLine {
	private GraphCanvas canvas;
	private Point start;
	private Point end;
	private Point dirtyStart;
	private Point dirtyEnd;

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
		this.dirtyStart = this.start;
		this.dirtyEnd = this.end;
		this.start = start;
		repaint();
	}

	public void setEndPoint(Point end) {
		this.dirtyStart = this.start;
		this.dirtyEnd = this.end;
		this.end = end;
		repaint();
	}

	public Rectangle getBounds() {
		if (GraphicsUtil.isPointValid(getStartPoint()) && GraphicsUtil.isPointValid(getEndPoint())) {
			return GraphicsUtil.createRectangle(getStartPoint(), getEndPoint());
		} else {
			return null;
		}
	}

	public void paint(Graphics graphics) {
		if (GraphicsUtil.isPointValid(getStartPoint()) && GraphicsUtil.isPointValid(getEndPoint())) {
			Graphics2D graphics2D = (Graphics2D) graphics;
			Stroke originStroke = graphics2D.getStroke();

			BasicStroke stroke = new BasicStroke(1);
			graphics2D.setStroke(stroke);
			graphics2D.setColor(Color.BLACK);
			graphics2D.drawLine(getStartPoint().x, getStartPoint().y, getEndPoint().x, getEndPoint().y);
			graphics2D.setStroke(originStroke);
		}
	}

	public void repaint() {
		if (this.dirtyStart != null && this.dirtyEnd != null) {
			Rectangle dirty = GraphicsUtil.createRectangle(this.dirtyStart, this.dirtyEnd);

			if (GraphicsUtil.isRegionValid(dirty)) {
				dirty.grow(2, 2);
				this.canvas.repaint(this.canvas.getCoordinateTransform().transform(dirty));
			}
			this.dirtyStart = null;
			this.dirtyEnd = null;
		}

		if (this.start != null && this.end != null) {
			Rectangle current = getBounds();

			if (GraphicsUtil.isRegionValid(current)) {
				current.grow(2, 2);
				this.canvas.repaint(this.canvas.getCoordinateTransform().transform(current));
			}
		}
	}
}
