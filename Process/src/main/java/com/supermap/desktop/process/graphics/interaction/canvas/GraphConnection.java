package com.supermap.desktop.process.graphics.interaction.canvas;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.graphics.GraphCanvas;
import com.supermap.desktop.process.graphics.GraphicsUtil;
import com.supermap.desktop.process.graphics.connection.DefaultLine;
import com.supermap.desktop.process.graphics.connection.RelationLine;
import com.supermap.desktop.process.graphics.graphs.AbstractGraph;
import com.supermap.desktop.process.graphics.graphs.DataGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by highsad on 2017/3/22.
 */
public class GraphConnection extends CanvasEventAdapter {
	private GraphCanvas canvas;
	private DefaultLine previewLine;
	private Rectangle dirtyRegion = null;
	private IGraph startGraph = null;
	private IGraph endGraph = null;

	public GraphConnection(GraphCanvas canvas) {
		this.canvas = canvas;
	}

	public void connecting() {
		this.previewLine = new DefaultLine(this.canvas);
		this.canvas.setEventHandlerEnabled(Selection.class, false);
		this.canvas.setEventHandlerEnabled(DraggedHandler.class, false);
		this.canvas.setEventHandlerEnabled(GraphCreator.class, false);
	}

	public void preview(Graphics g) {
		if (this.previewLine != null) {
			this.previewLine.paint(g);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			Point canvasPoint = this.canvas.getCoordinateTransform().inverse(e.getPoint());
			IGraph hit = this.canvas.findGraph(e.getPoint());

			boolean ready = hit instanceof AbstractGraph ? ((AbstractGraph) hit).contains(canvasPoint) : hit != null;
			if (ready) {
//				if (canBeStart(hit)) {
				this.startGraph = hit;
				this.previewLine.setStartPoint(hit.getCenter());
//				} else {
//					this.startGraph = null;
//				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (this.startGraph != null && this.endGraph != null) {
					RelationLine line = new RelationLine(this.canvas, this.startGraph, this.endGraph);
					this.canvas.addConnection(line);
					line.repaint();
//					refresh();
				}
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		} finally {
			this.previewLine.setStartPoint(null);
			this.previewLine.setEndPoint(null);
			this.previewLine.setEnabled(true);
			this.startGraph = null;
			this.endGraph = null;
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			if (SwingUtilities.isRightMouseButton(e)) {
				clean();
			}
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		try {
		} catch (Exception ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (this.previewLine == null || this.previewLine.getStartPoint() == null) {
				return;
			}

			IGraph hit = this.canvas.findGraph(e.getPoint());

			if (canBeEnd(hit)) {
				this.endGraph = hit;
				this.previewLine.setEnabled(true);
				this.previewLine.setEndPoint(hit.getCenter());
			} else {
				this.endGraph = null;
				this.previewLine.setEnabled(false);
				this.previewLine.setEndPoint(this.canvas.getCoordinateTransform().inverse(e.getPoint()));
			}

//			refresh();
		}
	}

	private void refresh() {
		if (this.previewLine.getStartPoint() != null && this.previewLine.getEndPoint() != null) {
			Rectangle refresh;
			if (GraphicsUtil.isRegionValid(this.dirtyRegion)) {
				refresh = this.dirtyRegion.union(this.previewLine.getBounds());
			} else {
				refresh = this.previewLine.getBounds();
			}
			this.dirtyRegion = this.previewLine.getBounds();
			this.canvas.repaint(refresh);
		}
	}

	private boolean canBeStart(IGraph graph) {
		return graph instanceof DataGraph;
	}

	private boolean canBeEnd(IGraph graph) {
		return graph instanceof ProcessGraph;
	}

	@Override
	public void clean() {
		try {
			if (this.previewLine != null) {
				this.previewLine.setStartPoint(null);
				this.previewLine.setEndPoint(null);
				this.previewLine.setEnabled(true);
				this.previewLine = null;
			}
			this.dirtyRegion = null;
			this.endGraph = null;
			this.startGraph = null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.canvas.setEventHandlerEnabled(Selection.class, true);
			this.canvas.setEventHandlerEnabled(DraggedHandler.class, true);
			this.canvas.setEventHandlerEnabled(GraphCreator.class, true);
		}
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && this.previewLine != null;
	}

	public Point[] calArrow(Point start, Point end) {
		return calArrow(start.x, start.y, end.x, end.y);
	}

	public Point[] calArrow(int startX, int startY, int endX, int endY) {
		double awrad = 15 * Math.PI / 180;// 30表示角度，但是在计算中要用弧度进行计算，所以要把角度转换为弧度
		double arraow_len = 12;// 箭头长度
		double[] arr1 = rotateVec(endX - startX, endY - startY, awrad, arraow_len);
		double[] arr2 = rotateVec(endX - startX, endY - startY, -awrad, arraow_len);
		double x1 = endX - arr1[0]; // (x3,y3)是第一端点
		double y1 = endY - arr1[1];
		double x2 = endX - arr2[0]; // (x4,y4)是第二端点
		double y2 = endY - arr2[1];
		Point point1 = new Point(intValue(x1), intValue(y1));
		Point point2 = new Point(intValue(x2), intValue(y2));
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

	private static int intValue(double value) {
		Double d = new Double(value);
		return d.intValue();
	}
}
